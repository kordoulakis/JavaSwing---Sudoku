import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;

public class Users {
    private ArrayList<User> users = new ArrayList<>();

    public Users() {

    }

    public int getSizeOfList() {
        return users.size();
    }

    public ArrayList<User> getList() {
        return users;
    }

    public static void main(String[] args) {
        serializeAndWriteFile(new Users());
    }

    public static Users loadFile() throws FileNotFoundException {
        Gson gson = new Gson();
        String path = "Users.json";
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));

        return new Gson().fromJson(bufferedReader, Users.class);
    }

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
        private ArrayList<Integer> solvedDuidokuPuzzles = new ArrayList<>();

        public User(String username) {
            this.username = username;
            System.out.println("Created new user as: " + username);
        }

        public void addSolvedClassicPuzzleToArraylist(int puzzleID) { solvedClassicPuzzles.add(puzzleID); }
        public void addSolvedDuidokuPuzzleToArraylist(int puzzleID) { solvedDuidokuPuzzles.add(puzzleID); }

        public ArrayList<Integer> getSolvedClassicPuzzles() { return solvedClassicPuzzles; }
        public ArrayList<Integer> getSolvedDuidokuPuzzles() { return solvedDuidokuPuzzles; }

        public String getUsername() { return username; }
    }
}

