public class Main {
    public static void main(String[] args) {
        try {
            Game game = new Game();
            game.Initialize();
            game.PrintField();
        }
        catch (Exception exc) {
            System.err.println(exc.getLocalizedMessage());
        }
    }
}
