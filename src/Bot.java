import java.util.ArrayList;
import java.util.Random;

public class Bot {
    private GameItem _hittedShip = null;

    public TurnResult GetTurnResult(Coord coord, ArrayList<GameItem> ships, ArrayList<GameItem> bombs) {
        for (int i = 0; i < ships.size(); i++)
            if (ships.get(i).Hit(coord)) {
                if (ships.get(i).IsKilled())
                    return TurnResult.Kill;
                return TurnResult.Hit;
            }
        for (int i = 0; i < bombs.size(); i++)
            if (bombs.get(i).Hit(coord)) {
                return TurnResult.Bomb;
            }
        return TurnResult.Miss;
    }

    public void SetTurnResult(TurnResult result, Coord coord, ArrayList<GameItem> ships, ArrayList<Shot> shots) {
        switch (result) {
            case Miss:
                shots.add(new Shot(coord));
                break;
            case Hit:
                for (int i = 0; i < ships.size(); i++)
                    if (ships.get(i).Hit(coord)) {
                        _hittedShip = ships.get(i);
                    }
                break;
            case Kill:
                _hittedShip = null;
                break;

            case Bomb:
                break;
        }
    }

    public Coord MakeTurn(CellState[][] field, ArrayList<Shot> shots)
            throws Exception {

        if (_hittedShip != null)
            return AttackShip(shots);

        Random r = new Random();
        for (int i = 0; i < 100; i++) {
            int row = r.nextInt(9);
            int column = r.nextInt(9);
            if (field[row][column] == CellState.Empty)
                return new Coord(row, column);
        }
        throw new Exception("Unable find new turn coordinates");
    }


    public Coord AttackShip(ArrayList<Shot> shots){
        Coord[] hittedDesk = _hittedShip.GetHittedDecks();
        Coord[] toCheck = GetCoordsToCheck(hittedDesk);

        Random r = new Random();
        while(true) {
            int i = r.nextInt(toCheck.length);
            if (toCheck[i].Row < 0 ||
                    toCheck[i].Row > 9 ||
                    toCheck[i].Column < 0 ||
                    toCheck[i].Column > 9)
                continue;
            boolean hitted = false;
            for(int j = 0; j < shots.size() && !hitted; j++)
                if(shots.get(j).GetCoord().IsEqual(toCheck[i]))
                    hitted = true;
            if(!hitted)
                return toCheck[i];
        }
    }

    private Coord[] GetCoordsToCheck(Coord[] hittedDesk)
    {
        if(hittedDesk.length > 1)
        {
            Coord[] toCheck = new Coord[2];
            if(hittedDesk[0].Row == hittedDesk[1].Row) {
                int min = hittedDesk[0].Column;
                int max = hittedDesk[0].Column;
                for(int i=1; i < hittedDesk.length; i++) {
                    if (hittedDesk[i].Column < min)
                        min = hittedDesk[i].Column;
                    if (hittedDesk[i].Column > max)
                        max = hittedDesk[i].Column;
                }
                toCheck[0] = new Coord(hittedDesk[0].Row, min-1);
                toCheck[1] = new Coord(hittedDesk[0].Row, max+1);
            }
            else
            {
                int min = hittedDesk[0].Row;
                int max = hittedDesk[0].Row;
                for(int i=1; i < hittedDesk.length; i++) {
                    if (hittedDesk[i].Row < min)
                        min = hittedDesk[i].Row;
                    if (hittedDesk[i].Row > max)
                        max = hittedDesk[i].Row;
                }
                toCheck[0] = new Coord(min-1, hittedDesk[0].Column);
                toCheck[1] = new Coord(max+1, hittedDesk[0].Column);
            }
            return  toCheck;
        }
        else
        {
            Coord[] toCheck = new Coord[4];
            toCheck[0] = new Coord(hittedDesk[0].Row-1, hittedDesk[0].Column);
            toCheck[1] = new Coord(hittedDesk[0].Row+1, hittedDesk[0].Column);
            toCheck[2] = new Coord(hittedDesk[0].Row, hittedDesk[0].Column-1);
            toCheck[3] = new Coord(hittedDesk[0].Row, hittedDesk[0].Column+1);
            return  toCheck;
        }
    }
}
