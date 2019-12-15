import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;

public class Cell extends JButton {

    private int userNumber, hiddenNumber,id;
    private boolean selected;
    private boolean isSelectable;

    public Cell(){

    }

    public Cell(int id,boolean isSelectable){
        setFont(new Font("Helvetica",Font.BOLD,80));
        selected = false;
        this.isSelectable = isSelectable;
        this.id = id;
        //Sets the color and makes the cell white, will only show color when clicked on
        if(id%3 == 0 && id%9 != 0)
            setBorder(BorderFactory.createMatteBorder(1,1,1,3,Color.GRAY));
        setForeground(Color.BLACK);
        setBackground(Color.ORANGE);
        setContentAreaFilled(false);
        setFocusPainted(false);
    }

    public void select(){ //Sets the current cell as selected
        selected = true;
        setContentAreaFilled(true);
    }
    public void deSelect(){
        selected = false;
        setContentAreaFilled(false);
    }

    public boolean isSelectable(){ return isSelectable; }
    public void paintUserError(){ this.setBackground(Color.RED);}
    public int getID(){ return id; }
}

