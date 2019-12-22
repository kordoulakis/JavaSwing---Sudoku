import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;

public class Cell extends JButton {

    private int userNumber, hiddenNumber;
    private boolean selected;
    private boolean isSelectable;
    private int positionX,positionY;

    public Cell(boolean isSelectable, int positionY, int positionX){
        super();
        selected = false;
        this.positionX = positionX;
        this.positionY = positionY;
        this.isSelectable = isSelectable;

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

    public void setSelectable(boolean selectable){ isSelectable = selectable; }
    public int getNumber(){ return userNumber; }
    public int getHiddenNumber() { return hiddenNumber; }
    public int getPositionX() { return positionX; }
    public int getPositionY() { return positionY; }

    public void setHiddenNumber(int number) { hiddenNumber = number; }
    public void setUserNumber(int number){ userNumber = number; }
}

