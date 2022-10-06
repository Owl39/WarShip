import java.lang.reflect.Field;

public class Game {
    private CellState[][] _enemyField = new CellState[10][10];
    private CellState[][] _ownField = new CellState[10][10];
    private Bot _bot = new Bot();

    public Game()
    {
    }

    public void Initialize()
    {
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++) {
                _ownField[i][j] = CellState.Empty;
                _enemyField[i][j] = CellState.Empty;
            }
        //_bot.FillField(_ownField);
        _bot.SetTurnResult(_ownField,new Coord(5,5),TurnResult.Kill);
        _bot.SetTurnResult(_ownField,new Coord(0,0),TurnResult.Bomb);
        _bot.SetTurnResult(_ownField,new Coord(5,9),TurnResult.Bomb);
        _bot.SetTurnResult(_ownField,new Coord(0,9),TurnResult.Bomb);
    }

    public void PrintField()
    {
        System.out.print("\n░░-Water\n¤¤-Bomb\n[]-Ship\n██-KilledShip\nxx-KilledBomb\n\n");
        System.out.print("  ");
        for (int i = 1; i <= 10; i++)
            System.out.print(Integer.toString(i)+" ");
        System.out.print('\n');

        System.out.print(" ");
        System.out.print('┌');
        for (int i = 1; i <= 10; i++)
            System.out.print("──");
        System.out.print('┐');

        System.out.print('\n');

        for (int i = 0; i < 10; i++) {
            System.out.print((char) ('A' + i));

            System.out.print('│');
            for (int j = 0; j < 10; j++) {
/*                if(Field[i][j] == CellState.Bomb ||
                        Field[i][j] == CellState.Ship)// hide ships
                    System.out.print(GetCellString(CellState.Water);
                else*/
                    System.out.print(GetCellString(_ownField[i][j]));
            }
            System.out.print('│');
            System.out.print('\n');
        }

        System.out.print(" ");
        System.out.print('└');
        for (int i = 1; i <= 10; i++)
            System.out.print("──");
        System.out.print('┘');
    }

    private String GetCellString(CellState cellState)
    {
        switch (cellState)
        {
            case Water: return "░░";
            case Bomb: return "¤¤";
            case Ship: return "[]";
            case HittedShip: return "██";
            case KilledBomb: return "хх";
            default: return "  ";
        }
    }
}
