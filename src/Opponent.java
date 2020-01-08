import java.util.ArrayList;

public interface Opponent {

    boolean makeAMove();
    ArrayList<Cell> getAvailableCellsFromBoard();

}
