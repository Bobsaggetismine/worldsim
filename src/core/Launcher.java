package core;

public class Launcher {
    private static Game game;

    public static void start_new_game() {
        if (game == null) {
            game = new Game();
            game.run();
        } else {
            game.set_reset();
        }
    }

    public static void main(String[] args) {
        start_new_game();
    }
}
