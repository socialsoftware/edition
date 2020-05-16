from sys import exit, argv
from typing import List
from os import path

def getTraceID(trace: List[str]) -> str:
    return trace[0][1] # e.g, <5[0,0]

def getController(trace: List[str]) -> str:
    *everything_else, controller_name, controller_method = trace[2].split(sep='.')
    return controller_name + "." + controller_method

def parseExecutionTrace(trace: str):
    if ("TraceId" in trace): # for now, i dont care about lines with a trace id
        return

    trace = trace.rstrip() # remove extra new lines
    print(trace)

    split_trace: List[str] = trace.split() # split by white spaces
    print(split_trace)

    if (len(split_trace) != 4): # According to Kieker's output, a trace seems to only have traces with 4 sections
        exit("[ERROR]: A trace with more than 4 fields was found.");
    
    # The only interesting parts of trace we want to parse are the 1st one (index 0) and 3rd one (index 2)
    print("Trace ID: " + getTraceID(split_trace))
    print("Controller.method: " + getController(split_trace))

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
