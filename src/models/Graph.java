package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {

    private Map<String, Node> nodes = new HashMap<>();
    private Map<String, List<String>> adjList = new HashMap<>();

    public void addNode(Node node) {
        
        nodes.put(node.id, node);
        adjList.put(node.id, new ArrayList<>());
    }

    public void addEdge(String from, String to) {
        adjList.get(from).add(to);
        adjList.get(to).add(from);
    }

    public Map<String, List<String>> getAdjList() {
        return adjList;
    }
}
