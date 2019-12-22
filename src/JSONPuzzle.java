import com.google.gson.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class JSONPuzzle {
    private int id;
    private Integer[][] grid;


    public JSONPuzzle(){
        grid = new Integer[9][9];
    }

    public static void serializePuzzle(){
        Gson gson = new Gson();
        String test = gson.toJson(new JSONPuzzle());
    }
    public static void main(String[] args) throws FileNotFoundException {
        serializePuzzle();
        deserializePuzzle();
    }

    public static JSONPuzzle deserializePuzzle() throws FileNotFoundException {
        String path = "src/Puzzles/puzzles.json";
        URL test = Main.class.getResource(path);
        BufferedReader b = new BufferedReader(new FileReader(path));

        Gson gson = new Gson();

        return gson.fromJson(b, JSONPuzzle.class);
    }

    public Integer[][] getGrid(){ return grid; }

}
