import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author Elias Kordoulas
 */
public class ClassicGrid extends JPanel implements SudokuGrid, ActionListener, KeyListener {
    private GridLayout m;
    private Cell currentSelectedCell = null;
    private String language;
    private LinkedList<String> availableLetters;
    private LinkedList<String> availableNumbers;
    private Cell[][] puzzle; //The puzzle represented as a 2D array
    private ArrayList<Cell> errorCells = new ArrayList<>();
    private Controller controller;

    public ClassicGrid() {
        super();
        puzzle = new Cell[9][9];
        m = new GridLayout(9, 9);
        setLayout(m);
        createGrid(9, 9);
        setVisible(true);
        controller = new Controller();
    }

    //Interface Methods
    @Override
    public void createGrid(int rows, int columns) {
        for (int y = 0; y < columns; ++y)
            for (int x = 0; x < rows; ++x) {
                Cell cell = new Cell(x * y, true, y, x); //x,y appear inverted because in GridLayout the grid is being filled sequentially from left to right
                cell.addKeyListener(this); //reacts to key presses
                cell.addActionListener(this); //reacts when clicked on
                puzzle[x][y] = cell;
                add(cell, m);
            }
        paintBorders();
    }
    @Override
    public void setCurrentSelectedCell(Cell cell) {
        if (currentSelectedCell != null && currentSelectedCell == cell)
            return;
        else
            controller.clearErrorCells();

        if (currentSelectedCell == null) {
            currentSelectedCell = cell;
            currentSelectedCell.select(); }
        else {
            currentSelectedCell.deSelect();
            currentSelectedCell = cell;
            currentSelectedCell.select(); }
    }

    private void loadPuzzle() { //TODO Please solve this

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Cell c = (Cell) e.getSource();
        if (c.isSelectable()) {
            setCurrentSelectedCell(c);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override

    public void keyPressed(KeyEvent e) { //Takes the user input and assigns it to the selected Cell.
        Character key = (Character) e.getKeyChar();
        String userInput = key.toString().toUpperCase();
        int userInputAsInt;
        //Prevention against special characters such as caps lock etc.
        try {
            userInputAsInt = Integer.parseInt(userInput);
        } catch (NumberFormatException n) {
            System.out.println("Cought number exception" + n);
            return;
        }
        if (currentSelectedCell != null)
            controller.handleUserInput(userInput, userInputAsInt, currentSelectedCell, puzzle);
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    //Paints the borders of the appropriate cells on the board for visual clarity.
    private void paintBorders() {  //TODO Maybe make this not like this? Not elegant, but it works
        for (int i = 0; i < 9; ++i)
            for (int f = 0; f < 9; ++f)
                puzzle[i][f].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.DARK_GRAY));
        for (int x = 0; x < 9; ++x) {
            puzzle[2][x].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 5, Color.DARK_GRAY));
            puzzle[5][x].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 5, Color.DARK_GRAY));
            puzzle[x][2].setBorder(BorderFactory.createMatteBorder(1, 1, 5, 1, Color.DARK_GRAY));
            puzzle[x][5].setBorder(BorderFactory.createMatteBorder(1, 1, 5, 1, Color.DARK_GRAY));
        }
        puzzle[2][2].setBorder(BorderFactory.createMatteBorder(1, 1, 5, 5, Color.DARK_GRAY));
        puzzle[2][5].setBorder(BorderFactory.createMatteBorder(1, 1, 5, 5, Color.DARK_GRAY));
        puzzle[5][2].setBorder(BorderFactory.createMatteBorder(1, 1, 5, 5, Color.DARK_GRAY));
        puzzle[5][5].setBorder(BorderFactory.createMatteBorder(1, 1, 5, 5, Color.DARK_GRAY));
    }
}


