import com.google.gson.Gson;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.ResourceBundle;

public class KillerJSONPuzzles {
    private KillerJSONPuzzle[] puzzles;

    public KillerJSONPuzzles(){
        puzzles = new KillerJSONPuzzle[10];
    }
    public KillerJSONPuzzle[] getPuzzles(){
        return puzzles;
    }
    static KillerJSONPuzzles deserializeKillerFile(){
        try {
            String path = "Puzzles/KillerPuzzles.json";
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));

            return new Gson().fromJson(bufferedReader, KillerJSONPuzzles.class);
        }
        catch (FileNotFoundException n){
            return null;
        }
    }

    private static void serialize(){
        String json= new Gson().toJson(new KillerJSONPuzzles());
    }

    public ArrayList<Integer> getAvailableKillerPuzzles(){
        ArrayList<Integer> completedPuzzles = Settings.getUserKillerPuzzles();
        ArrayList<Integer> temp = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10));
        temp.removeAll(completedPuzzles);
        if (!temp.isEmpty())
            return temp;
        else
            return null;
    }

    public KillerJSONPuzzle getRandomKillerPuzzle(ArrayList<Integer> availablePuzzles){
        if (availablePuzzles.size()>0) {
            int random = new Random().nextInt(availablePuzzles.size());
            return puzzles[availablePuzzles.get(random)-1];
        }
        return null;
    }

    public static class KillerJSONPuzzle {
        private int id;
        private Area[] Areas;

        public KillerJSONPuzzle(){

        }
        static class Area{
            private Integer AreaID;
            private Integer numberToReach;
            public ArrayList<Integer> Cells;

            public Area(){

            }
            public Integer getNumberToReach() { return numberToReach; }
            public ArrayList<Integer> getCellsOfArea() { return Cells; }
        }

        public Area[] getAreas(){ return Areas; }

        public int getId() { return id; }
    }
}
