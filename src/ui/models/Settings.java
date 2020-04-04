
package ui.models;

import java.io.*;
import java.util.Properties;

public class Settings {
    public String WORLD_IMAGE = "";
    public int REPRODUCTION_THRESHOLD = 0;                //lower this is, the quicker players will reproduce
    public int MAX_DAMAGE = 0;
    public int GREEN = 0;                     //color for land. FOR DEV: other green used: -11010225;
    public int STRONGER_WINS_CHANCE = 0;
    public int MAX_POPULATION = 0;
    public int MAX_AGE = 0;

    //mutation config (mutation changes damage on birth
    public int MUTATION_CHANCE = 0;                                //%chance to mutate (1 = 0.1%)
    public int MUTATION_AMOUNT = 0;                                //higher = higher mutation value range
    public int MUTATION_SUBTRACT = 0;                               //half MUTATION_AMOUNT = mutations will be equally likely to be stronger/weaker. the lower the more likely to be stronger

    //disease config
    public int DISEASE_MULTIPLIER = 0;                              //how many years of life does a diseased player lose per year they have this disease
    public int DISEASE_SPREAD_RATE = 0;                             //% chance to spread to children (1=0.01%)
    public int DISEASE_DAMAGE_HARM = 0;                             //how much damage do diseased peoples children lose?
    public int DISEASE_CURE_RATE = 0;                              // % chance to be cured each tick (1=1%)
    public int DISEASE_INFECTIVITY = 0;                    //% chance to spread disease on contact with tribe members
    public boolean LOG = false;

    public String CONFIG_FILE = "res/config.conf";
    private final Properties props;


    public Settings() {
        props = new Properties();
        loadProps();
    }

    public void loadProps() {
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(new File(CONFIG_FILE));
            props.load(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error cannot load config file, not found");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Properties getProps() {
        return props;
    }

    public void loadConfig() {
        try {
            Properties props = getProps();

            MAX_DAMAGE = Integer.parseInt(props.getProperty("max_damage"));
            STRONGER_WINS_CHANCE = Integer.parseInt(props.getProperty("stronger_wins"));
            MAX_POPULATION = Integer.parseInt(props.getProperty("max_population"));
            REPRODUCTION_THRESHOLD = Integer.parseInt(props.getProperty("reproduction_threshold"));
            MAX_AGE = Integer.parseInt(props.getProperty("max_age"));

            DISEASE_MULTIPLIER = Integer.parseInt(props.getProperty("disease_multiplier"));
            DISEASE_SPREAD_RATE = Integer.parseInt(props.getProperty("disease_spread_rate"));
            DISEASE_DAMAGE_HARM = Integer.parseInt(props.getProperty("disease_damage_harm"));
            DISEASE_INFECTIVITY = Integer.parseInt(props.getProperty("disease_infectivity"));
            DISEASE_CURE_RATE = Integer.parseInt(props.getProperty("disease_cure_rate"));

            MUTATION_SUBTRACT = Integer.parseInt(props.getProperty("mutation_subtract"));
            MUTATION_AMOUNT = Integer.parseInt(props.getProperty("mutation_amount"));
            MUTATION_CHANCE = Integer.parseInt(props.getProperty("mutation_chance"));
            GREEN = Integer.parseInt(props.getProperty("green"));
            WORLD_IMAGE = props.getProperty("map");
            LOG = Boolean.parseBoolean(props.getProperty("log"));

        } catch (Exception e) {
            System.out.println("Error loading configuration file");
            e.printStackTrace();
            System.exit(2);
        }

    }

    public void save() throws IOException {
        FileOutputStream out = new FileOutputStream(CONFIG_FILE);
        props.store(out, null);
        loadProps();
        loadConfig();

    }
}
