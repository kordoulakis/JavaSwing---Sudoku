import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

public class FrameMenuBar extends JMenuBar {
    static FrameMenuBar self;
    private JMenu settings, language, user, help;
    private JMenuItem addNewUser, selectUser;
    private JMenuItem english, greek;
    private static JCheckBoxMenuItem showTips, wordoku;
    private Settings gameSettings;


    public FrameMenuBar() throws FileNotFoundException {
        super();
        self = this;
        //Add the menus
        gameSettings = new Settings();
        settings = new JMenu("Settings");
        language = new JMenu("Language");
        user = new JMenu("User");
        help = new JMenu("In-game Settings");


        MenuItemListener menuItemListener = new MenuItemListener();

        wordoku = new JCheckBoxMenuItem("Wordoku", false);
        wordoku.setActionCommand("Wordoku");
        wordoku.addActionListener(menuItemListener);

        //Add submenus
        greek = new JMenuItem("Greek");
        greek.setActionCommand("Greek");
        english = new JMenuItem("English");
        english.setActionCommand("English");

        addNewUser = new JMenuItem("Add new User");
        addNewUser.setActionCommand("AddUser");
        selectUser = new JMenuItem("Select User");
        selectUser.setActionCommand("SelectUser");

        showTips = new JCheckBoxMenuItem("Show Tips", false);
        showTips.setActionCommand("Show Tips");
        //Set ActionListeners
        greek.addActionListener(menuItemListener);
        english.addActionListener(menuItemListener);
        addNewUser.addActionListener(menuItemListener);
        selectUser.addActionListener(menuItemListener);
        showTips.addActionListener(menuItemListener);

        //Add multiple submenus
        help.add(showTips);
        help.add(wordoku);

        language.add(greek);
        language.add(english);

        user.add(addNewUser);
        user.add(selectUser);

        settings.add(language);
        settings.add(user);
        settings.add(help);

        add(settings);
    }

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
                System.out.println("got tips");
            }
        }
    }

    public static boolean getShowTipsState(){
        return showTips.getState();
    }
    public static boolean getWordokuState(){
        return wordoku.getState();
    }

}
