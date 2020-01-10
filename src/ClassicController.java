import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.util.*;

public class ClassicController implements GridController {
    public static ArrayList<Cell> errorCells;
    private Cell currentSelectedCell = null;
    private Cell[][] puzzle; //The puzzle represented as a 2D array
    private ClassicGrid parent;
    private int guessesToBeMade;
    private ClassicJSONPuzzles.ClassicJSONPuzzle currentPuzzle;

    public ClassicController(ClassicGrid grid) {
        errorCells = new ArrayList<>();
        parent = grid;
        puzzle = new Cell[9][9];
        guessesToBeMade = 0;
    }

    /***
     * @definition This function creates a 9x9 grid of buttons and assigns them to our ClassicGrid
     * @variable [JPuzzles] is the deserialized JSON file containing the puzzles for Classic mode
     * @param rows Number of rows in the grid
     * @param columns Number of columns in the grid
     * @throws FileNotFoundException This is thrown because of JSON loading. Java likes to do it this way :)
     */
    public boolean createGrid(int rows, int columns, ClassicJSONPuzzles classicPuzzles) {
        Integer currentNumberFromGrid;
        currentPuzzle = classicPuzzles.getRandomClassicPuzzle(classicPuzzles.getAvailableClassicPuzzles());

        Integer[][] puzzleGrid = currentPuzzle.getGrid();
        for (int y = 0; y < columns; ++y)
            for (int x = 0; x < rows; ++x) {
                currentNumberFromGrid = puzzleGrid[y][x];
                Cell cell = new Cell(true, y, x); //x,y appear inverted because in GridLayout the grid is being filled sequentially from left to right so columns first
                cell.addKeyListener(this); //reacts to key presses
                cell.addActionListener(this); //reacts when clicked on

                if (currentNumberFromGrid > 9) {
                    cell.setSelectable(false);
                    currentNumberFromGrid = currentNumberFromGrid / 10;
                    if (Settings.getPuzzleRepresentation().equals("Numbers"))
                        cell.setText(currentNumberFromGrid.toString());
                    else
                        cell.setText(availableLetters.get(currentNumberFromGrid - 1));
                    cell.setUserNumber(currentNumberFromGrid);
                    cell.setDefaultColor(new Color(140, 200, 221));
                    cell.setBackground(new Color(140, 200, 221));
                } else {
                    cell.setHiddenNumber(currentNumberFromGrid);
                    ++guessesToBeMade;
                    cell.setText("");
                    cell.setDefaultColor(new Color(80, 255, 150));
                    cell.setForeground(Color.BLACK);
                }
                //cell.setText((test[y][x]).toString());

                puzzle[x][y] = cell;
                parent.add(cell, parent.getLayout());
            }
        paintBorders();
        return true;
    }

    public boolean solveSelf() {
        for (int y = 0; y < 9; ++y)
            for (int x = 0; x < 9; ++x) {
                if (puzzle[x][y].isSelectable())
                    setInputAtCell(Integer.toString(puzzle[x][y].getHiddenNumber()), puzzle[x][y], puzzle);
            }
        return true;
    }

    @Override
    public Cell[][] getPuzzle() {
        return puzzle;
    }

    @Override
    public boolean setInputAtCell(String userInput, Cell selectedCell, Cell[][] puzzle) {
        userInput = userInput.toUpperCase();
        if (userInput.equals(selectedCell.getText()))
            return false;
        selectedCell.setBackground(Color.ORANGE);
        clearErrorCells();
        Integer userInputAsInt = 0;
        if (availableLetters.contains(userInput)) {
            userInputAsInt = availableLetters.indexOf(userInput) + 1;
        } else
            userInputAsInt = Integer.parseInt(userInput);

        if (isUniqueInput(selectedCell, userInputAsInt, puzzle)) {
            if (Settings.getPuzzleRepresentation().equals("Numbers"))
                selectedCell.setText(userInputAsInt.toString());
            else
                selectedCell.setText(availableLetters.get(userInputAsInt - 1));

            if (userInputAsInt == selectedCell.getHiddenNumber()) {
                selectedCell.setUserNumber(userInputAsInt);
                guessesToBeMade = guessesToBeMade - 1;
                selectedCell.setSelectable(false);
                selectedCell.setBackground(selectedCell.getDefaultColor());
                selectedCell.setFilled(true);
                currentSelectedCell = null;
                if (guessesToBeMade == 0) { //Every cell is filled correctly, exit game
                    showSolvedPuzzleScreen();
                    saveUserData();
                }
                return true;
            }
        } else {
            for (Cell errorCell : errorCells) {
                errorCell.paintUserError();
            }
            selectedCell.setBackground(Color.RED);
            selectedCell.setText("");
        }
        return false;
    }

