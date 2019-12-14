import javax.swing.*;
import java.net.URL;
//Created for simplicity and added functionality, specifically for the main menu buttons.
public class MenuButton extends JButton {

    public MenuButton(String imageName){
        super();
        Icon icon;
        String imgName = "images/"+imageName+"Icon.png";
        URL imageURL = getClass().getResource(imgName);
        if (imageURL != null)
            icon = new ImageIcon(imageURL);
        else{
            //icon = null;
            URL safe = getClass().getResource("images/classicButtonIcon.png");
            icon = new ImageIcon(safe);
            System.out.println("ERROR:Image for MenuButton "+imageName+" not found");
        }

        setFocusable(false);
        setIcon(icon);
        setBorder(BorderFactory.createEmptyBorder());
        setContentAreaFilled(false);
        setText(imageName);
        setHorizontalTextPosition(SwingConstants.CENTER);
        setVerticalTextPosition(SwingConstants.TOP);
    }
}
