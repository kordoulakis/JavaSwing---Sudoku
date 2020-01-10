import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

public class DuidokuController implements GridController {
    private DuidokuGrid parent;
    private Cell[][] duidokuGrid;
    private Cell currentSelectedCell;
    private Opponent opponent;
    private boolean player;
    private boolean gameEnd;

    private LinkedList<String> availableLetters = new LinkedList<>(Arrays.asList("A", "B", "C", "D"));
    private LinkedList<String> availableNumbers = new LinkedList<>(Arrays.asList("1", "2", "3", "4"));

    public DuidokuController(DuidokuGrid grid, Opponent opponent) {
        parent = grid;
        this.opponent = opponent;
    }

    public boolean createGrid(int rows, int columns) {
        duidokuGrid = new Cell[rows][columns];
        for (int x = 0; x < rows; ++x)
            for (int y = 0; y < columns; ++y) {
                Cell cell = new Cell(true, x, y);
                cell.setFont(new Font("Arial", Font.BOLD, 80));
                cell.setText("");
                cell.addKeyListener(this);
                cell.addActionListener(this);
                cell.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.DARK_GRAY));
                parent.add(cell);
                duidokuGrid[y][x] = cell;
            }
        player = true;
        gameEnd = false;
        paintBorders();
        return true;
    }

    @Override
    public Cell[][] getPuzzle() {
        return duidokuGrid;
    }

    @Override
    public boolean setInputAtCell(String input, Cell selectedCell, Cell[][] puzzle) {
        if (selectedCell == null || !selectedCell.isSelectable())
            return false;

        input = input.toUpperCase();

        Integer userInputAsInt;

        if (availableLetters.contains(input)) {
            userInputAsInt = availableLetters.indexOf(input) + 1;
        } else
            userInputAsInt = Integer.parseInt(input);

        if (isUniqueInput(selectedCell, userInputAsInt, getPuzzle())) {
            selectedCell.setFont(new Font("Arial", Font.BOLD, 80));
            if (Settings.getPuzzleRepresentation().equals("Numbers"))
                selectedCell.setText(userInputAsInt.toString());
            else
                selectedCell.setText(availableLetters.get(userInputAsInt - 1));
            selectedCell.setUserNumber(userInputAsInt);
            if (player)
                selectedCell.setBackground(Color.CYAN);
            else
                selectedCell.setBackground(Color.RED);
            player = !player;

            selectedCell.setSelectable(false);
            return true;
        }
        return false;
    }

    @Override
    public boolean isUniqueInput(Cell currentSelectedCell, Integer input, Cell[][] puzzle) {
        int row = currentSelectedCell.getPositionX();
        int column = currentSelectedCell.getPositionY();
        String textToSet = null;
        for (int x = 0; x < 4; ++x) {
            if (Settings.getPuzzleRepresentation().equals("Letters"))
                textToSet = availableLetters.get(input - 1);
            else
                textToSet = input.toString();
            if (puzzle[row][x].getText().equals(textToSet))
                return false;
            if (puzzle[x][column].getText().equals(textToSet))
                return false;
        }
        return getTipsForCell(currentSelectedCell).contains(textToSet);
    }

    @Override
    public boolean setCurrentSelectedCell(Cell cell) {
        if (currentSelectedCell != null)
            if (currentSelectedCell.isSelectable())
                currentSelectedCell.setBackground(Color.WHITE);

        cell.setBackground(Color.ORANGE);
        currentSelectedCell = cell;
        updateBoard();
        return true;
    }

    @Override
    public Cell getCurrentSelectedCell() {
        return currentSelectedCell;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Cell cell = (Cell) e.getSource();
        if (cell.isSelectable())
            setCurrentSelectedCell(cell);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        Character key = e.getKeyChar();
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { //If player presses the Escape key, it promts him to go back to MainMenu
            Object[] f = {"Yes", "No"};
            int n = JOptionPane.showOptionDialog(parent, "Really Exit?", "Exit App", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, f, f[1]);
            if (n == 0)
                MainMenu.self.returnToMainMenu();
        }
        if (isAcceptableInput(key)) {
            if (setInputAtCell(key.toString().toUpperCase(), currentSelectedCell, duidokuGrid)) {
                updateBoard();
                if (!gameEnd) {
                    opponent.makeAMove();
                    updateBoard();
                }
            }
        }
    }

    private void endGame(){
        ResourceBundle gameBundle = Settings.getGameBundle();
        gameEnd = true;
        player = !player;
        String winner;
        if (player) winner = Settings.getCurrentUser().getUsername();
        else winner = gameBundle.getString("Computer");
        saveUserData();
        if (player)
            JOptionPane.showMessageDialog(parent, gameBundle.getString("Winner")  + winner, gameBundle.getString("EndOfGame"), JOptionPane.INFORMATION_MESSAGE);
        else
            JOptionPane.showMessageDialog(parent, gameBundle.getString("Winner")  + winner, gameBundle.getString("EndOfGame"), JOptionPane.ERROR_MESSAGE);
        MainMenu.self.returnToMainMenu();
    }
    private void evaluateGameState() {
        for (int x = 0; x < 4; ++x)
            for (int y = 0; y < 4; ++y) {
                if (duidokuGrid[x][y].isSelectable())
                    return;
            }
        //If we reach this point it means every cell is either filled or closed
        endGame();
    }

    private void updateBoard() {
        if (areThereAvailableMoves()) {
            for (int x = 0; x < 4; ++x)
                for (int y = 0; y < 4; ++y) {
                    HashSet<String> availableOptions = getTipsForCell(duidokuGrid[x][y]);
                    if (duidokuGrid[x][y].isSelectable())
                        if (availableOptions.isEmpty()) {
                            duidokuGrid[x][y].setText("");
                            duidokuGrid[x][y].setSelectable(false);
                            duidokuGrid[x][y].setBackground(Color.GRAY);
                        } else if (Settings.getShowTipsValue()) {
                            duidokuGrid[x][y].setFont(new Font("Arial", Font.BOLD, 20));
                            duidokuGrid[x][y].setText(availableOptions.toString());
                        }
                }
        }
        evaluateGameState();
    }

    public void saveUserData() {
        Users.User user = Settings.getCurrentUser();
        if (player)
            user.addGameToDuidokuArraylist("Victory");
        else
            user.addGameToDuidokuArraylist("Defeat");
        Users.serializeAndWriteFile(Settings.getCurrentUsersList());
    }

    @Override
    public boolean isAcceptableInput(Character input) {
        return availableNumbers.contains(input.toString()) || availableLetters.contains(input.toString().toUpperCase());
    }

    private boolean areThereAvailableMoves() {
        for (int x = 0; x < 4; ++x)
            for (int y = 0; y < 4; ++y) {
                if (duidokuGrid[x][y].isSelectable())
                    return true;
            }
        return false;
    }

    public HashSet<String> getTipsForCell(Cell cell) {
        HashSet<String> availableOptions;
        if (Settings.getPuzzleRepresentation().equals("Numbers"))
            availableOptions = new HashSet<>(Arrays.asList("1", "2", "3", "4"));
        else
            availableOptions = new HashSet<>(Arrays.asList("A", "B", "C", "D"));
        int row = cell.getPositionX();
        int column = cell.getPositionY();

        for (int x = 0; x < 4; ++x) {
            availableOptions.remove(duidokuGrid[row][x].getText());
            availableOptions.remove(duidokuGrid[x][column].getText());
        }
        int puzzleRow = row - row % 2;
        int puzzleColumn = column - column % 2;
        for (int x = puzzleRow; x < puzzleRow + 2; ++x)
            for (int y = puzzleColumn; y < puzzleColumn + 2; ++y) {
                availableOptions.remove(duidokuGrid[x][y].getText());
            }

        return availableOptions;
    }

    @Override
    public void paintBorders(){
        Cell[][] puzzle = getPuzzle();
        for (int x=0; x<4; ++x)
            for (int y=0; y<4; ++y)
                puzzle[x][y].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.DARK_GRAY));
        puzzle[1][0].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 3, Color.DARK_GRAY));
        puzzle[1][1].setBorder(BorderFactory.createMatteBorder(1, 1, 3, 3, Color.DARK_GRAY));
        puzzle[0][1].setBorder(BorderFactory.createMatteBorder(1, 1, 3, 1, Color.DARK_GRAY));
        puzzle[0][2].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.DARK_GRAY));
        puzzle[1][2].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 3, Color.DARK_GRAY));
        puzzle[1][3].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 3, Color.DARK_GRAY));

        puzzle[2][0].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.DARK_GRAY));
        puzzle[2][1].setBorder(BorderFactory.createMatteBorder(1, 1, 3, 1, Color.DARK_GRAY));
        puzzle[2][1].setBorder(BorderFactory.createMatteBorder(1, 1, 3, 1, Color.DARK_GRAY));
        puzzle[3][1].setBorder(BorderFactory.createMatteBorder(1, 1, 3, 1, Color.DARK_GRAY));
        puzzle[3][1].setBorder(BorderFactory.createMatteBorder(1, 1, 3, 1, Color.DARK_GRAY));
    }
}
