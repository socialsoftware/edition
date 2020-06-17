from sys import exit, argv
from typing import List, Dict, Tuple, Optional
from os import path
import json
import argparse
import re
import logging
import inspect
from enum import Enum
from ast import literal_eval 

class BaseMethod:
    def __init__(self, methodName: str, argumentTypes: List[str], className: str, returnType: str): 
        self.name = methodName
        self.argumentTypes = argumentTypes
        self.className = className
        self.returnType = returnType
    
    def getName(self) -> str:
        return self.name

    def getArgumentTypes(self) -> List[str]:
        return self.argumentTypes

    def getClassName(self) -> str:
        return self.className

    def getReturnType(self) -> str:
        return self.returnType

class Access:
    class Type(str, Enum):
        READ = "R"
        WRITE = "W"

    # FIXME maybe add a 3rd field responsible for keep tracking the depth of this access for comparison reasons
    def __init__(self, entityName: str, accessType: Type): 
        self.entity = entityName
        self.type = accessType
        self.call_order = -1
        self.stack_depth = -1
    
    def getEntity(self) -> str:
        return self.entity

    def getType(self) -> Type:
        return self.type

    def getCallOrder(self) -> int:
        return self.call_order

    def getStackDepth(self) -> int:
        return self.stack_depth

    def setCallOrderAndStackDepth(self, callOrder: int, stackDepth: int) -> None:
        self.call_order = callOrder
        self.stack_depth = stackDepth

    def __repr__(self):
        return "<Access entity=%s type=%s call_order=%s stack_depth=%s>" % (self.entity, self.type.name, self.call_order, self.stack_depth)

    def __eq__(self, other):
        return self.__dict__ == other.__dict__

class Trace:
    def __init__(self, label: str, frequency: int = 0):
        self.label = label
        self.frequency = frequency
        self.accesses_list: List[Access] = []
    
    def getLabel(self) -> str:
        return self.label

    def getAccessesList(self) -> List[Access]:
        return self.accesses_list

    def getFrequency(self) -> int:
        return self.frequency

    def addAccess(self, access: Access) -> None:
        self.accesses_list.append(access)

    def setFrequency(self, frequency: int) -> None:
        self.frequency = frequency

    def increaseFrequency(self) -> None:
        self.frequency += 1

    def __eq__(self, other):
        return self.getAccessesList() == other.getAccessesList()

class Functionality:
    def __init__(self, label: str, frequency: int = 0):
        self.label = label
        self.frequency = frequency
        self.traces_list: List[Trace] = []

    def getLabel(self) -> str:
        return self.label

    def getFrequency(self) -> int:
        return self.frequency

    def getTracesList(self) -> List[Trace]:
        return self.traces_list
    
    def setFrequency(self, frequency: int) -> None:
        self.frequency = frequency

    def increaseFrequency(self) -> None:
        self.frequency += 1
    
    def addTrace(self, new_trace: Trace) -> None:
        printAndLog("Adding trace: " + new_trace.getLabel(), lineno())
        # if trace already exists (increase its frequency)
        trace_already_exists = False

        for saved_trace in self.traces_list:
            # saved_trace_accesses_list = saved_trace.getAccessesList()
            # new_trace_accesses_list = new_trace.getAccessesList()

            # saved_trace_accesses_list_length = len(saved_trace_accesses_list)
            # new_trace_accesses_list_length = len(new_trace_accesses_list)

            if(saved_trace == new_trace):
                printAndLog("Trace already exists, raising its frequency...", lineno()) 

                saved_trace.increaseFrequency()  
                trace_already_exists = True     
                break;
            
            # if (saved_trace_accesses_list_length != new_trace_accesses_list_length):
            #     continue;
            
            # same_accesses_list = True

            # for i in range(saved_trace_accesses_list_length):
            #     if(saved_trace_accesses_list[i] != new_trace_accesses_list[i]):
            #         same_accesses_list = False
            #         break;
            
            # if (same_accesses_list):
            #     saved_trace.increaseFrequency()  
            #     trace_already_exists = True     
            #     break;         

        if (not trace_already_exists):
            printAndLog("Trace didn't exist yet, adding it to the traces list", lineno()) 
            self.traces_list.append(new_trace)

        self.increaseFrequency()

