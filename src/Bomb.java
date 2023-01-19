public class Bomb  extends GameItem {
    public Bomb (Coord coord)
    {
        super(coord);
    }


    @Override public void Print (CellState[][] cells, boolean hidden) {
        for (int i = 0; i < Coords().length; i++)
            if (Hitted()[i])
                cells[Coords()[i].Row][Coords()[i].Column] = CellState.KilledBomb;
            else if (!hidden)
                cells[Coords()[i].Row][Coords()[i].Column] = CellState.Bomb;
    }
}
