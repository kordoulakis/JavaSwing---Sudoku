import javax.swing.*;
import java.awt.*;

public class mainFrame extends JFrame {

    public mainFrame(){
        super("Sudoku");
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

        setSize((int)screen.getWidth()/2, (int)screen.getHeight()/2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        MainMenu mainMenu = new MainMenu();
        add(mainMenu);
    }

}
