import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;

/***
 * This class handles the transformation of the JSON Files "puzzles.json" into a java object using gson
 * It uses an array of Puzzle objects to store it
 * @function getRandomPuzzle returns a random puzzle that the current user has not yet completed.
 *
 */
public class JSONPuzzles {
    private JSONPuzzle[] puzzles;

    public JSONPuzzles(){
        puzzles = new JSONPuzzle[2];
    }

    static JSONPuzzles deserializeFile() throws FileNotFoundException {
        String path = "src/Puzzles/puzzles.json";
        URL test = Main.class.getResource(path);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));

        return new Gson().fromJson(bufferedReader, JSONPuzzles.class);
    }

    public JSONPuzzle getRandomPuzzle(){ //TODO change to random function
            return puzzles[0];
    }

    public class JSONPuzzle {
        private int id;
        private Integer[][] grid;

        private JSONPuzzle(){
            grid = new Integer[9][9];
        }

        public int getId(){ return id; }
        public Integer[][] getGrid(){ return grid; }
    }
}
