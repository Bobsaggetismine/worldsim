package core;

import java.util.concurrent.atomic.AtomicInteger;


//this is threadsafe as long as "Statistics" is, which it is.
public class Log implements Runnable {

    private final Statistics _statistics;
    boolean running = false;

    public Log(Statistics stat) {
        _statistics = stat;
    }

    public void stop() {
        running = false;
    }

    public void run() {
        running = true;
        while (running) {
            log();
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public void log() {

        AtomicInteger worldPop = new AtomicInteger(0);
        System.out.println("------------------POPULATION------------------\n");
        _statistics.getPopulations().forEach((s, i) -> {
            System.out.printf("%-30s %15d\n", s + " pop: ", i);
            worldPop.addAndGet(i);
        });
        System.out.printf("%-30s %15d\n", "World pop: ", worldPop.get());
        System.out.println("\n-----------------STRENGTH---------------------\n");
        _statistics.getStrengths().forEach((s, i) -> {
            Integer pop = _statistics.getPopulations().get(s);
            float averagestr = i.floatValue() / pop.floatValue();
            System.out.printf("%-30s %15.2f\n", s + " avg str: ", averagestr);
        });

        System.out.println("\n----------------DISEASE/DEATH-----------------");
        System.out.println();
        System.out.printf("%-30s %15d\n", "People with no disease: ", worldPop.get() - _statistics.unsafe().get());
        System.out.printf("%-30s %15d\n", "People with disease: ", _statistics.unsafe().get());
        System.out.println();
        System.out.printf("%-30s %15d\n", "People who died from disease: ", _statistics.diedDisease().get());
        System.out.printf("%-30s %15d\n", "People who died from aging: ", _statistics.diedAging().get());
        System.out.printf("%-30s %15d\n", "People who died from war: ", _statistics.diedWar().get());
    }
}
