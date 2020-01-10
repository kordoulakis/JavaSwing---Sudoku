import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


public class MainMenu extends JPanel implements ActionListener{
    static MainMenu self;
    private static SudokuGrid currentGrid;
    private MenuButton classicButton;  //Buttons are referenced here first so the global actionListener can access them
    private MenuButton killerSudokuButton;
    private MenuButton duidokuButton;
    private ClassicJSONPuzzles classicPuzzles;
    private KillerJSONPuzzles killerPuzzles;
    public MainMenu(){
        super();
        self = this;
        instantiateMenu();
    }

    private void instantiateMenu(){
        //Create Buttons
        classicButton = new MenuButton("classicButton");
        killerSudokuButton = new MenuButton("killerSudokuButton");
        duidokuButton = new MenuButton("duidokuButton");

        classicButton.addActionListener(this);
        killerSudokuButton.addActionListener(this);
        duidokuButton.addActionListener(this);

        //Set up Layout
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();

        setLayout(layout);

        gbc.ipadx=10; gbc.ipady=20;

        gbc.gridx = 0; gbc.gridy = 4; //changes the position of the button on the grid (x,y)
        add(classicButton,gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        add(killerSudokuButton,gbc);

        gbc.gridx = 2; gbc.gridy = 4;
        add(duidokuButton,gbc);

        classicPuzzles = ClassicJSONPuzzles.deserializeClassicFile();
        killerPuzzles = KillerJSONPuzzles.deserializeKillerFile();

        setVisible(true);
    }

    static SudokuGrid getGrid(){
        return currentGrid;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        MenuButton me = (MenuButton)e.getSource();
        if (me == classicButton) {
            if (classicPuzzles!=null) {
                if (classicPuzzles.getAvailableClassicPuzzles() != null) {
                    currentGrid = new ClassicGrid(classicPuzzles);
                    MainFrame.self.add((ClassicGrid) currentGrid);
                    setVisible(false);
                    Settings.setCurrentGrid(currentGrid);
                } else {
                    ResourceBundle bundle = Settings.getGameBundle();
                    JOptionPane.showMessageDialog(MainFrame.self, bundle.getString("AllSolved"));
                }
            }
            else{
                ResourceBundle bundle = Settings.getGameBundle();
                JOptionPane.showMessageDialog(MainFrame.self,bundle.getString("PuzzleNotFound"),bundle.getString("FileNotFound"),JOptionPane.ERROR_MESSAGE);
            }
        }
        else if (me == killerSudokuButton){
            if (killerPuzzles != null) {
                if (killerPuzzles.getAvailableKillerPuzzles() != null) {
                    currentGrid = new KillerSudokuGrid(killerPuzzles);
                    MainFrame.self.add((KillerSudokuGrid) currentGrid);
                    setVisible(false);
                    Settings.setCurrentGrid(currentGrid);
                } else {
                    ResourceBundle bundle = Settings.getGameBundle();
                    JOptionPane.showMessageDialog(MainFrame.self, bundle.getString("AllSolved"));
                }
            }
            else{
                ResourceBundle bundle = Settings.getGameBundle();
                JOptionPane.showMessageDialog(MainFrame.self,bundle.getString("PuzzleNotFound"),bundle.getString("FileNotFound"),JOptionPane.ERROR_MESSAGE);
            }


        }
        else if (me == duidokuButton) {
            currentGrid = new DuidokuGrid();
            MainFrame.self.add((DuidokuGrid) currentGrid);
            setVisible(false);
            Settings.setCurrentGrid(currentGrid);
        }
    }


    public void returnToMainMenu(){
        try {
            currentGrid.setVisibility(false);
            currentGrid = null;
            setVisible(true);
        }
        catch(NullPointerException n){
            System.out.println("Exception cought on return to menu");
        }

    }

}
