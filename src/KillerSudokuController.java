import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

public class KillerSudokuController implements GridController {
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
        currentPuzzle = killerPuzzles.getRandomKillerPuzzle(killerPuzzles.getAvailableKillerPuzzles()); //Doesn't work, only one puzzle available
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
        HashSet<Integer> ok = new HashSet<>();
        if (!areas.isEmpty())
            for (KillerJSONPuzzles.KillerJSONPuzzle.Area area : areas) {
                isFirst = true;
                temp = getRandomColor();
                for (Integer i : area.getCellsOfArea()) {
                    if (ok.contains(i)){ //Checks if there is an error in the puzzle
                        System.err.println("Found " + i + " twice");
                        return false;
                }
                    ok.add(i);
                    Cell cell = allCells.get(i - 1);
                    cell.setBackground(temp);
                    if (isFirst) {
                        JLabel numberText = new JLabel(area.getNumberToReach().toString(),null,JLabel.LEFT);
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
        if (solvedAreas == numberOfAreas) {
            showSolvedPuzzleScreen();
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
    public Cell getCurrentSelectedCell() {
        return currentSelectedCell;
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


    public Color getRandomColor() {
        Random r = new Random();
        Color c = new Color(r.nextInt(256 - 100) + 100,
                r.nextInt(256 - 100) + 100, r.nextInt(256 - 120) + 100);
        return c;
    }
}
