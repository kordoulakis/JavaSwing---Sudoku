import java.awt.*;
import java.util.HashMap;
import java.util.Random;

public class NaiveOpponent implements Opponent {
    DuidokuGrid parent;
    Cell[][] board;

    public NaiveOpponent(DuidokuGrid parent){
        this.parent = parent;
    }

    @Override
    public boolean makeAMove() {
        Cell cell = analyzeBoard();
        if(cell != null){
            Random r = new Random();
            Integer a = r.nextInt(3) + 1;
            if(!parent.getController().isCorrect(cell, a, parent.getController().getPuzzle()))
                makeAMove();
            else {
                cell.setText(a.toString());
                cell.setSelectable(false);
                cell.setBackground(Color.RED);
            }
        }
        return false;
    }

    @Override
    public Cell analyzeBoard() {
        board = parent.getController().getPuzzle();
        int availableCells=0;
        HashMap<Integer, Cell> map = new HashMap<>();
        for (int x=0; x<board.length; ++x)
            for (int y=0; y<board.length; ++y){
                Cell cell = board[x][y];
                if (board[x][y].isSelectable()){
                    availableCells++;
                    map.put(availableCells, cell);
                }
            }
        if (availableCells==0)
            return null;

        Random r = new Random();
        int selection = r.nextInt(map.size());

        return map.get(selection);
    }
}
