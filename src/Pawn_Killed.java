/*
This class is used for the undo move function.
In the GameLogic class, when moving a piece, a new Pawn_Killed object will be made and inserted to a stack.
The Stack will contain all the moves in the game.
If a piece was killed in a given turn, that piece will be added to the array (instead of the moving piece) and its "killed"
status will be true.
When undoing a move, we will pop Pawn_Killed objects from the stack.
If the popped Pawn_Killed.killed == true, we will bring it back to the board.
 */
public class Pawn_Killed {
    private final ConcretePiece piece;
    private final boolean killed;

    public Pawn_Killed(ConcretePiece p, boolean GotKilled)
    {
        this.piece = p;              //The piece who was killed or the piece who moved (if no piece was killed).
        this.killed = GotKilled;     //Will be true only if it was killed.
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
