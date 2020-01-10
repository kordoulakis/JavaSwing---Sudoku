import java.util.*;

/***
 *
 */
public class NaiveOpponent implements Opponent {
    DuidokuGrid parent;
    Cell[][] board;

    public NaiveOpponent(DuidokuGrid parent) {
        this.parent = parent;
    }

    @Override
    public boolean makeAMove() { //TODO Get the hashset of available moves, do one of them
        ArrayList<Cell> availableCells = getAvailableCellsFromBoard();
        if (availableCells == null)
            return false;
        Cell cell = availableCells.get(new Random().nextInt(availableCells.size()));
        DuidokuController controller = (DuidokuController) parent.getController();
        LinkedList<String> availableMoves = new LinkedList<>(controller.getTipsForCell(cell));
        if (!availableMoves.isEmpty()){
            String text = availableMoves.get(new Random().nextInt(availableMoves.size()));
            controller.setInputAtCell(text,cell,controller.getPuzzle());
            return true;
        }
        return false;
    }

    @Override
    public ArrayList<Cell> getAvailableCellsFromBoard() { //TODO Return hashset of cells, remove them if makeAMoveFails and try again.
        board = parent.getController().getPuzzle();
        ArrayList<Cell> availableCells = new ArrayList<>();

        int cells = 0;
        for (int x = 0; x < board.length; ++x)
            for (int y = 0; y < board.length; ++y) {
                Cell cell = board[x][y];
                if (board[x][y].isSelectable()) {
                    cells++;
                    availableCells.add(board[x][y]);
                }
            }
        if (cells == 0)
            return null;
        return availableCells;

    }
}
