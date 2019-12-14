import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainMenu extends JPanel implements ActionListener{
    private JButton classicButton;
    private JButton killerSudokuButton;
    private JButton duidokuButton;
    private SudokuGrid currentGrid;
    private boolean inGame;

    public MainMenu(){
        super();
        inGame = false;
        instantiateMenu();
    }

    private void instantiateMenu(){
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        this.setLayout(layout);

        classicButton = new MenuButton("classicButton");
        killerSudokuButton = new MenuButton("killerSudokuButton");
        duidokuButton = new MenuButton("duidokuButton");

        classicButton.addActionListener(this);
        killerSudokuButton.addActionListener(this);
        duidokuButton.addActionListener(this);

        //Set up Layout
        JButton me = new JButton("THTH");
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.ipady = 40;
        this.add(me,gbc);
        gbc.gridx = 0; gbc.gridy = 3;
        this.add(classicButton,gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        this.add(killerSudokuButton,gbc);
        gbc.gridx = 2; gbc.gridy = 3;
        this.add(duidokuButton,gbc);

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        MenuButton me = (MenuButton)e.getSource();
        if (e.getSource() == classicButton)
            currentGrid = new ClassicGrid();
        else if (e.getSource() == killerSudokuButton)
            ;
        else if (e.getSource() == duidokuButton)
            ;
        System.out.println("ActionListener worked, source: "+me.getText());
    }

}
