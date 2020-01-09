import javax.swing.*;
import java.awt.*;

public interface GridController {
    Cell[][] getPuzzle();
    void changeRepresentation();
    boolean setInputAtCell(String userInput, Cell selectedCell, Cell[][] puzzle);
    boolean setCurrentSelectedCell(Cell cell);
    boolean saveUserData();

    boolean isCorrect(Cell currentSelectedCell, Integer userInputAsInt, Cell[][] puzzle);

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
