import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class SelectUserButton extends JButton implements ActionListener {

    private String username;
    public SelectUserButton(String name){
        super();
        username = name;
        setText(name);
        setFocusPainted(false);
        setBackground(Color.WHITE);
        addActionListener(this);
        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Settings.changeUser(username);
    }
}
