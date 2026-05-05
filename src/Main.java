import gui.GameFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Jalankan di Event Dispatch Thread (standar Swing)
        SwingUtilities.invokeLater(GameFrame::new);
    }
}