package src;

public class Ship extends GameItem {
    public Ship(Coord coord, Direction direction, int decks) {
        super(coord, direction, decks);
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

        for (int c = 0; c < Coords().length; c++) {
            Coord coord = Coords()[c];
            if (Hitted()[c])
                cells[coord.Row][coord.Column] = CellState.HittedShip;
            else if (!hidden)
                cells[coord.Row][coord.Column] = CellState.Ship;
        }
    }

}
