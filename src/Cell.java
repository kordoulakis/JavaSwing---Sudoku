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
    private int positionX,positionY;

    public Cell(int id,boolean isSelectable, int positionX, int positionY){
        super();
        selected = false;
        this.positionX = positionX;
        this.positionY = positionY;
        this.isSelectable = isSelectable;
        this.id = id;
        String m = ((Integer)id).toString();
        //Sets the color and makes the cell white, will only show color when clicked on
        setForeground(Color.BLACK);
        setBackground(Color.ORANGE);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setFont(new Font("Helvetica",Font.BOLD,80));
    }

    public void select(){ //Sets the current cell as selected
        selected = true;
        setBackground(Color.ORANGE);
        setContentAreaFilled(true);
    }
    public void deSelect(){
        selected = false;
        setBackground(Color.ORANGE);
        setContentAreaFilled(false);
    }

    public boolean isSelectable(){ return isSelectable; }
    public void paintUserError(){
        this.setContentAreaFilled(true);
        this.setBackground(Color.RED);
    }
    public void clearUserError(){
        this.setContentAreaFilled(false);
        this.setBackground(Color.ORANGE);
    }
    public int getID(){ return id; }
    public int getNumber(){ return userNumber; }
    public int getPositionX() { return positionX; }
    public int getPositionY() { return positionY; }
    public void setUserNumber(int number){ userNumber = number; }
}

