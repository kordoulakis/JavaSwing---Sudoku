import javax.swing.*;
import java.awt.*;

/**
 * @author Elias Kordoulas
 */
public class ClassicGrid extends JPanel implements SudokuGrid{
    private GridLayout layout;
    private ClassicController classicController;

    public ClassicGrid(ClassicJSONPuzzles puzzles){
        super();
        layout = new GridLayout(9, 9);
        setLayout(layout);
        setVisible(true);
        classicController = new ClassicController(this);
        classicController.createGrid(9,9,puzzles);
        //classicController.solveSelf();
    }

    @Override
    public void setVisibility(boolean visible) {
        setVisible(visible);
    }
    public boolean getVisibility(){ return isVisible(); }
    public GridController getController(){ return classicController; }

}

