from sys import exit, argv
from typing import List, Dict, Tuple
from os import path
import json
from json import JSONEncoder, dumps
import argparse
import re
import logging
from enum import Enum
import inspect

def lineno():
    """Returns the current line number in our program."""
    return inspect.currentframe().f_back.f_lineno

def logAndExit(message: str, lineNumber: int):
    logging.error(message)
    exit("ERROR on line " + str(lineNumber) + ": " + message)

def printAndLog(message: str, lineNumber: int):
    if (verbosity):
        print(str(lineNumber) + ": " + message)
    
    logging.info(str(lineNumber) + ": " + message)

class Node:
    def __init__(self, id: str, label: str): # label can be 
        self.id = id; # the id that Kieker attributes to the node
        self.label = label; # either the name of the controller or the accessed entity
        # the parent attribute is needed (as an optimization) when deleting intermediary nodes to form a graph with 1 depth level
        # controllers are the parents themselves. The leaf nodes will be the accessed entities. 
        self.parent: str = None; # the id given by kieker TODO Check if this is really needed or if a dfs is enough (jump useless nodes)
        self.weight: int = 0;

    def getId(self):
        return self.id

    def getLabel(self):
        return self.label

    def getWeight(self):
        return self.weight

    def setWeight(self, weight: int):
        self.weight = weight

    def __repr__(self):
        return "<Node id=%s label=%s weight=%s />" % (self.id, self.label, self.weight)
    
    def __str__(self):
        return "<Node id=%s label=%s weight=%s />" % (self.id, self.label, self.weight)

class AST:
    def __init__(self):
        self.nodes: Dict[str, Node] = {} # node_id -> Node object
        self.tree: Dict[str, List[str]] = {} # node_id -> list<child_node_id>
    
    def addNode(self, node: Node):
        self.nodes[node.id] = node;
        self.tree[node.id] = [];

    def addEdge(self, sourceNodeId: str, targetNodeId: str, weight: int):
        self.tree[sourceNodeId].append(targetNodeId)
        self.getNode(targetNodeId).setWeight(weight)

    def getNodes(self):
        return self.nodes

    def getTree(self):
        return self.tree

    def getNode(self, node_id: str):
        return self.nodes[node_id]

    def __repr__(self):
        return "<Node id=%s label=%s frequency=%s>" % (self.id, self.label, self.frequency)
    
    def __str__(self):
        string = "";

        for node_id in self.tree:
            string += str(self.nodes[node_id]) + "\n";
            
            child_node_ids = self.tree[node_id];
            for child_node_id in child_node_ids:
                string += "\t" + str(self.nodes[child_node_id]) + "\n";

        return string



class Access:
    class Type(str, Enum):
        READ = "R"
        WRITE = "W"

    def __init__(self, entityName: str, accessType: Type, frequency: int):
        self.entity = entityName
        self.type = accessType
        self.frequency = frequency
    
    def getEntity(self):
        return self.entity

    def getType(self):
        return self.type

    def getFrequency(self):
        return self.frequency

class Functionality:
    def __init__(self, label: str, frequency: int = 0):
        self.accesses_list: List[Access] = []
        self.frequency = frequency
        self.label = label

    def getLabel(self):
        return self.label

    def getAccessesList(self):
        return self.accesses_list

    def getFrequency(self):
        return self.frequency
    
    def addAccess(self, access: Access):
        self.accesses_list.append(access)
    
    def setFrequency(self, frequency: int):
        self.frequency = frequency
                
verbosity: bool = False
file_content: Dict[str, Functionality] = {} # Controler.method aka functionality label -> { label, accesses_list, frequency }
ast: AST = AST();
current_functionality_label: str = "";

def getControllerAndMethod(trace: List[str]) -> str:
    *everything_else, controller_name, controller_method = trace[2].split(sep='.')
    return controller_name + "." + controller_method

