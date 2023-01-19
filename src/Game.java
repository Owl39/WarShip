import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

public class Game {
    private UI _ui;
    private Bot _bot = new Bot();
    private Bot _userBot = new Bot();

    private ArrayList<GameItem> _userShips = new ArrayList<GameItem>();
    private ArrayList<GameItem> _userBombs = new ArrayList<GameItem>();
    private ArrayList<Shot> _userShots = new ArrayList<Shot>();
    private ArrayList<GameItem> _botShips = new ArrayList<GameItem>();
    private ArrayList<GameItem> _botBombs = new ArrayList<GameItem>();
    private ArrayList<Shot> _botShots = new ArrayList<Shot>();

    public Game(AbstractFabric fabric)
    {
        _ui = fabric.GetUI();
    }

    public void Start()
            throws Exception {
        Initialize();
        Player user = new Player();
        user.Me = _userBot;
        user.Enemy = _bot;
        user.Shots = _userShots;
        user.Ships = _userShips;
        user.Bombs = _userBombs;
        user.EnemyShips = _botShips;
        user.EnemyBombs = _botBombs;

        Player bot = new Player();
        bot.Me = _bot;
        bot.Enemy = _userBot;
        bot.Shots = _botShots;
        bot.Ships = _botShips;
        bot.Bombs = _botBombs;
        bot.EnemyShips = _userShips;
        bot.EnemyBombs = _userBombs;

        Player current = user;
        Player second = bot;

        while (!IsFinished()) {
            CellState[][] cells = GetEmptyCells();
            FillItemsCells(cells, current.EnemyShips, true);
            FillItemsCells(cells, current.EnemyBombs, true);

            Coord coord = current.Me.MakeTurn(cells, current.Shots);
            TurnResult result = current.Enemy.GetTurnResult(coord, current.EnemyShips, current.EnemyBombs);
            current.Me.SetTurnResult(result, coord, current.EnemyShips, current.Shots);
            Draw();
            if(result != TurnResult.Hit && result != TurnResult.Kill) {
                Player temp = current;
                current = second;
                second = temp;
            }

            _ui.GetCoord();
        }
    }

    private boolean IsFinished() {
        boolean allKilled = true;
        for (int it = 0; it < _userShips.size() && allKilled; it++)
            if (!_userShips.get(it).IsKilled())
                allKilled = false;
        if (allKilled) {
            //_ui.PrintGameOver("Computer");
            return true;
        }

        allKilled = true;
        for (int it = 0; it < _botShips.size() && allKilled; it++)
            if (!_botShips.get(it).IsKilled())
                allKilled = false;
        if (allKilled) {
            //_ui.PrintGameOver("User");
            return true;
        }
        return false;
    }

    private void Draw()
    {
        _ui.DrawGame();

        CellState[][] cells = GetEmptyCells();
        FillItemsCells(cells, _userShips, false);
        FillItemsCells(cells, _userBombs, false);
        FillShotsCells(cells, _userShots);
        _ui.DrawField(cells);

        cells = GetEmptyCells();
        FillItemsCells(cells, _botShips, true);
        FillItemsCells(cells, _botBombs, true);
        FillShotsCells(cells, _botShots);
        _ui.DrawField(cells);
    }

    private void Initialize()
    {
        _userShips.clear();
        _userBombs.clear();
        _userShots.clear();
        _botShips.clear();
        _botBombs.clear();
        _botShots.clear();

        CreateShips(_userShips);
        CreateBombs(_userShips, _userBombs);
        CreateShips(_botShips);
        CreateBombs(_botShips, _botBombs);
        //_bot.SetTurnResult(_ownField,new Coord(5,5),TurnResult.Kill);
        //_bot.SetTurnResult(_ownField,new Coord(0,0),TurnResult.Bomb);
        //_bot.SetTurnResult(_ownField,new Coord(5,9),TurnResult.Bomb);
        //_bot.SetTurnResult(_ownField,new Coord(0,9),TurnResult.Bomb);
    }

    private CellState[][] GetEmptyCells()
    {
        CellState[][] cells = new CellState[10][10];
        for (int i=0;i < 10; i++)
            for (int j=0;j < 10; j++)
                cells[i][j] = CellState.Empty;
        return cells;
    }
    private void FillItemsCells(CellState[][] cells, ArrayList<GameItem> items, boolean hidden)
    {
        for(int it = 0; it < items.size(); it++)
            items.get(it).Print(cells, hidden);
    }
    private void FillShotsCells(CellState[][] cells, ArrayList<Shot> shots)
    {
        for(int it = 0; it < shots.size(); it++)
            shots.get(it).Print(cells);
    }

    public void CreateShips(ArrayList<GameItem> items) {
        for (int i = 4; i >= 1; i--) {
            for (int j = 1; j <= 5 - i; j++) {
                CreateShip(items, i);
            }
        }
    }

    public void CreateBombs(ArrayList<GameItem> ships, ArrayList<GameItem> bombs) {
        for (int i = 0; i < 3; i++)
            CreateBomb(ships, bombs);
    }

    private void CreateShip(ArrayList<GameItem> items, int decks) {
        Random r = new Random();
        boolean isOccupate = true;
        Coord coord = null;
        Direction direction = null;
        while (isOccupate) {
            isOccupate = false;

            coord = new Coord(r.nextInt(9), r.nextInt(9));
            direction = Direction.values()[r.nextInt(1, 4)];
            for (int i = 0; i < decks && !isOccupate; i++) {
                Coord nextCoord = null;
                switch (direction) {
                    case Left:
                        nextCoord = new Coord(coord.Row, coord.Column - i);
                        break;
                    case Right:
                        nextCoord = new Coord(coord.Row, coord.Column + i);
                        break;
                    case Top:
                        nextCoord = new Coord(coord.Row - i, coord.Column);
                        break;
                    case Bottom:
                        nextCoord = new Coord(coord.Row + i, coord.Column);
                        break;
                }
                if (nextCoord.Row < 0 ||
                        nextCoord.Row > 9 ||
                        nextCoord.Column < 0 ||
                        nextCoord.Column > 9) {
                    isOccupate = true;
                    continue;
                }
                for (int it = 0; it < items.size(); it++)
                    if (items.get(it).IsOccupate(nextCoord)) {
                        isOccupate = true;
                        break;
                    }
            }
        }
        items.add(new Ship(coord, direction, decks));
    }

    private void CreateBomb(ArrayList<GameItem> ships, ArrayList<GameItem> bombs) {
        Random r = new Random();
        boolean isOccupate = true;
        Coord coord = null;
        while (isOccupate) {
            isOccupate = false;
            coord = new Coord(r.nextInt(9), r.nextInt(9));
            for (int it = 0; it < ships.size() && !isOccupate; it++) {
                if (ships.get(it).IsOccupate(coord))
                    isOccupate = true;
            }
            for (int it = 0; it < bombs.size() && !isOccupate; it++) {
                if (bombs.get(it).IsOccupate(coord))
                    isOccupate = true;
            }
        }
        bombs.add(new Bomb(coord));
    }
}
