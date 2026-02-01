package views;

import models.Node;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MapPanel extends JPanel {

    private BufferedImage mapImage;

    private List<Node> nodes = new ArrayList<>();
    private List<String[]> edges = new ArrayList<>();

    private Node selectedNode = null;
    private char nextId = 'A';

    public MapPanel() {
        try {
            mapImage = ImageIO.read(new File("src/resources/mapa.png"));
            setPreferredSize(new Dimension(
                    mapImage.getWidth(),
                    mapImage.getHeight()
            ));
        } catch (IOException e) {
            System.err.println("Error cargando el mapa: " + e.getMessage());
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                int x = e.getX();
                int y = e.getY();

                Node clickedNode = getNodeAt(x, y);

                if (clickedNode != null) {

                    if (selectedNode == null) {
                        selectedNode = clickedNode;
                    } else {
                        if (!selectedNode.id.equals(clickedNode.id)) {
                            edges.add(new String[]{selectedNode.id, clickedNode.id});
                        }
                        selectedNode = null;
                    }
                    repaint();
                }
                else {
                    String id = String.valueOf(nextId);
                    nextId++;

                    Node node = new Node(id, x, y);
                    nodes.add(node);
                    repaint();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (mapImage != null) {
            g.drawImage(mapImage, 0, 0, this);
        }

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.GRAY);
        g2.setStroke(new BasicStroke(2));

        for (String[] edge : edges) {
            Node n1 = findNode(edge[0]);
            Node n2 = findNode(edge[1]);

            if (n1 != null && n2 != null) {
                g2.drawLine(n1.x, n1.y, n2.x, n2.y);
            }
        }

        for (Node node : nodes) {
            if (node == selectedNode) {
                g2.setColor(Color.BLUE);
            } else {
                g2.setColor(Color.RED);
            }
            g2.fillOval(node.x - 6, node.y - 6, 12, 12);
            g2.setColor(Color.BLACK);
            g2.drawString(node.id, node.x + 8, node.y - 8);
        }
    }

    private Node getNodeAt(int x, int y) {
        for (Node node : nodes) {
            int dx = x - node.x;
            int dy = y - node.y;
            if (dx * dx + dy * dy <= 400) { 
                return node;
            }
        }
        return null;
    }

    private Node findNode(String id) {
        for (Node node : nodes) {
            if (node.id.equals(id)) {
                return node;
            }
        }
        return null;
    }
}
