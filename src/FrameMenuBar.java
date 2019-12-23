import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrameMenuBar extends JMenuBar {
    private JMenu settings, language, user, help;
    private JMenuItem addNewUser, selectUser;
    private JMenuItem english, greek;
    private static JCheckBoxMenuItem showTips, wordoku;
    private Settings gameSettings;


    public FrameMenuBar(){
        super();
        //Add the menus
        gameSettings = new Settings();
        settings = new JMenu("Settings");
        language = new JMenu("Language");
        user = new JMenu("User");
        help = new JMenu("Help");


        MenuItemListener menuItemListener = new MenuItemListener();

        wordoku = new JCheckBoxMenuItem("Wordoku", false);
        wordoku.setActionCommand("Wordoku");
        wordoku.addActionListener(menuItemListener);

        //Add submenus
        greek = new JMenuItem("Greek");
        greek.setActionCommand("Greek");
        english = new JMenuItem("English");
        english.setActionCommand("English");

        addNewUser = new JMenuItem("Add new User"); //TODO Get users from JSON and add appropriate amount of MenuItems
        addNewUser.setActionCommand("AddUser");
        selectUser = new JMenuItem("Select User");
        selectUser.setActionCommand("SelectUser");

        showTips = new JCheckBoxMenuItem("Show Tips", false);

        //Set ActionListeners
        greek.addActionListener(menuItemListener);
        english.addActionListener(menuItemListener);

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
                gameSettings.changeLanguage(command);
            }
            if (command.equals("Wordoku")) {
                gameSettings.changeRepresentation();
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