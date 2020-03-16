import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Canvas extends JPanel {
    BufferedImage worldImg = null;


    public Canvas(String image) {
        this.setSize(new Dimension(Game.WIDTH, Game.HEIGHT));
        try {
            worldImg = ImageIO.read(new File(image));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-3);
        }
    }


    public void paintComponent(Graphics g) {
        g.drawImage(worldImg, 0, 0, null);
    }
}
