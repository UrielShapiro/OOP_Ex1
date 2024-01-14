public class PositionAndSteps {
    private Position pos;
    private int _steps;
    public PositionAndSteps(Position p, int steps)
    {
        pos = p;
        _steps = steps;
    }
    public int getSteps()
    {
        return _steps;
    }
    public Position getPosition()
    {
        return pos;
    }
}
