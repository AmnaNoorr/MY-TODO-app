import ui.TodoFrame;
import javax.swing.*;
import ui.WelcomeFrame;

public class Main {
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Nimbus LAF not available, using default.");
        }

        SwingUtilities.invokeLater(() -> new WelcomeFrame().setVisible(true));
    }
}
