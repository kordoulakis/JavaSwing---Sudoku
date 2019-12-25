import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Random;

/***
 * This class handles the transformation of the JSON Files "puzzles.json" into a java object using gson
 * It uses an array of Puzzle objects to store it
 * @function @getRandomPuzzle returns a random puzzle that the current user has not yet completed.
 *
 */
public class JSONPuzzles {
    private JSONPuzzle[] puzzles;

    public JSONPuzzles(){
        puzzles = new JSONPuzzle[2];
    }

    /***
     *
     * @param path The absolute path to the file, must be in a folder called Puzzles after artifact is made
     * @return returns the loaded JSON as a JSONPuzzles object which contains an array of arrays representing the puzzles
     * @throws FileNotFoundException If the file is not there, it's handled higher up in the codebase
     */
    static JSONPuzzles deserializeFile() throws FileNotFoundException {
        String path = "Puzzles/puzzles.json";
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));

        return new Gson().fromJson(bufferedReader, JSONPuzzles.class);
    }

    public JSONPuzzle getRandomPuzzle(){
        Random r = new Random();
        int random = r.nextInt(puzzles.length);
        return puzzles[random];
    }

    public static class JSONPuzzle {
        private int id;
        private Integer[][] grid;

        public JSONPuzzle(){
            grid = new Integer[9][9];
        }

        public int getId(){ return id; }
        public void setId(int id) { this.id = id; }
        public Integer[][] getGrid(){ return grid; }
    }
}
