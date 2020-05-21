from sys import exit, argv
from typing import List, Dict, Tuple
from os import path
import json
import argparse
import re
import logging

verbosity: bool = True;

file_content: Dict[str, List[List[str]]] = {};
current_traceID: str = "-1";
current_controller_and_method: str = "";

def printAndLog(message: str):
    if (verbosity):
        print(message)
    
    logging.info(message)

def deleteControllersWithNoAccesses():
    file_content_copy = file_content.copy() # to avoid RuntimeError: dictionary changed size during iteration

    for controller, accessesList in file_content_copy.items():
        if (not accessesList):
            del file_content[controller]

def getTraceID(trace: List[str]) -> str:
    return re.findall("\<([0-9]+)\[", trace[0])[0] # e.g, <5[0,0]

def getControllerAndMethod(trace: List[str]) -> str:
    *everything_else, controller_name, controller_method = trace[2].split(sep='.')
    return controller_name + "." + controller_method

def getClassNameAndMethod(trace: List[str]) -> Tuple[str, str]:
    *everything_else, class_name, method = trace[2].split(sep='.')
    return class_name, method

def parseMethod(class_name: str, method: str) -> Tuple[str, str]: # (accessed entity, access type)
    printAndLog("Parsing method: " + method + " of class " + class_name)
    
    lowered_case_method = method.lower()
    if (lowered_case_method.startswith("get")):
        if (lowered_case_method.endswith("set")):
            return method[3:-4], "R" # unfortunately I have to do this hack because I can't obtain for now the functions' return type
        
        elif (class_name.startswith("DomainRoot") or class_name == "FenixFramework"):
            return "LdoD", "R"

        elif (class_name.endswith("_Base")):     
            return class_name[: -len("_Base")], "R" # if it's only a get, then the entity is the class iself
        
        else:
            exit("[WARNING]: You were not expecting this method " + method + "from class " + class_name + " in the parseMethod function")


    elif (lowered_case_method.startswith("set")):
        return method[3:], "W"
    elif (method.startswith("add")):
        return method[3:-1], "W" # TODO speak with samuel about reads and writes
    # elif (method.startswith("remove")):
    else:
        exit("[WARNING]: You were not expecting this method " + method + "from class " + class_name + " in the parseMethod function")

def parseExecutionTrace(trace: str):
    global file_content
    global current_controller_and_method

    if ("TraceId" in trace): # for now, i dont care about lines with a trace id
        return

    trace = trace.rstrip() # remove extra new lines

    split_trace: List[str] = trace.split() # split by white spaces

    if (len(split_trace) != 4): # According to Kieker's output, a trace seems to only have traces with 4 sections
        exit("[ERROR]: A trace with more than 4 fields was found.");
    
    # The only interesting parts of trace we want to parse are the 1st one (index 0) and 3rd one (index 2)
    traceID = getTraceID(split_trace)
    printAndLog("Trace ID: " + traceID)

    global current_traceID
    if (current_traceID != traceID): # if the previous ID is different from the new one, it means we are in a different trace
        current_traceID = traceID # and consequently, we need to start by parsing the controller first

        controller_and_method = getControllerAndMethod(split_trace)
        printAndLog("Controller.method: " + controller_and_method)

        file_content[controller_and_method] = []

        current_controller_and_method = controller_and_method

        return
    
    else: # if traceID hasn't changed, then we must continue to parse it and add the result to the current controller_and_method
        class_name, method = getClassNameAndMethod(split_trace)

        if (class_name == "FenixFramework"): 
            pass # FIXME check with samuel or rito to see if this is considered a read because we're already accessing the DomainRoot_Base
        elif ("_Base" not in class_name):
            return

        accessed_entity, access_type = parseMethod(class_name, method) # passing class_name cuz e.g, the getAcronym comes form the Edition(_Base) entity
        printAndLog("Accessed entity: " + accessed_entity)
        printAndLog("Access type: " + access_type)

        file_content[current_controller_and_method].append([accessed_entity, access_type])

def checkFileExists(file_dir: str):
    if path.isfile(file_dir):
        return file_dir
    else:
        exit("[ERROR]: The file " + file_dir + " you entered does not exist")

def checkDirectoryExists(dir_path: str):
    if path.isdir(dir_path):
        return dir_path
    else:
        exit("[ERROR]: The directory " + dir_path + " does not exist")


def parseCommandLineArguments() -> Dict[str, object]:
    ap = argparse.ArgumentParser()
    ap.add_argument("-i", "--inputdir", type=checkFileExists, required=True, help="directory of the file to be parsed", metavar="")
    ap.add_argument("-o", "--outputdir", help="directory for the generated file", default="./", metavar="")
    ap.add_argument("-v", "--verbose", action="store_true", help="logging enabler")
    
    args = vars(ap.parse_args())
    return args

if __name__ == "__main__":
    logging.basicConfig(filename="executionTracesToJson.log", filemode="w", format="%(message)s", level=logging.DEBUG)

    args = parseCommandLineArguments()

    input_file_dir: str = args["inputdir"]
    output_file_dir: str = args["outputdir"]
    verbosity = args["verbose"]

    printAndLog(args)

    with open(input_file_dir) as f:
        for line in f:
            if line in ['\n', '\r\n']: # ignore blank lines
                continue

            parseExecutionTrace(line)

    deleteControllersWithNoAccesses()
    
    with open(output_file_dir, 'w') as file:
        file.write(json.dumps(file_content, indent = 2))
