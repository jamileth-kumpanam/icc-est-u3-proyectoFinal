package utils;
import models.*;
import java.io.*;
import java.util.*;
public class FileManager {
    public static void saveGraph(Graph g, String file) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            pw.println("NODES");
            for (Node n : g.getNodes()) pw.println(n.getId() + "," + n.getX() + "," + n.getY() + "," + n.isBlocked());
            pw.println("EDGES");
            Set<String> done = new HashSet<>();
            for (Node n : g.getNodes()) {
                for (Node nb : g.getNeighbors(n.getId())) {
                    String s = n.getId().compareTo(nb.getId()) < 0 ? n.getId() + "," + nb.getId() : nb.getId() + "," + n.getId();
                    if (done.add(s)) pw.println(s);
                }
            }
        } catch (Exception e) {}
    }
    public static void loadGraph(Graph g, String file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line, mode = "";
            while ((line = br.readLine()) != null) {
                if (line.equals("NODES") || line.equals("EDGES")) { mode = line; continue; }
                String[] p = line.split(",");
                if (mode.equals("NODES")) {
                    Node n = new Node(p[0], Integer.parseInt(p[1]), Integer.parseInt(p[2]));
                    if (p.length > 3) n.setBlocked(Boolean.parseBoolean(p[3]));
                    g.addNode(n);
                } else g.addEdge(p[0], p[1]);
            }
        } catch (Exception e) {}
    }
}