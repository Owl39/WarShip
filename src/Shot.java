public class Shot {
    private Coord _coord;
    public Shot(Coord coord)
    {
        _coord = coord;
    }

    public void Print (CellState[][] cells) {
        cells[_coord.Row][_coord.Column] = CellState.Water;
    }

    public Coord GetCoord()
    {
        return _coord;
    }
}
