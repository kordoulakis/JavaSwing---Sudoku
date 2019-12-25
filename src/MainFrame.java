import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;

public class MainFrame extends JFrame {
    static MainFrame self;
    public MainFrame(){
        super("Sudoku");
        self = this;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        //setSize((int)screen.getWidth()/2, (int)screen.getHeight()/2);
        setSize(1080,1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FrameMenuBar menuBar = null;
        MainMenu mainMenu = new MainMenu();
        try {
            menuBar = new FrameMenuBar();
        }
        catch (FileNotFoundException n){ }
        setJMenuBar(menuBar);
        add(mainMenu);

        setVisible(true);
    }

}
