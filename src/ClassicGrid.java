import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;

/**
 * @author Elias Kordoulas
 */
public class ClassicGrid extends JPanel implements SudokuGrid{
    private GridLayout layout;
    private ClassicController classicController;

    public ClassicGrid() throws FileNotFoundException {
        super();
        layout = new GridLayout(9, 9);
        setLayout(layout);
        setVisible(true);
        classicController = new ClassicController(this);
        if(classicController.createGrid(9,9))
        ;
        else
        {
            System.err.println("Failure to create grid");
            setVisible(false);
            MainMenu.self.returnToMainMenu();
        }
    }

    @Override
    public void setVisibility(boolean visible) {
        setVisible(visible);
    }


    public boolean getVisibility(){ return isVisible(); }
    public GridController getController(){ return classicController; }

}

