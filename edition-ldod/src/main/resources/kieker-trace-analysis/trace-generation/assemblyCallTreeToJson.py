from sys import exit, argv
from typing import List, Dict, Tuple
from os import path
import json
from json import JSONEncoder, dumps
import argparse
import re
import logging
from enum import Enum  

class Node:
    def __init__(self, id: str, label: str): # label can be 
        self.id = id; # the id that Kieker attributes to the node
        self.label = label; # either the name of the controller or the accessed entity
        # the parent attribute is needed (as an optimization) when deleting intermediary nodes to form a graph with 1 depth level
        # controllers are the parents themselves. The leaf nodes will be the accessed entities. 
        self.parent: str = None; # the id given by kieker TODO Check if this is really needed or if a dfs is enough (jump useless nodes)
        self.frequency: int = 0;

    def getId(self):
        return self.id

    def getLabel(self):
        return self.label

    def getFrequency(self):
        return self.frequency

    def __repr__(self):
        return "<Node id=%s label=%s frequency=%s />" % (self.id, self.label, self.frequency)
    
    def __str__(self):
        return "<Node id=%s label=%s frequency=%s />" % (self.id, self.label, self.frequency)

class AST:
    def __init__(self):
        self.nodes: Dict[str, Node] = {} # node_id -> Node object
        self.tree: Dict[str, List[str]] = {} # node_id -> list<child_node_id>
    
    def addNode(self, node: Node):
        self.nodes[node.id] = node;
        self.tree[node.id] = [];

    def addEdge(self, sourceNodeId: str, targetNodeId: str):
        self.tree[sourceNodeId].append(targetNodeId)

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
    class Type(Enum):
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
    def __init__(self, label: str):
        self.accesses_list: List[Access] = []
        self.frequency: int = 0
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

def logAndExit(message: str):
    logging.error(message)
    exit("[ERROR]: " + message)

def printAndLog(message: str):
    if (verbosity):
        print(message)
    
    logging.info(message)

def getControllerAndMethod(trace: List[str]) -> str:
    *everything_else, controller_name, controller_method = trace[2].split(sep='.')
    return controller_name + "." + controller_method

def getClassNameAndMethod(packageName: str) -> Tuple[str, str]:
    printAndLog("SPLIT PACKAGE NAME: " + str(re.findall(r"[^{\\n, \.}]\w+", packageName)))
    *everything_else, class_name, method = re.findall(r"[^{\\n, \.}]\w+", packageName)
    return class_name, method

def parseMethod(class_name: str, method: str) -> Tuple[str, str]: # (accessed entity, access type)
    printAndLog("Parsing method: " + method + " of class " + class_name)
    
    lowered_case_method = method.lower()
    if (lowered_case_method.startswith("get")):
        if (lowered_case_method.endswith("set")):
            return method[3:-4], Access.Type.READ # unfortunately I have to do this hack because I can't obtain for now the functions' return type
        
        elif (class_name.startswith("DomainRoot") or class_name == "FenixFramework"):
            return "LdoD", Access.Type.READ

        elif (class_name.endswith("_Base")):     
            return class_name[: -len("_Base")], Access.Type.READ # if it's only a get, then the entity is the class iself
        
        else:
            logAndExit("You were not expecting this method " + method + "from class " + class_name + " in the parseMethod function")
            return;

    elif (lowered_case_method.startswith("set")):
        return method[3:], Access.Type.WRITE
    elif (method.startswith("add")):
        return method[3:-1], Access.Type.WRITE # TODO speak with samuel about reads and writes
    # elif (method.startswith("remove")):
    else:
        logAndExit("You were not expecting this method " + method + "from class " + class_name + " in the parseMethod function")

