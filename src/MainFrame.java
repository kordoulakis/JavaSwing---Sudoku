import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    static MainFrame self;
    public MainFrame(){
        super("Sudoku");
        self = this;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        //setSize((int)screen.getWidth()/2, (int)screen.getHeight()/2);
        setSize(1080,1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MainMenu mainMenu = new MainMenu();
        FrameMenuBar menuBar = new FrameMenuBar();
        setJMenuBar(menuBar);
        add(mainMenu);

        setVisible(true);
    }

}
