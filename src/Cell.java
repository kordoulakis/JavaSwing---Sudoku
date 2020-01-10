import javax.swing.*;
import java.awt.*;

public class Cell extends JButton {

    private int userNumber, hiddenNumber;
    private boolean isFilled;
    private boolean isSelectable;
    private int positionX,positionY;

    private Color defaultBackground;

    public Cell(boolean isSelectable, int positionY, int positionX){
        super();
        isFilled = false;
        this.positionX = positionX;
        this.positionY = positionY;
        this.isSelectable = isSelectable;

        //Sets the color and makes the cell white, will only show color when clicked on
        setForeground(Color.BLACK);
        setBackground(Color.WHITE);
        setFocusPainted(false);
        setFont(new Font("Moderne Sans",Font.BOLD,80));
        setUserNumber(-1);
    }

    public void select(){ //Sets the current cell as selected
        setBackground(Color.ORANGE);
    }
    public void deSelect(){
        setBackground(Color.WHITE);
    }

    public void setFilled(boolean b) { isFilled = b; }
    public boolean isFilled(){ return isFilled; }

    public boolean isSelectable(){ return isSelectable; }

    public void paintUserError(){
        this.setBackground(Color.RED);
    }
    public void clearUserError(){
        this.setBackground(defaultBackground);
    }

    public void setDefaultColor(Color color){ defaultBackground = color; }
    public Color getDefaultColor(){ return defaultBackground; }

    public void setSelectable(boolean selectable){ isSelectable = selectable; }
    public int getUserNumber(){ return userNumber; }
    public int getHiddenNumber() { return hiddenNumber; }
    public int getPositionX() { return positionX; }
    public int getPositionY() { return positionY; }

    public void setHiddenNumber(int number) { hiddenNumber = number; }
    public void setUserNumber(int number){ userNumber = number; }
}

