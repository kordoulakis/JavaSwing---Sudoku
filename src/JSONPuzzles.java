import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
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
        puzzles = new JSONPuzzle[10];
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
    public ArrayList<Integer> getAvailableClassicPuzzles(){
        ArrayList<Integer> completedPuzzles = Settings.getUserClassicPuzzles();
        ArrayList<Integer> temp = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10));
        temp.removeAll(completedPuzzles);
        if (!temp.isEmpty())
            return temp;
        else
            return null;
    }
    public JSONPuzzle getRandomClassicPuzzle(ArrayList<Integer> availablePuzzles){
        Random r = new Random();
        if (availablePuzzles.size()>0) {
            int random = r.nextInt(availablePuzzles.size());
            return puzzles[availablePuzzles.get(random)-1];
        }
        return null; //This handles in case the user has solved all puzzles
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
