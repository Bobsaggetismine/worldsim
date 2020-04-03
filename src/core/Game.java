package core;

import cell.CellManager;
import ui.GameWindow;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class Game implements Runnable {
    //non app specific config
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    private final Config gameConfig;
    private final Statistics gameStatistics;
    private final CellManager cellManager;
    private final GameWindow gameWindow;
    public int world_population;

    private final Random random;

    @SuppressWarnings("FieldCanBeLocal")
    private boolean running = false;

    private boolean reset_event = false;

    public Game() {
        world_population = 0;
        random = new Random();
        gameConfig = new Config();
        gameStatistics = new Statistics();

        try {
            gameConfig.loadConfig();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        gameWindow = new GameWindow(gameConfig.WORLD_IMAGE, gameConfig);
        cellManager = new CellManager(gameStatistics, gameConfig, gameWindow, this);
        loadCountries();

        Thread logThread = new Thread(new Log(gameStatistics));
        logThread.start();
    }

    public void run() {
        try {

            running = true;
            while (running) {
                update();
                render();
                if (reset_event) {
                    reset();
                }
            }
        } catch (ArithmeticException ex) {
            Object[] options = {"OK"};
            JOptionPane.showOptionDialog(null, "All units died, please fix settings so this cannot happen", "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
            System.exit(-4);
        }
    }

    private void render() {
        gameWindow.repaint();
    }

    private void update() {
        cellManager.update();
        gameStatistics.resetStatistics();
    }

    private void reset() {
        gameWindow.reset(gameConfig.WORLD_IMAGE);
        cellManager.reset();
        gameConfig.loadConfig();
        gameStatistics.fullReset();
        world_population = 1;
        loadCountries();
        reset_event = false;
    }

    public void set_reset() {
        reset_event = true;
    }

    private void loadCountries() {
        File countries_file = new File("res\\countries.conf");
        try {
            Scanner in = new Scanner(countries_file);
            String line;
            while (in.hasNext()) {
                line = in.nextLine();
                String[] options = line.split(",");
                if (options.length != 8) {
                    System.err.println("Invalid countries config file");
                    System.exit(-6);
                }
                generateCountries(options[0], Integer.parseInt(options[1]), Integer.parseInt(options[2]), Integer.parseInt(options[3]), Integer.parseInt(options[4]), Integer.parseInt(options[5]), Integer.parseInt(options[6]), Integer.parseInt(options[7]));
            }
        } catch (FileNotFoundException e) {
            System.err.println("Cannot load countries config file");
            System.exit(-5);
        }
    }

    private void generateCountries(String t, int r, int g, int b, int iStart, int iEnd, int yStart, int yEnd) {
        int damage = random.nextInt(gameConfig.MAX_DAMAGE) + 1;
        for (int i = iStart; i < iEnd; ++i) {
            for (int j = yStart; j < yEnd; ++j) {
                if (random.nextInt(3) == 1) {
                    cellManager.setCountry(i, j, new Color(r, g, b), t, damage, false, 0, 0);
                }
            }
        }
    }
}

