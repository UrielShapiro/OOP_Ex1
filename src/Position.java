public class Position
{
    private final int _x;
    private final int _y;
    public Position(int x, int y)
    {
        _x = x;
        _y = y;
    }
    public Position(Position p)
    {
        this._y = p.getY();
        this._x = p.getX();
    }

    public int getX()
    {
        return _x;
    }
    public int getY()
    {
        return _y;
    }
    @Override
    public String toString()
    {
        return "("+_x+", "+_y+")";
    }
    /*
    Checks if a given position is in one of the corners of the board.
    Returns true if the position is in one of the corners.
    Built in order to prevent Pawns from getting in the corners.
     */
    public static boolean isCorner(Position pos)
    {
        return (pos.getX() == 0 && pos.getY() == 0) ||
                (pos.getX() == GameLogic.getBoardSizeStatic() - 1 && pos.getY() == GameLogic.getBoardSizeStatic() - 1)
                || (pos.getX() == GameLogic.getBoardSizeStatic() - 1 && pos.getY() == 0)
                || (pos.getX() == 0 && pos.getY() == GameLogic.getBoardSizeStatic() - 1);
    }
    public boolean equal(Position b)
    {
        return this._x == b.getX() && this._y == b.getY();
    }
}
