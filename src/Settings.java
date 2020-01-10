import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.Flow;

/***
 * This class binds to FrameMenuBar which is used for housing the settings menus
 * It handles the loading and handling of the user settings as well as the current gameplay options such as Tips and
 * Wordoku.
 */
public class Settings {
    private static String language;
    private static String puzzleRepresentation;
    private static Users currentUsersList;
    private static Users.User currentUser;
    private static JFrame changeUserFrame;
    private static JFrame gamesAgainstComputerFrame;

    private static SudokuGrid currentGrid;
    private static boolean showTips;

    public Settings(){
        language = "English";
        puzzleRepresentation = "Numbers";
        try { //If the file is deleted or doesn't exist, it creates a new Users Object
            currentUsersList = Users.loadFile();
            currentUser = currentUsersList.getList().get(0);
        }
        catch (FileNotFoundException n){
            currentUsersList = new Users();
            currentUser = new Users.User("Anonymous");
        }
    }
    public static void setCurrentGrid(SudokuGrid grid) { currentGrid = grid; }

    public static void changeRepresentation() {
        if (puzzleRepresentation.equals("Numbers"))
            puzzleRepresentation = "Letters";
        else
            puzzleRepresentation = "Numbers";
        if (currentGrid!=null) {
            System.out.println("Puzzle Representation: "+puzzleRepresentation);
            currentGrid.getController().changeRepresentation();
        }
    }

    public static String getPuzzleRepresentation() {
        return puzzleRepresentation;
    }



    public static ArrayList<Integer> getUserClassicPuzzles(){ return currentUser.getSolvedClassicPuzzles(); }
    public static ArrayList<Integer> getUserKillerPuzzles() { return currentUser.getSolvedKillerSudokuPuzzles(); }
    public static String getLanguage() {
        return language;
    }
    public static void setLanguage(String language) { Settings.language = language; }

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
    public void showCurrentUserGamesAgainstComputer(){
        ArrayList<String> games = currentUser.getGamesAgainstComputer();
        if (games.isEmpty()) {
            System.out.println("empty");
            return;
        }
        gamesAgainstComputerFrame = new JFrame();
        gamesAgainstComputerFrame.setTitle(currentUser.getUsername()+"'s games");
        gamesAgainstComputerFrame.setVisible(true);
        gamesAgainstComputerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gamesAgainstComputerFrame.setLayout(new FlowLayout(FlowLayout.LEFT));
        gamesAgainstComputerFrame.setSize(400,400);
        Integer i=0;
        for (String string : games){
            ++i;
            System.out.println(string);
            JButton temp = new JButton("Game "+ i + ": "+ string);
            temp.setContentAreaFilled(false);
            temp.setFocusable(false);
            gamesAgainstComputerFrame.add(temp);
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


    public static void setShowTips() { showTips = !showTips; }
    public static boolean getShowTipsValue() {
        return showTips;
    }

}
