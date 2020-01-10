import javax.swing.*;
import java.awt.*;

public class KillerSudokuGrid extends JPanel implements SudokuGrid {
    private GridLayout layout;
    private KillerSudokuController controller;

    public KillerSudokuGrid(KillerJSONPuzzles puzzles) {
        super();
        layout = new GridLayout(9, 9);
        setLayout(layout);
        setVisible(true);
        controller = new KillerSudokuController(this);
        controller.createGrid(9,9,puzzles);
    }

    @Override
    public void setVisibility(boolean visible) {
        setVisible(false);
    }

    @Override
    public GridController getController() {
        return controller;
    }
}
