import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class FrameMenuBar extends JMenuBar {
    private JMenu settings, language, user, help;
    private JMenuItem addNewUser, selectUser, showGames;
    private JMenuItem english, greek;
    private static JCheckBoxMenuItem showTips, wordoku;
    private Settings gameSettings;
    private static ArrayList<JMenu> allMenus;
    private static ArrayList<JMenuItem> allMenuItems;
    private static ArrayList<JCheckBoxMenuItem> allCheckBoxMenuItems;

    public FrameMenuBar(){
        super();
        allMenus = new ArrayList<>();
        allMenuItems = new ArrayList<>();
        allCheckBoxMenuItems = new ArrayList<>();

        gameSettings = new Settings();

        //Add the menus
        settings = new JMenu("Settings");
        settings.setName("Settings");
        language = new JMenu("Language");
        language.setName("Language");
        user = new JMenu("User");
        user.setName("User");
        help = new JMenu("In-game Settings");
        help.setName("Help");
        //Add menus to ArrayList
        allMenus.add(settings);
        allMenus.add(language);
        allMenus.add(user);
        allMenus.add(help);

        MenuItemListener menuItemListener = new MenuItemListener();
        //Add CheckBoxSubMenus
        wordoku = new JCheckBoxMenuItem("Wordoku", false);
        wordoku.setActionCommand("Wordoku");
        wordoku.addActionListener(menuItemListener);
        showTips = new JCheckBoxMenuItem("Show Tips", false);
        showTips.setActionCommand("Show Tips");
        showTips.addActionListener(menuItemListener);
        showTips.setName("ShowTips");
        allCheckBoxMenuItems.add(showTips);

        //Add submenus
        greek = new JMenuItem("Greek");
        greek.setActionCommand("Greek");
        greek.setName("Greek");
        english = new JMenuItem("English");
        english.setActionCommand("English");
        english.setName("English");

        addNewUser = new JMenuItem("Add new User");
        addNewUser.setActionCommand("AddUser");
        addNewUser.setName("AddNewUser");

        selectUser = new JMenuItem("Select User");
        selectUser.setActionCommand("SelectUser");
        selectUser.setName("SelectUser");
        showGames = new JMenuItem("Show Games Standing");
        showGames.setActionCommand("ShowGames");
        showGames.setName("ShowGames");

        allMenuItems.add(greek);
        allMenuItems.add(english);
        allMenuItems.add(addNewUser);
        allMenuItems.add(selectUser);
        allMenuItems.add(showGames);

        //Set ActionListeners
        greek.addActionListener(menuItemListener);
        english.addActionListener(menuItemListener);
        addNewUser.addActionListener(menuItemListener);
        selectUser.addActionListener(menuItemListener);
        showGames.addActionListener(menuItemListener);

        //Add multiple submenus
        help.add(showTips);
        help.add(wordoku);

        language.add(greek);
        language.add(english);

        user.add(addNewUser);
        user.add(selectUser);
        user.add(showGames);

        settings.add(language);
        settings.add(user);
        settings.add(help);

        //Adds the menus and their children to the FrameMenuBar
        add(settings);
    }
    public static ArrayList<JMenu> getAllMenus() { return allMenus; }
    public static ArrayList<JMenuItem> getAllMenuItems() { return allMenuItems; }
    public static ArrayList<JCheckBoxMenuItem> getAllCheckBoxMenuItems() { return allCheckBoxMenuItems; }
    class MenuItemListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("English") || command.equals("Greek")) {
                gameSettings.setLanguage(command);
            }
            else if (command.equals("Wordoku")) {
                gameSettings.changeRepresentation();
            }
            else if(command.equals("AddUser")){
                gameSettings.addUser();
            }
            else if(command.equals("SelectUser")){
                gameSettings.showUsersList();
            }
            else if (command.equals("Show Tips")) {
                gameSettings.setShowTips();
            }
            else if(command.equals("ShowGames"))
                gameSettings.showCurrentUserGamesAgainstComputer();
        }
    }
}
