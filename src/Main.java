import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new); //initiates the app into the Event Dispatch Thread
    }
}
