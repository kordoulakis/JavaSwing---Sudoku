import java.util.ArrayList;
import java.util.Set;

public class Settings {

    private static String language;
    private static String puzzleRepresentation;
    private ArrayList<String> completedPuzzlesIds;
    private boolean showTips;

    public Settings(){
        language = "English";
        puzzleRepresentation = "Numbers";
    }

    public void changeLanguage(String language){
        this.language = language;
    }
    public static void changeRepresentation(){
        if (puzzleRepresentation.equals("Numbers"))
            puzzleRepresentation = "Letters";
        else
            puzzleRepresentation = "Numbers";
    }
    public static String getPuzzleRepresentation(){ return puzzleRepresentation; }
    public static String getLanguage(){ return language; }

    public void setShowTips(){ }

    public boolean getShowTipsValue(){
        return showTips;
    }

}
