package utils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;
public class TimeLogger {
    private static List<Object[]> logs = new ArrayList<>();
    public static void log(String alg, double time, int nodes) { logs.add(new Object[]{alg, String.format("%.3f ms", time), nodes}); }
    public static void showTable() {
        JFrame f = new JFrame("Rendimiento");
        DefaultTableModel m = new DefaultTableModel(new String[]{"Algoritmo", "Tiempo", "Nodos"}, 0);
        for (Object[] r : logs) m.addRow(r);
        f.add(new JScrollPane(new JTable(m)));
        f.setSize(400, 300);
        f.setVisible(true);
    }
}