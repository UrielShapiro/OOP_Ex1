public class King extends ConcretePiece
{
    public King(ConcretePlayer player , Position position, String pieceNumber, int number)
    {
        super(player,"♔", position, pieceNumber, number);
    }
    /*
    This function is made just for safe code purpose.
    The king can't kill any other piece, so its number opf kills will always be 0.
     */
    @Override
    public int getNumberOfKills()
    {
        return 0;
    }
}
