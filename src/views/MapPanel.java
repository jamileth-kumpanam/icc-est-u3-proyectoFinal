package views;

import controllers.GraphController;
import models.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class MapPanel extends JPanel {
    private GraphController controller;
    private Node selectedNode = null; // Nodo origen para la conexión
    private List<Node> path; 
    private List<VisitedEdge> vEdges;
    private int eIdx = 0;
    private boolean exploring = false, eDone = true;
    private Timer timer;
    private Image bg;

    public MapPanel(GraphController gc) {
        this.controller = gc;
        this.bg = new ImageIcon("src/resources/mapa.png").getImage();

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Node clickedNode = controller.getGraph().findNodeNear(e.getX(), e.getY(), 20);

                if (clickedNode == null) {
                    // Si no hay nodo cerca, creamos uno nuevo
                    String id = "N" + (controller.getGraph().getNodes().size() + 1);
                    controller.getGraph().addNode(new Node(id, e.getX(), e.getY()));
                    selectedNode = null; 
                } else {
                    // Si hacemos clic en un nodo...
                    if (selectedNode == null) {
                        // Seleccionamos el primero (Origen)
                        selectedNode = clickedNode;
                    } else if (selectedNode != clickedNode) {
                        // Si ya había uno seleccionado, conectamos con el segundo (Destino)
                        controller.getGraph().addEdge(selectedNode.getId(), clickedNode.getId());
                        selectedNode = null; // Resetear selección
                    } else {
                        selectedNode = null; // Deseleccionar si clicamos el mismo
                    }
                }
                repaint();
            }
        });
    }

    public void setPath(List<Node> p, List<VisitedEdge> ve, boolean em) {
        this.path = p; this.vEdges = ve; this.exploring = em; this.eDone = !em; this.eIdx = 0;
        if (timer != null) timer.stop();
        if (em && ve != null) {
            timer = new Timer(50, e -> {
                if (eIdx < vEdges.size()) { eIdx++; repaint(); }
                else { eDone = true; timer.stop(); repaint(); }
            });
            timer.start();
        } else repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. Dibujar conexiones existentes (Negro)
        g2.setStroke(new BasicStroke(1));
        g2.setColor(Color.BLACK);
        for (Node a : controller.getGraph().getNodes()) {
            for (Node b : controller.getGraph().getNeighbors(a.getId())) {
                g2.drawLine(a.getX(), a.getY(), b.getX(), b.getY());
            }
        }

        // 2. Trazar RUTA ROJA (Modo Resultado)
        if (path != null && !path.isEmpty() && eDone) {
            g2.setColor(Color.RED);
            g2.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (int i = 0; i < path.size() - 1; i++) {
                g2.drawLine(path.get(i).getX(), path.get(i).getY(), 
                           path.get(i+1).getX(), path.get(i+1).getY());
            }
        }

        // 3. Dibujar Nodos (Azul / Amarillo si está seleccionado)
        for (Node n : controller.getGraph().getNodes()) {
            g2.setColor(n == selectedNode ? Color.YELLOW : Color.BLUE);
            g2.fillOval(n.getX() - 12, n.getY() - 12, 24, 24);
            g2.setColor(Color.WHITE);
            g2.drawString(n.getId(), n.getX() - 8, n.getY() + 5);
        }
    }
}