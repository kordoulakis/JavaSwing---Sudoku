import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.LinkedList;

public interface GridController extends KeyListener, ActionListener {
    Cell[][] getPuzzle();
    LinkedList<String> availableNumbers = new LinkedList<>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9"));
    LinkedList<String> availableLetters = new LinkedList<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I"));

    void changeRepresentation();

    boolean setInputAtCell(String userInput, Cell selectedCell, Cell[][] puzzle);

    boolean setCurrentSelectedCell(Cell cell);

    void saveUserData();

    boolean isAcceptableInput(Character input);
    boolean isUniqueInput(Cell selectedCell, Integer userNumber, Cell[][] puzzle);

    default LinkedList<String> showTipsForCell(Cell cell, Cell[][] puzzle){
        LinkedList<String> availableOptions;
        Integer number;
        boolean representation = Settings.getPuzzleRepresentation().equals("Numbers");
        if (representation)
            availableOptions = new LinkedList<>(availableNumbers);
        else
            availableOptions = new LinkedList<>(availableLetters);

        for (number = 1; number < 10; ++number){
            if (!isUniqueInput(cell,number, puzzle))
                if (representation)
                    availableOptions.remove(availableNumbers.get(number-1));
                else
                    availableOptions.remove(availableLetters.get(number-1));
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
}
