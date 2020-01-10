import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

public class KillerSudokuController implements GridController {
    private LinkedList<String> availableLetters = new LinkedList<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I"));
    private LinkedList<String> availableNumbers = new LinkedList<>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9"));
    private Cell[][] puzzle;
    private Cell currentSelectedCell;

    private KillerSudokuGrid parent;
    private KillerJSONPuzzles.KillerJSONPuzzle currentPuzzle;
    private LinkedList<Cell> allCells = new LinkedList<>();
    private Color previousCellColor;
    private String previousText;

    private LinkedList<KillerJSONPuzzles.KillerJSONPuzzle.Area> areas;

    public KillerSudokuController(KillerSudokuGrid grid) {
        parent = grid;
        puzzle = new Cell[9][9];
    }

    public boolean createGrid(int rows, int columns, KillerJSONPuzzles killerPuzzles) {
        //currentPuzzle = killerPuzzles.getRandomKillerPuzzle(killerPuzzles.getAvailableKillerPuzzles()); //Doesn't work, only one puzzle available
        currentPuzzle = killerPuzzles.getPuzzles()[0];
        for (int y = 0; y < columns; ++y) {
            for (int x = 0; x < rows; ++x) {
                Cell cell = new Cell(true, y, x);
                cell.addKeyListener(this); //reacts to key presses
                cell.addActionListener(this); //reacts when clicked on

                cell.setUserNumber(0);
                cell.setFont(new Font("Arial", Font.BOLD, 60));
                cell.setText("");
                allCells.add(cell);
                parent.add(cell);
                puzzle[x][y] = cell;
            }
        }
        paintBorders();

        areas = new LinkedList<>(Arrays.asList(currentPuzzle.getAreas()));
        Color temp;
        Font f = new Font("Arial", Font.BOLD, 60);
        boolean isFirst;
        if (!areas.isEmpty())
            for (KillerJSONPuzzles.KillerJSONPuzzle.Area area : areas) {
                isFirst = true;
                temp = getRandomColor();
                for (Integer i : area.getCellsOfArea()) {
                    Cell cell = allCells.get(i - 1);
                    cell.setBackground(temp);

                    if (isFirst) {
                        //cell.setText(area.getNumberToReach().toString());
                        JLabel numberText = new JLabel(area.getNumberToReach().toString());
                        cell.add(numberText);
                        isFirst = false;
                    }
                }
            }
        else
            return false;
        return true;
    }

    @Override
    public Cell[][] getPuzzle() {
        return puzzle;
    }

    @Override
    public void changeRepresentation() {
        boolean representation;
        if (Settings.getPuzzleRepresentation().equals("Numbers"))
            representation = true;
        else
            representation = false;
        for (int x = 0; x < 9; ++x)
            for (int y = 0; y < 9; ++y) {
                Integer userNumber = puzzle[x][y].getUserNumber();
                if (!puzzle[x][y].getText().equals(""))
                    if (representation)
                        puzzle[x][y].setText(userNumber.toString());
                    else
                        puzzle[x][y].setText(availableLetters.get(userNumber - 1));
            }
    }

    @Override
    public boolean setInputAtCell(String userInput, Cell selectedCell, Cell[][] puzzle) {
        Integer userInputAsInt;
        if (availableLetters.contains(userInput)) //This makes our input into an integer regardless of what the user pressed
            userInputAsInt = availableLetters.indexOf(userInput) + 1;
        else
            userInputAsInt = Integer.parseInt(userInput);

        if (isUniqueInput(currentSelectedCell, userInputAsInt, puzzle)) {
            if (Settings.getPuzzleRepresentation().equals("Numbers"))
                currentSelectedCell.setText(userInputAsInt.toString());
            else
                currentSelectedCell.setText(availableLetters.get(userInputAsInt - 1));

            currentSelectedCell.setUserNumber(userInputAsInt);
            currentSelectedCell.setFont(new Font("Arial", Font.BOLD, 80));
            System.out.println("Set usernumber as " + userInputAsInt);
            evaluateGameState();
        } else
            currentSelectedCell.paintUserError();
        return true;

    }

