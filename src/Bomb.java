package src;

public class Bomb extends GameItem {
    public Bomb(Coord coord) {
        super(coord);
    }

    @Override
    public void Print(CellState[][] cells, boolean hidden) {
        if (IsKilled()) {
            for (int c = 0; c < Coords().length; c++) {
                Coord coord = Coords()[c];
                for (int i = coord.Row - 1; i <= coord.Row + 1; i++) {
                    for (int j = coord.Column - 1; j <= coord.Column + 1; j++) {
                        if (i < 0 || i > 9 || j < 0 || j > 9)
                            continue;
                        cells[i][j] = CellState.Water;
                    }
                }
            }
        }

        for (int i = 0; i < Coords().length; i++)
            if (Hitted()[i])
                cells[Coords()[i].Row][Coords()[i].Column] = CellState.KilledBomb;
            else if (!hidden)
                cells[Coords()[i].Row][Coords()[i].Column] = CellState.Bomb;
    }
}
