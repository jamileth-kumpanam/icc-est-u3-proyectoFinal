import java.io.File;

import javax.swing.SwingUtilities;

import views.MainFrame;
    
public class App {
    public static void main(String[] args) {
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        // Ejecuta la aplicación en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            MainFrame gui = new MainFrame();
            gui.setLocationRelativeTo(null); // Centra la ventana en pantalla
            gui.setVisible(true);
        });
    }

    }

