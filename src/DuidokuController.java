import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

public class DuidokuController implements GridController, ActionListener, KeyListener {
    private DuidokuGrid parent;
    private Cell[][] duidokuGrid;
    private Cell currentSelectedCell;
    private Opponent opponent;
    private boolean player;

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
                player = true;
            }
        return true;
    }

    @Override
    public Cell[][] getPuzzle() {
        return duidokuGrid;
    }

    @Override
    public void changeRepresentation() {
        for (int x = 0; x < 4; ++x)
            for (int y = 0; y < 4; ++y) {
                String text = duidokuGrid[x][y].getText();
                Integer textAsInt = 0;
                if (!text.equals("")) {
                    if (availableLetters.contains(text))
                        textAsInt = availableLetters.indexOf(text) + 1;
                    else if (availableNumbers.contains(text)) {
                        textAsInt = Integer.parseInt(text);
                        text = availableLetters.get(textAsInt - 1);
                    } else
                        break;

                    if (Settings.getPuzzleRepresentation().equals("Numbers"))
                        duidokuGrid[x][y].setText(textAsInt.toString());
                    else
                        duidokuGrid[x][y].setText(text);
                }
            }
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

        if (isCorrect(selectedCell, userInputAsInt, getPuzzle())) {
            selectedCell.setFont(new Font("Arial", Font.BOLD, 80));
            if (Settings.getPuzzleRepresentation().equals("Numbers"))
                selectedCell.setText(userInputAsInt.toString());
            else
                selectedCell.setText(availableLetters.get(userInputAsInt - 1));

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
    public boolean isCorrect(Cell currentSelectedCell, Integer input, Cell[][] puzzle) {
        int row = currentSelectedCell.getPositionX();
        int column = currentSelectedCell.getPositionY();

        for (int x = 0; x < 4; ++x) {
            if (puzzle[row][x].getText().equals(input.toString()))
                return false;
            if (puzzle[x][column].getText().equals(input.toString()))
                return false;
        }

        return true;
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
    public boolean showTipsForCurrentCell() {
        return false;
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
        if (availableLetters.contains(key.toString().toUpperCase()) || availableNumbers.contains(key.toString())) {
            if (setInputAtCell(key.toString().toUpperCase(), currentSelectedCell, duidokuGrid)) {
                updateBoard();
                opponent.makeAMove();
                updateBoard();
            }
        }
    }

    private boolean updateBoard() {
        if (areThereAvailableMoves()) {
            for (int x = 0; x < 4; ++x)
                for (int y = 0; y < 4; ++y) {
                    HashSet<String> availableOptions = getTipsForCurrentCell(duidokuGrid[x][y]);
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
        else
        {
            player = !player;
            String text;
            if (player) text = "Player"; else text = "Computer";
            JOptionPane.showMessageDialog(parent, "Winner: " +text, "END OF GAME", JOptionPane.ERROR_MESSAGE);
            MainMenu.self.returnToMainMenu();
        }
        return true;
    }

    private boolean areThereAvailableMoves() {
        int available = 0;
        for (int x = 0; x < 4; ++x)
            for (int y = 0; y < 4; ++y) {
                if (duidokuGrid[x][y].isSelectable())
                    available++;
            }

        return available != 0;
    }

    public HashSet<String> getTipsForCurrentCell(Cell cell) {
        HashSet<String> availableOptions = new HashSet<>(Arrays.asList("1", "2", "3", "4"));
        int row = cell.getPositionX();
        int column = cell.getPositionY();

        for (int x = 0; x < 4; ++x) {
            availableOptions.remove(duidokuGrid[row][x].getText());
            availableOptions.remove(duidokuGrid[x][column].getText());
        }

        return availableOptions;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
