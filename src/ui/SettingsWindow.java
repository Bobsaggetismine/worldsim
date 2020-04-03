package ui;

import core.Config;
import core.Launcher;

import javax.swing.*;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class SettingsWindow extends JFrame {

    private JPanel panel;

    private JTextField t_maxDamage, t_strongerWins,t_maxPopulation,t_reproductionThreashhold, t_maxAge;
    private JTextField t_diseaseMultiplier,t_diseaseSpreadRate, t_diseaseHarm, t_diseaseInfectivity, t_diseaseCureRate;
    private JTextField t_mutationChance,t_mutationAmount, t_mutationSubtract;
    private JTextField t_configFile;

    private final AtomicBoolean canReset;

    private final Config gameConfig;
    public SettingsWindow(Config gameConfig){
        this.gameConfig = gameConfig;
        canReset= new AtomicBoolean(true);
        init();
    }

    public synchronized void toggleReset(){
        canReset.set(!canReset.get());
    }


    private void init(){
        ImageIcon img = new ImageIcon("res/ico.png");

        setIconImage(img.getImage());
        this.setLayout(null);
        panel = new JPanel();
        this.setContentPane(panel);
        panel.setLayout(null);
        initLabels();
        initTextBoxes();
        initButtons();
        this.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        this.setResizable(false);
        this.setSize(300,390);
        this.setVisible(true);
    }
    private void saveProperties(){
        Properties props = gameConfig.getProps();
        props.setProperty("max_damage",t_maxDamage.getText());
        props.setProperty("stronger_wins",t_strongerWins.getText());
        props.setProperty("max_population",t_maxPopulation.getText());
        props.setProperty("reproduction_threshold",t_reproductionThreashhold.getText());
        props.setProperty("max_age",t_maxAge.getText());
        props.setProperty("disease_multiplier",t_diseaseMultiplier.getText());
        props.setProperty("disease_spread_rate",t_diseaseSpreadRate.getText());
        props.setProperty("disease_damage_harm",t_diseaseHarm.getText());
        props.setProperty("disease_infectivity",t_diseaseInfectivity.getText());
        props.setProperty("disease_cure_rate",t_diseaseCureRate.getText());
        props.setProperty("mutation_chance",t_mutationChance.getText());
        props.setProperty("mutation_amount",t_mutationAmount.getText());
        props.setProperty("mutation_subtract",t_mutationSubtract.getText());
        try {
            gameConfig.save();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private void initButtons(){
        JButton b_saveAndRestart = new JButton("Save and restart");
        b_saveAndRestart.setSize(150,20);
        b_saveAndRestart.setLocation(3,300);
        panel.add(b_saveAndRestart);

        JButton b_save = new JButton("save");
        b_save.setSize(140,20);
        b_save.setLocation(143,300);
        panel.add(b_save);

        JButton b_loadConfig = new JButton("load config");
        b_loadConfig.setSize(140,20);
        b_loadConfig.setLocation(3,320);
        panel.add(b_loadConfig);


        b_save.addActionListener(e -> saveProperties());
        b_saveAndRestart.addActionListener(e ->{
            saveProperties();
            Launcher.start_new_game();
        });
        b_loadConfig.addActionListener(e ->{
            try{
                gameConfig.CONFIG_FILE = t_configFile.getText();
                gameConfig.loadProps();
                gameConfig.loadConfig();
                init();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        });
    }
    private void initTextBoxes(){
        t_maxDamage = new JTextField();
        t_strongerWins = new JTextField();
        t_maxPopulation = new JTextField();
        t_reproductionThreashhold = new JTextField();
        t_maxAge = new JTextField();
        t_diseaseMultiplier = new JTextField();
        t_diseaseSpreadRate = new JTextField();
        t_diseaseHarm = new JTextField();
        t_diseaseInfectivity = new JTextField();
        t_diseaseCureRate = new JTextField();

        t_mutationChance = new JTextField();
        t_mutationAmount = new JTextField();
        t_mutationSubtract = new JTextField();

        t_configFile = new JTextField();

        t_maxDamage.setLocation(184,20);
        t_strongerWins.setLocation(184,40);
        t_maxPopulation.setLocation(184,60);
        t_reproductionThreashhold.setLocation(184,80);
        t_maxAge.setLocation(184,100);
        t_diseaseMultiplier.setLocation(184,120);
        t_diseaseSpreadRate.setLocation(184,140);
        t_diseaseHarm.setLocation(184,160);
        t_diseaseInfectivity.setLocation(184,180);
        t_diseaseCureRate.setLocation(184,200);
        t_mutationChance.setLocation(184,220);
        t_mutationAmount.setLocation(184,240);
        t_mutationSubtract.setLocation(184,260);

        t_configFile.setLocation(184, 280);

        t_maxDamage.setSize(100,20);
        t_strongerWins.setSize(100,20);
        t_maxPopulation.setSize(100,20);
        t_reproductionThreashhold.setSize(100,20);
        t_maxAge.setSize(100,20);
        t_diseaseMultiplier.setSize(100,20);
        t_diseaseSpreadRate.setSize(100,20);
        t_diseaseHarm.setSize(100,20);
        t_diseaseInfectivity.setSize(100,20);
        t_diseaseCureRate.setSize(100,20);
        t_mutationChance.setSize(100,20);
        t_mutationAmount.setSize(100,20);
        t_mutationSubtract.setSize(100,20);

        t_configFile.setSize(100,20);

        t_maxDamage.setText( gameConfig.MAX_DAMAGE+"");
        t_strongerWins.setText( gameConfig.STRONGER_WINS_CHANCE + "");
        t_maxPopulation.setText( gameConfig.MAX_POPULATION+"");
        t_reproductionThreashhold.setText( gameConfig.REPRODUCTION_THRESHOLD+"");
        t_maxAge.setText( gameConfig.MAX_AGE+"");
        t_diseaseMultiplier.setText( gameConfig.DISEASE_MULTIPLIER + "" );
        t_diseaseSpreadRate.setText( gameConfig.DISEASE_SPREAD_RATE + "" );
        t_diseaseHarm.setText( gameConfig.DISEASE_DAMAGE_HARM + "" );
        t_diseaseInfectivity.setText( gameConfig.DISEASE_INFECTIVITY + "" );
        t_diseaseCureRate.setText( gameConfig.DISEASE_CURE_RATE + "" );
        t_mutationChance.setText( gameConfig.MUTATION_CHANCE + "");
        t_mutationAmount.setText( gameConfig.MUTATION_AMOUNT + "" );
        t_mutationSubtract.setText( gameConfig.MUTATION_SUBTRACT + "");

        t_configFile.setText(gameConfig.CONFIG_FILE);

        panel.add(t_maxDamage);
        panel.add(t_strongerWins);
        panel.add(t_maxPopulation);
        panel.add(t_reproductionThreashhold);
        panel.add(t_maxAge);
        panel.add(t_diseaseMultiplier);
        panel.add(t_diseaseSpreadRate);
        panel.add(t_diseaseHarm);
        panel.add(t_diseaseInfectivity);
        panel.add(t_diseaseCureRate);
        panel.add(t_mutationChance);
        panel.add(t_mutationAmount);
        panel.add(t_mutationSubtract);
        panel.add(t_configFile);
    }
    private void initLabels(){
        JLabel l_maxDamage = new JLabel("Max Damage");
        JLabel l_strongerWins = new JLabel("Stronger Wins");
        JLabel l_maxPopulation = new JLabel("Max population");
        JLabel l_reproductionThreashhold = new JLabel("Reproduction Threashold");
        JLabel l_maxAge = new JLabel("Max age");
        JLabel l_diseaseMultiplier = new JLabel("Disease Multiplier");
        JLabel l_diseaseSpreadRate = new JLabel("Disease Spread Rate");
        JLabel l_diseaseHarm = new JLabel("Disease Harm");
        JLabel l_diseaseInfectivity = new JLabel("Disease Infectivity");
        JLabel l_diseaseCureRate = new JLabel("Disease Cure Rate");
        JLabel l_configFile = new JLabel("Config File");
        JLabel l_mutationChance = new JLabel("Mutation Chance");
        JLabel l_mutationAmount = new JLabel("Mutation Amount");
        JLabel l_mutationSubtract = new JLabel("Mutation Subtract");

        l_maxDamage.setLocation(3,20);
        l_strongerWins.setLocation(3,40);
        l_maxPopulation.setLocation(3,60);
        l_reproductionThreashhold.setLocation(3,80);
        l_maxAge.setLocation(3,100);
        l_diseaseMultiplier.setLocation(3,120);
        l_diseaseSpreadRate.setLocation(3,140);
        l_diseaseHarm.setLocation(3,160);
        l_diseaseInfectivity.setLocation(3,180);
        l_diseaseCureRate.setLocation(3,200);
        l_mutationChance.setLocation(3,220);
        l_mutationAmount.setLocation(3,240);
        l_mutationSubtract.setLocation(3,260);
        l_configFile.setLocation(3,280);
        l_maxDamage.setSize(200,20);
        l_strongerWins.setSize(200,20);
        l_maxPopulation.setSize(200,20);
        l_reproductionThreashhold.setSize(200,20);
        l_maxAge.setSize(200,20);
        l_diseaseMultiplier.setSize(200,20);
        l_diseaseSpreadRate.setSize(200,20);
        l_diseaseHarm.setSize(200,20);
        l_diseaseInfectivity.setSize(200,20);
        l_diseaseCureRate.setSize(200,20);
        l_mutationChance.setSize(200,20);
        l_mutationAmount.setSize(200,20);
        l_mutationSubtract.setSize(200,20);
        l_configFile.setSize(200,20);
        panel.add(l_maxDamage);
        panel.add(l_strongerWins);
        panel.add(l_maxPopulation);
        panel.add(l_reproductionThreashhold);
        panel.add(l_maxAge);
        panel.add(l_diseaseMultiplier);
        panel.add(l_diseaseSpreadRate);
        panel.add(l_diseaseHarm);
        panel.add(l_diseaseInfectivity);
        panel.add(l_diseaseCureRate);
        panel.add(l_mutationChance);
        panel.add(l_mutationAmount);
        panel.add(l_mutationSubtract);
        panel.add(l_configFile);
    }

}
