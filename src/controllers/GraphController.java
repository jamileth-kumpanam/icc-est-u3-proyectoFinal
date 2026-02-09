package controllers;
import models.*;
import utils.TimeLogger;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
public class GraphController {
    private Graph graph;
    private BufferedImage mapImage;
    public GraphController(Graph graph) {
        this.graph = graph;
        try {
            File file = new File("data/mapa.jpg");
            if (file.exists()) mapImage = ImageIO.read(file);
        } catch (Exception e) {}
    }
    public Graph getGraph() { return graph; }
    public SearchResult buscarRuta(String metodo, Node inicio, Node fin) {
        long start = System.nanoTime();
        SearchResult res = metodo.equals("BFS") ? SearchController.bfs(graph, this, inicio, fin) : SearchController.dfs(graph, this, inicio, fin);
        long end = System.nanoTime();
        TimeLogger.log(metodo, (end - start) / 1e6, res.getPath().size());
        return res;
    }
    public boolean esCaminoSeguro(Node a, Node b) {
        if (mapImage == null) return true;
        int precision = 20;
        for (int i = 0; i <= precision; i++) {
            int px = a.getX() + (b.getX() - a.getX()) * i / precision;
            int py = a.getY() + (b.getY() - a.getY()) * i / precision;
            if (px >= 0 && px < mapImage.getWidth() && py >= 0 && py < mapImage.getHeight()) {
                Color c = new Color(mapImage.getRGB(px, py));
                if (c.getRed() < 150 && c.getGreen() < 150 && c.getBlue() < 150) return false;
            }
        }
        return true;
    }
}