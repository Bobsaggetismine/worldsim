import netscape.javascript.JSObject;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class Game implements Runnable
{
    //non app specific config
    static final int WIDTH = 1280;
    static final int HEIGHT = 720;

    private Config gameConfig;
    private Statistics gameStatistics;
    private CellManager cellManager;
    private GameWindow gameWindow;
    private boolean running;
    private int world_population;

    private Random random;

    private int frames;
    private boolean reset_event = false;


    public Game() {
        world_population = 0;
        random = new Random();
        gameConfig = new Config();
        gameStatistics = new Statistics();
        cellManager = new CellManager();
        try{
            gameConfig.loadConfig();
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }

        gameWindow = new GameWindow(gameConfig.WORLD_IMAGE,gameConfig);
        loadCountries();
    }
    public void run() {
        try{
            running = true;
            frames=0;
            //gameStatistics.fullReset();
            while (running) {
                update();
                render();
                frames++;
                //uncomment this to lower memory usage but decrease performance. This just suggests to the jvm to gc, it does not force gc.
                //System.gc();
                if(reset_event){
                    reset();
                }
            }
        }catch(ArithmeticException ex){
            Object[] options = { "OK" };
            JOptionPane.showOptionDialog(null, "All units died, please fix settings so this cannot happen", "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
            System.exit(-4);
        }

    }
    private void render() {
        gameWindow.repaint();
    }
    private void update() {
        for (int i = 0; i < cellManager.length(); ++i) {
            for (int j = 0; j < cellManager.length(i); ++j) {
                if (cellManager.get(i,j).active()) {

                    Cell p = cellManager.get(i,j);

                    gameStatistics.addToPopulationStatistics(p);
                    gameStatistics.unsafePeople += p.handleAging(random,gameConfig);

                    if (p.shouldDie(gameConfig)) {
                        world_population--;
                        cellManager.kill(i, j, gameWindow,gameConfig, gameStatistics, true);
                        continue;
                    } else {
                        gameWindow.canvas.worldImg.setRGB(i, j, p.color.getRGB());
                    }

                    int x = random.nextInt(1 - -1 + 1) + -1;
                    int y = random.nextInt(1 - -1 + 1) + -1;
                    //if the location we're trying to move to is grass
                    if (gameWindow.canvas.worldImg.getRGB(i + x, j + y) == gameConfig.GREEN) {
                        //if we should reproduce
                        if (p.reproduction > gameConfig.REPRODUCTION_THRESHOLD) {
                            if (world_population < gameConfig.MAX_POPULATION) {
                                //we do
                                p.reproduction = 0;
                                gameWindow.canvas.worldImg.setRGB(i + x, j + y, p.color.getRGB());
                                cellManager.setCountry( (i+x), (j+y),p.color,p.tribe,p.getKidsDamage(random,gameConfig), p.getIsChildDiseased(random, gameConfig) );
                                world_population++;
                            }

                        } else {
                            //otherwise we just move
                            gameWindow.canvas.worldImg.setRGB(i, j, gameConfig.GREEN);
                            gameWindow.canvas.worldImg.setRGB(i + x, j + y, p.color.getRGB());
                            cellManager.setCountry( (i+x), (j+y),p.color,p.tribe,p.getDamage(), p.getIsChildDiseased(random,gameConfig),p.xbias,p.ybias,p.age );
                            cellManager.get(i+x,j+y).reproduction = p.reproduction;
                            cellManager.kill(i,j, gameWindow,gameConfig,gameStatistics, false);
                        }

                    }
                    //if its not green, is it a player? (if not its water so we will do nothing)
                    else if (cellManager.get(i+x, j+y).active()) {
                        //is this player nit in our tribe?
                        if (!cellManager.get(i+x,j+y).tribe.equals(p.tribe)) {
                            //fight!
                            if ( cellManager.get(i+x, j+y) .getDamage() > p.getDamage() && (random.nextInt(100) < gameConfig.STRONGER_WINS_CHANCE) ) {
                                cellManager.kill(i,j, gameWindow,gameConfig,gameStatistics,  false);
                                world_population--;
                                gameWindow.canvas.worldImg.setRGB(i, j, gameConfig.GREEN);
                                gameStatistics.diedToWar++;
                            } else {
                                cellManager.setCountry(i + x,j + y,p.color, p.tribe, p.getDamage(), p.isDiseased, p.xbias, p.ybias,p.age);
                                cellManager.kill(i,j, gameWindow,gameConfig, gameStatistics,  false);
                                world_population--;
                                gameWindow.canvas.worldImg.setRGB(i, j, gameConfig.GREEN);
                                gameStatistics.diedToWar++;
                            }
                        }
                        //its a player in our tribe
                        else {
                            if (p.isDiseased) {
                                if (random.nextInt(100) < gameConfig.DISEASE_INFECTIVITY)
                                    cellManager.get(x+i,j+y).infect();
                            }
                        }
                    }

                }
            }
        }
        if(gameConfig.LOG) {
            frames=Log.log(gameStatistics, frames);
            gameStatistics.resetStatistics();
        }
    }

    private void reset(){
        gameWindow.reset(gameConfig.WORLD_IMAGE);
        cellManager.reset();
        try {
            gameConfig.loadConfig();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        gameStatistics.fullReset();
        world_population = 0;
        loadCountries();
        reset_event = false;
    }

    public void set_reset(){
        reset_event = true;
    }
    private void loadCountries() {
        File countries_file = new File("res\\countries.conf");
        try {
            Scanner in = new Scanner(countries_file);
            String line;
            while(in.hasNext()){
                line = in.nextLine();
                String[] options = line.split(",");
                if(options.length != 8){
                    System.err.println("Invalid countries config file");
                    System.exit(-6);
                }
                generateCountries(options[0], Integer.parseInt(options[1]) ,Integer.parseInt(options[2]),Integer.parseInt(options[3]),Integer.parseInt(options[4]),Integer.parseInt(options[5]),Integer.parseInt(options[6]),Integer.parseInt(options[7]));
            }
        } catch (FileNotFoundException e) {
            System.err.println("Cannot load countries config file");
            System.exit(-5);
        }
    }
    private void generateCountries(String t, int r, int g, int b, int iStart, int iEnd, int yStart, int yEnd){
    int damage = random.nextInt(gameConfig.MAX_DAMAGE) + 1;
    for (int i = iStart; i < iEnd; ++i) {
        for (int j = yStart; j < yEnd; ++j) {
            if (random.nextInt(3) == 1) {
                cellManager.setCountryInitially(i,j,new Color(r, g, b), t, damage);
                world_population++;
            }
        }
    }
}
}

