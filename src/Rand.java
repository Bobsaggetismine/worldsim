import java.util.Random;

public class Rand {

    private static Random random = new Random();
    
    public static int randomBias(){
        return random.nextInt(1 - -1 + 1) + -1;
    }
}