    /**
     * This function takes in a gridCell, the user's typed key and finds out if it can be placed on the board
     *
     * @return True if the user's input is valid.
     * @variable selectedCell The cell the user has clicked on.
     * @variable userNumber   The user's input represented as int. This makes sure that it's an available character.
     * @variable puzzle       The Array storing the whole Grid of Cells
     * @variable errors An int to know if an errors has been found
     */
    @Override
    public boolean isUniqueInput(Cell selectedCell, Integer userNumber, Cell[][] puzzle) {
        int row = selectedCell.getPositionX();
        int column = selectedCell.getPositionY();
        int errors = 0;

        for (int i = 0; i < 9; ++i) { //Checks the row and column of the cell for the same input.
            if (puzzle[row][i].getUserNumber() == userNumber) {
                errorCells.add(puzzle[row][i]);
                ++errors;
            }
            if (puzzle[i][column].getUserNumber() == userNumber) {
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
                if (puzzle[i][x].getUserNumber() == userNumber) {
                    errorCells.add(puzzle[i][x]);
                    ++errors;
                }
        return errors <= 0;
    }

    //Clears the array of Cells that are marked as errors for the user's previous input
    public void clearErrorCells() {
        if (!errorCells.isEmpty()) {
            for (Cell cell : errorCells)
                if (cell.isFilled())
                    cell.setBackground(cell.getDefaultColor());
                else
                    cell.clearUserError();
            errorCells.clear();
        }
    }

    public boolean isAcceptableInput(Character input) {
        if (currentSelectedCell != null)
            currentSelectedCell.setFont(new Font("Arial", Font.BOLD, 80));
        return availableLetters.contains(input.toString().toUpperCase()) || availableNumbers.contains(input.toString());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Cell c = (Cell) e.getSource();
        if (c.isSelectable()) {
            setCurrentSelectedCell(c);
        }
    }

    @Override
    public void saveUserData() {
        Users.User user = Settings.getCurrentUser();
        user.addSolvedClassicPuzzleToArraylist(currentPuzzle.getId());
        Users.serializeAndWriteFile(Settings.getCurrentUsersList());
        MainMenu.self.returnToMainMenu();
    }

    public Cell getCurrentSelectedCell() {
        return currentSelectedCell;
    }

    @Override
    public boolean setCurrentSelectedCell(Cell cell) {
        if (currentSelectedCell != null && currentSelectedCell == cell)
            return false;
        else
            clearErrorCells();

        if (currentSelectedCell == null) {
            currentSelectedCell = cell;
            currentSelectedCell.select();
        } else {
            //This checks if the cell has already been filled with the correct number
            if (currentSelectedCell.isSelectable()) {
                returnCellToDefaultState();
                currentSelectedCell.deSelect();
                currentSelectedCell = cell;
                currentSelectedCell.select();
            }
        }
        if (Settings.getShowTipsValue())
            showTipsForCell(cell, puzzle);
        return true;

    }

    private void returnCellToDefaultState() {
        if (currentSelectedCell.isSelectable()) {
            currentSelectedCell.setFont(new Font("Arial", Font.BOLD, 80));
            currentSelectedCell.setText(" ");
        }
    }


}
