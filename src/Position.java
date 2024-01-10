public class Position
{
    private int _x,_y;
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
}
