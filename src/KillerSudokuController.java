import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;

public class KillerSudokuController extends SudokuController implements GridController, ActionListener, KeyListener {
    private Cell[][] puzzle;
    private Cell currentSelectedCell;
    private int guessesToBeMade;
    private ArrayList<Cell> errorCells;
    private KillerSudokuGrid parent;
    private KillerJSONPuzzles.KillerJSONPuzzle currentPuzzle;
    private LinkedList<Cell> allCells = new LinkedList<>();

    public KillerSudokuController(KillerSudokuGrid grid) {
        errorCells = new ArrayList<>();
        parent = grid;
        puzzle = new Cell[9][9];
        guessesToBeMade = 0;
    }

    public boolean createGrid(int rows, int columns, KillerJSONPuzzles killerPuzzles) {
        currentPuzzle = killerPuzzles.getRandomKillerPuzzle(killerPuzzles.getAvailableKillerPuzzles());
        currentPuzzle = killerPuzzles.getPuzzles()[0];
        for (int y = 0; y < columns; ++y) {
            for (int x = 0; x < rows; ++x) {
                Cell cell = new Cell(true, y, x);
                cell.addKeyListener(this); //reacts to key presses
                cell.addActionListener(this); //reacts when clicked on

                cell.setFont(new Font("Arial", Font.BOLD, 60));
                cell.setText("");
                allCells.add(cell);
                parent.add(cell);
                puzzle[x][y] = cell;
            }
        }
        paintBorders();

        LinkedList<KillerJSONPuzzles.KillerJSONPuzzle.Area> areas = new LinkedList<>(Arrays.asList(currentPuzzle.getAreas()));
        Color temp;
        Font f = new Font("Arial", Font.BOLD, 60);
        boolean isFirst;
        if (!areas.isEmpty())
            for (KillerJSONPuzzles.KillerJSONPuzzle.Area area : areas) {
                isFirst = true;
                temp = getRandomColor();
                for (Integer i : area.getCellsOfArea()) {
                    Cell cell = allCells.get(i - 1);
                    cell.setBackground(temp);
                        cell.setDefaultState(temp, f, null);
                    if (isFirst) {
                        cell.setText(area.getNumberToReach().toString());
                        cell.setDefaultState(temp, f, area.getNumberToReach().toString());
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
    public void changeRepresentation() {

    }

    @Override
    public boolean setInputAtCell(String userInput, Cell selectedCell, Cell[][] puzzle) {
        return false;
    }

    @Override
    public boolean setCurrentSelectedCell(Cell cell) {
        if (currentSelectedCell!=null && currentSelectedCell.isSelectable())
            currentSelectedCell.deSelect();
        currentSelectedCell = cell;
        currentSelectedCell.select();
        return false;
    }

    @Override
    public boolean saveUserData() {
        return false;
    }

    @Override
    public boolean isCorrect(Cell currentSelectedCell, Integer userInputAsInt, Cell[][] puzzle) {
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Cell cell = (Cell) e.getSource();
        setCurrentSelectedCell(cell);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) { //TODO Check inputs, implement updateboard()
        Character key = e.getKeyChar();
        if (currentSelectedCell!=null)
            currentSelectedCell.setText(key.toString());
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public Color getRandomColor(){
        Random r = new Random();
        Color c =new Color(r.nextInt(256-120)+120,
                r.nextInt(256-120)+120,r.nextInt(256-120)+120);
        return c;
    }
}
