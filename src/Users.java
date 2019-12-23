import java.util.ArrayList;

public class Users {
    static ArrayList<User> users = new ArrayList<>();

    public Users(){

    }

    public void addToList(User user){ users.add(user); }

    static class User {
        private String username;
        private ArrayList<Integer> solvedClassicPuzzles = new ArrayList<>();
        private ArrayList<Integer> getSolvedDuidokuPuzzles = new ArrayList<>();

        public User(String username) {
            this.username = username;
            System.out.println("Created new user as: "+username);
        }

        public void addSolvedClassicPuzzleToArraylist(int puzzleID) {
            solvedClassicPuzzles.add(puzzleID);
        }

        public void addSolvedDuidokuPuzzleToArraylist(int puzzleID) {
            getSolvedDuidokuPuzzles.add(puzzleID);
        }

        public String getUsername() {
            return username;
        }
    }
}
