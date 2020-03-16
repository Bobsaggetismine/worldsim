import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class Statistics {

    public int unsafePeople;
    public int diedToDiease;
    public int diedToWar;
    public int diedToAging;

    public Map<String, Integer> populations;
    public Map<String, Integer> strengths;

    public Statistics(){
        populations = new HashMap<>();
        strengths = new HashMap<>();
    }

    public void resetStatistics(){
        populations.clear();
        strengths.clear();
        unsafePeople = 0;
    }
    public void fullReset(){
        populations.clear();
        strengths.clear();
        diedToAging = 0;
        diedToWar = 0;
        diedToDiease = 0;
    }
    public void addToPopulationStatistics(Cell p) {
        var LOG = true;
        if(!LOG) return;
        if(populations.get(p.tribe) == null) populations.put(p.tribe, 1);
        else populations.put(p.tribe, populations.get(p.tribe) + 1);
        if(strengths.get(p.tribe) == null) strengths.put(p.tribe, p.getDamage());
        else strengths.put(p.tribe, strengths.get(p.tribe) + p.getDamage());
    }
}
