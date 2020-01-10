import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;

public class MainFrame extends JFrame {
    static MainFrame self;
    public MainFrame(){
        super("Sudoku");
        self = this;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int)screen.getHeight()/2+200, (int)screen.getHeight()/2+200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FrameMenuBar menuBar = null;
        menuBar = new FrameMenuBar();
        MainMenu mainMenu = new MainMenu();
        setJMenuBar(menuBar);
        add(mainMenu);
        setVisible(true);
    }

}
