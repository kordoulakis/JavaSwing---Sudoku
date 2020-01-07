import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.LinkedList;

public class DuidokuController implements  GridController, ActionListener, KeyListener {
    private DuidokuGrid parent;
    private Cell[][] duidokuGrid;
    private Cell currentSelectedCell;
    private Opponent opponent;

    private LinkedList<String> availableLetters = new LinkedList<>(Arrays.asList("A", "B", "C", "D"));
    private LinkedList<String> availableNumbers = new LinkedList<>(Arrays.asList("1", "2", "3", "4"));

    public DuidokuController(DuidokuGrid grid, Opponent opponent) {
        parent = grid;
        this.opponent = opponent;
    }

    public boolean createGrid(int rows, int columns){
        duidokuGrid = new Cell[rows][columns];
        for (int x=0; x<rows; ++x)
            for (int y=0; y<columns; ++y){
                Cell cell = new Cell(true,x,y);
                cell.setFont(new Font("Arial", Font.BOLD, 80));
                cell.setText("");
                cell.addKeyListener(this);
                cell.addActionListener(this);
                parent.add(cell);
                duidokuGrid[y][x] = cell; //
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
                    else {
                        textAsInt = Integer.parseInt(text);
                        text = availableLetters.get(textAsInt - 1);
                    }
                    if (Settings.getPuzzleRepresentation().equals("Numbers"))
                        duidokuGrid[x][y].setText(textAsInt.toString());
                    else
                        duidokuGrid[x][y].setText(text);
                }
            }
    }

    @Override
    public boolean setInputAtCell(String input, Cell selectedCell, Cell[][] puzzle) {
        input = input.toUpperCase();

        Integer userInputAsInt;

        if (availableLetters.contains(input)) {
            userInputAsInt = availableLetters.indexOf(input) + 1;
        }
        else
            userInputAsInt = Integer.parseInt(input);

        if (isCorrect(currentSelectedCell, userInputAsInt, getPuzzle())) {
            if (Settings.getPuzzleRepresentation().equals("Numbers"))
                selectedCell.setText(userInputAsInt.toString());
            else
                selectedCell.setText(availableLetters.get(userInputAsInt - 1));

            currentSelectedCell.setBackground(Color.CYAN);
            currentSelectedCell.setSelectable(false);
            return true;
        }
        return false;
    }

    @Override
    public boolean isCorrect(Cell currentSelectedCell, Integer input, Cell[][] puzzle){
        int row = currentSelectedCell.getPositionX();
        int column = currentSelectedCell.getPositionY();

        for (int x=0;x<4; ++x){
            if (puzzle[row][x].getText().equals(input.toString()))
                return false;
            if (puzzle[x][column].getText().equals(input.toString()))
                return false;
        }

        return true;
    }
    @Override
    public boolean setCurrentSelectedCell(Cell cell) {
        if(currentSelectedCell!=null)
            if (currentSelectedCell.isSelectable())
                currentSelectedCell.setBackground(Color.WHITE);

        cell.setBackground(Color.ORANGE);
        currentSelectedCell = cell;
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
            if(setInputAtCell(key.toString().toUpperCase(), currentSelectedCell, duidokuGrid))
                opponent.makeAMove();
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
