import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;


public class MainMenu extends JPanel implements ActionListener{
    static MainMenu self;
    private SudokuGrid currentGrid;
    private MenuButton classicButton;  //Buttons are referenced here first so the global actionListener can access them
    private MenuButton killerSudokuButton;
    private MenuButton duidokuButton;
    private JSONPuzzles classicPuzzles;
    private JSONPuzzles killerPuzzles;
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
        try {
            classicPuzzles = JSONPuzzles.deserializeClassicFile();
            System.out.println("Got classics");
            killerPuzzles = JSONPuzzles.deserializeKillerFile();
            System.out.println("Got killers");
        }
        catch (FileNotFoundException f){
            JOptionPane.showMessageDialog(MainFrame.self,"Puzzles file not found.\nMake sure there is a Puzzles" +
                " folder in your directory","FUckedup",JOptionPane.ERROR_MESSAGE);
        }
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        MenuButton me = (MenuButton)e.getSource();
        if (me == classicButton) {
            if(classicPuzzles.getAvailableClassicPuzzles()!=null) {
                currentGrid = new ClassicGrid(classicPuzzles);
                MainFrame.self.add((ClassicGrid) currentGrid);
                setVisible(false);
                Settings.setCurrentGrid(currentGrid);
            }
            else
                JOptionPane.showMessageDialog(MainFrame.self, "No more puzzles to solve!");
        }
        else if (me == killerSudokuButton){
            if(killerPuzzles.getAvailableClassicPuzzles()!=null) {
                currentGrid = new ClassicGrid(killerPuzzles);
                MainFrame.self.add((ClassicGrid) currentGrid);
                setVisible(false);
                Settings.setCurrentGrid(currentGrid);
            }
            else
                JOptionPane.showMessageDialog(MainFrame.self, "No more puzzles to solve!");
        }
        else if (e.getSource() == duidokuButton)
            currentGrid = new DuidokuGrid();

        //System.out.println("ActionListener worked, source: "+me.getText());
    }


    public void returnToMainMenu(){
        try {
            currentGrid.setVisibility(false);
            currentGrid = null;
        }
        catch(NullPointerException n){;}

        setVisible(true);

    }

}
