Pavan Vanjre Ravindranath <br />

Programming Project 2 : Shortest Paths in a Network

The programming project is about constructing the graph with given vertices and edges
and finding the shortest path between any two vertices considering the edge and vertices condition.
Shortest path is calculated using dijkstra algorithm.

Algorithm: Dijkstra 
Input: A graph represented as an adjacency list and Non-negative weights associated with edges.

Initialization:

Create a distance array to store the current shortest distance from the source to each node. <br />
Initialize distances to all nodes as infinity, except for the source node (distance[source] = 0). <br />
Use a priority queue or a min-heap to keep track of nodes with their current distance values.  <br />

Algorithm Steps:

Start with the source node.  <br />
For the current node, update the distances to its neighbors if a shorter path is found. <br />
Add the neighbors to the priority queue.  <br />
Extract the node with the smallest distance from the priority queue.  <br />
Repeat steps 2-4 until all nodes have been processed.  <br />


To represent the graph the adjacency list is used and is implemented using a map, with vertex as key and list of edges as values for the key. <br />
The Vertex is a Class which consist of name and a property isDown to check whether the vertex is down or not.
The Edge  is a Class which consist of start and end vertex and weight associated with it. <br />
The Graph class consist of different method like addVertex() to create a vertex, addEdge() to create the edge ,
deleteEdge() to remove the edge from the graph,  printNetwork() to print the graph is specified format, dijkstra_GetMinDistances() method to calcuate and return the path between given two vertex that is optimal
and findReachableNodesDFS() to list all possible vertices that can be reached with given vertex

To find all the reachable nodes, the DFS algorithm is used

DFS(Graph G, Node start): <br />
// Assume all nodes are initially unvisited <br />
for each node v in G: <br />
mark v as unvisited <br />

  Start the DFS from the given node <br />
  DFSVisit(start) <br />

DFSVisit(Node v): <br />
// Mark the current node as visited <br />
mark v as visited <br />

  Process the current node (optional) <br />

  Explore all adjacent nodes <br />
    for each neighbor u of v: <br />
        if u is unvisited: <br />
            // Recursively visit the unvisited neighbor <br />
            DFSVisit(u) <br />

Time complexity: 
The time complexity of DFS is O(|V+E|), but here we are finding for all vertices the
time complexity would be O(|V(|V+E|)|).

Programming Language Used: JAVA  
Compiler Version 21.0.1

Folder Structure <br />
The main folder consist of ShortestPathNetwork.java which consist of all main logic for the code,
and next consist of heap folder where code for min heap is present in MinHeap.java and code for heap node is present in HeapNode.java

Steps to run the programs <br />

Firstly the ShortestPathNetwork.java file needs to be compiled using java command
<br /> `javac ShortestPathNetwork.java`
<br /> run the code using  along with the file name from which graph needs to build and file name from which queries come
<br />`java ShortestPathNetwork network.txt queries.txt`

[//]: # (Once the code is running, it accepts all information through command line.)

[//]: # (Firstly to construct the graph the command)

[//]: # (`graph <filename>`)

[//]: # (needs to be given and then rest of the queries can be given)
Once the code is running, it build the graph from the input file and then start reading each queries and prints the respective output to a "Output.txt" file.

what work well  
1) If queries are given without constructing the graph, it gives error and prompts the user to construct the graph first. 
2) Finds the shortest distance and prints the path
3) Finds all the reachable nodes at given time
4) Prints the graph and indicates the vertices and edges that are down.

what fails
1) while giving the queries if the line of arguments dont match it gives array of of bound error, which needs to be handled to give proper error message.

