import java.awt.*;
import java.util.Random;

class Cell {
    //todo make these private and actually use getters and setters and stop pretending package-private is private when you're not using packages
    Color color;

    private boolean _active = false;

    //Tribe tribe;
    int age;
    int reproduction;
    boolean isDiseased;
    public String tribe;
    int xbias, ybias;

    private int damage;
    private Cell(){
        age = 0;
        reproduction = 0;
        isDiseased = false;
        tribe = "";
        xbias = 0;
        ybias = 0;
    }
    /*
    Cell(Color color, String tribe, int damage, boolean isDiseased, int xbias, int ybias, int age) {
        this.color = color;
        this.tribe = tribe;
        this.age = age;
        this.reproduction = 0;
        this.damage = damage;
        this.isDiseased = isDiseased;
        this.xbias = xbias;
        this.ybias = ybias;
    }
     */
    void set(Color color, String tribe, int damage, boolean isDiseased, int xbias, int ybias, int age) {
        this.color = color;
        this.tribe = tribe;
        this.age = age;
        this.reproduction = 0;
        this.damage = damage;
        this.isDiseased = isDiseased;
        this.xbias = xbias;
        this.ybias = ybias;
        _active = true;
    }
    boolean active(){
        return _active;
    }
    public static Cell emptyCell(){
        return new Cell();
    }
    int getKidsDamage(Random r, Config gameConfig) {
        if (r.nextInt(1000) < gameConfig.MUTATION_CHANCE) {
            if (isDiseased) {
                int temp = ((damage - gameConfig.DISEASE_DAMAGE_HARM) + r.nextInt(gameConfig.MUTATION_AMOUNT) - gameConfig.MUTATION_SUBTRACT);
                if (temp > 0) return temp;
                return 1;
            } else {
                int temp = damage + (r.nextInt(gameConfig.MUTATION_AMOUNT) - gameConfig.MUTATION_SUBTRACT);
                if(temp > 0)return temp;
                return 1;
            }
        }
        if (isDiseased && damage - gameConfig.DISEASE_DAMAGE_HARM > 0)
            return damage - gameConfig.DISEASE_DAMAGE_HARM;
        else if(isDiseased) return 1;

        return damage;
    }

    int getDamage()
    {
        return damage;
    }

    boolean getIsChildDiseased(Random r, Config gameConfig) {
        if (!isDiseased) {
            if (r.nextInt(10000) < gameConfig.DISEASE_SPREAD_RATE) {
                return true;
            }
        }
        return false;
    }


    private void cure() {
        isDiseased = false;
    }

    void infect() {
        isDiseased = true;
    }
    void setInactive(){
        _active = false;
    }
    int handleAging(Random random, Config gameConfig) {
        reproduction++;
        if (isDiseased) {
            if (random.nextInt(100) < gameConfig.DISEASE_CURE_RATE) cure();
            age += gameConfig.DISEASE_MULTIPLIER;
            return 1;
        } else {
            age++;
            return 0;
        }
    }

    public boolean shouldDie(Config gameConfig) {
        return age > gameConfig.MAX_AGE;
    }
}
