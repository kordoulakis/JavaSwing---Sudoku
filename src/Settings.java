import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class Settings {
    private static String language;
    private static String puzzleRepresentation;
    private boolean showTips;

    public Settings() {
        language = "English";
        puzzleRepresentation = "Numbers";
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

    public void showUsersList(){ //TODO Get from JSON, better styling for this, maybe radiobuttons?
        ArrayList<Users.User> currentUsers = Users.users;
        JFrame f = new JFrame();
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setLayout(new FlowLayout());
        f.setSize(400,400);
        for (Users.User u : currentUsers){
            System.out.println(u.getUsername());
            f.add(new JButton(u.getUsername()));
        }
    }
    public void addUser() {
        try {
            String newUser = JOptionPane.showInputDialog(MainFrame.self, "Username:", "Add New User", JOptionPane.QUESTION_MESSAGE);
            if(!newUser.isEmpty()) {
                Users.users.add(new Users.User(newUser));
            }
        } catch (NullPointerException n) { }
    }

    public void setShowTips() {
    }

    public boolean getShowTipsValue() {
        return showTips;
    }

}
