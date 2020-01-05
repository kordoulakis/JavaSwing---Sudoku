import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class DuidokuController implements  GridController, ActionListener, KeyListener {
    private DuidokuGrid parent;

    public DuidokuController(DuidokuGrid grid) {
        parent = grid;

    }

    public boolean createGrid(int rows, int columns){
        Integer[][] dGrid = new Integer[rows][columns];

        for (int x=0; x<rows; ++x)
            for (int y=0; y<columns; ++y){
                Cell cell = new Cell(true,x,y);
                cell.setText("");
                cell.addKeyListener(this);
                cell.addActionListener(this);
                parent.add(cell);
            }
        return true;
    }



    @Override
    public Cell[][] getPuzzle() {
        return new Cell[0][0];
    }

    @Override
    public void changeRepresentation() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
