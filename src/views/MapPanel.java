package views;

import controllers.GraphController;
import models.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class MapPanel extends JPanel {
    private GraphController controller;
    private Node selectedNode = null;
    private List<Node> path;
    private List<VisitedEdge> vEdges;
    private int eIdx = 0;
    private boolean exploring = false, eDone = true;
    private Timer timer;
    private Image bg;

    public MapPanel(GraphController gc) {
        this.controller = gc;
        this.bg = new ImageIcon("src/resources/mapa.png").getImage();
        
        // IMPORTANTE: Hacer que el panel acepte clics
        this.setFocusable(true);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Buscamos si hay un nodo cerca
                Node clickedNode = controller.getGraph().findNodeNear(e.getX(), e.getY(), 25);

                if (clickedNode == null) {
                    // CLIC EN VACÍO -> CREAR NODO
                    String id = "N" + (controller.getGraph().getNodes().size() + 1);
                    controller.getGraph().addNode(new Node(id, e.getX(), e.getY()));
                    selectedNode = null; 
                } else {
                    // CLIC EN NODO -> SELECCIONAR O CONECTAR
                    if (selectedNode == null) {
                        selectedNode = clickedNode;
                    } else if (selectedNode != clickedNode) {
                        controller.getGraph().addEdge(selectedNode.getId(), clickedNode.getId());
                        selectedNode = null; 
                    } else {
                        selectedNode = null; 
                    }
                }
                repaint(); // Forzar redibujado para ver el nuevo nodo
            }
        });
    }

    // Métodos de apoyo para el MainFrame
    public Node getSelectedNode() { return selectedNode; }
    public void clearSelection() { this.selectedNode = null; this.path = null; repaint(); }

    public void setPath(List<Node> p, List<VisitedEdge> ve, boolean em) {
        if (timer != null) timer.stop();
        this.path = p;
        this.vEdges = ve;
        this.exploring = em;
        this.eIdx = 0;
        
        if (em && ve != null && !ve.isEmpty()) {
            this.eDone = false;
            timer = new Timer(50, e -> {
                if (eIdx < vEdges.size()) {
                    eIdx++;
                    repaint();
                } else {
                    timer.stop();
                    // PAUSA DE 2 SEGUNDOS ANTES DEL ROJO
                    Timer pause = new Timer(2000, ev -> {
                        this.eDone = true;
                        repaint();
                    });
                    pause.setRepeats(false);
                    pause.start();
                }
            });
            timer.start();
        } else {
            this.eDone = true;
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (bg != null) {
            g2.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
        }

        // Dibujar Aristas (Negras)
        g2.setStroke(new BasicStroke(1));
        g2.setColor(Color.BLACK);
        for (Node a : controller.getGraph().getNodes()) {
            for (Node b : controller.getGraph().getNeighbors(a.getId())) {
                g2.drawLine(a.getX(), a.getY(), b.getX(), b.getY());
            }
        }

        // Dibujar Exploración (Verde)
        if (exploring && vEdges != null) {
            g2.setColor(new Color(52, 168, 83));
            g2.setStroke(new BasicStroke(3));
            for (int i = 0; i < eIdx; i++) {
                VisitedEdge ve = vEdges.get(i);
                g2.drawLine(ve.getFrom().getX(), ve.getFrom().getY(), ve.getTo().getX(), ve.getTo().getY());
            }
        }

        // Dibujar Ruta Final (Roja)
        if (path != null && !path.isEmpty() && eDone) {
            g2.setColor(new Color(234, 67, 53));
            g2.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (int i = 0; i < path.size() - 1; i++) {
                g2.drawLine(path.get(i).getX(), path.get(i).getY(), path.get(i+1).getX(), path.get(i+1).getY());
            }
        }

        // Dibujar Nodos (Azul / Amarillo)
        for (Node n : controller.getGraph().getNodes()) {
            g2.setColor(n == selectedNode ? Color.YELLOW : new Color(25, 103, 210));
            g2.fillOval(n.getX() - 12, n.getY() - 12, 24, 24);
            g2.setColor(Color.WHITE);
            g2.drawString(n.getId(), n.getX() - 8, n.getY() + 5);
        }
    }
}