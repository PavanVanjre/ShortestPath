import java.io.*;
import java.util.*;
import heap.*;

class Vertex implements Comparable<Vertex>{
    String name;
    boolean isDown;

    public Vertex(String name) {
        this.name = name;
        this.isDown = false;
    }

    public void setStatus(boolean status) { //method to set isDown Value
        this.isDown = status;
    }
    public boolean getStatus() {
       return this.isDown;
    } //method to return if isDown value

    @Override
    public int compareTo(Vertex other) {
        return this.name.compareTo(other.name);
    }
}

class Edge implements Comparable<Edge>{
    Vertex start;
    Vertex end;
    float weight;
    boolean isDown;

    public Edge(Vertex start, Vertex end, float weight) {
        this.start = start;
        this.end = end;
        this.weight = weight;
        this.isDown = false;
    }

    public boolean getStatus() { //method to return if isDown value
        return this.isDown;
    }

    public void setStatus(boolean status) {
        this.isDown = status;
    } //method to set isDown Value

    @Override
    public int compareTo(Edge other) {
        return this.end.name.compareTo(other.end.name);
    }
}

class Graph {
    List<Vertex> vertices;
    Map<Vertex, List<Edge>> adjacencyList;
     List<Edge> edges;

    public Graph() {
        this.vertices = new ArrayList<>();
        this.adjacencyList = new HashMap<>();
        this.edges = new ArrayList<>();
    }

    public void addVertex(String vertexName) {
        Vertex vertex = new Vertex(vertexName);
        vertices.add(vertex);
        adjacencyList.put(vertex, new ArrayList<>());
    }

    public void addEdge(String startVertex, String endVertex, float weight, Boolean isGraphBuilder) {
        Vertex start = getVertex(startVertex);
        Vertex end = getVertex(endVertex);

        if (start != null && end != null) {
            Edge edge = new Edge(start, end, weight);

            if(!isGraphBuilder){
                System.out.println(adjacencyList.get(start));
                for(Edge e:adjacencyList.get(start) ){
                    if(e.end == end && e.start == start){
                        e.weight = weight;
                        return;
                    }
                }
            }

            adjacencyList.get(start).add(edge);
            edges.add(edge);

            if(isGraphBuilder){
                Edge reverseEdge = new Edge(end, start, weight);
                adjacencyList.get(end).add(reverseEdge);
                edges.add(reverseEdge);
            }

        }
    }

    public void deleteEdge(String startVertex, String endVertex) {
        Vertex start = getVertex(startVertex);
        Vertex end = getVertex(endVertex);

        if (start != null && end != null) {
            List<Edge> edges = adjacencyList.get(start);
            edges.removeIf(edge -> edge.end == end);
        }
    }
    public Vertex getVertex(String vertexName) {
        for (Vertex vertex : vertices) {
            if (vertex.name.equals(vertexName)) {
                return vertex;
            }
        }
        return null;
    }

    public void printAdjacencyList() {
        for (Vertex vertex : vertices) {
            System.out.print(vertex.name + " ->");

            List<Edge> edges = adjacencyList.get(vertex);
            for (Edge edge : edges) {
                System.out.print(" " + edge.end.name + " (" + edge.weight + ")");
            }
            System.out.println();
        }
    }


    public void printNetwork() {
        // To print is alphabetic order I have referred to https://www.geeksforgeeks.org/sorting-custom-object-by-implementing-comparable-interface-in-java/
        Collections.sort(vertices);
        StringBuilder result = new StringBuilder();
        for (Vertex vertex : vertices) {

            System.out.println(vertex.name + ((vertex.getStatus()) ? " DOWN" : ""));  // to print conditionally referred https://stackoverflow.com/questions/19599880/if-statement-inside-the-print-statement
            result.append(vertex.name + ((vertex.getStatus()) ? " DOWN" : "") + "\n");
            List<Edge> edges = adjacencyList.get(vertex);
            Collections.sort(edges);

            for (Edge edge : edges) {
                System.out.println("    " + edge.end.name + " " + edge.weight + ((edge.getStatus()) ? " DOWN" : ""));
                result.append("    " + edge.end.name + " " + edge.weight + ((edge.getStatus()) ? " DOWN" : "") + "\n");
            }
        }
        writeToFile(result.toString());
    }

