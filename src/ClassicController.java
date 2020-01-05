import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.util.*;

public class ClassicController implements GridController, ActionListener, KeyListener {
    public static LinkedList<String> availableLetters = new LinkedList<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I"));
    public static LinkedList<String> availableNumbers = new LinkedList<>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9"));

    public static ArrayList<Cell> errorCells;
    private Cell currentSelectedCell = null;
    private static Cell[][] puzzle; //The puzzle represented as a 2D array
    private ClassicGrid parent;
    private int guessesToBeMade;
    private JSONPuzzles.JSONPuzzle currentPuzzle;

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
    public boolean createGrid(int rows, int columns, JSONPuzzles classicPuzzles) {
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
                } else {
                    cell.setHiddenNumber(currentNumberFromGrid);
                    ++guessesToBeMade;
                    cell.setText("");
                    cell.setForeground(new Color(120, 0, 200, 255));
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
    public void changeRepresentation() {
        for (int x = 0; x < 9; ++x)
            for (int y = 0; y < 9; ++y) {
                String text = puzzle[x][y].getText();
                Integer textAsInt = 0;
                if (!text.equals("")) {
                    if (availableLetters.contains(text))
                        textAsInt = availableLetters.indexOf(text) + 1;
                    else {
                        textAsInt = Integer.parseInt(text);
                        text = availableLetters.get(textAsInt - 1);
                    }
                    if (Settings.getPuzzleRepresentation().equals("Numbers"))
                        puzzle[x][y].setText(textAsInt.toString());
                    else
                        puzzle[x][y].setText(text);
                }
            }
    }

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

        if (isCorrect(selectedCell, userInputAsInt, puzzle)) {
            if (Settings.getPuzzleRepresentation().equals("Numbers"))
                selectedCell.setText(userInputAsInt.toString());
            else
                selectedCell.setText(availableLetters.get(userInputAsInt - 1));

            selectedCell.setUserNumber(userInputAsInt);
            if (userInputAsInt == selectedCell.getHiddenNumber()) {
                guessesToBeMade = guessesToBeMade - 1;
                selectedCell.setSelectable(false);
                selectedCell.setBackground(Color.PINK);
                selectedCell.setFilled(true);
                currentSelectedCell = null;
                if (guessesToBeMade == 0) {
                    JOptionPane.showMessageDialog(MainFrame.self, "CONGRATULATIONS, YOU MADE IT!");
                    Users.User user = Settings.getCurrentUser();
                    user.addSolvedClassicPuzzleToArraylist(currentPuzzle.getId());
                    Users.serializeAndWriteFile(Settings.getCurrentUsersList());
                    MainMenu.self.returnToMainMenu();
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
    public boolean isCorrect(Cell selectedCell, Integer userNumber, Cell[][] puzzle) {
        Thread t = new Thread();
        t.start();

        int row = selectedCell.getPositionX();
        int column = selectedCell.getPositionY();
        int errors = 0;

        for (int i = 0; i < 9; ++i) { //Checks the row and column of the cell for the same input.
            if (puzzle[row][i].getText().equals(userNumber.toString())) {
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
                if (cell.isFilled())
                    cell.setBackground(Color.PINK);
                else
                    cell.clearUserError();
            errorCells.clear();
        }
    }

    public boolean isAcceptableInput(Character input) {
        if (availableLetters.contains(input.toString().toUpperCase()))
            return true;
        if (currentSelectedCell!=null)
            currentSelectedCell.setFont(new Font("Arial", Font.BOLD, 80));
        return availableNumbers.contains(input.toString());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Cell c = (Cell) e.getSource();
        if (c.isSelectable()) {
            setCurrentSelectedCell(c);
            //if (FrameMenuBar.getShowTipsState()) { //TODO This works, make functions for the checking of rows and stuff, this is messy as fuck
            // showTipsForCurrentCell(c);
            // }
        }
    }

    public void showTipsForCurrentCell() { //TODO DO THIS ANYWAY, SAVES TIME AND CHECK FOR ERRORS ONLY IF HASHSET DOESN'T CONTAIN THE USERINPUT

        String[] numbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
        HashSet<String> available = new HashSet<>(Arrays.asList(numbers));
        HashSet<String> numbersA = new HashSet<>();
        StringBuilder temp = new StringBuilder();
        int row = currentSelectedCell.getPositionX();
        int column = currentSelectedCell.getPositionY();

        for (int i = 0; i < 9; ++i) { //Checks the row and column of the cell for the same input.
            if (!puzzle[row][i].getText().equals("")) //not
                numbersA.add(puzzle[row][i].getText());
            if (!puzzle[i][column].getText().equals(""))
                numbersA.add(puzzle[i][column].getText());
        }
        int puzzleRow = row - row % 3;
        int puzzleColumn = column - column % 3;
        for (int i = puzzleRow; i < puzzleRow + 3; i++)
            for (int x = puzzleColumn; x < puzzleColumn + 3; x++)
                if (!puzzle[i][x].getText().equals("")) //not
                    numbersA.add(puzzle[i][x].getText());
        for (String s : numbersA) {
            available.remove(s);
        }
        for (String s : available) {
            currentSelectedCell.setFont(new Font("Arial", Font.BOLD, 20));
            temp.append(s).append(" ");
            currentSelectedCell.setText(temp.toString());
        }
        System.out.println();
    }

    public boolean setCurrentSelectedCell(Cell cell) {

        if (currentSelectedCell != null && currentSelectedCell == cell)
            return false;
        else
            clearErrorCells();

        if (currentSelectedCell == null) {
            currentSelectedCell = cell;
            currentSelectedCell.select();
        }
        else {
            //This checks if the cell has already been filled with the correct number
            if (currentSelectedCell.isSelectable()){
                returnCellToDefaultState();
                currentSelectedCell.deSelect();
                currentSelectedCell = cell;
                currentSelectedCell.select();
            }
        }
        if (Settings.getShowTipsValue())
            showTipsForCurrentCell();
        return true;

    }

    private void returnCellToDefaultState(){
        if (currentSelectedCell.isSelectable()){
            currentSelectedCell.setFont(new Font("Arial", Font.BOLD, 80));
            currentSelectedCell.setText(" ");
        }
    }
    /***
     *
     * @param e The typed key.
     */
    @Override
    public void keyPressed(KeyEvent e) { //Takes the user input and assigns it to the selected Cell.
        Character key = e.getKeyChar();
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { //If player presses the Escape key, it promts him to go back to MainMenu
            Object[] f = {"Yes", "No"};
            int n = JOptionPane.showOptionDialog(parent, "Really Exit?", "Exit App", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, f, f[1]);
            if (n == 0)
                MainMenu.self.returnToMainMenu();
        } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && currentSelectedCell != null && currentSelectedCell.isSelectable())
            currentSelectedCell.setText("");

        if (isAcceptableInput(key) && currentSelectedCell != null)
            setInputAtCell(key.toString(), currentSelectedCell, puzzle);
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
