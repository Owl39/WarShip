package src;

import java.util.Random;

abstract class GameItem {
    private Coord[] _coords;
    private boolean[] _hitted;
    private int _decks;

    public GameItem(Coord coord, Direction direction, int decks) {
        _decks = decks;
        _coords = new Coord[decks];
        _hitted = new boolean[decks];

        for (int i = 0; i < decks; i++) {
            Coord nextCoord = switch (direction) {
                case Left -> new Coord(coord.Row, coord.Column - i);
                case Right -> new Coord(coord.Row, coord.Column + i);
                case Top -> new Coord(coord.Row - i, coord.Column);
                case Bottom -> new Coord(coord.Row + i, coord.Column);
            };
            _coords[i] = nextCoord;
            _hitted[i] = false;
        }
    }

    public GameItem(Coord coord) {
        _decks = 1;
        _coords = new Coord[1];
        _hitted = new boolean[1];

        _coords[0] = coord;
    }

    abstract public void Print(CellState[][] cells, boolean hidden);

    public boolean Hit(Coord coord) {
        for (int i = 0; i < _decks; i++) {
            if (_coords[i].IsEqual(coord)) {
                _hitted[i] = true;
                return true;
            }
        }
        return false;
    }

    public boolean IsOccupate(Coord coord) {
        for (int i = 0; i < _decks; i++) {
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

    protected Coord[] Coords() {
        return _coords;
    }

    protected boolean[] Hitted() {
        return _hitted;
    }

    public boolean IsKilled() {
        for (int i = 0; i < _decks; i++)
            if (!_hitted[i])
                return false;
        return true;
    }

    public boolean IsHitted() {
        for (int i = 0; i < _decks; i++)
            if (_hitted[i])
                return true;
        return false;
    }

    public Coord[] GetHittedDecks() {
        int count = 0;
        for (int i = 0; i < _decks; i++)
            if (_hitted[i])
                count++;

        Coord[] coords = new Coord[count];
        count = 0;
        for (int i = 0; i < _decks; i++)
            if (_hitted[i])
                coords[count++] = _coords[i];
        return coords;
    }

    public Coord GetNextHittedDeck() {
        Coord coord = _coords[0];
        if (!IsHitted()) {
            Random r = new Random();
            coord = _coords[r.nextInt(_decks)];
        } else {
            for (int i = 0; i < _decks; i++)
                if (_hitted[i] != _hitted[i + 1]) {
                    if (_hitted[i])
                        coord = _coords[i + 1];
                    else
                        coord = _coords[i];
                    break;
                }
        }
        Hit(coord);
        return coord;
    }
}


