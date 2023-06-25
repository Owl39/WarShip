package src;

import java.util.ArrayList;
import java.util.Random;

public class Game implements CallbackListener {
    private UI _ui;
    private Bot _bot = new Bot();
    private Bot _userBot = new Bot();
    private Player _userPlayer;
    private Player _botPlayer;

    private boolean _playerTurn;
    private ArrayList<GameItem> _userShips = new ArrayList<>();
    private ArrayList<GameItem> _userBombs = new ArrayList<>();
    private ArrayList<Shot> _userShots = new ArrayList<>();
    private ArrayList<GameItem> _botShips = new ArrayList<>();
    private ArrayList<GameItem> _botBombs = new ArrayList<>();
    private ArrayList<Shot> _botShots = new ArrayList<>();

    public Game(UI ui) {
        _ui = ui;
        _ui.SetCallbackListener(this);
    }

    public void Start() {
        Initialize();
        _userPlayer = new Player();
        _userPlayer.Me = _userBot;
        _userPlayer.Enemy = _bot;
        _userPlayer.Shots = _userShots;
        _userPlayer.EnemyShips = _botShips;
        _userPlayer.EnemyBombs = _botBombs;
        _userPlayer.Name = "User";

        _botPlayer = new Player();
        _botPlayer.Me = _bot;
        _botPlayer.Enemy = _userBot;
        _botPlayer.Shots = _botShots;
        _botPlayer.EnemyShips = _userShips;
        _botPlayer.EnemyBombs = _userBombs;
        _botPlayer.Name = "Computer";

        _playerTurn = true;
        Draw();
    }

    private boolean IsFinished() {
        boolean allKilled = true;
        for (int it = 0; it < _userShips.size() && allKilled; it++)
            if (!_userShips.get(it).IsKilled())
                allKilled = false;
        if (allKilled) {
            _ui.ShowMessage("Computer wins!");
            return true;
        }

        allKilled = true;
        for (int it = 0; it < _botShips.size() && allKilled; it++)
            if (!_botShips.get(it).IsKilled())
                allKilled = false;
        if (allKilled) {
            _ui.ShowMessage("User wins!");
            return true;
        }
        return false;
    }


    private void Draw() {
        _ui.DrawGame();

        CellState[][] cells = GetEmptyCells();
        FillItemsCells(cells, _userShips, false);
        FillItemsCells(cells, _userBombs, false);
        FillShotsCells(cells, _botShots);
        _ui.DrawField(cells, true);

        cells = GetEmptyCells();
        FillItemsCells(cells, _botShips, true);
        FillItemsCells(cells, _botBombs, true);
        FillShotsCells(cells, _userShots);
        _ui.DrawField(cells, false);
    }

    private void Initialize() {
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
    }

    private CellState[][] GetEmptyCells() {
        CellState[][] cells = new CellState[10][10];
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                cells[i][j] = CellState.Empty;
        return cells;
    }

    private void FillItemsCells(CellState[][] cells, ArrayList<GameItem> items, boolean hidden) {
        for (GameItem item : items) item.Print(cells, hidden);
    }

    private void FillShotsCells(CellState[][] cells, ArrayList<Shot> shots) {
        for (Shot shot : shots) shot.Print(cells);
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

            coord = new Coord(r.nextInt(10), r.nextInt(10));
            direction = Direction.values()[r.nextInt(4)];
            for (int i = 0; i < decks && !isOccupate; i++) {
                Coord nextCoord = switch (direction) {
                    case Left -> new Coord(coord.Row, coord.Column - i);
                    case Right -> new Coord(coord.Row, coord.Column + i);
                    case Top -> new Coord(coord.Row - i, coord.Column);
                    case Bottom -> new Coord(coord.Row + i, coord.Column);
                };
                if (nextCoord.Row < 0 ||
                        nextCoord.Row > 9 ||
                        nextCoord.Column < 0 ||
                        nextCoord.Column > 9) {
                    isOccupate = true;
                    continue;
                }
                for (GameItem item : items)
                    if (item.IsOccupate(nextCoord)) {
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
            coord = new Coord(r.nextInt(10), r.nextInt(10));
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

    private void PlayerTurn(Coord coord) {
        CellState[][] cells = GetEmptyCells();
        FillItemsCells(cells, _botShips, true);
        FillItemsCells(cells, _botBombs, true);
        FillShotsCells(cells, _userShots);

        if (IsFinished()) {
            return;
        }

        if (cells[coord.Row][coord.Column] != CellState.Empty)
            return;
        TurnResult turnResult = _userPlayer.Enemy.GetTurnResult(coord, _userPlayer.EnemyShips, _userPlayer.EnemyBombs);
        _userPlayer.Me.SetTurnResult(turnResult, coord, _userPlayer.EnemyShips, _userPlayer.Shots);
        if (turnResult == TurnResult.Bomb) {
            Coord hittedDesk = _userPlayer.Me.GetNextHittedDeck(_botPlayer.EnemyShips);
            _botPlayer.Me.SetTurnResult(TurnResult.Hit, hittedDesk, _botPlayer.EnemyShips, _botPlayer.Shots);
        }
        DrawField();
        if (turnResult == TurnResult.Hit || turnResult == TurnResult.Kill)
            return;

        _playerTurn = false;
        BotTurn();
    }

    private void BotTurn() {
        while (!IsFinished()) {
            CellState[][] cells = GetEmptyCells();
            FillItemsCells(cells, _userShips, true);
            FillItemsCells(cells, _userBombs, true);
            FillShotsCells(cells, _botShots);

            Coord coord;
            try {
                coord = _botPlayer.Me.MakeTurn(cells, _botPlayer.Shots);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            TurnResult turnResult = _botPlayer.Enemy.GetTurnResult(coord, _botPlayer.EnemyShips, _botPlayer.EnemyBombs);
            _botPlayer.Me.SetTurnResult(turnResult, coord, _botPlayer.EnemyShips, _botPlayer.Shots);

            if (turnResult == TurnResult.Bomb) {
                Coord hittedDesk = _botPlayer.Me.GetNextHittedDeck(_userPlayer.EnemyShips);
                _userPlayer.Me.SetTurnResult(TurnResult.Hit, hittedDesk, _userPlayer.EnemyShips, _userPlayer.Shots);
                DrawField();
                _playerTurn = true;
                return;
            }
            DrawField();
            if (turnResult != TurnResult.Hit && turnResult != TurnResult.Kill) {
                _playerTurn = true;
                return;
            }
        }
    }

    private void DrawField() {

        CellState[][] cells = GetEmptyCells();
        FillItemsCells(cells, _userShips, false);
        FillItemsCells(cells, _userBombs, false);
        FillShotsCells(cells, _botShots);
        _ui.DrawField(cells, true);

        cells = GetEmptyCells();
        FillItemsCells(cells, _botShips, true);
        FillItemsCells(cells, _botBombs, true);
        FillShotsCells(cells, _userShots);
        _ui.DrawField(cells, false);

        _ui.Repaint();
    }

    @Override
    public void onStep(Coord coord) {
        if (!_playerTurn)
            return;
        PlayerTurn(coord);
    }
}
