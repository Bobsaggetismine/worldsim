package core;

import cell.CellManager;
import core.network.Client;
import core.network.NetworkListener;
import core.network.NetworkSender;
import ui.models.Settings;
import ui.views.GameWindow;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class Game implements Runnable {
    //non app specific config
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    private final Settings gameSettings;
    private final Statistics gameStatistics;
    private final CellManager cellManager;
    private final GameWindow gameWindow;
    public int world_population;

    private final Random random;

    @SuppressWarnings("FieldCanBeLocal")
    private boolean running = false;

    private boolean reset_event = false;

    private Thread logThread, connectionAccepterThread, networkTransmitterThread;


    public Game() {
        world_population = 0;
        random = new Random();
        gameSettings = new Settings();
        gameStatistics = new Statistics();

        try {
            gameSettings.loadConfig();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        gameWindow = new GameWindow(gameSettings.WORLD_IMAGE, gameSettings);
        cellManager = new CellManager(gameStatistics, gameSettings, gameWindow, this);
        loadCountries();

        ArrayList<Client> clients = new ArrayList<Client>();

        logThread = new Thread(new Log(gameStatistics));
        connectionAccepterThread = new Thread(new NetworkListener(clients));
        networkTransmitterThread = new Thread(new NetworkSender(clients, gameWindow.canvas().world()));
        logThread.start();
        connectionAccepterThread.start();
        networkTransmitterThread.start();
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
        gameWindow.reset(gameSettings.WORLD_IMAGE);
        cellManager.reset();
        gameSettings.loadConfig();
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
        int damage = random.nextInt(gameSettings.MAX_DAMAGE) + 1;
        for (int i = iStart; i < iEnd; ++i) {
            for (int j = yStart; j < yEnd; ++j) {
                if (random.nextInt(3) == 1) {
                    cellManager.setCountry(i, j, new Color(r, g, b), t, damage, false, 0, 0);
                }
            }
        }
    }
}

