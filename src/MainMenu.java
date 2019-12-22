import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;


public class MainMenu extends JPanel implements ActionListener{
    private SudokuGrid currentGrid;
    private MenuButton classicButton;  //Buttons are referenced here first so the global actionListener can access them
    private MenuButton killerSudokuButton;
    private MenuButton duidokuButton;
    private MainFrame root;

    public MainMenu(MainFrame root){
        super();
        this.root = root;
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
        gbc.fill = GridBagConstraints.BOTH; //tells the components how to resize if they need to
        this.setLayout(layout);

        gbc.ipadx=10; gbc.ipady=20;

        gbc.gridx = 0; gbc.gridy = 4; //changes the position of the button on the grid (x,y)
        this.add(classicButton,gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        this.add(killerSudokuButton,gbc);

        gbc.gridx = 2; gbc.gridy = 4;
        this.add(duidokuButton,gbc);

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        MenuButton me = (MenuButton)e.getSource();
        if (me == classicButton) {
            try {
                currentGrid = new ClassicGrid(this);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            root.add((ClassicGrid)currentGrid);
            this.setVisible(false);
        }
        else if (e.getSource() == killerSudokuButton)
            ;
        else if (e.getSource() == duidokuButton)
            ;
        System.out.println("ActionListener worked, source: "+me.getText());
    }

    public void returnToMainMenu(){
        currentGrid.setVisibility(false);
        currentGrid = null;
        this.setVisible(true);
    }

}
