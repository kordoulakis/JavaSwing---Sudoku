import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;

/**
 * @author Elias Kordoulas
 */
public class ClassicGrid extends JPanel implements SudokuGrid{
    private GridLayout layout;
    private ClassicController classicController;

    public ClassicGrid(MainMenu root) throws FileNotFoundException {
        super();
        layout = new GridLayout(9, 9);
        setLayout(layout);
        setVisible(true);
        classicController = new ClassicController(this,root);
        classicController.createGrid(9,9);
    }

    @Override
    public void setVisibility(boolean visible) {
        setVisible(visible);
    }
}