verbosity: bool = True
file_content: Dict[str, Functionality] = {}
current_kieker_traceID: str = "-1"
current_functionality_label: str = ""
current_trace: Trace = None
base_methods = None # to save in memory the file that contains all base methods and their specs
domain_entities = None # to save in memory the file that contains all domain entities

def lineno():
    """Returns the current line number in our program."""
    return inspect.currentframe().f_back.f_lineno

def logAndExit(message: str, lineNumber: int) -> None:
    logging.error(message)
    exit("ERROR on line " + str(lineNumber) + ": " + message)

def printAndLog(message: str, lineNumber: int) -> None:
    if (verbosity):
        print(str(lineNumber) + ": " + message, flush=True)
    
    logging.info(str(lineNumber) + ": " + message)

def deleteFunctionalitiesWithNoAccesses():
    file_content_copy: Dict[str, Functionality] = file_content.copy() # to avoid RuntimeError: dictionary changed size during iteration

    for functionality_label in file_content_copy:
        hasAccesses = False
        for trace in file_content_copy[functionality_label].getTracesList():
            if (trace.getAccessesList()):
                hasAccesses = True
                break
        
        if (not hasAccesses):
            del file_content[functionality_label]

def getTraceID(trace: List[str]) -> str:
    return re.findall("\<([0-9]+)\[", trace[0])[0] # e.g, trace[0] = "<5[0,0]"

def getMethodCallOrderAndStackDepth(trace: List[str]) -> Tuple[int, int]:
    call_order, stack_depth = re.findall("([0-9]+),([0-9]+)", trace[0])[0] # e.g, trace[0] = "<5[0,0]"
    return call_order, stack_depth

def getClassNameAndMethod(trace: List[str]) -> Tuple[str, str]:
    *everything_else, class_name, method = trace[2].split(sep='.')
    return class_name, method

def getMethodEntityAccesses(class_name: str, method: str) -> List[Access]: # (accessed entity, access type)
    printAndLog("Parsing method: " + method + " of class " + class_name, lineno())
    
    class_name_and_method = ".".join([class_name, method])

    if (class_name_and_method in base_methods):
        base_method = base_methods[class_name_and_method]
        accesses_list = []
        lowered_case_method = method.lower()

        if (lowered_case_method.startswith("get")): 
            accesses_list.append(Access(base_method["declaringType"][: -len("_Base")], Access.Type.READ))
            
            if (base_method["returnType"] in domain_entities):
                accesses_list.append(Access(base_method["returnType"], Access.Type.READ))

            return accesses_list        
            
        elif (lowered_case_method.startswith("set") or method.startswith("add") or method.startswith("remove")):
            accesses_list.append(Access(base_method["declaringType"][: -len("_Base")], Access.Type.WRITE))

            for arg_type in base_method["argumentTypes"]:            
                if (arg_type in domain_entities):
                    accesses_list.append(Access(arg_type, Access.Type.WRITE))

            return accesses_list
        else:
            logAndExit(
                "[WARNING]: You were not expecting the method " + method + " from class " + class_name + " on the getMethodEntityAccesses function",
                lineno()
            )
    else:
        logAndExit(
            "[WARNING]: You were not expecting the method " + method + " from class " + class_name + " on the getMethodEntityAccesses function",
            lineno()
        )

