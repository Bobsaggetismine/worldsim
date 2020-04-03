package core;

import java.util.Random;

public class Rand {

    private static final Random random = new Random();

    public static int randomBias() {
        return random.nextInt(1 - -1 + 1) + -1;
    }

    public static int randomInt(int bound) {
        return random.nextInt(bound);
    }
}