    // THis method is for dijkstra using inbuilt priority queue
    public List<String> findShortestPath(String fromVertexName, String toVertexName) {
        Map<Vertex, Float> distance = new HashMap<>();
        Map<Vertex, Vertex> previous = new HashMap<>();
        PriorityQueue<Vertex> priorityQueue = new PriorityQueue<>(Comparator.comparing(distance::get));

        Vertex source = getVertex(fromVertexName);
        Vertex destination = getVertex(toVertexName);

        if (source == null || destination == null) {
            // Handle invalid vertices
            return Collections.emptyList();
        }

        for (Vertex vertex : vertices) {
            distance.put(vertex, Float.MAX_VALUE);
            previous.put(vertex, null);
        }


        distance.put(source, 0.0f);
        priorityQueue.add(source);

        while (!priorityQueue.isEmpty()) {
            Vertex currentVertex = priorityQueue.poll();

            if (currentVertex.getStatus()) {
                continue;
            }

            if (currentVertex == destination) {
                // Destination reached, construct the path
                List<String> path = new ArrayList<>();
                double totalWeight = distance.get(destination);
                while (previous.get(currentVertex) != null) {
                    path.add(0, currentVertex.name);
                    currentVertex = previous.get(currentVertex);
                }
                path.add(0, source.name);
                System.out.println("totalWeight   ->>>>" +totalWeight );
                path.add(String.format("%.2f", totalWeight));
                return path;
            }

            for (Edge edge : getOutgoingEdges(currentVertex)) {
                Vertex neighbor = edge.end;

                if (edge.getStatus()) {
                    continue;
                }

                float newDistance = distance.get(currentVertex) + edge.weight;

                if (newDistance < distance.get(neighbor)) {
                    distance.put(neighbor, newDistance);
                    previous.put(neighbor, currentVertex);
                    priorityQueue.add(neighbor);
                }
            }
        }

        // No path found
        return Collections.emptyList();
    }

