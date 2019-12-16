public interface SudokuGrid {

    Cell currentSelectedCell=null;
    String language=null;

    void setCurrentSelectedCell(Cell cell);
    void createGrid(int rows, int columns);
}
