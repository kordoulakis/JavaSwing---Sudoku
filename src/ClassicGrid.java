import javax.swing.*;
/*This class handles the creation of a classic Sudoku grid.
   It extends JPanel as it is itself a JPanel filled with 81 buttons.
   Every button is of class Cell.
   It loads the puzzle in the form of JSON and assigns it to each individual Cell;
 */
public class ClassicGrid extends JPanel implements SudokuGrid {

    public ClassicGrid(){

    }


    //Interface Methods
    @Override
    public void createGrid(int rows, int columns) {
        for (int i=0; i<rows; ++i)
            for (int x=0; x<columns; ++x){

            }
    }

    @Override
    public void instantiateGrid() {

    }

    @Override
    public void loadPuzzle() {

    }
}
