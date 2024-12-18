# **Shortest Paths in a Network**
---

### **Overview**
This project involves constructing a graph with given vertices and edges and finding the shortest path between any two vertices while considering conditions on edges and vertices. The primary goal is to implement **Dijkstra's Algorithm** to calculate the shortest paths efficiently and manage network changes dynamically.

### **Features**
1. **Graph Construction**: Build an adjacency list representation of the network graph.
2. **Dijkstra's Algorithm**: Calculate the shortest path between vertices using non-negative weights.
3. **Dynamic Graph Updates**:
   - Add or delete edges.
   - Mark edges and vertices as up or down.
4. **Reachable Nodes**: Identify all reachable nodes from a given vertex using **Depth First Search (DFS)**.
5. **Graph Display**: Print the graph in a specified format, indicating the status of vertices and edges.
6. **Error Handling**: 
   - Prompts for missing input files or incorrect commands.
   - Ensures graph is built before queries are processed.

---

### **Algorithm Details**

#### **1. Dijkstra's Algorithm**
- **Input**: A graph represented as an adjacency list and non-negative edge weights.
- **Output**: Shortest path and its total cost between two vertices.
- **Steps**:
  1. Initialize distances for all vertices (`infinity` except for the source).
  2. Use a min-heap (priority queue) for efficient distance updates.
  3. Relax edges and update neighbors iteratively.
  4. Stop when all vertices are processed.

#### **2. Reachable Nodes**
- Implemented using DFS:
  - Marks nodes as visited and recursively explores all unvisited neighbors.
  - Outputs reachable nodes for each vertex in alphabetical order.

#### **Time Complexity**:
- **Dijkstra's Algorithm**: \(O((V + E) \log V)\) using a binary heap.
- **DFS**: \(O(V + E)\) per vertex.

---

### **Implementation Details**

#### **Data Structures**
- **Graph**: Represented using an adjacency list (map with vertex as the key and edges as the value).
- **Vertex Class**:
  - Stores vertex name and status (`up` or `down`).
- **Edge Class**:
  - Stores start vertex, end vertex, and weight.
- **Graph Methods**:
  - `addVertex()`: Create a new vertex.
  - `addEdge()`: Add a directed edge between vertices.
  - `deleteEdge()`: Remove a directed edge.
  - `printNetwork()`: Print graph details.
  - `dijkstra_GetMinDistances()`: Find shortest path between vertices.
  - `findReachableNodesDFS()`: Identify reachable nodes.

#### **Folder Structure**
- **Main Logic**: `ShortestPathNetwork.java`
- **Supporting Classes**:
  - `MinHeap.java` (for priority queue implementation).
  - `HeapNode.java` (nodes used in the heap).

---

### **Steps to Run**

1. **Compile the Program**:
   ```bash
   javac ShortestPathNetwork.java
   ```
2. **Run the Program**:
    ```bash
    java ShortestPathNetwork <network_file> <queries_file>
    ```
    - **Example**
        ```bash
        java ShortestPathNetwork network.txt queries.txt
        ```
3. Output:
    - The results of the queries are written to ```Output.txt```.

## Sample Input

**network.txt**:
```plaintext
Belk Grigg 1.2
Belk Health 0.5
Duke Belk 0.6
Belk Woodward 0.25
...
```
**network.txt**:
```plaintext
addedge A B 1.5
deleteedge A B
path A B
print
reachable
quit
```

## What Works Well
- Prompts the user to build the graph first before processing queries.
- Successfully finds the shortest paths and reachable nodes.
- Handles dynamic updates to vertices and edges.
- Prints the graph, including the status of vertices and edges.

## Known Issues
- Improper query formats result in an ArrayIndexOutOfBoundsException instead of a user-friendly error message.
- No built-in mechanism for handling large datasets efficiently beyond the constraints of memory.

## Programming Language
- Java, version 21.0.1