    @Override
    public boolean isUniqueInput(Cell selectedCell, Integer userNumber, Cell[][] puzzle) {
        int row = selectedCell.getPositionX();
        int column = selectedCell.getPositionY();
        for (int i = 0; i < 9; ++i) { //Checks the row and column of the cell for the same input.
            if (puzzle[row][i].getUserNumber() == userNumber)
                return false;
            if (puzzle[i][column].getUserNumber() == userNumber)
                return false;
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
                    return false;
                }

        return true;
    }

    private void evaluateGameState() {
        int numberOfAreas = areas.size();
        int solvedAreas = 0;
        for (KillerJSONPuzzles.KillerJSONPuzzle.Area area : areas) {
            int numberToReach = area.getNumberToReach();
            int totalNumberOfCells = 0;

            for (Integer i : area.getCellsOfArea()) {
                totalNumberOfCells = totalNumberOfCells + allCells.get(i - 1).getUserNumber();
            }
            if (numberToReach == totalNumberOfCells)
                ++solvedAreas;
        }
        System.out.println("Solved areas: " + solvedAreas);
        if (solvedAreas == numberOfAreas) {
            JOptionPane.showMessageDialog(MainFrame.self, "CONGRATULATIONS, YOU SOLVED THE PUZZLE!","END OF GAME",JOptionPane.INFORMATION_MESSAGE);
            saveUserData();
        }
    }

    @Override
    public boolean setCurrentSelectedCell(Cell cell) {
        if (currentSelectedCell != null) {
            currentSelectedCell.setBackground(previousCellColor);
            if (!availableLetters.contains(currentSelectedCell.getText()) && !availableNumbers.contains(currentSelectedCell.getText())) {
                currentSelectedCell.setText(previousText);
                currentSelectedCell.setFont(new Font("Arial", Font.BOLD, 80));
            }

        }
        currentSelectedCell = cell;
        previousCellColor = currentSelectedCell.getBackground();
        currentSelectedCell.select();
        if (Settings.getShowTipsValue()) {
            previousText = currentSelectedCell.getText();
            showTipsForCell(currentSelectedCell, puzzle);
        }

        return false;
    }

    @Override
    public void saveUserData() {
        Users.User user = Settings.getCurrentUser();
        user.addSolvedKillerSudokuPuzzleToArraylist(currentPuzzle.getId());
        Users.serializeAndWriteFile(Settings.getCurrentUsersList());
        MainMenu.self.returnToMainMenu();
    }


    @Override
    public boolean isAcceptableInput(Character input) {
        return availableLetters.contains(input.toString().toUpperCase()) || availableNumbers.contains(input.toString());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Cell cell = (Cell) e.getSource();
        if (currentSelectedCell != null)
            currentSelectedCell.setDefaultColor(currentSelectedCell.getDefaultColor());
        if (cell.isSelectable())
            setCurrentSelectedCell(cell);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) { //TODO Check inputs, implement updateboard()
        Character key = e.getKeyChar();
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { //If player presses the Escape key, it promts him to go back to MainMenu
            Object[] f = {"Yes", "No"};
            int n = JOptionPane.showOptionDialog(parent, "Really Exit?", "Exit App", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, f, f[1]);
            if (n == 0)
                MainMenu.self.returnToMainMenu();
        } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
            if (currentSelectedCell != null && currentSelectedCell.isSelectable()) {
                currentSelectedCell.setUserNumber(0);
                currentSelectedCell.setText("");
            }

        if (isAcceptableInput(key))
            setInputAtCell(key.toString().toUpperCase(), currentSelectedCell, puzzle);
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public Color getRandomColor() {
        Random r = new Random();
        Color c = new Color(r.nextInt(256 - 120) + 120,
                r.nextInt(256 - 120) + 120, r.nextInt(256 - 120) + 120);
        return c;
    }
}
