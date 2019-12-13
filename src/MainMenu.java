import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;

public class MainMenu extends JPanel implements ActionListener{
    private JButton classicButton;
    private JButton killerSudokuButton;
    private JButton duidokuButton;
    private JLabel name;
    private GridLayout myL;


    public MainMenu(){
        super();
        instantiateMenu();
    }

    private void instantiateMenu(){
        myL = new GridLayout(3,3);
        this.setLayout(myL);
        classicButton = new JButton("CLASSIC");
        classicButton.setFocusable(false);
        killerSudokuButton = new JButton("KILLER SUDOKU");
        killerSudokuButton.setFocusable(false);
        duidokuButton = new JButton("DUIDOKU");
        duidokuButton.setFocusable(false);

        classicButton.addActionListener(this);
        killerSudokuButton.addActionListener(this);
        duidokuButton.addActionListener(this);


        this.add(classicButton);
        this.add(killerSudokuButton);
        this.add(duidokuButton);

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == classicButton)
            ;
        else if (e.getSource() == killerSudokuButton)
            ;
        else if (e.getSource() == duidokuButton)
            ;
        System.out.println("Hey");
    }
}
