import jdk.jfr.StackTrace;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class Controller implements ActionListener, KeyListener {
    private LinkedList<String> availableLetters;
    private LinkedList<String> availableNumbers;
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
        fillHashsets();
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

    public void handleUserInput(String userInput, int userInputAsInt, Cell selectedCell, Cell puzzle[][]) {
        if (availableNumbers.contains(userInput) || availableLetters.contains(userInput)) {  //TODO Have only one hashset, no need for 2, just use a settings option for it.
            if (userInput.equals(selectedCell.getText()))
                return;
            clearErrorCells();
            if (isCorrect(selectedCell, userInputAsInt, puzzle)) {
                //selectedCell.setUserNumber(userInputAsInt);
                selectedCell.setBackground(Color.ORANGE);
                selectedCell.setText(userInput);

                if (userInputAsInt == selectedCell.getHiddenNumber()) { //TODO Security breach, if you change between the correct number and a wrong one it still counts it.
                    guessesToBeMade = guessesToBeMade - 1;
                    System.out.println(guessesToBeMade);
                    if(guessesToBeMade==0)
                        System.out.println("Congratulations, you made it!");
                }
            }
            else {
                for (Cell errorCell : errorCells)
                    errorCell.paintUserError();
                selectedCell.setBackground(Color.RED);
                selectedCell.setText("");
            }
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
    public boolean isCorrect(Cell selectedCell, int userNumber, Cell[][] puzzle) { //TODO Add list of cells that interfere, paint them red
        int row = selectedCell.getPositionX();
        int column = selectedCell.getPositionY();
        int errors = 0;

        for (int i=0; i<9; ++i) { //Checks the row and column of the cell for the same input.
            if (puzzle[row][i].getNumber() == userNumber) {
                errorCells.add(puzzle[row][i]);
                ++errors;
                break;
            }
        }
        for (int i=0; i<9; ++i){
            if (puzzle[i][column].getNumber() == userNumber) {
                errorCells.add(puzzle[i][column]);
                ++errors;
                break;
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
                if (puzzle[i][x].getNumber() == userNumber) {
                    errorCells.add(puzzle[i][x]);
                    ++errors;
                    break;
                }

        if (errors>0)
            return false;
        else
            return true;
        //return !existsInColumn(column, userNumber, puzzle) && !existsInRow(row, userNumber, puzzle) && !existsInArea(row, column, userNumber, puzzle);
    }
    //Clears the array of Cells that are marked as errors for the user's previous input
    public void clearErrorCells() {
        if (!errorCells.isEmpty()) {
            for (Cell cell : errorCells)
                cell.clearUserError();
            errorCells.clear();
        }
    }
    /***
     * @variable letters,numbers: The available sets for characters while playing.
     */
    public void fillHashsets() { //Have to hard code the available options, no real way out of it
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I"};
        String[] numbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
        availableLetters = new LinkedList<>(Arrays.asList(letters));
        availableNumbers = new LinkedList<>(Arrays.asList(numbers));
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
            currentSelectedCell.select(); }
        else {
            currentSelectedCell.deSelect();
            currentSelectedCell = cell;
            currentSelectedCell.select(); }
    }

    @Override
    public void keyPressed(KeyEvent e) { //Takes the user input and assigns it to the selected Cell.
        Character key = (Character) e.getKeyChar();
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            ++escapes;
            if (escapes>1){
                root.returnToMainMenu();
            }
        }
        String userInput = key.toString().toUpperCase();
        int userInputAsInt=0;
        //Prevention against special characters such as caps lock etc.
        try {
            userInputAsInt = Integer.parseInt(userInput);
            System.out.println(userInputAsInt);
        } catch (NumberFormatException n) {
            System.out.println("Caught number exception" + n);
            return;
        }

        if (currentSelectedCell != null)
            handleUserInput(userInput, userInputAsInt, currentSelectedCell, puzzle);
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