def getClassNameAndMethod(packageName: str) -> Tuple[str, str]:
    printAndLog("SPLIT PACKAGE NAME: " + str(re.findall(r"[^{\\n, \.}]\w+", packageName)), lineno())
    *everything_else, class_name, method = re.findall(r"[^{\\n, \.}]\w+", packageName)
    return class_name, method

def parseMethod(class_name: str, method: str) -> Tuple[str, str]: # (accessed entity, access type)
    printAndLog("Parsing method: " + method + " of class " + class_name, lineno())
    
    lowered_case_method = method.lower()
    if (lowered_case_method.startswith("get")):
        if (lowered_case_method.endswith("set")):
            return method[3:-3], Access.Type.READ # unfortunately I have to do this hack because I can't obtain for now the functions' return type
        
        elif (class_name.startswith("DomainRoot") or class_name == "FenixFramework"):
            return "LdoD", Access.Type.READ

        elif (class_name.endswith("_Base")):     
            return class_name[: -len("_Base")], Access.Type.READ # if it's only a get, then the entity is the class iself
        
        else:
            logAndExit(
                "You were not expecting this method " + method + "from class " + class_name + " in the parseMethod function",
                lineno()
            )
            return;

    elif (lowered_case_method.startswith("set")):
        return method[3:], Access.Type.WRITE
    elif (method.startswith("add")):
        return method[3:-1], Access.Type.WRITE # TODO speak with samuel about reads and writes
    # elif (method.startswith("remove")):
    else:
        logAndExit(
            "You were not expecting this method " + method + "from class " + class_name + " in the parseMethod function",
            lineno()
        )

def parseGraphSpec(graphElementSpec: str): # entry method to parse a Graphviz graph
    graphElementSpec = graphElementSpec.rstrip() # remove extra new lines
    printAndLog(graphElementSpec, lineno())
    
    # an Edge is a string that contains the pair <source_node_id> -> <target_node_id>
    if ("->" in graphElementSpec):
        matchedGroup: List[Tuple[str, str, str]] = re.findall("(\d+).*?->.*?(\d+).*?label=\"(.+?)\"", graphElementSpec);
        printAndLog("INCOMING EDGE: " + str(matchedGroup), lineno())

        # there can be only 1 group and the group must have 3 elements that we want to catch (sourceNode, targetNode, frequency)
        if (len(matchedGroup) != 1 or len(matchedGroup[0]) != 3):
            logAndExit(
                "Unexpected edge format: " + graphElementSpec,
                lineno()
            )
            return;

        source_node_id, target_node_id, frequency = matchedGroup[0]
        printAndLog("source_node_id: " +  source_node_id, lineno());
        printAndLog("target_node_id: " + target_node_id, lineno());
        if ((source_node_id not in ast.getNodes())):
            logAndExit(
                "An edge must be declared after the source node definition",
                lineno()
            )
            return;

        elif (target_node_id not in ast.getNodes()):
            logAndExit(
                "An edge must be declared after the target node definition",
                lineno()
            )
            return;

        else: # everything is fine, so the edge can be added to the ast
            ast.addEdge(source_node_id, target_node_id, frequency)
            return

    else:
        # we don't care about the root element which Kieker calls it "Entry"
        if ("Entry" in graphElementSpec and graphElementSpec[0] == "0"): # FIXME we actually care about the controllers frequency
            ast.addNode(Node("0", "Root")) # temporary unique root, each controller will be in the end a root by itself
            return;

        matchedGroup: List[Tuple[str, str]] = re.findall("(\d+)\[.*?label.*?\=\"\@\d+\:(.+?)\"", graphElementSpec);
        printAndLog("INCOMING NODE: " + str(matchedGroup), lineno())

        # there can be only 1 group and the group must have 2 elements that we want to catch (sourceNode, targetNode, frequency)
        if (len(matchedGroup) != 1 or len(matchedGroup[0]) != 2):
            logAndExit(
                "Unexpected node format",
                lineno()
            )
            return;

        source_node_id, source_node_label = matchedGroup[0]
        printAndLog("source_node_id: " +  source_node_id, lineno());
        printAndLog("source_node_label: " +  source_node_label, lineno());
        if ((source_node_id in ast.getNodes())):
            logAndExit(
                "The node named: " + source_node_label + " already exists in the AST",
                lineno()
            )
            return;
        
        ast.addNode(Node(source_node_id, source_node_label))
        

    # split_graphElementSpec: List[str] = graphElementSpec.split() # split by white spaces

    # controller_and_method = getControllerAndMethod(split_graphElementSpec)
    # printAndLog("Controller.method: " + controller_and_method)

    # file_content[controller_and_method] = []

    # current_controller_and_method = controller_and_method

    return

