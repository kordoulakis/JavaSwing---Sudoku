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

    /*
      ImagePanel panel = new ImagePanel(new ImageIcon("src/images/classicButtonIcon.png").getImage());
      add(panel);
      static class ImagePanel extends JPanel{
        private Image img;

        public ImagePanel(String img) {
            this(new ImageIcon(img).getImage());
        }

        public ImagePanel(Image img) {
            this.img = img;
            Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
            setPreferredSize(size);
            setMinimumSize(size);
            setMaximumSize(size);
            setSize(size);
            setToolTipText("Hello");
            setLayout(null);
        }
        public void paintComponent(Graphics g) {
            g.drawImage(img, 0, 0, null);
        }
    } */

}