def parseExecutionTrace(trace: str) -> None:
    global file_content
    global current_functionality_label
    global current_kieker_traceID
    global current_trace

    if ("TraceId" in trace): # for now, i dont care about lines with a trace id
        return

    trace = trace.rstrip() # remove extra new lines

    split_trace: List[str] = trace.split() # split by white spaces

    if (len(split_trace) != 4): # According to Kieker's output, a trace seems to only have traces with 4 sections
        logAndExit("[ERROR]: A trace with more than 4 fields was found.", lineno());
    
    # The only interesting parts of a trace we want to parse are the 1st one (index 0) and 3rd one (index 2)
    traceID = getTraceID(split_trace)
    printAndLog("Trace ID: " + traceID, lineno())

    if (current_kieker_traceID != traceID): # if the previous ID is different from the new one, it means we are in a different trace
        if (current_trace is not None):
            file_content[current_functionality_label].addTrace(current_trace)
            current_trace = None

        current_kieker_traceID = traceID

        class_name, method = getClassNameAndMethod(split_trace)

        current_functionality_label = ".".join([class_name, method])
        printAndLog("Functionality: " + current_functionality_label, lineno())

        if (current_functionality_label not in file_content):
            file_content[current_functionality_label] = Functionality(current_functionality_label)
            current_trace = Trace("-".join([current_functionality_label, "0"]), 1)
        
        else:
            current_trace = Trace(
                "-".join([current_functionality_label, str(file_content[current_functionality_label].getFrequency())]),
                 1
            )
    
    else: # if traceID hasn't changed, then we must continue to parse the current one and add the access to the current_trace
        class_name, method = getClassNameAndMethod(split_trace)
        if (class_name == "FenixFramework"): 
            return # FIXME right now we can't discover which entities are being read when FenixFramework.getDomainObject is executed
        elif ("_Base" not in class_name):
            return

        call_order, stackDepth = getMethodCallOrderAndStackDepth(split_trace)
        accesses = getMethodEntityAccesses(class_name, method)

        for access in accesses:
            printAndLog(str(access), lineno())
            current_trace.addAccess(access.setCallOrderAndStackDepth(call_order, stackDepth))

def checkFileExists(file_dir: str) -> Optional[str]:
    if path.isfile(file_dir):
        return file_dir
    else:
        logAndExit("[ERROR]: The file " + file_dir + " you entered does not exist", lineno())

def checkDirectoryExists(dir_path: str) -> Optional[str]:
    if path.isdir(dir_path):
        return dir_path
    else:
        logAndExit("[ERROR]: The directory " + dir_path + " does not exist", lineno())


def parseCommandLineArguments() -> Dict[str, object]:
    ap = argparse.ArgumentParser()
    ap.add_argument("-i", "--inputDir", type=checkFileExists, required=True, help="directory of the file to be parsed", metavar="")
    ap.add_argument("-o", "--outputDir", help="directory for the generated file", default="./ldod.json", metavar="")
    ap.add_argument("-bm", "--baseMethods", type=checkFileExists, required=True, help="directory of the json file containing the _Base methods and their specification", metavar="")
    ap.add_argument("-de", "--domainEntities", type=checkFileExists, required=True, help="directory of the json file containing the all domain entities", metavar="")
    ap.add_argument("-v", "--verbose", action="store_true", help="logging enabler")
    
    args = vars(ap.parse_args())
    return args

if __name__ == "__main__":
    logging.basicConfig(filename="executionTracesToJson.log", filemode="w", format="%(message)s", level=logging.DEBUG)

    args = parseCommandLineArguments()

    input_file_dir: str = args["inputDir"]
    output_file_dir: str = args["outputDir"]
    base_methods_file_dir: str = args["baseMethods"]
    domain_entities_file_dir: str = args["domainEntities"]

    verbosity = args["verbose"]

    printAndLog(str(args), lineno())

    with open(base_methods_file_dir) as base_methods_file:
        base_methods = json.load(base_methods_file)

    with open(domain_entities_file_dir) as domain_entities_file:
        domain_entities = json.load(domain_entities_file)
            
    with open(input_file_dir) as f:
        for line in f:
            if line in ['\n', '\r\n']: # ignore blank lines
                continue

            parseExecutionTrace(line)

    deleteFunctionalitiesWithNoAccesses()

    # printAndLog(str(json.dumps(file_content, default=lambda o: o.__dict__, indent=2, sort_keys=False)), lineno())
    
    with open(output_file_dir, 'w') as file:
        file.write(json.dumps(file_content, default=lambda o: o.__dict__, indent = 2, sort_keys=False))
