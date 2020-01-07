public interface GridController {
    Cell[][] getPuzzle();
    void changeRepresentation();
    boolean setInputAtCell(String userInput, Cell selectedCell, Cell[][] puzzle);
    boolean setCurrentSelectedCell(Cell cell);
    boolean showTipsForCurrentCell();

    boolean isCorrect(Cell currentSelectedCell, Integer userInputAsInt, Cell[][] puzzle);

}
