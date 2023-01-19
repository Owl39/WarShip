import java.util.ArrayList;

public class Player {
    public Bot Me;
    public Bot Enemy;

    public ArrayList<GameItem> Ships;
    public ArrayList<GameItem> Bombs;
    public ArrayList<GameItem> EnemyShips;
    public ArrayList<GameItem> EnemyBombs;
    public ArrayList<Shot> Shots;

    private ArrayList<GameItem> _enemyItems = null;

    public Iterable<GameItem> GetMyItems()
    {
        ArrayList<GameItem> items = new ArrayList<GameItem>();
        items.addAll(Ships);
        items.addAll(Bombs);
        return items;
    }
}
