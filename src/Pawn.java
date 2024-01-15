public class Pawn extends ConcretePiece {
    public Pawn(ConcretePlayer player, Position position, String pieceNumber, int number) {
        super(player, player.GetType(), position, pieceNumber, number);
    }
}

