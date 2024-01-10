public class Pawn_Killed {
    private final ConcretePiece piece;
    private final boolean killed;

    public Pawn_Killed(ConcretePiece p, boolean killedOtherPiece)
    {
        this.piece = p;
        this.killed = killedOtherPiece;
    }

    public ConcretePiece getPiece()
    {
        return piece;
    }
    public boolean gotKilled()
    {
        return killed;
    }
}