def parseGraphSpec(graphElementSpec: str): # entry method to parse a Graphviz graph
    graphElementSpec = graphElementSpec.rstrip() # remove extra new lines
    printAndLog(graphElementSpec)
    
    # an Edge is a string that contains the pair <source_node_id> -> <target_node_id>
    if ("->" in graphElementSpec):
        matchedGroup: List[Tuple[str, str, str]] = re.findall("(\d+).*?->.*?(\d+).*?label=\"(.+?)\"", graphElementSpec);
        printAndLog("INCOMING EDGE: " + str(matchedGroup))

        # there can be only 1 group and the group must have 3 elements that we want to catch (sourceNode, targetNode, frequency)
        if (len(matchedGroup) != 1 or len(matchedGroup[0]) != 3):
            logAndExit("Unexpected edge format: " + graphElementSpec)
            return;

        source_node_id, target_node_id, frequency = matchedGroup[0]
        printAndLog("source_node_id: " +  source_node_id);
        printAndLog("target_node_id: " + target_node_id);
        if ((source_node_id not in ast.getNodes())):
            logAndExit("An edge must be declared after the source node definition")
            return;

        elif (target_node_id not in ast.getNodes()):
            logAndExit("An edge must be declared after the target node definition")
            return;

        else: # everything is fine, so the parsing can start
            ast.addEdge(source_node_id, target_node_id)
            return

    else:
        # we don't care about the root element which Kieker calls it "Entry"
        if ("Entry" in graphElementSpec and graphElementSpec[0] == "0"): # FIXME we actually care about the controllers frequency
            ast.addNode(Node("0", "Root")) # temporary unique root, each controller will be in the end a root by itself
            return;

        matchedGroup: List[Tuple[str, str]] = re.findall("(\d+)\[.*?label.*?\=\"\@\d+\:(.+?)\"", graphElementSpec);
        printAndLog("INCOMING NODE: " + str(matchedGroup))

        # there can be only 1 group and the group must have 2 elements that we want to catch (sourceNode, targetNode, frequency)
        if (len(matchedGroup) != 1 or len(matchedGroup[0]) != 2):
            logAndExit("Unexpected node format")
            return;

        source_node_id, source_node_label = matchedGroup[0]
        printAndLog("source_node_id: " +  source_node_id);
        printAndLog("source_node_label: " +  source_node_label);
        if ((source_node_id in ast.getNodes())):
            logAndExit("The node named: " + source_node_label + " already exists in the AST")
            return;
        
        ast.addNode(Node(source_node_id, source_node_label))
        

    # split_graphElementSpec: List[str] = graphElementSpec.split() # split by white spaces

    # controller_and_method = getControllerAndMethod(split_graphElementSpec)
    # printAndLog("Controller.method: " + controller_and_method)

    # file_content[controller_and_method] = []

    # current_controller_and_method = controller_and_method

    return

def dfs(ast: AST, startNodeId: str, visited: List[str], functionalityLabel: str):
    global current_functionality_label

    if startNodeId not in visited:
        visited.append(startNodeId)

        node = ast.getNode(startNodeId)
        printAndLog("Visiting node: " +  str(node))
        node_label = node.getLabel()
        
        if ("Root" not in node_label):
            class_name, method = getClassNameAndMethod(node_label)

            printAndLog("CLASS NAME: " + class_name)
            printAndLog("METHOD: " + method)

            if ("Controller" in class_name):
                # passing class_name cuz e.g, the getAcronym comes form the Edition(_Base) entity
                functionality_label = ".".join([class_name, method])
                file_content[functionality_label] = Functionality(functionality_label)

                functionalityLabel = functionality_label

            elif ("_Base" in class_name):
                accessed_entity, access_type = parseMethod(class_name, method) 
                printAndLog("Accessed entity: " + str(accessed_entity))
                printAndLog("Access type: " + str(access_type))

                file_content[functionalityLabel].addAccess(Access(accessed_entity, access_type, 0))

        graph = ast.getTree()
        for child_node in graph[startNodeId]:
            dfs(ast, child_node, visited, functionalityLabel)
    
    return visited

def generateJson():
    visited = dfs(ast, "0", [], "")
    printAndLog(visited)

def checkFileExists(file_dir: str):
    if path.isfile(file_dir):
        return file_dir
    else:
        logAndExit("The file " + file_dir + " you entered does not exist")

def checkDirectoryExists(dir_path: str):
    if path.isdir(dir_path):
        return dir_path
    else:
        logAndExit("The directory " + dir_path + " does not exist")


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

    printAndLog(args)

    with open(input_file_dir) as f:
        for line in f:
            if line in ['\n', '\r\n'] or not line[0].isdigit(): # ignore blank lines and lines that don't start by a number
                continue

            parseGraphSpec(line)
    
    printAndLog(ast)

    generateJson()

    class Encoder(JSONEncoder):
        def default(self, o):
            return o.__dict__

    print(json.dumps(file_content))



    # with open(output_file_dir, 'w') as file:
    #     file.write(json.dumps(file_content, indent = 2))
