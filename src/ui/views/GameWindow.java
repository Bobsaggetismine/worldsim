package ui.views;

import core.Canvas;
import ui.models.Settings;
import core.Game;
import core.Launcher;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    private Canvas _canvas;
    public JMenuBar menub;
    public JMenu menu;
    public JMenuItem restart_item;
    public JMenuItem open_settings_item;

    private final Settings gameSettings;
    public GameWindow(String image, Settings gameSettings) {
        this.gameSettings = gameSettings;
        init(image);
    }
    private void init(String image){
        ImageIcon img = new ImageIcon("res\\ico.png");

        setIconImage(img.getImage());
        this.setSize(new Dimension(Game.WIDTH, Game.HEIGHT+60));
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _canvas = new Canvas(image);
        menub = new JMenuBar();
        menu = new JMenu("game");
        restart_item = new JMenuItem("reset game");
        open_settings_item = new JMenuItem("settings");
        menu.add(open_settings_item);
        menu.add(restart_item);
        menub.setBackground(Color.gray);
        restart_item.addActionListener(e -> Launcher.start_new_game());
        open_settings_item.addActionListener(e -> new SettingsWindow(gameSettings));
        menub.add(menu);
        menub.setBorder(BorderFactory.createLineBorder(Color.BLACK, 0));

        this.setJMenuBar(menub);
        this.setContentPane(_canvas);
        this.setVisible(true);
    }
    public void reset(String image){
        _canvas = new Canvas(image);
        _canvas.setLayout(null);
        this.setContentPane(_canvas);
        this.setVisible(true);
    }
    public void setPixel(int x, int y, int color){
        _canvas.world().setRGB(x, y, color);
    }
    public int getPixel(int x, int y){
        return _canvas.world().getRGB(x,y);
    }
    public Canvas canvas(){
        return _canvas;
    }
}
