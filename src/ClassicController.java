import jdk.jfr.StackTrace;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.util.*;

public class ClassicController implements ActionListener, KeyListener {
    private LinkedList<String> availableLetters = new LinkedList<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I"));
    private LinkedList<String> availableNumbers = new LinkedList<>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9"));

    public static ArrayList<Cell> errorCells;
    private Cell currentSelectedCell = null;
    private Cell[][] puzzle; //The puzzle represented as a 2D array
    private ClassicGrid parent;
    private JDialog escape = new JDialog();
    private int escapes;
    private MainMenu root;
    private int guessesToBeMade;

    public Controller(ClassicGrid grid, MainMenu root) {
        this.root = root;
        escapes = 0;
        errorCells = new ArrayList<>();
        parent = grid;
        puzzle = new Cell[9][9];
        guessesToBeMade = 0;
    }
    public void createGrid(int rows, int columns) throws FileNotFoundException {
        JSONPuzzle Jpuzzle = JSONPuzzle.deserializePuzzle();
        Integer currentNumberFromGrid;
        Integer[][] puzzleGrid = Jpuzzle.getGrid();
        for (int y = 0; y < columns; ++y)
            for (int x = 0; x < rows; ++x) {
                currentNumberFromGrid = puzzleGrid[y][x];
                Cell cell = new Cell(true, y, x); //x,y appear inverted because in GridLayout the grid is being filled sequentially from left to right so columns first
                cell.addKeyListener(this); //reacts to key presses
                cell.addActionListener(this); //reacts when clicked on

                if(currentNumberFromGrid > 9) {
                    cell.setSelectable(false);
                    currentNumberFromGrid = currentNumberFromGrid / 10;
                    cell.setText(currentNumberFromGrid.toString());
                    cell.setUserNumber(currentNumberFromGrid);
                }
                else {
                    cell.setHiddenNumber(currentNumberFromGrid);
                    ++guessesToBeMade;
                    cell.setText("");
                }
                    //cell.setText((test[y][x]).toString());

                puzzle[x][y] = cell;
                parent.add(cell, parent.getLayout());
            }
        paintBorders();
    }

    public void setInputAtCell(String userInput, Cell selectedCell, Cell puzzle[][]) { //TODO Have only one hashset, no need for 2, just use a settings option for it.
        userInput = userInput.toUpperCase();
        if (userInput.equals(selectedCell.getText()))
            return;
        selectedCell.setBackground(Color.ORANGE);
        clearErrorCells();
        Integer userInputAsInt=0;
        if (availableLetters.contains(userInput)) {
            userInputAsInt = availableLetters.indexOf(userInput) + 1;;
        }
        else
            userInputAsInt = Integer.parseInt(userInput);

        if (isCorrect(selectedCell, userInputAsInt, puzzle)) {
            if(Settings.getPuzzleRepresentation().equals("Numbers"))
                selectedCell.setText(userInputAsInt.toString());
            else
                selectedCell.setText(userInput);

            selectedCell.setUserNumber(userInputAsInt);
            if (userInputAsInt == selectedCell.getHiddenNumber()) {
                guessesToBeMade = guessesToBeMade - 1;
                selectedCell.setSelectable(false);
                selectedCell.setBackground(Color.PINK);
                selectedCell.setFilled(true);
                if (guessesToBeMade == 0)
                    JOptionPane.showMessageDialog(root, "CONGRATULATIONS, YOU MADE IT!");
            }
        }
        else {
            for (Cell errorCell : errorCells) {
                errorCell.paintUserError();
            }
            selectedCell.setBackground(Color.RED);
            selectedCell.setText("");
        }
    }
    /**
     * This function takes in a gridCell, the user's typed key and finds out if it can be placed on the board
     * @param selectedCell The cell the user has clicked on.
     * @param userNumber The user's input represented as int. This makes sure that it's an available character.
     * @param puzzle The Array storing the whole Grid of Cells
     * @variable errors An int to know if an errors has been found
     * @return True if the user's input is valid.
     */
    public boolean isCorrect(Cell selectedCell, Integer userNumber, Cell[][] puzzle) { //TODO Add list of cells that interfere, paint them red
        int row = selectedCell.getPositionX();
        int column = selectedCell.getPositionY();
        int errors = 0;

        for (int i=0; i<9; ++i) { //Checks the row and column of the cell for the same input.
            if (puzzle[row][i].getNumber() == userNumber) {
                errorCells.add(puzzle[row][i]);
                ++errors;
            }
            if (puzzle[i][column].getText().equals(userNumber.toString())) {
                errorCells.add(puzzle[i][column]);
                ++errors;
            }
        }
        /***
         * @definition This part checks if there is the same input in the corresponding 3X3 Area
         *             First we get the left uppermost position of the area and run through it sequentially.
         *             If an error is found, it returns.
         */
        int puzzleRow = row - row % 3;
        int puzzleColumn = column - column % 3;
        for (int i = puzzleRow; i < puzzleRow + 3; i++)
            for (int x = puzzleColumn; x < puzzleColumn + 3; x++)
                if (puzzle[i][x].getText().equals(userNumber.toString())) {
                    errorCells.add(puzzle[i][x]);
                    ++errors;
                }
        return errors <= 0;
    }
    //Clears the array of Cells that are marked as errors for the user's previous input
    public void clearErrorCells() {
        if (!errorCells.isEmpty()) {
            for (Cell cell : errorCells)
                if(cell.isFilled())
                    cell.setBackground(Color.PINK);
                else
                    cell.clearUserError();
            errorCells.clear();
        }
    }

    public boolean isAcceptableInput(Character input){
        if (availableLetters.contains(input.toString().toUpperCase()))
            return true;
        if (availableNumbers.contains(input.toString()))
            return true;
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Cell c = (Cell) e.getSource();
        if (c.isSelectable()) {
            setCurrentSelectedCell(c);
        }
    }
    public void setCurrentSelectedCell(Cell cell) {

        if (currentSelectedCell != null && currentSelectedCell == cell)
            return;
        else
            clearErrorCells();
        if (currentSelectedCell == null) {
            currentSelectedCell = cell;
            currentSelectedCell.select();
        }
        else {
            //This checks if the cell has already been filled with the correct number
            if(currentSelectedCell.getBackground() == Color.PINK) { //TODO Maybe don't keep this
                currentSelectedCell = cell;
                currentSelectedCell.select();
                return;
            }
            else {
                currentSelectedCell.deSelect();
                currentSelectedCell = cell;
                currentSelectedCell.select();
            }

        }
    }

    /***
     *
     * @param e The typed key.
     */
    @Override
    public void keyPressed(KeyEvent e) { //Takes the user input and assigns it to the selected Cell.
        Character key = (Character) e.getKeyChar();
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { //If player presses the Escape key, it promts him to go back to MainMenu
            Object[] f  = {"Yes", "No"};
            int n = JOptionPane.showOptionDialog(parent, "Really Exit?", "Exit App", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE,null,f,f[1]);
            if (n==0)
                root.returnToMainMenu();
        }
        else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE && currentSelectedCell.isSelectable())
            currentSelectedCell.setText("");
        if(isAcceptableInput(key) && currentSelectedCell != null)
            setInputAtCell(key.toString(),currentSelectedCell, puzzle);
        else
            return;
    }
    @Override
    public void keyReleased(KeyEvent e) {

    }
    @Override
    public void keyTyped(KeyEvent e) {

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
