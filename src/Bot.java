import java.util.ArrayList;
import java.util.Random;

public class Bot {
    private boolean _killInProgress=false;
    private Coord[] _hittedShipCords = new Coord[4];
    private int _hittedDeskCount = 0;
    private boolean IsCellValid(CellState[][] field, Coord coord){
        return !(coord.Row < 0 || coord.Row > 9 || coord.Column < 0 || coord.Column > 9 ||
                field[coord.Row][coord.Column] != CellState.Empty ||
                coord.Row < 9 && field[coord.Row+1][coord.Column] != CellState.Empty ||
                coord.Row > 0 && field[coord.Row-1][coord.Column] != CellState.Empty ||
                coord.Column < 9 && field[coord.Row][coord.Column+1] != CellState.Empty ||
                coord.Row < 9 && coord.Column < 9 && field[coord.Row+1][coord.Column+1] != CellState.Empty ||
                coord.Row > 0 && coord.Column < 9 && field[coord.Row-1][coord.Column+1] != CellState.Empty ||
                coord.Column > 0 && field[coord.Row][coord.Column-1] != CellState.Empty ||
                coord.Row < 9 && coord.Column > 0 && field[coord.Row+1][coord.Column-1] != CellState.Empty ||
                coord.Row > 0 && coord.Column > 0 && field[coord.Row-1][coord.Column-1] != CellState.Empty);
    }

    private void CreateShip(CellState[][] field, int decks) {
        Random r = new Random();
        boolean isShipValid = false;
        while (!isShipValid){
            isShipValid = true;

            Coord coord = new Coord(r.nextInt(9), r.nextInt(9));
            Direction direction = Direction.values()[r.nextInt(1,4)];
            for (int i = 0; i < decks && isShipValid; i++) {
                Coord nextCoord = null;
                switch (direction)
                {
                    case Left:
                        nextCoord = new Coord(coord.Row, coord.Column-i);
                        break;
                    case Right:
                        nextCoord = new Coord(coord.Row, coord.Column+i);
                        break;
                    case Top:
                        nextCoord = new Coord(coord.Row-i, coord.Column);
                        break;
                    case Bottom:
                        nextCoord = new Coord(coord.Row+i, coord.Column);
                        break;
                }
                if(!IsCellValid(field, nextCoord))
                    isShipValid = false;
            }

            if(!isShipValid)
                continue;

            for (int i = 0; i < decks; i++){
                Coord nextCoord = null;
                switch (direction)
                {
                    case Left:
                        nextCoord = new Coord(coord.Row, coord.Column-i);
                        break;
                    case Right:
                        nextCoord = new Coord(coord.Row, coord.Column+i);
                        break;
                    case Top:
                        nextCoord = new Coord(coord.Row-i, coord.Column);
                        break;
                    case Bottom:
                        nextCoord = new Coord(coord.Row+i, coord.Column);
                        break;
                }
                field[nextCoord.Row][nextCoord.Column]=CellState.Ship;
            }
        }
    }

    private void CreateBomb(CellState[][] field) {
        Random r = new Random();
        for (;;){
            Coord coord = new Coord(r.nextInt(9), r.nextInt(9));
             if(IsCellValid(field, coord))
             {
                 field[coord.Row][coord.Column]=CellState.Bomb;
                 return;
             }
        }
    }

    public void FillField(CellState[][] field) {
        for (int i = 4; i >= 1; i--) {
            for (int j = 1; j <= 5 - i; j++) {
                CreateShip(field, i);
            }
        }
        for (int i = 0; i < 3; i++)
            CreateBomb(field);
    }

    private boolean IsCellEmpty (CellState[][] field, Coord coord) {
        return field[coord.Row][coord.Column] == CellState.Empty;
    }

    public void MarkShipKilled(CellState[][] field) {
        int minRow = 9;
        int maxRow = 0;
        int minCol = 9;
        int maxCol = 0;
        for (int i = 0; i < _hittedDeskCount; i++) {
            field[_hittedShipCords[i].Row][_hittedShipCords[i].Column] = CellState.HittedShip;
            if (minRow > _hittedShipCords[i].Row)
                minRow = _hittedShipCords[i].Row;
            if (maxRow < _hittedShipCords[i].Row)
                maxRow = _hittedShipCords[i].Row;
            if (minCol > _hittedShipCords[i].Column)
                minCol = _hittedShipCords[i].Column;
            if (maxCol < _hittedShipCords[i].Column)
                maxCol = _hittedShipCords[i].Column;

        }
        for (int i = minRow; i <= maxRow; i++) {
            if (minCol > 0)
                field[i][minCol - 1] = CellState.Water;
            if (maxCol < 9)
                field[i][maxCol + 1] = CellState.Water;

        }
        for (int i = minCol; i <= maxCol; i++) {
            if (minRow > 0)
                field[minRow - 1][i] = CellState.Water;
            if (maxRow < 9)
                field[maxRow + 1][i] = CellState.Water;
        }
        if (minRow > 0 && minCol > 0)
            field[minRow - 1][minCol - 1] = CellState.Water;
        if (maxRow < 9 && maxCol < 9)
            field[maxRow + 1][maxCol + 1] = CellState.Water;
        if (minRow > 0 && maxCol < 9)
            field[minRow - 1][maxCol + 1] = CellState.Water;
        if (maxRow < 9 && minCol > 0)
            field[maxRow + 1][minCol - 1] = CellState.Water;
    }

