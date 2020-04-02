package core;

import java.util.concurrent.atomic.AtomicInteger;

public class Log {
    public static int log(Statistics gameStatistics, int frames) {
        //disabling logging (or making it more rare) will markedly improve performance as console IO is expensive
            if (frames == 60) {
                frames = 0;

                AtomicInteger worldPop = new AtomicInteger(0);
                int x = 3;
                System.out.println("------------------POPULATION------------------\n");
                gameStatistics.populations.forEach((s, i) -> {
                    System.out.printf("%-30s %15d\n", s + " pop: ", i);
                    worldPop.addAndGet(i);
                });
                System.out.printf("%-30s %15d\n", "World pop: ", worldPop.get());
                System.out.println("\n-----------------STRENGTH---------------------\n");
                gameStatistics.strengths.forEach((s, i) -> {
                    Integer pop = gameStatistics.populations.get(s);
                    float averagestr = i.floatValue() / pop.floatValue();
                    System.out.printf("%-30s %15.2f\n", s + " avg str: ", averagestr);
                });

                System.out.println("\n----------------DISEASE/DEATH-----------------");
                System.out.println();
                System.out.printf("%-30s %15d\n", "People with no disease: ", worldPop.get() - gameStatistics.unsafePeople);
                System.out.printf("%-30s %15d\n", "People with disease: ", gameStatistics.unsafePeople);
                System.out.println();
                System.out.printf("%-30s %15d\n", "People who died from disease: ", gameStatistics.diedToDiease);
                System.out.printf("%-30s %15d\n", "People who died from aging: ", gameStatistics.diedToAging);
                System.out.printf("%-30s %15d\n", "People who died from war: ", gameStatistics.diedToWar);
            }
            return frames;
        }
}
