import java.io.*;
import java.util.Properties;

class Config {
    String WORLD_IMAGE = "";
    int GREEN = 0;                     //color for land. FOR DEV: other green used: -11010225;
    int REPRODUCTION_THRESHOLD = 0;                //lower this is, the quicker players will reproduce
    int MAX_DAMAGE = 0;
    int STRONGER_WINS_CHANCE = 0;
    int MAX_POPULATION = 0;
    int MAX_AGE = 0;

    //mutation config (mutation changes damage on birth
    int MUTATION_CHANCE = 0;                                //%chance to mutate (1 = 0.1%)
    int MUTATION_AMOUNT = 0;                                //higher = higher mutation value range
    int MUTATION_SUBTRACT = 0;                               //half MUTATION_AMOUNT = mutations will be equally likely to be stronger/weaker. the lower the more likely to be stronger

    //disease config
    int DISEASE_MULTIPLIER = 0;                              //how many years of life does a diseased player lose per year they have this disease
    int DISEASE_SPREAD_RATE = 0;                             //% chance to spread to children (1=0.01%)
    int DISEASE_DAMAGE_HARM = 0;                             //how much damage do diseased peoples children lose?
    int DISEASE_CURE_RATE = 0;                              // % chance to be cured each tick (1=1%)
    int DISEASE_INFECTIVITY = 0;                    //% chance to spread disease on contact with tribe members
    boolean LOG = false;

    String CONFIG_FILE = "res\\config.conf";
    private Properties props;

    Config(){
        props = new Properties();
        loadProps();
    }
    void loadProps(){
        InputStream inputStream = null;
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
    Properties getProps() throws FileNotFoundException {
        return props;
    }

    void loadConfig() throws FileNotFoundException {
        try
        {
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
            GREEN=Integer.parseInt(props.getProperty("green"));
            WORLD_IMAGE = props.getProperty("map");
            LOG= Boolean.parseBoolean(props.getProperty("log"));

        }
        catch (Exception e)
        {
            System.out.println("Error loading configuration file");
            e.printStackTrace();
            System.exit(2);
        }

    }
    void save() throws IOException {
        FileOutputStream out = new FileOutputStream(CONFIG_FILE);
        props.store(out, null);
        loadProps();
        loadConfig();

    }
}