    public void MarkBombKilled(CellState[][] field, Coord coord) {
        if (coord.Column > 0)
            field[coord.Row][coord.Column - 1] = CellState.Water;
        if (coord.Column < 9)
            field[coord.Row][coord.Column + 1] = CellState.Water;

        if (coord.Row > 0)
            field[coord.Row - 1][coord.Column] = CellState.Water;
        if (coord.Row < 9)
            field[coord.Row + 1][coord.Column] = CellState.Water;

        if (coord.Row > 0 && coord.Column > 0)
            field[coord.Row - 1][coord.Column - 1] = CellState.Water;
        if (coord.Row < 9 && coord.Column < 9)
            field[coord.Row + 1][coord.Column + 1] = CellState.Water;
        if (coord.Row > 0 && coord.Column < 9)
            field[coord.Row - 1][coord.Column + 1] = CellState.Water;
        if (coord.Row < 9 && coord.Column > 0)
            field[coord.Row + 1][coord.Column - 1] = CellState.Water;
    }

    public Coord AttackDirection(CellState[][] field){
        int minRow = 9;
        int maxRow = 0;
        int minCol = 9;
        int maxCol = 0;
        for (int i = 0; i < _hittedDeskCount; i++) {
            field[_hittedShipCords[i].Row][_hittedShipCords[i].Column] = CellState.HittedShip;
            if (minRow > _hittedShipCords[i].Row)
                minRow = _hittedShipCords[i].Row;
            if (maxRow < _hittedShipCords[i].Row)
                maxRow = _hittedShipCords[i].Row;
            if (minCol > _hittedShipCords[i].Column)
                minCol = _hittedShipCords[i].Column;
            if (maxCol < _hittedShipCords[i].Column)
                maxCol = _hittedShipCords[i].Column;
        }
        Coord[] toCheck = new Coord[4];
        int coordsToCheck = 0;
        Random r = new Random();
        if(maxRow-minRow != 0)
        {
            // vert
            if(minRow > 0 && field[minRow-1][minCol] == CellState.Empty)
                toCheck[coordsToCheck++] = new Coord(minRow-1, minCol);
            if(maxRow < 9 && field[maxRow+1][minCol] == CellState.Empty)
                toCheck[coordsToCheck++] = new Coord(maxRow+1, minCol);
        }
        else if(maxCol-minCol != 0)
        {
            // horz
            if(minCol > 0 && field[minRow][minCol-1] == CellState.Empty)
                toCheck[coordsToCheck++] = new Coord(minRow, minCol-1);
            if(maxCol < 9 && field[minRow][maxCol+1] == CellState.Empty)
                toCheck[coordsToCheck++] = new Coord(minRow, maxCol+1);
        }
        else
        {
            // unkn
            if(minCol > 0 && field[minRow][minCol-1] == CellState.Empty)
                toCheck[coordsToCheck++] = new Coord(minRow, minCol-1);
            if(maxCol < 9 && field[minRow][maxCol+1] == CellState.Empty)
                toCheck[coordsToCheck++] = new Coord(minRow, maxCol+1);
            if(minRow > 0 && field[minRow-1][minCol] == CellState.Empty)
                toCheck[coordsToCheck++] = new Coord(minRow-1, minCol);
            if(maxRow < 9 && field[maxRow+1][minCol] == CellState.Empty)
                toCheck[coordsToCheck++] = new Coord(maxRow+1, minCol);
        }
        return toCheck [r.nextInt(coordsToCheck)];
    }
    public void SetTurnResult(CellState[][] field, Coord coord, TurnResult result){
        switch (result){
            case Miss: field [coord.Row][coord.Column] = CellState.Water;
                break;

            case Hit: field [coord.Row][coord.Column] = CellState.HittedShip;
                _killInProgress=true;

                _hittedShipCords[_hittedDeskCount++] = coord;
                break;

            case Kill:
                _hittedShipCords[_hittedDeskCount++] = coord;
                MarkShipKilled(field);
                _killInProgress=false;
                _hittedDeskCount=0;
                break;

            case Bomb:
                field [coord.Row][coord.Column] = CellState.KilledBomb;
                MarkBombKilled(field,coord);
                break;
        }
        return;
    }

    public Coord MakeTurn(CellState[][] field)
            throws Exception
    {
        if (_killInProgress==true)
            return AttackDirection(field);
        Random r = new Random();
        for(int i=0;i<100;i++) {
            Coord coord = new Coord(r.nextInt(9), r.nextInt(9));
            if (IsCellEmpty(field, coord)) {
                return coord;
            }
        }
        throw new Exception("Unable find new turn coordinates");
    }
}
