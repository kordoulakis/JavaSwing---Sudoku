import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;


public class Settings {
    private static String language;
    private static String puzzleRepresentation;
    private static Users currentUsersList;
    private static Users.User currentUser;
    private static JFrame changeUserFrame;
    private boolean showTips;

    public Settings() throws FileNotFoundException {
        language = "English";
        puzzleRepresentation = "Numbers";
        try { //If the file is deleted or doesn't exist, it creates a new Users Object off that.
            currentUsersList = Users.loadFile();
            currentUser = currentUsersList.getList().get(0);
        }
        catch (FileNotFoundException n){
            currentUsersList = new Users();
            currentUser = new Users.User("Anonymous");
        }
    }

    public void changeLanguage(String language) {
        this.language = language;
    }

    public static void changeRepresentation() {
        if (puzzleRepresentation.equals("Numbers"))
            puzzleRepresentation = "Letters";
        else
            puzzleRepresentation = "Numbers";
    }

    public static String getPuzzleRepresentation() {
        return puzzleRepresentation;
    }

    public static ArrayList<Integer> getUserClassicPuzzles(){ return currentUser.getSolvedClassicPuzzles(); }
    public static String getLanguage() {
        return language;
    }
    public static Users.User getCurrentUser(){ return currentUser; }
    public static Users getCurrentUsersList(){ return currentUsersList; }

    public void showUsersList(){
        changeUserFrame = new JFrame();
        changeUserFrame.setTitle("Current User: "+currentUser.getUsername());
        changeUserFrame.setVisible(true);
        changeUserFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        changeUserFrame.setLayout(new FlowLayout());
        changeUserFrame.setSize(400,400);
        for (Users.User u : currentUsersList.getList()){
            System.out.println(u.getUsername());
            changeUserFrame.add(new SelectUserButton(u.getUsername()));
        }
    }
    public void addUser() {
            String newUser = JOptionPane.showInputDialog(MainFrame.self, "Username:", "Add New User", JOptionPane.QUESTION_MESSAGE);
            if(!newUser.isEmpty()) {
                currentUser = new Users.User(newUser);
                currentUsersList.addToList(currentUser);
                Users.serializeAndWriteFile(currentUsersList);
            }
            else
                System.err.println("Null string, evaded");
    }

    public static void changeUser(String username){
        for (Users.User user : currentUsersList.getList()){
            if (user.getUsername().equals(username)) {
                currentUser = user;
                changeUserFrame.setTitle("Current User: "+username);
            }
        }
    }


    public void setShowTips() {
    }

    public boolean getShowTipsValue() {
        return showTips;
    }

}
