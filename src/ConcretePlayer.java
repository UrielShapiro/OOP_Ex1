import java.util.*;

public class ConcretePlayer implements Player
{
    private int _wins;
    private final boolean _isPlayerOne;
    private final ArrayList<ConcretePiece> pieces = new ArrayList<>();
    private boolean _wonLastGame;
    public ConcretePlayer(boolean playerOne)
    {
        _wins = 0;
        _isPlayerOne = playerOne;
        _wonLastGame = false;
    }
    @Override
    public boolean isPlayerOne() {
        return _isPlayerOne;
    }

    @Override
    public int getWins() {
        return _wins;
    }
    public void increaseWins()
    {
        this._wins++;
    }
    public String GetType()
    {
        if(_isPlayerOne)
        {
           return "♙";
        }
        return  "♟";
    }
    public void addPieceToStack(ConcretePiece a)
    {
        pieces.add(a);
    }
    public void emptyPiecesArray()
    {
        pieces.clear();
    }
    public int getArrayLength()
    {
        return pieces.size();
    }
    public ConcretePiece getPieceFromArray(int i)
    {
        return pieces.get(i);
    }

    public void setWonLastGame(boolean won)
    {
        _wonLastGame = won;
    }
    public boolean getWonLastGame()
    {
        return _wonLastGame;
    }
    public void sortAccordingToNumOfSteps()
    {
       Comparator<ConcretePiece> comp = new Comparator<ConcretePiece>() {
           @Override
           public int compare(ConcretePiece o1, ConcretePiece o2) {
               if (o1.getNumOfMoves() == o2.getNumOfMoves())
               {
                   return Integer.compare(o1.getPieceNumber(), o2.getPieceNumber());
               }
               return Integer.compare(o1.getNumOfMoves(), o2.getNumOfMoves()) ;
           }
       };
        pieces.sort(comp);
    }

}
