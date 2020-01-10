import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
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

    private ResourceBundle currentBundle;

    private static SudokuGrid currentGrid;
    private static boolean showTips;

    private Locale currentLocale;

    public Settings() {
        //Locale Initialization
        language = "English";
        currentLocale = new Locale("en", "US");
        currentBundle = ResourceBundle.getBundle("i18n.SettingsBundle", currentLocale);

        puzzleRepresentation = "Numbers";
        try { //If the file is deleted or doesn't exist, it creates a new Users Object
            currentUsersList = Users.loadFile();
            if (!currentUsersList.getList().isEmpty())
                currentUser = currentUsersList.getList().get(0);
            else
                throw new FileNotFoundException();
        } catch (FileNotFoundException n) {
            currentUsersList = new Users();
            currentUser = new Users.User("Anonymous");
        }
    }

    public static void setCurrentGrid(SudokuGrid grid) {
        currentGrid = grid;
    }

    public void changeRepresentation() {
        if (puzzleRepresentation.equals("Numbers"))
            puzzleRepresentation = "Letters";
        else
            puzzleRepresentation = "Numbers";
        if (currentGrid != null) {
            System.out.println("Puzzle Representation: " + puzzleRepresentation);
            currentGrid.getController().changeRepresentation();
        }
    }

    public static String getPuzzleRepresentation() {
        return puzzleRepresentation;
    }


    public static ArrayList<Integer> getUserClassicPuzzles() {
        return currentUser.getSolvedClassicPuzzles();
    }

    public static ArrayList<Integer> getUserKillerPuzzles() {
        return currentUser.getSolvedKillerSudokuPuzzles();
    }


    public static String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        if (Settings.language.equals(language))
            return;
        Settings.language = language;
        if (language.equals("English"))
            currentLocale = new Locale("en", "US");
        else
            currentLocale = new Locale("el", "GR");
        updateViewWithLanguage();
    }

    private void updateViewWithLanguage() {
        ResourceBundle bundle = ResourceBundle.getBundle("i18n.SettingsBundle", currentLocale);
        currentBundle = bundle;
        for (JMenu item : FrameMenuBar.getAllMenus()) {
            item.setText(bundle.getString(item.getName()));
        }
        for (JMenuItem item : FrameMenuBar.getAllMenuItems())
            item.setText(bundle.getString(item.getName()));
        for (JCheckBoxMenuItem item : FrameMenuBar.getAllCheckBoxMenuItems())
            item.setText(bundle.getString(item.getName()));
    }
    public static ResourceBundle getGameBundle(){
        return ResourceBundle.getBundle("i18n.GameBundle", getCurrentLocale());
    }
    public static ResourceBundle getSettingsBundle(){
        return ResourceBundle.getBundle("i18n.SettingsBundle", getCurrentLocale());
    }

    public static Locale getCurrentLocale() {
        if (Settings.language.equals("English"))
            return new Locale("en", "US");
        else
            return new Locale("el", "GR");
    }

    public static Users.User getCurrentUser() {
        return currentUser;
    }

    public static Users getCurrentUsersList() {
        return currentUsersList;
    }

    public void showUsersList() {
        changeUserFrame = new JFrame();
        changeUserFrame.setTitle(currentBundle.getString("CurrentUser") + currentUser.getUsername());
        changeUserFrame.setVisible(true);
        changeUserFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        changeUserFrame.setLayout(new FlowLayout());
        changeUserFrame.setSize(400, 400);
        for (Users.User u : currentUsersList.getList()) {
            System.out.println(u.getUsername());
            changeUserFrame.add(new SelectUserButton(u.getUsername()));
        }
    }

    public void showCurrentUserGamesAgainstComputer() {
        ArrayList<String> games = currentUser.getGamesAgainstComputer();
        if (games.isEmpty()) {
            return;
        }
        gamesAgainstComputerFrame = new JFrame();
        gamesAgainstComputerFrame.setTitle(currentBundle.getString("Games")+ " of " + currentUser.getUsername());
        gamesAgainstComputerFrame.setVisible(true);
        gamesAgainstComputerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gamesAgainstComputerFrame.setLayout(new FlowLayout(FlowLayout.LEFT));
        gamesAgainstComputerFrame.setSize(400, 400);
        Integer i = 0;
        for (String string : games) {
            ++i;
            JButton temp = new JButton(currentBundle.getString("Game")+" " + i + ": " + currentBundle.getString(string));
            temp.setContentAreaFilled(false);
            temp.setFocusable(false);
            gamesAgainstComputerFrame.add(temp);
        }
    }

    public void addUser() {
        String newUser = JOptionPane.showInputDialog(MainFrame.self, currentBundle.getString("UserName"), currentBundle.getString("AddNewUser"), JOptionPane.QUESTION_MESSAGE);
        if (newUser != null) {
            currentUser = new Users.User(newUser);
            currentUsersList.addToList(currentUser);
            Users.serializeAndWriteFile(currentUsersList);
        } else
            System.err.println("Null Username, evaded.");
    }

    public static void changeUser(String username) {
        for (Users.User user : currentUsersList.getList()) {
            if (user.getUsername().equals(username)) {
                currentUser = user;
                changeUserFrame.setTitle(getSettingsBundle().getString("CurrentUser") + username);
            }
        }
    }

    public void setShowTips() {
        showTips = !showTips;
    }

    public static boolean getShowTipsValue() {
        return showTips;
    }

}
