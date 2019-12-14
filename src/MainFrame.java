import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame(){
        super("Sudoku");
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        //setSize((int)screen.getWidth()/2, (int)screen.getHeight()/2);
        setSize(1080,1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        MainMenu mainMenu = new MainMenu();
        add(mainMenu);
    }
}
