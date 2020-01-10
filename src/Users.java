import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

/***
 * @Class_Definition
 * This class represents the user's data
 * Users acts as an @ArrayList of user objects and allows it to dynamically change through the buttons inside the
 * Settings class.
 * User is an inner class that stores each individual user's data and each one is created at runtime when loading the
 * appropriate ClassicJSONPuzzles files from the game's directory.
 */
public class Users {
    private ArrayList<User> users = new ArrayList<>();

    public Users() {

    }

    public ArrayList<User> getList() {
        return users;
    }


    public static Users loadFile() throws FileNotFoundException {
        Gson gson = new Gson();
        String path = "Users.json";
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));

        return new Gson().fromJson(bufferedReader, Users.class);
    }

    /**
     * @definition This function grabs the current users object and overwrites the existing JSON file
     *             Uses the gson library
     * @param users all the current users
     */
    public static void serializeAndWriteFile(Users users) {
        try (Writer writer = new FileWriter("Users.json")) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(users);
            writer.write(gson.toJson(users));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addToList(User user) {
        users.add(user);
    }

    static class User {
        private String username;
        private ArrayList<Integer> solvedClassicPuzzles = new ArrayList<>();
        private ArrayList<Integer> solvedKillerPuzzles = new ArrayList<>();
        private ArrayList<String> gamesAgainstComputer = new ArrayList<>();

        public User(String username) {
            this.username = username;
            System.out.println("Created new user as: " + username);
        }

        public void addSolvedClassicPuzzleToArraylist(int puzzleID) { solvedClassicPuzzles.add(puzzleID); }
        public void addSolvedKillerSudokuPuzzleToArraylist(int puzzleID) { solvedKillerPuzzles.add(puzzleID); }
        public void addGameToDuidokuArraylist(String winner) { gamesAgainstComputer.add(winner); }

        public ArrayList<Integer> getSolvedClassicPuzzles() { return solvedClassicPuzzles; }
        public ArrayList<Integer> getSolvedKillerSudokuPuzzles() { return solvedKillerPuzzles; }
        public ArrayList<String> getGamesAgainstComputer() { return gamesAgainstComputer; }

        public String getUsername() { return username; }
    }
}

