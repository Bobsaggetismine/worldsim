package core;

import cell.Cell;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Statistics {

    private final AtomicInteger unsafePeople;
    private final AtomicInteger diedToDiease;
    private final AtomicInteger diedToWar;
    private final AtomicInteger diedToAging;

    private final AtomicInteger c_unsafePeople;


    private final ConcurrentMap<String, Integer> populations;
    private final ConcurrentMap<String, Integer> strengths;

    private final ConcurrentMap<String, Integer> c_strengths;
    private final ConcurrentMap<String, Integer> c_populations;

    public Statistics() {
        populations = new ConcurrentHashMap<>();
        strengths = new ConcurrentHashMap<>();
        c_strengths = new ConcurrentHashMap<>();
        c_populations = new ConcurrentHashMap<>();
        diedToAging = new AtomicInteger(0);
        diedToWar = new AtomicInteger(0);
        diedToDiease = new AtomicInteger(0);
        unsafePeople = new AtomicInteger(0);
        c_unsafePeople = new AtomicInteger(0);
    }

    public void resetStatistics() {
        //keeping the last version of the statistics is a good enough way to get this working (update dependant statistics but a timer based async IO system), copying the data once every update will ensure we have a full Map when we print (unless the print happens between the time we clear and the add, which could be locked, but the IO is not important enough to take that performance hit)
        c_populations.clear();
        c_strengths.clear();
        c_unsafePeople.set(0);

        c_populations.putAll(populations);
        c_strengths.putAll(strengths);
        c_unsafePeople.set(unsafePeople.get());

        populations.clear();
        strengths.clear();
        unsafePeople.set(0);
    }

    public void fullReset() {
        populations.clear();
        strengths.clear();
        diedToAging.set(0);
        diedToWar.set(0);
        diedToDiease.set(0);
    }

    public void addToPopulationStatistics(Cell p) {
        if (populations.get(p.tribe()) == null) populations.put(p.tribe(), 1);
        else populations.put(p.tribe(), populations.get(p.tribe()) + 1);
        if (strengths.get(p.tribe()) == null) strengths.put(p.tribe(), p.damage());
        else strengths.put(p.tribe(), strengths.get(p.tribe()) + p.damage());
        if (p.diseased()) {
            unsafePeople.incrementAndGet();
        }
    }

    public Map<String, Integer> getPopulations() {
        return c_populations;
    }

    public Map<String, Integer> getStrengths() {
        return c_strengths;
    }

    public AtomicInteger unsafe() {
        return c_unsafePeople;
    }

    public AtomicInteger diedDisease() {
        return diedToDiease;
    }

    public AtomicInteger diedWar() {
        return diedToWar;
    }

    public AtomicInteger diedAging() {
        return diedToAging;
    }

    public void incDiedToAging() {
        diedToAging.incrementAndGet();
    }

    public void incDiedToDisease() {
        diedToDiease.incrementAndGet();
    }

    public void incDiedToWar() {
        diedToWar.incrementAndGet();
    }
}
