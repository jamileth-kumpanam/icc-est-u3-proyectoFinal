package views;

import controllers.GraphController;
import models.*;
import utils.FileManager;
import utils.TimeLogger;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class MainFrame extends JFrame {
    private Graph g = new Graph();
    private GraphController gc;
    private MapPanel mp;
    
    // UI Components
    private JTextField tS = new JTextField("N1"), tE = new JTextField("N4");
    private JComboBox<String> cb = new JComboBox<>(new String[]{"BFS", "DFS"});
    private JCheckBox chkExp = new JCheckBox("Modo Exploración");
    private JLabel lblEstado = new JLabel("Estado: Listo");

    public MainFrame() {
        gc = new GraphController(g);
        FileManager.loadGraph(g, "grafo.txt");
        
        setTitle("SafeCity Maps - Sistema de Navegación Inteligente");
        setSize(1300, 850);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);

        // Usamos JLayeredPane para poner los botones ENCIMA del mapa
        JLayeredPane layeredPane = new JLayeredPane();
        
        // 1. EL MAPA (Ocupa todo el fondo)
        mp = new MapPanel(gc);
        mp.setBounds(0, 0, 1300, 850);
        layeredPane.add(mp, JLayeredPane.DEFAULT_LAYER);

        // 2. PANEL DE BÚSQUEDA FLOTANTE (Estilo Google)
        JPanel searchPanel = createFloatingPanel();
        searchPanel.setBounds(20, 20, 320, 450);
        
        // Estilo de los componentes internos
        searchPanel.setLayout(new GridLayout(0, 1, 10, 10));
        searchPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("SafeCity Navigation");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(new Color(25, 103, 210)); // Azul Google

        searchPanel.add(title);
        searchPanel.add(new JLabel("Punto de Partida:"));
        styleTextField(tS); searchPanel.add(tS);
        
        searchPanel.add(new JLabel("Destino Final:"));
        styleTextField(tE); searchPanel.add(tE);
        
        searchPanel.add(new JLabel("Algoritmo de Ruta:"));
        searchPanel.add(cb);
        
        chkExp.setOpaque(false);
        searchPanel.add(chkExp);

        JButton btnBuscar = createModernButton("Buscar Ruta", new Color(25, 103, 210));
        searchPanel.add(btnBuscar);

        // 3. BARRA DE HERRAMIENTAS INFERIOR (Acciones secundarias)
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        toolBar.setOpaque(false);
        toolBar.setBounds(350, 740, 600, 60);

        JButton btnLimpiar = createModernButton("Limpiar Todo", new Color(217, 48, 37));
        JButton btnMetricas = createModernButton("Métricas", Color.DARK_GRAY);
        JButton btnDelNodo = createModernButton("Eliminar Nodo", Color.DARK_GRAY);
        
        toolBar.add(btnLimpiar);
        toolBar.add(btnMetricas);
        toolBar.add(btnDelNodo);

        layeredPane.add(searchPanel, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(toolBar, JLayeredPane.PALETTE_LAYER);

        add(layeredPane);

        // LÓGICA DE EVENTOS (Mantenemos la misma)
        btnBuscar.addActionListener(e -> {
            Node n1 = g.getNode(tS.getText().trim());
            Node n2 = g.getNode(tE.getText().trim());
            if (n1 != null && n2 != null) {
                SearchResult res = gc.buscarRuta((String)cb.getSelectedItem(), n1, n2);
                mp.setPath(res.getPath(), res.getVisitedEdges(), chkExp.isSelected());
            }
        });

        btnLimpiar.addActionListener(e -> {
            g.getNodes().clear();
            mp.setPath(null, null, false);
            repaint();
        });

        btnMetricas.addActionListener(e -> TimeLogger.showTable());
        
        btnDelNodo.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("ID del nodo:");
            if(id != null) { g.removeNode(id); repaint(); }
        });
    }

    // --- MÉTODOS DE ESTILO PARA EL LOOK "GOOGLE" ---

    private JPanel createFloatingPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 240)); // Blanco traslúcido
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        return panel;
    }

    private void styleTextField(JTextField field) {
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(218, 220, 224), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    private JButton createModernButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return btn;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}