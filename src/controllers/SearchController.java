package controllers;
import models.*;
import java.util.*;
public class SearchController {
    public static SearchResult bfs(Graph g, GraphController gc, Node start, Node end) {
        Queue<Node> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> prev = new HashMap<>();
        List<Node> visitedNodes = new ArrayList<>();
        List<VisitedEdge> visitedEdges = new ArrayList<>();
        queue.add(start);
        visited.add(start.getId());
        visitedNodes.add(start);
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            if (current.equals(end)) break;
            for (Node neighbor : g.getNeighbors(current.getId())) {
                if (!visited.contains(neighbor.getId()) && !neighbor.isBlocked() && gc.esCaminoSeguro(current, neighbor)) {
                    queue.add(neighbor);
                    visited.add(neighbor.getId());
                    prev.put(neighbor.getId(), current.getId());
                    visitedNodes.add(neighbor);
                    visitedEdges.add(new VisitedEdge(current, neighbor));
                }
            }
        }
        return new SearchResult(reconstructPath(g, prev, start.getId(), end.getId()), visitedNodes, visitedEdges);
    }
    public static SearchResult dfs(Graph g, GraphController gc, Node start, Node end) {
        Stack<Node> stack = new Stack<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> prev = new HashMap<>();
        List<Node> visitedNodes = new ArrayList<>();
        List<VisitedEdge> visitedEdges = new ArrayList<>();
        stack.push(start);
        while (!stack.isEmpty()) {
            Node current = stack.pop();
            if (visited.contains(current.getId())) continue;
            visited.add(current.getId());
            visitedNodes.add(current);
            if (current.equals(end)) break;
            for (Node neighbor : g.getNeighbors(current.getId())) {
                if (!visited.contains(neighbor.getId()) && !neighbor.isBlocked() && gc.esCaminoSeguro(current, neighbor)) {
                    stack.push(neighbor);
                    if (!prev.containsKey(neighbor.getId())) {
                        prev.put(neighbor.getId(), current.getId());
                        visitedEdges.add(new VisitedEdge(current, neighbor));
                    }
                }
            }
        }
        return new SearchResult(reconstructPath(g, prev, start.getId(), end.getId()), visitedNodes, visitedEdges);
    }
    private static List<Node> reconstructPath(Graph g, Map<String, String> prev, String startId, String endId) {
        List<Node> path = new ArrayList<>();
        if (!prev.containsKey(endId) && !startId.equals(endId)) return path;
        String at = endId;
        while (at != null) {
            path.add(g.getNode(at));
            at = prev.get(at);
        }
        Collections.reverse(path);
        return path;
    }
}