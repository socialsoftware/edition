from sys import exit, argv
from typing import List, Dict, Tuple
from os import path
import json

file_content: Dict[str, List[List[str]]] = {};
current_traceID: str = "-1";
current_controller_and_method: str = "";

def deleteControllersWithNoAccesses():
    file_content_copy = file_content.copy() # to avoid RuntimeError: dictionary changed size during iteration

    for controller, accessesList in file_content_copy.items():
        if (not accessesList):
            del file_content[controller]

def getTraceID(trace: List[str]) -> str:
    return trace[0][1] # e.g, <5[0,0]

def getControllerAndMethod(trace: List[str]) -> str:
    *everything_else, controller_name, controller_method = trace[2].split(sep='.')
    return controller_name + "." + controller_method

def getClassNameAndMethod(trace: List[str]) -> Tuple[str, str]:
    *everything_else, class_name, method = trace[2].split(sep='.')
    return class_name, method

def parseMethod(method: str) -> Tuple[str, str]: # (accessed entity, access type)
    lowered_case_method = method.lower()
    # print(method)
    if (lowered_case_method.startswith("get")):
        if (lowered_case_method.endswith("set")):
            return method[3:-4], "R"
        
        return method[3:], "R"

    elif (lowered_case_method.startswith("set")):
        return method[3:], "W"
    # elif (method.startswith("add")):
    # elif (method.startswith("remove")):
    else:
        exit("[WARNING]: You were not expecting this method " + method + " in the parseMethod function")

def parseExecutionTrace(trace: str):
    global file_content
    global current_controller_and_method

    if ("TraceId" in trace): # for now, i dont care about lines with a trace id
        return

    trace = trace.rstrip() # remove extra new lines
    # print(trace)

    split_trace: List[str] = trace.split() # split by white spaces
    # print(split_trace)

    if (len(split_trace) != 4): # According to Kieker's output, a trace seems to only have traces with 4 sections
        exit("[ERROR]: A trace with more than 4 fields was found.");
    
    # The only interesting parts of trace we want to parse are the 1st one (index 0) and 3rd one (index 2)
    traceID = getTraceID(split_trace)
    print("Trace ID: " + traceID)

    global current_traceID
    if (current_traceID != traceID): # if the previous ID is different from the new one, it means we are in a different trace
        current_traceID = traceID # and consequently, we need to start by parsing the controller first

        controller_and_method = getControllerAndMethod(split_trace)
        print("Controller.method: " + controller_and_method)

        file_content[controller_and_method] = []

        current_controller_and_method = controller_and_method

        return
    
    else: # if traceID hasn't changed, then we must continue to parse it and add the result to the current controller_and_method
        class_name, method = getClassNameAndMethod(split_trace)

        if ("_Base" not in class_name):
            return

        accessed_entity, access_type = parseMethod(method)
        print("accessed_entity: " + accessed_entity)
        print("access_type: " + access_type)

        file_content[current_controller_and_method].append([accessed_entity, access_type])
        

if __name__ == "__main__":
    command_line_args: List[str] = argv
    command_line_args_len: int = len(command_line_args)

    print("Number of arguments: " + str(command_line_args_len) + " arguments")
    print("Arguments list: " + str(command_line_args))

    if (command_line_args_len < 2):
        exit("[ERROR]: More than one command line argument was provided. Please pass the path to the file you want to parse");

    file_dir: str = command_line_args[1]

    if (not path.isfile(file_dir)):
        exit("[ERROR]: The file you entered does not exist");

    with open(file_dir) as f:
        for line in f:
            if line in ['\n', '\r\n']: # ignore blank lines
                continue

            parseExecutionTrace(line)

    # deleteControllersWithNoAccesses()
    print(json.dumps(file_content, indent = 2))
