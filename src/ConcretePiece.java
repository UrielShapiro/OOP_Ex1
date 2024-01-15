import java.util.ArrayList;

public abstract class ConcretePiece implements Piece
{
    private ConcretePlayer _player;
    private final String _type;
    private Position _position;
    private final String _piecePosition;
    private final int _pieceNumber;
    private ArrayList<Position> moves = new ArrayList<>();
    private int _kills;
    public ConcretePiece(ConcretePlayer player,String type, Position position, String piecePosition, int number)
    {
        _player = player;
        _type = type;
        _position = position;
        _piecePosition = piecePosition;
        _kills = 0;
        _pieceNumber = number;
        moves.add(position);
    }
    @Override
    public Player getOwner() {
        return _player;
    }
    public ConcretePlayer getConcreteOwner() {return _player;}

    @Override
    public String getType() {
        return _type;
    }
    public void setPosition(Position newPosition)
    {
        _position = new Position(newPosition);
    }
    public Position getPositiom()
    {
        return _position;
    }
    public String getPiecePosition()
    {
        return _piecePosition;
    }
    public void addPositionToArray(Position position)
    {
        this.moves.add(position);
    }
    public int getNumOfMoves()
    {
        return this.moves.size();
    }
    public ArrayList<Position> getMoves()
    {
        return moves;
    }
    public void removeLastMove()
    {
        moves.removeLast();
    }
    public String MovesToString()
    {
        String ans = "[";
        for (int i = 0; i < moves.size() - 1; i++) {
            ans += moves.get(i).toString()+", ";
        }
        if (!moves.isEmpty()) {
            ans += moves.getLast().toString();
        }
        ans += "]";
        return ans;
    }
    public int getNumberOfKills()
    {
            return _kills;
    }
    public void kill()
    {
        if (!this.getPiecePosition().equals("K7")) {
            _kills++;
        }
    }
    public void undoKill()
    {
        _kills--;
    }
    public int getPieceNumber()
    {
        return _pieceNumber;
    }
    public int getDistance()
    {
        int sum = 0;
        for (int i = 0; i < this.moves.size() - 1; i++) {
            sum += calcDistance(this.moves.get(i), this.moves.get(i+1));
        }
        return sum;
    }
    private int calcDistance(Position o1, Position o2)
    {
        return Math.abs(o1.getY() - o2.getY()) + Math.abs(o1.getX() - o2.getX());
    }
}
