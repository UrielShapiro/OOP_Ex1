/*
This class is used for the undo move function.
In the GameLogic class, when moving a piece, a new Pawns_Killed object will be made and inserted to a stack.
The Stack will contain all the moves in the game.
If a piece was killed in a given turn, that piece will be added to the array (instead of the moving piece) and its "killed"
status will be true.
When undoing a move, we will pop Pawns_Killed objects from the stack.
If the popped Pawns_Killed.killed == true, we will bring it back to the board.
 */
public class Pawns_Killed {
    private final ConcretePiece piece;
    private final boolean killed;
    private Pawn piece1;
    private Pawn piece2;
    private Pawn piece3;
    private Pawn piece4;


    public Pawns_Killed(ConcretePiece p, boolean GotKilled, Pawn p1, Pawn p2, Pawn p3, Pawn p4)
    {
        this.piece = p;              //The piece who was killed or the piece who moved (if no piece was killed).
        this.killed = GotKilled;     //Will be true only if it was killed.
       if (p1 != null) {
           piece1 = new Pawn(p1);
       }
       if (p2 != null) {
           piece2 = new Pawn(p2);
       }
       if (p3 != null) {
           piece3 = new Pawn(p3);
       }
       if (p4 != null) {
           piece4 = new Pawn(p4);
       }
    }
    public ConcretePiece getPiece()
    {
        return piece;
    }
    public boolean killedPieces()
    {
        return killed;
    }
    public Pawn getPiece1()
    {
        return piece1;
    }

    public Pawn getPiece2() {
        return piece2;
    }

    public Pawn getPiece3() {
        return piece3;
    }

    public Pawn getPiece4() {
        return piece4;
    }
}
