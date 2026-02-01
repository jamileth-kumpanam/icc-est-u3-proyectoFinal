package controllers;

import java.util.*;
import models.Graph;

public class SearchController {

    public List<String> bfs(Graph graph, String start, String end) {
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> parent = new HashMap<>();

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();

            if (current.equals(end)) break;

            for (String neighbor : graph.getAdjList().get(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parent.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }
        return buildPath(parent, start, end);
    }

    public List<String> dfs(Graph graph, String start, String end) {
        Set<String> visited = new HashSet<>();
        Map<String, String> parent = new HashMap<>();

        dfsRecursive(start, end, graph, visited, parent);
        return buildPath(parent, start, end);
    }

    private boolean dfsRecursive(String current, String end, Graph graph,
                                 Set<String> visited, Map<String, String> parent) {

        visited.add(current);

        if (current.equals(end)) return true;

        for (String neighbor : graph.getAdjList().get(current)) {
            if (!visited.contains(neighbor)) {
                parent.put(neighbor, current);
                if (dfsRecursive(neighbor, end, graph, visited, parent))
                    return true;
            }
        }
        return false;
    }

    private List<String> buildPath(Map<String, String> parent,
                                   String start, String end) {

        List<String> path = new ArrayList<>();
        String current = end;

        while (current != null && !current.equals(start)) {
            path.add(current);
            current = parent.get(current);
        }

        if (current == null) return path; // no hay ruta

        path.add(start);
        Collections.reverse(path);
        return path;
    }
}
