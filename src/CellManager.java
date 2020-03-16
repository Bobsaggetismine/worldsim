import java.awt.*;

public class CellManager {

    private Cell[][] countries;


    CellManager(){
        countries = new Cell[Game.WIDTH][Game.HEIGHT];
    }
    public void reset(){
        countries = new Cell[Game.WIDTH][Game.HEIGHT];
    }
    public void setCountryInitially(int x, int y, Color col,String t, int damage){
        countries[x][y] = new Cell(col, t, damage, false, Rand.randomBias(), Rand.randomBias(),0);
    }
    public void setCountry(int x, int y, Color col,String t, int damage, boolean diseased){
        countries[x][y] = new Cell(col, t, damage, diseased, Rand.randomBias(), Rand.randomBias(),0);
    }
    public void setCountry(int x, int y, Color col,String t, int damage, boolean diseased, int biasx, int biasy, int age){
        countries[x][y] = new Cell(col, t, damage, diseased, biasx,biasy,age);
    }
    public void setReproduction(int x, int y, int reproduction){
        countries[x][y].reproduction = reproduction;
    }
    public int length(){
        return countries.length;
    }
    public int length(int x){
        return countries[x].length;
    }
    public Cell get(int x, int y){
        return countries[x][y];
    }
    public void kill(int x, int y, GameWindow gameWindow, Config gameConfig, Statistics gameStatistics, boolean addToStats){
        if(countries[x][y].isDiseased && addToStats) gameStatistics.diedToDiease++;
        else if (countries[x][y].age > gameConfig.MAX_AGE && addToStats) gameStatistics.diedToAging++;
        countries[x][y] = null;
        gameWindow.canvas.worldImg.setRGB(x, y, gameConfig.GREEN);
    }

}
