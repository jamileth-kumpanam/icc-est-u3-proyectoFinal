package views;

import controllers.GraphController;
import models.*;
import utils.TimeLogger;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class MainFrame extends JFrame {
    private Graph g = new Graph();
    private GraphController gc;
    private MapPanel mp;
    private JTextField tS = new JTextField("N1"), tE = new JTextField("N2");
    private JComboBox<String> cb = new JComboBox<>(new String[]{"BFS", "DFS"});
    private JCheckBox chkExp = new JCheckBox("Modo Exploración");

    public MainFrame() {
        gc = new GraphController(g);
        setTitle("Sebastian Lopez y Jamileth Kumpanam 67.01 - Proyecto Final");
        setSize(1300, 850);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLayeredPane layeredPane = new JLayeredPane();
        mp = new MapPanel(gc);
        mp.setBounds(0, 0, 1300, 850);
        layeredPane.add(mp, JLayeredPane.DEFAULT_LAYER);

        
        JPanel sidePanel = createFloatingPanel();
        sidePanel.setBounds(25, 25, 300, 580); 
        sidePanel.setLayout(new GridLayout(0, 1, 8, 8));
        sidePanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel logo = new JLabel("Proyecto Final", SwingConstants.CENTER);
        logo.setFont(new Font("SansSerif", Font.BOLD, 20));
        logo.setForeground(new Color(25, 103, 210));

        // Diseño de los botones con colores personalizados
        JButton btnBuscar = createBtn("BUSCAR RUTA", new Color(25, 103, 210));
        JButton btnEliminarNodo = createBtn("ELIMINAR NODO", new Color(217, 48, 37));
        JButton btnEliminarEdge = createBtn("ELIMINAR CONEXIÓN", new Color(244, 180, 0)); 
        JButton btnLimpiar = createBtn("LIMPIAR TODO", Color.GRAY);
        JButton btnMetricas = createBtn("VER MÉTRICAS", Color.DARK_GRAY);

        sidePanel.add(logo);
        sidePanel.add(new JLabel("Desde:")); sidePanel.add(tS);
        sidePanel.add(new JLabel("Hasta:")); sidePanel.add(tE);
        sidePanel.add(new JLabel("Algoritmo:")); sidePanel.add(cb);
        sidePanel.add(chkExp);
        sidePanel.add(btnBuscar);
        sidePanel.add(btnEliminarNodo);
        sidePanel.add(btnEliminarEdge); 
        sidePanel.add(btnLimpiar);
        sidePanel.add(btnMetricas);

        layeredPane.add(sidePanel, JLayeredPane.PALETTE_LAYER);
        add(layeredPane);

        // --- LÓGICA DEL BOTÓN ELIMINAR CONEXIÓN ---
        btnEliminarEdge.addActionListener(e -> {
            String id1 = tS.getText().trim();
            String id2 = tE.getText().trim();

            if (g.getNode(id1) != null && g.getNode(id2) != null) {
                g.removeEdge(id1, id2);
                mp.setPath(null, null, false); // Limpiar ruta resaltada si existía
                repaint();
                JOptionPane.showMessageDialog(this, "Conexión entre " + id1 + " y " + id2 + " eliminada.");
            } else {
                JOptionPane.showMessageDialog(this, "Asegúrate de poner IDs válidos en los campos 'Desde' y 'Hasta'.");
            }
        });

        
        btnEliminarNodo.addActionListener(e -> {
            Node seleccionado = mp.getSelectedNode();
            if (seleccionado != null) {
                g.removeNode(seleccionado.getId());
                mp.clearSelection();
                repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Primero toca un nodo en el mapa para seleccionarlo.");
            }
        });

        btnBuscar.addActionListener(e -> {
            Node start = g.getNode(tS.getText().trim());
            Node end = g.getNode(tE.getText().trim());
            if (start != null && end != null) {
                SearchResult res = gc.buscarRuta((String)cb.getSelectedItem(), start, end);
                mp.setPath(res.getPath(), res.getVisitedEdges(), chkExp.isSelected());
            }
        });

        btnLimpiar.addActionListener(e -> { g.getNodes().clear(); mp.clearSelection(); repaint(); });
        btnMetricas.addActionListener(e -> TimeLogger.showTable());
    }

    private JPanel createFloatingPanel() {
        JPanel p = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 245));
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 25, 25));
                g2.dispose();
            }
        };
        p.setOpaque(false);
        return p;
    }

    private JButton createBtn(String t, Color c) {
        JButton b = new JButton(t);
        b.setBackground(c); b.setForeground(Color.WHITE);
        b.setFocusPainted(false); b.setBorderPainted(false);
        b.setFont(new Font("SansSerif", Font.BOLD, 11));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}