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

    public MainMenu(){ //TODO transfer puzzle loading here, do it once for the whole instance of the game
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

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        MenuButton me = (MenuButton)e.getSource();
        if (me == classicButton) {
            try { //TODO Move this to the whole method, one try for everything
                currentGrid = new ClassicGrid(); //TODO Fix this please
                MainFrame.self.add((ClassicGrid)currentGrid);
                setVisible(false); //TODO THIS is the problem when there are no more puzzles to solve
                Settings.setCurrentGrid(currentGrid);
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(MainFrame.self,"Puzzles file not found.\nMake sure there is a Puzzles" +
                        " folder in your directory","FUckedup",JOptionPane.ERROR_MESSAGE);
                currentGrid=null;
            }
        }
        else if (e.getSource() == killerSudokuButton)
            ;
        else if (e.getSource() == duidokuButton)
            ;
        System.out.println("ActionListener worked, source: "+me.getText());
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
