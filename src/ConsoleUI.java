import java.io.IOException;
import java.util.Random;

public class ConsoleUI extends UI{
    public void DrawGame()
    {
        ClearScreen();

        System.out.print("\n░░-Water\n¤¤-Bomb\n[]-Ship\n██-KilledShip\nxx-KilledBomb\n\n");
    }

    private void ClearScreen()
    {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public Coord GetCoord() {
        Random r = new Random();

        char c = 0;
        do {
            try {
                c = (char) System.in.read();
            } catch (IOException exc) {
            }
        }
        while(c != ' ');
        return new Coord(r.nextInt(9), r.nextInt(9));
    }

    public void DrawField(CellState[][] cells)
    {
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
                DrawCell(cells[i][j]);
/*                if(Field[i][j] == CellState.Bomb ||
                        Field[i][j] == CellState.Ship)// hide ships
                    System.out.print(GetCellString(CellState.Water);
                else*/
                //            System.out.print(GetCellString(_ownField[i][j]));
            }
            System.out.print('│');
            System.out.print('\n');
        }

        System.out.print(" ");
        System.out.print('└');
        for (int i = 1; i <= 10; i++)
            System.out.print("──");
        System.out.print('┘');
        System.out.print('\n');
    }

    private void DrawCell(CellState cellState)
    {
        String cellString = "  ";
        switch (cellState) {
            case Water:
                cellString = "░░";
                break;
            case Bomb:
                cellString = "¤¤";
                break;
            case Ship:
                cellString = "[]";
                break;
            case HittedShip:
                cellString = "██";
                break;
            case KilledBomb:
                cellString = "хх";
                break;
        }
        System.out.print(cellString);
    }
}
