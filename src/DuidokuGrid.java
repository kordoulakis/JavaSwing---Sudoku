import javax.swing.*;
import java.awt.*;

public class DuidokuGrid extends JPanel implements SudokuGrid {

    private GridLayout layout;
    private DuidokuController duidokuController;

    public DuidokuGrid(){
        super();
        layout = new GridLayout(4,4);
        setLayout(layout);
        setVisible(true);
        duidokuController = new DuidokuController(this);
        duidokuController.createGrid(4,4);
    }
    @Override
    public void setVisibility(boolean visible) {

    }

    @Override
    public GridController getController() {
        return null;
    }
}
