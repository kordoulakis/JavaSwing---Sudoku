import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;
import java.util.Arrays;
import java.util.HashSet;

/*This class handles the creation of a classic Sudoku grid.
   It extends JPanel as it is itself a JPanel filled with 81 buttons.
   Every button is of class Cell.
   It loads the puzzle in the form of JSON and assigns it to each individual Cell;
 */
public class ClassicGrid extends JPanel implements SudokuGrid,ActionListener,KeyListener {
    private GridLayout m;
    private Cell currentSelectedCell=null;
    private String language;
    private HashSet<String> availableLetters;
    private HashSet<String> availableNumbers;

    public ClassicGrid(){
            super();
            m = new GridLayout(9,9);
            setLayout(m);
            createGrid(9,9);
            setVisible(true);
    }

    //Interface Methods
    @Override
    public void createGrid(int rows, int columns) {
        for (int i=0; i<rows*columns; ++i){
            Cell cell = new Cell(i+1,true);
            cell.addKeyListener(this);
            cell.addActionListener(this);
            fillHashsets();
            add(cell,m);
        }
    }
    @Override
    public void setCurrentSelectedCell(Cell cell) {
        if (currentSelectedCell == null) {
            currentSelectedCell = cell;
            currentSelectedCell.select();
        }
        else{
            currentSelectedCell.deSelect();
            currentSelectedCell = cell;
            currentSelectedCell.select();
        }
    }
    @Override
    public void loadPuzzle() {

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Cell c = (Cell) e.getSource();
        if (c.isSelectable()) {
            setCurrentSelectedCell(c);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) { //Takes the user input and assigns it to the selected Cell.
        Character key = (Character) e.getKeyChar();
        String userInput = key.toString().toUpperCase();
        if (currentSelectedCell!= null)
            if (availableNumbers.contains(userInput) || availableLetters.contains(userInput))
                currentSelectedCell.setText(userInput);
    }
    @Override
    public void keyReleased(KeyEvent e) {}

    public void fillHashsets(){ //Have to hard code the available options, no real way out of it
        String[] letters = {"A","B","C","D","E","F","G","H","I"};
        String[] numbers = {"1","2","3","4","5","6","7","8","9"};
        availableLetters = new HashSet<>(Arrays.asList(letters));
        availableNumbers = new HashSet<>(Arrays.asList(numbers));
    }
}

