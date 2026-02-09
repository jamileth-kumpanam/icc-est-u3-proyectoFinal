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
            
            // Si llegamos al destino, terminamos la búsqueda
            if (current.getId().equals(end.getId())) break;

            for (Node neighbor : g.getNeighbors(current.getId())) {
                // Validación de seguridad y bloqueo
                if (!visited.contains(neighbor.getId()) && !neighbor.isBlocked()) {
                    // Si tienes la lógica de esCaminoSeguro, mantenla aquí:
                    // if (gc.esCaminoSeguro(current, neighbor)) { ... }
                    
                    visited.add(neighbor.getId());
                    prev.put(neighbor.getId(), current.getId());
                    
                    // IMPORTANTE: Registrar la arista para la animación verde
                    visitedEdges.add(new VisitedEdge(current, neighbor));
                    visitedNodes.add(neighbor);
                    
                    queue.add(neighbor);
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

            if (current.getId().equals(end.getId())) break;

            for (Node neighbor : g.getNeighbors(current.getId())) {
                if (!visited.contains(neighbor.getId()) && !neighbor.isBlocked()) {
                    
                    // En DFS, guardamos quién descubrió a quién para la animación
                    prev.put(neighbor.getId(), current.getId());
                    visitedEdges.add(new VisitedEdge(current, neighbor));
                    
                    stack.push(neighbor);
                }
            }
        }
        return new SearchResult(reconstructPath(g, prev, start.getId(), end.getId()), visitedNodes, visitedEdges);
    }

    private static List<Node> reconstructPath(Graph g, Map<String, String> prev, String startId, String endId) {
        List<Node> path = new ArrayList<>();
        
        // Si el destino nunca fue alcanzado y no es el mismo nodo de inicio
        if (!prev.containsKey(endId) && !startId.equals(endId)) return path;

        String at = endId;
        while (at != null) {
            Node n = g.getNode(at);
            if (n != null) path.add(n);
            at = prev.get(at);
            if (at != null && at.equals(startId)) {
                path.add(g.getNode(startId));
                break;
            }
        }
        Collections.reverse(path);
        return path;
    }
}