    private List<Edge> getOutgoingEdges(Vertex vertex) {
        List<Edge> outgoingEdges = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.start == vertex && !edge.isDown) {
                outgoingEdges.add(edge);
            }
        }
        return outgoingEdges;
    }

    //for implementing dijkstra using min heap I have taken   from https://tutorialhorizon.com/algorithms/dijkstras-shortest-path-algorithm-spt-adjacency-list-and-min-heap-java-implementation/
    public List<String> dijkstra_GetMinDistances(String sourceVertexName, String destinationVertexName){
        Vertex sourceVertex = getVertex(sourceVertexName);
        Vertex destinationVertex = getVertex(destinationVertexName);
        Map<Vertex, Vertex> previous = new HashMap<>();
        Map<Vertex, Float> distance = new HashMap<>();

        if (sourceVertex == null || destinationVertex == null ) {
            // Handle invalid vertices
            return Collections.emptyList();
        }

        for (Vertex vertex : vertices) {
            distance.put(vertex, Float.MAX_VALUE);
            previous.put(vertex, null);
        }


        distance.put(sourceVertex, 0.0f);
        //add all the vertices to the MinHeap
        MinHeap minHeap = new MinHeap(vertices.size());
        for (int i = 0; i <vertices.size() ; i++) {
            minHeap.insert(vertices.get(i).name,Float.MAX_VALUE);
        }
        minHeap.updateNodeDistance(sourceVertexName,0.0f);
        //while minHeap is not empty
        while(!minHeap.isEmpty()){
            //extract the min
            HeapNode extractedNode = minHeap.extractMin();


            //extracted vertex
            String extractedVertex = extractedNode.getName();
            Vertex currentVertex = getVertex(extractedVertex);

            if (currentVertex.getStatus()) {
                continue;
            }
            if (currentVertex == destinationVertex) {
                // Destination reached, construct the path
                List<String> path = new ArrayList<>();
                while (previous.get(currentVertex) != null) {
                    path.add(0, currentVertex.name);
                    currentVertex = previous.get(currentVertex);
                }
                path.add(0, sourceVertexName);
                path.add(String.format("%.2f", distance.get(destinationVertex)));
                return path;
            }

            //iterate through all the adjacent vertices
            List<Edge> list = adjacencyList.get(getVertex(extractedVertex));
            for (int i = 0; i <list.size() ; i++) {
                Edge edge = list.get(i);

                if (edge.getStatus()) {
                    continue;
                }
                Vertex destination = edge.end;

                    Float newDistance = distance.get(currentVertex) + edge.weight;

                    if(newDistance < distance.get(destination)){
                       distance.put(destination, newDistance);
                        minHeap.updateNodeDistance(destination.name,newDistance);
                        previous.put(destination, getVertex(extractedVertex));
                    }

//                }
            }
        }
       return Collections.emptyList();
    }

    public void printReachableVertices() {
        Collections.sort(vertices);
        StringBuilder result = new StringBuilder();
        for (Vertex vertex : vertices) {
            if(!vertex.getStatus()){
                List<Vertex> reachableNodes = findReachableNodesDFS(vertex);
                Collections.sort(reachableNodes);
                System.out.println( vertex.name + " ");
                result.append(vertex.name + " " + "\n");
                for(Vertex v: reachableNodes){
                    System.out.println("  " + v.name);
                    result.append("  " + v.name + "\n");
                }
                result.append("\n");
                System.out.println();
            }
        }
        writeToFile(result.toString());
    }

    public void writeToFile(String Content) {
        try {
            // Create a FileWriter with append mode
            FileWriter fileWriter = new FileWriter("Output.txt", true);

            // Create a BufferedWriter for efficient writing
            BufferedWriter writer = new BufferedWriter(fileWriter);

            // Write some content to the file
            writer.write(Content);

            // Close the writer to release resources
            writer.close();

            System.out.println("Data has been written to the file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Vertex> findReachableNodesDFS(Vertex sourceVertex) {
        Set<Vertex> visited = new HashSet<>();
        List<Vertex> reachableNodes = new ArrayList<>();
        Stack<Vertex> stack = new Stack<>();

        stack.push(sourceVertex);

        while (!stack.isEmpty()) {
            Vertex currentVertex = stack.pop();

            if (!visited.contains(currentVertex)) {
                visited.add(currentVertex);
                if(currentVertex != sourceVertex && !currentVertex.getStatus()) {
                    reachableNodes.add(currentVertex);
                }
                for (Edge edge : getOutgoingEdges(currentVertex)) {
                    Vertex neighbor = edge.end;

                    // Check if both the edge and vertex are not down
                    if (!edge.getStatus() && !neighbor.getStatus() && !visited.contains(neighbor)) {
                        stack.push(neighbor);
                    }
                }
            }
        }

        return reachableNodes;
    }
}

public class ShortestPathNetwork {
//    public static void main(String[] args) {
//
//        Graph graph = null;
//        boolean graphConstructed = false;
//        System.out.println("***  Shortest Paths in a Network  ***");
//
//
//        Scanner scanner = new Scanner(System.in);
//        // Run an infinite loop to continuously read commands
//        while (true) {
//            System.out.print("Enter a command (type 'quit' to exit): ");
//            String command = scanner.nextLine();
//
//            if (command.equalsIgnoreCase("quit")) {
//                System.out.println("Quiting program...");
//                break;
//            }
//
//            System.out.println("You entered: " + command);
//            String[] parts = command.split("\\s+");
//            if (!graphConstructed && !parts[0].equalsIgnoreCase("graph")) {
//                System.out.println("Please construct the graph first.");
//                continue;
//            }
//
//            switch (parts[0]) {
//                case "graph" -> {
//                    graph = buildGraphFromFile(parts[1]);
//                    graphConstructed = true;
//                }
//                case "addedge" -> {
//                    assert graph != null;
//                    if (!graph.vertices.contains(graph.getVertex(parts[1]))) {
//                        graph.addVertex(parts[1]);
//                    }
//                    if (!graph.vertices.contains(graph.getVertex(parts[2]))) {
//                        graph.addVertex(parts[2]);
//                    }
//                    graph.addEdge(parts[1], parts[2], Float.parseFloat(parts[3]));
//                }
//                case "deleteedge" -> {
//                    assert graph != null;
//                    graph.deleteEdge(parts[1], parts[2]);
//                }
//                case "edgedown" -> {
//                    assert graph != null;
//                    Vertex startVertex = graph.getVertex(parts[1]);
//                    Vertex endVertex = graph.getVertex(parts[2]);
//
//                    if (startVertex != null && endVertex != null) {
//                        List<Edge> edges = graph.adjacencyList.get(startVertex);
//                        for (Edge edge : edges) {
//                            if (edge.end == endVertex) {
//                                edge.isDown = true;
//                                break;
//                            }
//                        }
//                    }
//                }
//                case "edgeup" -> {
//                    assert graph != null;
//                    Vertex startVertex = graph.getVertex(parts[1]);
//                    Vertex endVertex = graph.getVertex(parts[2]);
//
//                    if (startVertex != null && endVertex != null) {
//                        List<Edge> edges = graph.adjacencyList.get(startVertex);
//                        for (Edge edge : edges) {
//                            if (edge.end == endVertex) {
//                                edge.setStatus(false);
//                                break;
//                            }
//                        }
//                    }
//                }
//                case "vertexdown" -> {
//                    assert graph != null;
//                    Vertex v = graph.getVertex(parts[1]);
//                    v.setStatus(true);
//                }
//                case "vertexup" -> {
//                    assert graph != null;
//                    Vertex v = graph.getVertex(parts[1]);
//                    v.setStatus(false);
//                }
//                case "path" -> {
//                    graph.dijkstra_GetMinDistances(parts[1], parts[2]);
//                    List<String> result;
//                    result = graph.dijkstra_GetMinDistances(parts[1], parts[2]);
//                    if(result.size() ==0){
//                        System.out.println("No Path");
//                        break;
//                    }
//                    for (int i = 0; i < result.size(); i++) {
//                        System.out.print(result.get(i) + " ");
//                    }
//                    System.out.println();
//                }
//                case "print" -> {
//                    graph.printNetwork();
//                }
//                case "reachable" -> {
//                    graph.printReachableVertices();
//                }
//            }
//        }
//
//        // Close the scanner to avoid resource leaks
//        scanner.close();
//
//    }
public static void main(String[] args) {
    if (args.length == 0) {
        System.out.println("Usage: java ShortestPathGraph <filename>");
        System.exit(1);
    }

    String fileName = args[0];

    Graph graph = buildGraphFromFile(fileName);

    if (graph != null) {
        System.out.println();
        graph.printNetwork();
    }

    String queryFileName = args[1];
    System.out.println("queryFileName" + queryFileName);
    graphModifyFromQueryFile(queryFileName, graph);

}

    public static Graph buildGraphFromFile(String fileName) {
        Graph graph = new Graph();

        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNext()) {
                String startVertex = scanner.next();
                String endVertex = scanner.next();
                float weight = scanner.nextFloat();

                if (!graph.vertices.contains(graph.getVertex(startVertex))) {
                    graph.addVertex(startVertex);
                }

                if (!graph.vertices.contains(graph.getVertex(endVertex))) {
                    graph.addVertex(endVertex);
                }

                graph.addEdge(startVertex, endVertex, weight,true);
            }
            System.out.println("Graph Constructed");
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + fileName);
            return null;
        }

        return graph;
    }

    public static void graphModifyFromQueryFile(String fileName, Graph graph) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                String[] parts = line.split("\\s+");
                switch (parts[0]) {
                case "graph" -> {
                    graph = buildGraphFromFile(parts[1]);
                }
                case "addedge" -> {
                    assert graph != null;
                    if (!graph.vertices.contains(graph.getVertex(parts[1]))) {
                        graph.addVertex(parts[1]);
                    }
                    if (!graph.vertices.contains(graph.getVertex(parts[2]))) {
                        graph.addVertex(parts[2]);
                    }
                    graph.addEdge(parts[1], parts[2], Float.parseFloat(parts[3]),false);
                }
                case "deleteedge" -> {
                    assert graph != null;
                    graph.deleteEdge(parts[1], parts[2]);
                }
                case "edgedown" -> {
                    assert graph != null;
                    Vertex startVertex = graph.getVertex(parts[1]);
                    Vertex endVertex = graph.getVertex(parts[2]);

                    if (startVertex != null && endVertex != null) {
                        List<Edge> edges = graph.adjacencyList.get(startVertex);
                        for (Edge edge : edges) {
                            if (edge.end == endVertex) {
                                edge.isDown = true;
                                break;
                            }
                        }
                    }
                }
                case "edgeup" -> {
                    assert graph != null;
                    Vertex startVertex = graph.getVertex(parts[1]);
                    Vertex endVertex = graph.getVertex(parts[2]);

                    if (startVertex != null && endVertex != null) {
                        List<Edge> edges = graph.adjacencyList.get(startVertex);
                        for (Edge edge : edges) {
                            if (edge.end == endVertex) {
                                edge.setStatus(false);
                                break;
                            }
                        }
                    }
                }
                case "vertexdown" -> {
                    assert graph != null;
                    Vertex v = graph.getVertex(parts[1]);
                    v.setStatus(true);
                }
                case "vertexup" -> {
                    assert graph != null;
                    Vertex v = graph.getVertex(parts[1]);
                    v.setStatus(false);
                }
                case "path" -> {
                    graph.dijkstra_GetMinDistances(parts[1], parts[2]);
                    List<String> result;
                    StringBuilder content = new StringBuilder();
                    result = graph.dijkstra_GetMinDistances(parts[1], parts[2]);
                    if(result.size() ==0){
                        System.out.println("No Path");
                        content.append("No Path from " + parts[1] + " " + parts[2] + " \n");
                        graph.writeToFile(content.toString());
                        break;
                    }
                    for (int i = 0; i < result.size(); i++) {
                        System.out.print(result.get(i) + " ");
                        content.append(result.get(i) + " ");
                    }
                    content.append("\n");
                    System.out.println();
                    graph.writeToFile(content.toString());
                }
                case "print" -> {
                    graph.printNetwork();
                }
                case "reachable" -> {
                    graph.printReachableVertices();
                }
            }
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
