public class Coord {
    public int Row;
    public int Column;

    public Coord (int row, int column){
        Row = row;
        Column = column;
    }

    public boolean IsEqual(Coord coord)
    {
        return Row == coord.Row && Column == coord.Column;
    }

    public boolean IsEqual(int row, int column)
    {
        return Row == row && Column == column;
    }
}
