abstract class GameItem {
    private Coord[] _coords;
    private boolean[] _hitted;

    public GameItem(Coord coord, Direction direction, int decks) {
        _coords = new Coord[decks];
        _hitted = new boolean[decks];

        for (int i = 0; i < decks; i++) {
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
            _coords[i] = nextCoord;
            _hitted[i] = false;
        }
    }

    public GameItem(Coord coord) {
        _coords = new Coord[1];
        _hitted = new boolean[1];

        _coords[0] = coord;
        _hitted[0] = false;
    }

    abstract public void Print (CellState[][] cells, boolean hidden);

    public boolean Hit (Coord coord)
    {
        for(int i=0; i < _coords.length; i++) {
            if (_coords[i].IsEqual(coord)) {
                _hitted[i] = true;
                return true;
            }
        }
        return false;
    }

    public boolean IsOccupate (Coord coord)
    {
        for(int i=0; i < _coords.length; i++) {
            if (_coords[i].IsEqual(coord) ||
                    coord.IsEqual(_coords[i].Row - 1, _coords[i].Column - 1) ||
                    coord.IsEqual(_coords[i].Row - 1, _coords[i].Column) ||
                    coord.IsEqual(_coords[i].Row - 1, _coords[i].Column + 1) ||
                    coord.IsEqual(_coords[i].Row, _coords[i].Column - 1) ||
                    coord.IsEqual(_coords[i].Row, _coords[i].Column + 1) ||
                    coord.IsEqual(_coords[i].Row + 1, _coords[i].Column - 1) ||
                    coord.IsEqual(_coords[i].Row + 1, _coords[i].Column) ||
                    coord.IsEqual(_coords[i].Row + 1, _coords[i].Column + 1))
                return true;
        }
        return false;
    }

    protected Coord[] Coords()
    {
        return _coords;
    }

    protected boolean[] Hitted()
    {
        return _hitted;
    }

    public boolean IsKilled()
    {
        for(int i = 0; i < _hitted.length; i++)
            if(!_hitted[i])
                return false;
        return true;
    }

    public Coord[] GetHittedDecks() {
        int count = 0;
        for (int i = 0; i < _hitted.length; i++)
            if (_hitted[i])
                count++;

        Coord[] coords = new Coord[count];
        count = 0;
        for (int i = 0; i < _hitted.length; i++)
            if (_hitted[i])
                coords[count++] = _coords[i];
        return coords;
    }
}


