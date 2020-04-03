package core;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Canvas extends JPanel {
    BufferedImage _worldImg = null;


    public Canvas(String image) {
        this.setSize(new Dimension(Game.WIDTH, Game.HEIGHT));
        try {
            _worldImg = ImageIO.read(new File(image));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-3);
        }
    }

    public BufferedImage world() {
        return _worldImg;
    }


    public void paintComponent(Graphics g) {
        g.drawImage(_worldImg, 0, 0, null);
    }
}
