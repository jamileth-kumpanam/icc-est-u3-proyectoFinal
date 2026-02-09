package models;
import java.util.*;

public class Graph {
    private Map<String, Node> nodes = new HashMap<>();
    private Map<String, List<String>> adjList = new HashMap<>();

    public void addNode(Node node) {
        nodes.put(node.getId(), node);
        adjList.putIfAbsent(node.getId(), new ArrayList<>());
    }

    public void addEdge(String id1, String id2) {
        if (nodes.containsKey(id1) && nodes.containsKey(id2)) {
            if (!adjList.get(id1).contains(id2)) adjList.get(id1).add(id2);
            if (!adjList.get(id2).contains(id1)) adjList.get(id2).add(id1);
        }
    }

    public void removeNode(String id) {
        nodes.remove(id);
        adjList.remove(id);
        for (List<String> neighbors : adjList.values()) {
            neighbors.remove(id);
        }
    }

    public Node getNode(String id) { return nodes.get(id); }
    public Collection<Node> getNodes() { return nodes.values(); }
    public List<Node> getNeighbors(String id) {
        List<Node> neighbors = new ArrayList<>();
        for (String nid : adjList.getOrDefault(id, new ArrayList<>())) {
            neighbors.add(nodes.get(nid));
        }
        return neighbors;
    }

    public Node findNodeNear(int x, int y, int tolerance) {
        for (Node n : nodes.values()) {
            double dist = Math.sqrt(Math.pow(n.getX() - x, 2) + Math.pow(n.getY() - y, 2));
            if (dist < tolerance) return n;
        }
        return null;
    }

    public void removeEdge(String id1, String id2) {
    if (adjList.containsKey(id1)) {
        adjList.get(id1).remove(id2);
    }
    if (adjList.containsKey(id2)) {
        adjList.get(id2).remove(id1);
    }
}

    
}