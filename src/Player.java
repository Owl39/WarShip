import java.util.ArrayList;

public class Player {
    public Bot Me;
    public Bot Enemy;
    public String Name;
    public ArrayList<GameItem> EnemyShips;
    public ArrayList<GameItem> EnemyBombs;
    public ArrayList<Shot> Shots;

    private ArrayList<GameItem> _enemyItems = null;

    public Iterable<GameItem> GetEnemyItems()
    {
        ArrayList<GameItem> items = new ArrayList<GameItem>();
        items.addAll(EnemyShips);
        items.addAll(EnemyBombs);
        return items;
    }
}
