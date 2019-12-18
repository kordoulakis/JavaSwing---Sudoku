import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * @author Elias Kordoulas
 *
 */
//TODO Create class Controller so this thing can get a bit clearer
public class ClassicGrid extends JPanel implements SudokuGrid,ActionListener,KeyListener {
    private GridLayout m;
    private Cell currentSelectedCell=null;
    private String language;
    private LinkedList<String> availableLetters;
    private LinkedList<String> availableNumbers;
    private LinkedList<JPanel> viewPanels;
    private Cell[][] puzzle; //The puzzle represented as a 2D array
    private ArrayList<Cell> errorCells = new ArrayList<>();
    public ClassicGrid(){
            super();
            viewPanels = new LinkedList<>();
            puzzle = new Cell[9][9];
            m = new GridLayout(9,9);
            setLayout(m);
            createGrid(9,9);
            setVisible(true);
    }

    //Interface Methods
    @Override
    public void createGrid(int rows, int columns) {
        fillHashsets();
        for(int i=0;i<9;++i) {
            JPanel me = new JPanel();
            me.setLayout(new GridLayout());
            viewPanels.add(me);
        }
        for (int x=0;x<rows;++x)
            for (int y=0;y<columns;++y){
                Cell cell = new Cell(x*y,true,x,y);
                cell.addKeyListener(this); //reacts to key presses
                cell.addActionListener(this); //reacts when clicked on
                puzzle[x][y] = cell;
                add(cell,m);
            }
        paintBorders();
    }

    @Override
    public void setCurrentSelectedCell(Cell cell) {
        if (currentSelectedCell == null) {
            currentSelectedCell = cell;
            currentSelectedCell.select();
        }
        else{
            currentSelectedCell.deSelect();
            currentSelectedCell = cell;
            currentSelectedCell.select();
        }
        clearErrorCells();
    }
    private void loadPuzzle() { //TODO Please solve this

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Cell c = (Cell) e.getSource();
        if (c.isSelectable()) {
            setCurrentSelectedCell(c);

        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) { //Takes the user input and assigns it to the selected Cell.
        Character key = (Character) e.getKeyChar();
        String userInput = key.toString().toUpperCase();
        int userInputAsInt;

        try{ userInputAsInt = Integer.parseInt(userInput); }
        catch (NumberFormatException n){System.out.println("Cought number exception"+n);return; }

        if (currentSelectedCell!= null)
            if (availableNumbers.contains(userInput) || availableLetters.contains(userInput)) {  //TODO Check for settings option and use that as an if, don't allow characters in a numbers game
                clearErrorCells();
                if(userInput.equals(currentSelectedCell.getText()))
                    return;
                if (isCorrect(currentSelectedCell, userInputAsInt)) { //TODO Just check the hidden number of the puzzle, this is only needed for AI solving. maybe
                    currentSelectedCell.setUserNumber(userInputAsInt);
                    currentSelectedCell.setBackground(Color.ORANGE);
                    currentSelectedCell.setText(userInput);
                } else {
                    for (Cell errorCell : errorCells)
                        errorCell.paintUserError();
                    currentSelectedCell.setBackground(Color.RED);
                }
            }
    }
    @Override
    public void keyReleased(KeyEvent e) {}

    public void fillHashsets(){ //Have to hard code the available options, no real way out of it
        String[] letters = {"A","B","C","D","E","F","G","H","I"};
        String[] numbers = {"1","2","3","4","5","6","7","8","9"};
        availableLetters = new LinkedList<>(Arrays.asList(letters));
        availableNumbers = new LinkedList<>(Arrays.asList(numbers));
    }

    //Checks user input against the 2D array of existing cells.
    public void clearErrorCells(){
        if(!errorCells.isEmpty()) {
            for (Cell cell : errorCells)
                cell.clearUserError();
            errorCells.clear();
        }
    }
    public boolean isCorrect(Cell c,int userNumber) { //TODO Add list of cells that interfere, paint them red
        int row = c.getPositionX();
        int column = c.getPositionY();

        existsInColumn(column, userNumber);
        existsInRow(row, userNumber);
        existsInArea(row, column, userNumber);

        return !existsInColumn(column, userNumber) && !existsInRow(row, userNumber) && !existsInArea(row, column, userNumber);
    }
    private boolean existsInRow(int row,int userNumber){
        for (int i = 0; i < 9; i++)
            if (puzzle[row][i].getNumber() == userNumber) {
                errorCells.add(puzzle[row][i]);
                return true;
            }
        return false;
    }
    private boolean existsInColumn(int column, int number) {
        for (int i = 0; i < 9; i++)
            if (puzzle[i][column].getNumber() == number) {
                errorCells.add(puzzle[i][column]);
                return true;
            }
        return false;
    }
    private boolean existsInArea(int row, int column, int userNumber) {
        int puzzleRow = row - row % 3;
        int puzzleColumn = column - column % 3;
        for (int i=puzzleRow;i<puzzleRow+3;i++)
            for (int x = puzzleColumn;x<puzzleColumn+3;x++)
                if (puzzle[i][x].getNumber() == userNumber) {
                    errorCells.add(puzzle[i][x]);
                    return true;
                }
        return false;
    }

    private void paintBorders(){  //TODO Maybe make this not like this? Not elegant, but it works
        for (int i=0;i<9;++i)
            for (int f=0;f<9;++f)
                puzzle[i][f].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.DARK_GRAY));
        for (int x=0;x<9;++x) {
            puzzle[x][2].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 5, Color.DARK_GRAY));
            puzzle[x][5].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 5, Color.DARK_GRAY));
            puzzle[2][x].setBorder(BorderFactory.createMatteBorder(1, 1, 5, 1, Color.DARK_GRAY));
            puzzle[5][x].setBorder(BorderFactory.createMatteBorder(1, 1, 5, 1, Color.DARK_GRAY));
        }
        puzzle[2][2].setBorder(BorderFactory.createMatteBorder(1, 1, 5, 5, Color.DARK_GRAY));
        puzzle[2][5].setBorder(BorderFactory.createMatteBorder(1, 1, 5, 5, Color.DARK_GRAY));
        puzzle[5][2].setBorder(BorderFactory.createMatteBorder(1, 1, 5, 5, Color.DARK_GRAY));
        puzzle[5][5].setBorder(BorderFactory.createMatteBorder(1, 1, 5, 5, Color.DARK_GRAY));
    }
}


