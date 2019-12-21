import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class Controller {
    private LinkedList<String> availableLetters;
    private LinkedList<String> availableNumbers;
    public static ArrayList<Cell> errorCells;

    public Controller() {
        errorCells = new ArrayList<>();
        fillHashsets();
    }

    public void handleUserInput(String userInput, int userInputAsInt, Cell selectedCell, Cell puzzle[][]) {
        if (availableNumbers.contains(userInput) || availableLetters.contains(userInput)) {  //TODO Check for settings option and use that as an if, don't allow characters in a numbers game
            if (userInput.equals(selectedCell.getText()))
                return;
            if (isCorrect(selectedCell, userInputAsInt, puzzle)) { //TODO Just check the hidden number of the puzzle, this is only needed for AI solving. maybe
                selectedCell.setUserNumber(userInputAsInt);
                selectedCell.setBackground(Color.ORANGE);
                selectedCell.setText(userInput);
                clearErrorCells();
            } else {
                for (Cell errorCell : errorCells)
                    errorCell.paintUserError();
                selectedCell.setBackground(Color.RED);
            }
        }
    }

    /**
     * This function takes in a gridCell, the user's typed key and finds out if it can be placed on the board
     * @param selectedCell The cell the user has clicked on.
     * @param userNumber The user's input represented as int. This makes sure that it's an available character.
     * @param puzzle The Array storing the whole Grid of Cells
     * @variable errors An int to know if an errors has been found
     * @return True if the user's input is valid.
     */
    public boolean isCorrect(Cell selectedCell, int userNumber, Cell[][] puzzle) { //TODO Add list of cells that interfere, paint them red
        int row = selectedCell.getPositionX();
        int column = selectedCell.getPositionY();
        int errors = 0;

        for (int i=0; i<9; ++i) { //Checks the row and column of the cell for the same input.
            if (puzzle[row][i].getNumber() == userNumber) {
                errorCells.add(puzzle[row][i]);
                ++errors;
                break;
            }
        }
        for (int i=0; i<9; ++i){
            if (puzzle[i][column].getNumber() == userNumber) {
                errorCells.add(puzzle[i][column]);
                ++errors;
                break;
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
                if (puzzle[i][x].getNumber() == userNumber) {
                    errorCells.add(puzzle[i][x]);
                    ++errors;
                    break;
                }

        if (errors>0)
            return false;
        else
            return true;
        //return !existsInColumn(column, userNumber, puzzle) && !existsInRow(row, userNumber, puzzle) && !existsInArea(row, column, userNumber, puzzle);
    }
    public void clearErrorCells() {
        if (!errorCells.isEmpty()) {
            for (Cell cell : errorCells)
                cell.clearUserError();
            errorCells.clear();
        }
    }
    /***
     * @variable letters,numbers: The available sets for characters while playing.
     */
    public void fillHashsets() { //Have to hard code the available options, no real way out of it
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I"};
        String[] numbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
        availableLetters = new LinkedList<>(Arrays.asList(letters));
        availableNumbers = new LinkedList<>(Arrays.asList(numbers));
    }
}
