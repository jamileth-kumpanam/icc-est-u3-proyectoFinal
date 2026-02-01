package views;

import javax.swing.JFrame;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Ruta óptima - BFS / DFS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MapPanel mapPanel = new MapPanel();
        add(mapPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
