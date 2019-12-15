import javax.swing.*;
import java.awt.*;
import java.net.URL;
//Created for simplicity and added functionality specifically for the main menu buttons.
//All Button Icons must be 1:~1 aspect ratio and of type .png
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
            System.err.println("MENUBUTTON_ERROR: Image for MenuButton "+imageName+" not found");
        }

        setFocusable(false);
        setIcon(icon);
        setBorder(BorderFactory.createEmptyBorder());
        setContentAreaFilled(false);

        //For development, delete for release
        setText(imageName);
        setHorizontalTextPosition(SwingConstants.CENTER);
        setVerticalTextPosition(SwingConstants.TOP);
    }
}