def dfs(ast: AST, startNodeId: str, visited: List[str], functionalityLabel: str):
    if startNodeId not in visited:
        visited.append(startNodeId)

        node = ast.getNode(startNodeId)
        printAndLog("Visiting node: " +  str(node), lineno())
        node_label = node.getLabel()
        node_weight = node.getWeight()
        
        if ("Root" not in node_label):
            class_name, method = getClassNameAndMethod(node_label)

            printAndLog("CLASS NAME: " + class_name, lineno())
            printAndLog("METHOD: " + method, lineno())

            if ("Controller" in class_name):
                # passing class_name cuz e.g, the getAcronym comes form the Edition(_Base) entity
                functionality_label = ".".join([class_name, method])
                file_content[functionality_label] = Functionality(functionality_label, node_weight)

                functionalityLabel = functionality_label

            elif ("_Base" in class_name):
                accessed_entity, access_type = parseMethod(class_name, method) 
                printAndLog("Accessed entity: " + str(accessed_entity), lineno())
                printAndLog("Access type: " + str(access_type), lineno())

                file_content[functionalityLabel].addAccess(Access(accessed_entity, access_type, node_weight))

        graph = ast.getTree()
        for child_node in graph[startNodeId]:
            dfs(ast, child_node, visited, functionalityLabel)
    
    return

def generateJson():
    dfs(ast, "0", [], "")

def checkFileExists(file_dir: str):
    if path.isfile(file_dir):
        return file_dir
    else:
        logAndExit(
            "The file " + file_dir + " you entered does not exist",
            lineno()
        )

def checkDirectoryExists(dir_path: str):
    if path.isdir(dir_path):
        return dir_path
    else:
        logAndExit(
            "The directory " + dir_path + " does not exist",
            lineno()
        )


def parseCommandLineArguments() -> Dict[str, object]:
    ap = argparse.ArgumentParser()
    ap.add_argument("-i", "--inputdir", type=checkFileExists, required=True, help="directory of the file to be parsed", metavar="")
    ap.add_argument("-o", "--outputdir", help="directory for the generated file", default="./", metavar="")
    ap.add_argument("-v", "--verbose", action="store_true", help="logging enabler")
    
    args = vars(ap.parse_args())
    return args

if __name__ == "__main__":
    logging.basicConfig(filename="assemblyCallTreeToJson.log", filemode="w", format="%(message)s", level=logging.DEBUG)

    args = parseCommandLineArguments()

    input_file_dir: str = args["inputdir"]
    output_file_dir: str = args["outputdir"]
    verbosity = args["verbose"]

    printAndLog(str(args), lineno())

    with open(input_file_dir) as f:
        for line in f:
            if line in ['\n', '\r\n'] or not line[0].isdigit(): # ignore blank lines and lines that don't start by a number
                continue

            parseGraphSpec(line)
    
    generateJson()
    printAndLog(str(json.dumps(file_content, default=lambda o: o.__dict__, indent=2, sort_keys=True)), lineno())



    # with open(output_file_dir, 'w') as file:
    #     file.write(json.dumps(file_content, indent = 2))
