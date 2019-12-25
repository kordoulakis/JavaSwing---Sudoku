import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;


public class Settings {
    private static String language;
    private static String puzzleRepresentation;
    private Users currentUsersList;
    private Users.User currentUser;

    private boolean showTips;

    public Settings() {
        language = "English";
        puzzleRepresentation = "Numbers";
        try { //If the file is deleted or doesn't exist, it creates a new Users Object off that.
            currentUsersList = Users.loadFile();
            currentUser = currentUsersList.getList().get(0);
        }
        catch (FileNotFoundException n){
            currentUsersList = new Users();
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

    public static String getLanguage() {
        return language;
    }

    public void showUsersList(){
        JFrame f = new JFrame();
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setLayout(new FlowLayout());
        f.setSize(400,400);
        for (Users.User u : currentUsersList.getList()){
            System.out.println(u.getUsername());
            f.add(new SelectUserButton(u.getUsername()));
        }
    }
    public void addUser() {
            String newUser = JOptionPane.showInputDialog(MainFrame.self, "Username:", "Add New User", JOptionPane.QUESTION_MESSAGE);
            if(!newUser.isEmpty()) {
                currentUser = new Users.User(newUser);
                currentUser.addSolvedClassicPuzzleToArraylist(1);
                currentUsersList.addToList(currentUser);
                Users.serializeAndWriteFile(currentUsersList);
            }
            else
                System.err.println("Null string, evaded");
    }

    public void changeUser(){

    }

    public void setShowTips() {
    }

    public boolean getShowTipsValue() {
        return showTips;
    }

}
