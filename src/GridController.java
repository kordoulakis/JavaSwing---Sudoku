import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.ResourceBundle;

public interface GridController extends KeyListener, ActionListener {
    Cell[][] getPuzzle();

    LinkedList<String> availableNumbers = new LinkedList<>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9"));
    LinkedList<String> availableLetters = new LinkedList<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I"));

    default void changeRepresentation() {
        Cell puzzle[][] = getPuzzle();
        for (int x = 0; x < puzzle.length; ++x)
            for (int y = 0; y < puzzle.length; ++y) {
                Cell cell = puzzle[x][y];
                    Integer cellNumber = cell.getUserNumber();
                    if (cellNumber > 0) {
                        String text = availableLetters.get(cellNumber - 1);
                        if (Settings.getPuzzleRepresentation().equals("Numbers")) {
                            puzzle[x][y].setText(cellNumber.toString());
                        } else {
                            puzzle[x][y].setText(text);
                        }
                    }
            }
    }

    boolean setInputAtCell(String userInput, Cell selectedCell, Cell[][] puzzle);

    boolean setCurrentSelectedCell(Cell cell);

    Cell getCurrentSelectedCell();

    void saveUserData();

    boolean isAcceptableInput(Character input);

    boolean isUniqueInput(Cell selectedCell, Integer userNumber, Cell[][] puzzle);

    default LinkedList<String> showTipsForCell(Cell cell, Cell[][] puzzle) {
        LinkedList<String> availableOptions;
        Integer number;
        boolean representation = Settings.getPuzzleRepresentation().equals("Numbers");
        if (representation)
            availableOptions = new LinkedList<>(availableNumbers);
        else
            availableOptions = new LinkedList<>(availableLetters);

        for (number = 1; number < 10; ++number) {
            if (!isUniqueInput(cell, number, puzzle))
                if (representation)
                    availableOptions.remove(availableNumbers.get(number - 1));
                else
                    availableOptions.remove(availableLetters.get(number - 1));
        }
        if (!availableOptions.isEmpty()) {
            cell.setFont(new Font("Arial", Font.BOLD, 20 - availableOptions.size()));
            cell.setText(availableOptions.toString());
        }
        return availableOptions;
    }

    //Paints the borders of the appropriate cells on the board for visual clarity.
    default void paintBorders() {  //TODO Maybe make this not like this? Not elegant, but it works
        Cell[][] puzzle = getPuzzle();
        for (int i = 0; i < puzzle.length; ++i)
            for (int f = 0; f < puzzle.length; ++f)
                puzzle[i][f].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.DARK_GRAY));
        if (puzzle.length > 4) {
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

    default void keyPressed(KeyEvent e) {
        Character key = e.getKeyChar();
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { //If player presses the Escape key, it promts him to go back to MainMenu
            Object[] f = {"Yes", "No"};
            int n = JOptionPane.showOptionDialog((Component) MainMenu.getGrid(), "Really Exit?", "Exit App", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, f, f[1]);
            if (n == 0)
                MainMenu.self.returnToMainMenu();
        } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
            if (getCurrentSelectedCell() != null && getCurrentSelectedCell().isSelectable()) {
                getCurrentSelectedCell().setUserNumber(0);
                getCurrentSelectedCell().setText("");
            }
        if (isAcceptableInput(key) && getCurrentSelectedCell() != null)
            setInputAtCell(key.toString().toUpperCase(), getCurrentSelectedCell(), getPuzzle());
    }

    default void showSolvedPuzzleScreen() {
        ResourceBundle gameBundle = Settings.getGameBundle();
        JOptionPane.showMessageDialog(MainFrame.self, gameBundle.getString("Congratulations"));
    }

    default void keyReleased(KeyEvent e) {
        ;
    }

    default void keyTyped(KeyEvent e) {
        ;
    }
}

