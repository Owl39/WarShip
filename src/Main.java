public class Main {
    public static void main(String[] args) {
        try {
            AbstractFabric fabric = new ConsoleFabric();
            Game game = new Game(fabric);
            game.Start();
        }
        catch (Exception exc) {
            System.err.println(exc.getLocalizedMessage());
        }
    }
}
