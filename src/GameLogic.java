import java.util.*;

public class GameLogic implements PlayableLogic {
    private static final int BOARD_SIZE = 11;
    private final ConcretePlayer _firstPlayer = new ConcretePlayer(true);
    private final ConcretePlayer _secondPlayer = new ConcretePlayer(false);
    private final ConcretePiece[][] _board = new ConcretePiece[BOARD_SIZE][BOARD_SIZE];
    private final Stack<Position> undoStack = new Stack<Position>();
    private final Stack<Position> currentPosition = new Stack<Position>();
    private final Stack<Pawn_Killed> killedPieces = new Stack<Pawn_Killed>();
    private static int[][] numOfStepsBoard = new int[BOARD_SIZE][BOARD_SIZE];
    private boolean firstPlayerTurn;
    private static boolean kingCaptured;
    private static boolean kingWin;
    private boolean killedApiece = false;

    /*
    Constructor for the GameLogic object which initializes all the parameters that are required in order to start
    a game.
    Including:
    - Initializing pieces on board.
    - initializing the boolean variables that say the king status (captured/win)
    - Initializing the turns of the players. setting the second player turn first.
     */
    public GameLogic() {
        InitialSetup();
        kingCaptured = false;
        firstPlayerTurn = false;
        kingWin = false;
    }

    /*
    A function that initializes all the pieces on board, with their starting positions.
    The function also call other function which attach each piece to its owner (Player 1/2).
     */
    public void InitialSetup() {
        clearPlayerPiecesArrays();

        _board[3][0] = new Pawn(_secondPlayer, new Position(3, 0), "A1", 1);
        _board[4][0] = new Pawn(_secondPlayer, new Position(4, 0), "A2", 2);
        _board[5][0] = new Pawn(_secondPlayer, new Position(5, 0), "A3", 3);
        _board[6][0] = new Pawn(_secondPlayer, new Position(6, 0), "A4", 4);
        _board[7][0] = new Pawn(_secondPlayer, new Position(7, 0), "A5", 5);
        _board[5][1] = new Pawn(_secondPlayer, new Position(5, 1), "A6", 6);

        _board[0][3] = new Pawn(_secondPlayer, new Position(0, 3), "A7", 7);
        _board[0][4] = new Pawn(_secondPlayer, new Position(0, 4), "A9", 9);
        _board[0][5] = new Pawn(_secondPlayer, new Position(0, 5), "A11", 11);
        _board[0][6] = new Pawn(_secondPlayer, new Position(0, 6), "A15", 15);
        _board[0][7] = new Pawn(_secondPlayer, new Position(0, 7), "A17", 17);
        _board[1][5] = new Pawn(_secondPlayer, new Position(1, 5), "A12", 12);

        _board[3][10] = new Pawn(_secondPlayer, new Position(3, 10), "A20", 20);
        _board[4][10] = new Pawn(_secondPlayer, new Position(4, 10), "A21", 21);
        _board[5][10] = new Pawn(_secondPlayer, new Position(5, 10), "A22", 22);
        _board[6][10] = new Pawn(_secondPlayer, new Position(6, 10), "A23", 23);
        _board[7][10] = new Pawn(_secondPlayer, new Position(7, 10), "A24", 24);
        _board[5][9] = new Pawn(_secondPlayer, new Position(5, 9), "A19", 19);

        _board[10][3] = new Pawn(_secondPlayer, new Position(10, 3), "A8", 8);
        _board[10][4] = new Pawn(_secondPlayer, new Position(10, 4), "A10", 10);
        _board[10][5] = new Pawn(_secondPlayer, new Position(10, 5), "A14", 14);
        _board[10][6] = new Pawn(_secondPlayer, new Position(10, 6), "A16", 16);
        _board[10][7] = new Pawn(_secondPlayer, new Position(10, 7), "A18", 18);
        _board[9][5] = new Pawn(_secondPlayer, new Position(9, 5), "A13", 13);

        _board[5][3] = new Pawn(_firstPlayer, new Position(5, 3), "D1", 1);
        _board[4][4] = new Pawn(_firstPlayer, new Position(4, 4), "D2", 2);
        _board[5][4] = new Pawn(_firstPlayer, new Position(5, 4), "D3", 3);
        _board[6][4] = new Pawn(_firstPlayer, new Position(6, 4), "D4", 4);
        _board[3][5] = new Pawn(_firstPlayer, new Position(3, 5), "D5", 5);
        _board[4][5] = new Pawn(_firstPlayer, new Position(4, 5), "D6", 6);
        _board[5][5] = new King(_firstPlayer, new Position(5, 5), "K7", 7);
        _board[6][5] = new Pawn(_firstPlayer, new Position(6, 5), "D8", 8);
        _board[7][5] = new Pawn(_firstPlayer, new Position(7, 5), "D9", 9);
        _board[4][6] = new Pawn(_firstPlayer, new Position(4, 6), "D10", 10);
        _board[5][6] = new Pawn(_firstPlayer, new Position(5, 6), "D11", 11);
        _board[6][6] = new Pawn(_firstPlayer, new Position(6, 6), "D12", 12);
        _board[5][7] = new Pawn(_firstPlayer, new Position(5, 7), "D13", 13);

        attachPiecesToPlayer();
        initializeStepsBoard();
    }

    /*
    This function adds each piece on the board to its owner.
    Each player has an array containing all of its pieces.
     */
    private void attachPiecesToPlayer() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (_board[i][j] instanceof Pawn | _board[i][j] instanceof King) {
                    if (_board[i][j].getOwner().isPlayerOne()) {
                        _firstPlayer.addPieceToStack(_board[i][j]);
                    }
                    else {
                        _secondPlayer.addPieceToStack(_board[i][j]);
                    }
                }
            }
        }
    }
/*
This function clears both players arrays of pieces.
is used in order to start a game from zero.
 */
    private void clearPlayerPiecesArrays() {
        _firstPlayer.emptyPiecesArray();
        _secondPlayer.emptyPiecesArray();
    }

    private void initializeStepsBoard() {
        for (int i = 0; i < numOfStepsBoard.length; i++) {
            for (int j = 0; j < numOfStepsBoard.length; j++) {
                if (_board[i][j] instanceof Pawn || _board[i][j] instanceof King) {
                    numOfStepsBoard[i][j]++;
                } else {
                    numOfStepsBoard[i][j] = 0;
                }
            }
        }
    }


    @Override
    public boolean move(Position a, Position b) {
        if (getPieceAtPosition(a).getOwner().isPlayerOne() && !firstPlayerTurn) {
            return false;
        }
        if (firstPlayerTurn && !getPieceAtPosition(a).getOwner().isPlayerOne()) {
            return false;
        }
        if (!(legalMove(a, b))) {
            return false;
        }
        if (!(SpaceIsClear(a, b))) {
            return false;
        }
        if (getPieceAtPosition(a) instanceof Pawn && Position.isCorner(b))
        {
            return false;
        }
        undoStack.add(a);
        currentPosition.add(b);
        _board[a.getX()][a.getY()].setPosition(b);
        _board[b.getX()][b.getY()] = _board[a.getX()][a.getY()];
        _board[a.getX()][a.getY()] = null;
        addPositionToPieceArray(b);
        boolean aKillWasMade = eat(_board[b.getX()][b.getY()]);
        if (!aKillWasMade) {
            killedPieces.add(new Pawn_Killed(_board[b.getX()][b.getY()], false));
        }
        firstPlayerTurn = !firstPlayerTurn;
        kingWin = kingInCorner();
        if (isGameFinished()) {
            endGamePrints();
        }
        return true;
    }
    /*
    Each piece has an array containing the positions it stepped on.
    This function adds a given position to the array of positions of the piece that stands on the given position.
    */
    private void addPositionToPieceArray(Position b) {
        if (_board[b.getX()][b.getY()].getOwner().isPlayerOne()) {
            int i = 0;
            while (!_firstPlayer.getPieceFromArray(i).getPiecePosition().equals(_board[b.getX()][b.getY()].getPiecePosition())) {
                i++;
            }
            _firstPlayer.getPieceFromArray(i).addPositionToStack(b);
        }
        if (!_board[b.getX()][b.getY()].getOwner().isPlayerOne()) {
            int j = 0;
            while (!_secondPlayer.getPieceFromArray(j).getPiecePosition().equals(_board[b.getX()][b.getY()].getPiecePosition())) {
                j++;
            }
            _secondPlayer.getPieceFromArray(j).addPositionToStack(b);
        }
    }
    /*
    This function checks if a given position is legal to move to.
    The function checks if the position is inside the board borders and if it is not the current position (meaning,
    there is a difference between both positions).
    If the position is indeed inside the border and is different from the current position.
    The function will return if that position in the border is not taken (is null).
    The function is used to check if a given position is eligible to move to.
     */
    public boolean legalMove(Position a, Position b) {
        if (b.getX() > BOARD_SIZE || b.getY() > BOARD_SIZE || b.getX() < 0 || b.getY() < 0) {
            return false;
        }
        if ((a.getX() != b.getX()) && (a.getY() != b.getY())) {
            return false;
        }
        return _board[b.getX()][b.getY()] == null;
    }
    /*
    This function checks if all cells (the road) between position 'a' to position 'b' are clear.
    If the road is not clear, the function will return false.
    Else, the function returns true.
    This function is used in order to check if a given piece at position 'a' is able to move to position 'b' according
    to the game rules.
     */
    public boolean SpaceIsClear(Position a, Position b) {
        if (b.getY() > a.getY()) {
            for (int i = a.getY() + 1; i <= b.getY(); i++) {
                if (_board[a.getX()][i] != null) {
                    return false;
                }
            }
        }
        if (b.getY() < a.getY()) {
            for (int i = a.getY() - 1; i >= b.getY(); i--) {
                if (_board[a.getX()][i] != null) {
                    return false;
                }
            }
        }
        if (b.getX() > a.getX()) {
            for (int i = a.getX() + 1; i <= b.getX(); i++) {
                if (_board[i][a.getY()] != null) {
                    return false;
                }
            }
        }
        if (b.getX() < a.getX()) {
            for (int i = a.getX() - 1; i >= b.getX(); i--) {
                if (_board[i][a.getY()] != null) {
                    return false;
                }
            }
        }
        return true;
    }
    /*
    This function returns the 'Piece' at a given position by referring to the board at that position.
     */
    @Override
    public Piece getPieceAtPosition(Position position) {
        return _board[position.getX()][position.getY()];
    }
    /*
    This function returns the first player 'Player' object.
     */
    @Override
    public Player getFirstPlayer() {
        return _firstPlayer;
    }
    /*
    This function returns the second player 'Player' object.
    */
    @Override
    public Player getSecondPlayer() {
        return _secondPlayer;
    }

    @Override
    public boolean isGameFinished() {
        if (kingCaptured) {
            _secondPlayer.increaseWins();
            _secondPlayer.setWonLastGame(true);
            _firstPlayer.setWonLastGame(false);
        }
        if (kingWin) {
            _firstPlayer.increaseWins();
            _firstPlayer.setWonLastGame(true);
            _secondPlayer.setWonLastGame(false);
        }
        return kingCaptured | kingWin;
    }

    private void calcNumOfSteps() {
        for (int i = 0; i < _firstPlayer.getArrayLength(); i++) {
            ArrayList<Position> firstPlayerMovesArray = new ArrayList<>(removeDuplicates(_firstPlayer.getPieceFromArray(i).getMoves()));
            for (int j = 0; j < firstPlayerMovesArray.size(); j++) {
                int x = firstPlayerMovesArray.get(j).getX();
                int y = firstPlayerMovesArray.get(j).getY();
                numOfStepsBoard[x][y]++;
            }
        }
        for (int i = 0; i < _secondPlayer.getArrayLength(); i++) {
            ArrayList<Position> secondPlayerMovesArray = new ArrayList<>(removeDuplicates(_secondPlayer.getPieceFromArray(i).getMoves()));
            for (int j = 0; j < secondPlayerMovesArray.size(); j++) {
                int x = secondPlayerMovesArray.get(j).getX();
                int y = secondPlayerMovesArray.get(j).getY();
                numOfStepsBoard[x][y]++;
            }
        }
    }

    private ArrayList<Position> removeDuplicates(ArrayList<Position> array) {
        ArrayList<Position> arr = new ArrayList<>();
        arr.add(array.getFirst());
        boolean notDuplicant = true;
        for (int i = 1; i < array.size(); i++) {
            for (int j = 0; j < arr.size(); j++) {
                if (arr.get(j).equal(array.get(i))) {
                    notDuplicant = false;
                }
            }
            if (notDuplicant)
            {
                arr.add(array.get(i));
            }
            notDuplicant = true;
        }
        return arr;
    }

    private void endGamePrints() {
        //Q2_1
        _firstPlayer.sortAccordingToNumOfSteps();
        _secondPlayer.sortAccordingToNumOfSteps();
        if (_firstPlayer.getWonLastGame()) {
            for (int i = 0; i < _firstPlayer.getArrayLength(); i++) {
                if (_firstPlayer.getPieceFromArray(i).getNumOfMoves() > 1) {
                    System.out.print(_firstPlayer.getPieceFromArray(i).getPiecePosition() + ": ");
                    System.out.print(_firstPlayer.getPieceFromArray(i).MovesToString());
                    System.out.println();
                }
            }
        }
        for (int i = 0; i < _secondPlayer.getArrayLength(); i++) {
            if (_secondPlayer.getPieceFromArray(i).getNumOfMoves() > 1) {
                System.out.print(_secondPlayer.getPieceFromArray(i).getPiecePosition() + ": ");
                System.out.print(_secondPlayer.getPieceFromArray(i).MovesToString());
                System.out.println();
            }
        }
        if (!_firstPlayer.getWonLastGame()) {
            for (int i = 0; i < _firstPlayer.getArrayLength(); i++) {
                if (_firstPlayer.getPieceFromArray(i).getNumOfMoves() > 1) {
                    System.out.print(_firstPlayer.getPieceFromArray(i).getPiecePosition() + ": ");
                    System.out.print(_firstPlayer.getPieceFromArray(i).MovesToString());
                    System.out.println();
                }
            }
        }
        System.out.println("***************************************************************************");
        //Q2_2
        ArrayList<ConcretePiece> MergedPlayersArray = new ArrayList<>();
        for (int i = 0; i < _firstPlayer.getArrayLength(); i++) {
            MergedPlayersArray.add(_firstPlayer.getPieceFromArray(i));
        }
        for (int i = 0; i < _secondPlayer.getArrayLength(); i++) {
            MergedPlayersArray.add(_secondPlayer.getPieceFromArray(i));
        }
        Q2_2Sort(MergedPlayersArray);
        for (ConcretePiece piece : MergedPlayersArray) {
            if (piece.getNumberOfKills() > 0) {
                System.out.print(piece.getPiecePosition() + ": ");
                System.out.println(piece.getNumberOfKills()+ " kills");
            }
        }
        System.out.println("***************************************************************************");
       //Q2_3
        Q2_3Sort(MergedPlayersArray);
        for (ConcretePiece piece : MergedPlayersArray) {
            if (piece.getDistance() > 0) {
                System.out.println(piece.getPiecePosition() + ": "
                        + piece.getDistance() + " squares");
            }
        }
        System.out.println("***************************************************************************");
        //Q2_4
        calcNumOfSteps();
        ArrayList<PositionAndSteps> arr = Q2_4Sort();
        for (int i = 0; i < arr.size(); i++) {
            Position pos = new Position(arr.get(i).getPosition());
            System.out.println("(" + pos.getX() + ", " + pos.getY() + "): " + getNumOfSteps(pos) + " pieces");
        }
    }

    public void Q2_2Sort(ArrayList<ConcretePiece> array) {
        Comparator<ConcretePiece> KillComp = (o1, o2) -> {
            if (o1.getNumberOfKills() == o2.getNumberOfKills()) {
                if (o1.getPieceNumber() == o2.getPieceNumber()) {
                    return Boolean.compare(o1.getConcreteOwner().getWonLastGame(), o2.getConcreteOwner().getWonLastGame());
                }
                return Integer.compare(o1.getPieceNumber(), o2.getPieceNumber());
            }
            return -1 * Integer.compare(o1.getNumberOfKills(), o2.getNumberOfKills());
        };
        array.sort(KillComp);
    }

    public void Q2_3Sort(ArrayList<ConcretePiece> arr) {
        Comparator<ConcretePiece> DistanceComp = new Comparator<ConcretePiece>() {
            @Override
            public int compare(ConcretePiece o1, ConcretePiece o2) {
                if (o1.getDistance() == o2.getDistance()) {
                    if (o1.getPieceNumber() == o2.getPieceNumber()) {
                        return Boolean.compare(o1.getConcreteOwner().getWonLastGame(),
                                o2.getConcreteOwner().getWonLastGame());
                    }
                    return Integer.compare(o1.getPieceNumber(), o2.getPieceNumber());
                }
                return -1 * Integer.compare(o1.getDistance(), o2.getDistance());
            }
        };
        arr.sort(DistanceComp);
    }
    public ArrayList<PositionAndSteps> Q2_4Sort()
    {
        ArrayList<PositionAndSteps> arr = new ArrayList<>();
        for (int i = 0; i < numOfStepsBoard.length; i++) {
            for (int j = 0; j < numOfStepsBoard.length; j++) {
                Position pos = new Position(i,j);
                if (getNumOfSteps(pos) > 1)
                {
                    arr.add(new PositionAndSteps(pos, getNumOfSteps(pos)));
                }
            }
        }
        Comparator<PositionAndSteps> comp = new Comparator<PositionAndSteps>() {
            @Override
            public int compare(PositionAndSteps o1, PositionAndSteps o2) {
                if (o1.getSteps() == o2.getSteps())
                {
                    if (o1.getPosition().getX() == o2.getPosition().getX())
                    {
                        return Integer.compare(o1.getPosition().getY(), o2.getPosition().getY());
                    }
                    return Integer.compare(o1.getPosition().getX(), o2.getPosition().getX());
                }
                return -1 * Integer.compare(o1.getSteps(), o2.getSteps());
            }
        };
        arr.sort(comp);
        return arr;
    }
    public static int getNumOfSteps(Position p)
    {
        return numOfStepsBoard[p.getX()][p.getY()];
    }
    @Override
    public boolean isSecondPlayerTurn() {
        return !firstPlayerTurn;
    }

    @Override
    public void reset() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                _board[i][j] = null;
            }
        }
        InitialSetup();
        kingCaptured = false;
        firstPlayerTurn = false;
        currentPosition.clear();
        undoStack.clear();
    }

    private boolean eat(ConcretePiece a) {
        int y = a.getPositiom().getY();
        int x = a.getPositiom().getX();
        if (a instanceof Pawn) {
            //Check if he is close to the borders
            if (x == 1 && _board[x - 1][y] instanceof Pawn &&
                    !(a.getOwner().equals(_board[x - 1][y].getOwner()))) {
                killedPieces.add(new Pawn_Killed(_board[x - 1][y], true));
                _board[x - 1][y] = null;
                killedApiece = true;
            }
            if (x == 9 && _board[x + 1][y] instanceof Pawn
                    && !(a.getOwner().equals(_board[x + 1][y].getOwner()))) {
                killedPieces.add(new Pawn_Killed(_board[x + 1][y], true));
                _board[x + 1][y] = null;
                killedApiece = true;
            }
            if (y == 9 && _board[x][y + 1] instanceof Pawn
                    && !(a.getOwner().equals(_board[x][y + 1].getOwner()))) {
                killedPieces.add(new Pawn_Killed(_board[x][y + 1], true));
                _board[x][y + 1] = null;
                killedApiece = true;
            }
            if (y == 1 && _board[x][y - 1] instanceof Pawn
                    && !(a.getOwner().equals(_board[x][y - 1].getOwner()))) {
                killedPieces.add(new Pawn_Killed(_board[x][y - 1], true));
                _board[x][y - 1] = null;
                killedApiece = true;
            }
            //Check his surroundings
            if (x + 2 < BOARD_SIZE) {
                if (_board[x + 1][y] instanceof Pawn && _board[x + 2][y] instanceof Pawn) {
                    if (!_board[x + 1][y].getOwner().equals(_board[x][y].getOwner())
                            && _board[x][y].getOwner().equals(_board[x + 2][y].getOwner())) {
                        killedPieces.add(new Pawn_Killed(_board[x + 1][y], true));
                        _board[x + 1][y] = null;
                        killedApiece = true;
                    }
                }
            }
            if (x - 2 >= 0) {
                if (_board[x - 1][y] instanceof Pawn && _board[x - 2][y] instanceof Pawn) {
                    if (!_board[x - 1][y].getOwner().equals(_board[x][y].getOwner())
                            && _board[x][y].getOwner().equals(_board[x - 2][y].getOwner())) {
                        killedPieces.add(new Pawn_Killed(_board[x - 1][y], true));
                        _board[x - 1][y] = null;
                        killedApiece = true;
                    }
                }
            }
            if (y + 2 < BOARD_SIZE) {
                if (_board[x][y + 1] instanceof Pawn && _board[x][y + 2] instanceof Pawn) {
                    if (!_board[x][y + 1].getOwner().equals(_board[x][y].getOwner())
                            && _board[x][y].getOwner().equals(_board[x][y + 2].getOwner())) {
                        killedPieces.add(new Pawn_Killed(_board[x][y + 1], true));
                        _board[x][y + 1] = null;
                        killedApiece = true;
                    }
                }
            }
            if (y - 2 >= 0) {
                if (_board[x][y - 1] instanceof Pawn && _board[x][y - 2] instanceof Pawn) {
                    if (!_board[x][y - 1].getOwner().equals(_board[x][y].getOwner())
                            && _board[x][y].getOwner().equals(_board[x][y - 2].getOwner())) {
                        killedPieces.add(new Pawn_Killed(_board[x][y - 1], true));
                        _board[x][y - 1] = null;
                        killedApiece = true;
                    }
                }
            }
        }
        eatKing(a);
        if (killedApiece) {
            addKill(a.getPositiom());
            killedApiece = !killedApiece;
            return true;
        }
        return false;
    }

    private void addKill(Position a) {
        int x = a.getX();
        int y = a.getY();
        if (_board[x][y].getOwner().isPlayerOne()) {
            for (int i = 0; i < _firstPlayer.getArrayLength(); i++) {
                if (_board[x][y].getPiecePosition().equals(_firstPlayer.getPieceFromArray(i).getPiecePosition()))
                    _firstPlayer.getPieceFromArray(i).kill();
            }
        }
        if (!_board[x][y].getOwner().isPlayerOne()) {
            for (int i = 0; i < _secondPlayer.getArrayLength(); i++) {
                if (_board[x][y].getPiecePosition().equals(_secondPlayer.getPieceFromArray(i).getPiecePosition())) {
                    _secondPlayer.getPieceFromArray(i).kill();
                }
            }
        }
    }

    public void eatKing(ConcretePiece piece) {
        int x = piece.getPositiom().getX();
        int y = piece.getPositiom().getY();
        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;
        boolean flag4 = false;
        if (x + 1 < BOARD_SIZE) {
            if (_board[x + 1][y] instanceof King) {
                if (x + 2 < BOARD_SIZE) {
                    if (_board[x + 2][y] instanceof Pawn && !_board[x + 2][y].getOwner().isPlayerOne()) {
                        flag1 = true;
                    }
                } else {
                    flag1 = true;
                }
                if (y + 1 < BOARD_SIZE) {
                    if (_board[x + 1][y + 1] instanceof Pawn && !_board[x + 1][y + 1].getOwner().isPlayerOne()) {
                        flag2 = true;
                    }
                } else {
                    flag2 = true;
                }
                if (y - 1 >= 0) {
                    if (_board[x + 1][y - 1] instanceof Pawn && !_board[x + 1][y - 1].getOwner().isPlayerOne()) {
                        flag3 = true;
                    }
                } else {
                    flag3 = true;
                }
                if (!_board[x][y].getOwner().isPlayerOne()) {
                    flag4 = true;
                }
                kingCaptured = flag1 && flag2 && flag3 && flag4;
                if (kingCaptured) {
                    killedApiece = true;
                }
                return;
            }
        }
        if (x - 1 >= 0) {
            if (_board[x - 1][y] instanceof King) {
                if (x - 2 >= 0) {
                    if (_board[x - 2][y] instanceof Pawn && !_board[x - 2][y].getOwner().isPlayerOne()) {
                        flag1 = true;
                    }
                } else {
                    flag1 = true;
                }
                if (y + 1 < BOARD_SIZE) {
                    if (_board[x - 1][y + 1] instanceof Pawn && !_board[x - 1][y + 1].getOwner().isPlayerOne()) {
                        flag2 = true;
                    }
                } else {
                    flag2 = true;
                }
                if (y - 1 >= 0) {
                    if (_board[x - 1][y - 1] instanceof Pawn && !_board[x - 1][y - 1].getOwner().isPlayerOne()) {
                        flag3 = true;
                    }
                } else {
                    flag3 = true;
                }
                if (!_board[x][y].getOwner().isPlayerOne()) {
                    flag4 = true;
                }
                kingCaptured = flag1 && flag2 && flag3 && flag4;
                return;
            }
        }
        if (y + 1 < BOARD_SIZE) {
            if (_board[x][y + 1] instanceof King) {
                if (x + 1 < BOARD_SIZE) {
                    if (_board[x + 1][y + 1] instanceof Pawn && !_board[x + 1][y + 1].getOwner().isPlayerOne()) {
                        flag1 = true;
                    }
                } else {
                    flag1 = true;
                }
                if (y + 2 < BOARD_SIZE) {
                    if (_board[x][y + 2] instanceof Pawn && !_board[x][y + 2].getOwner().isPlayerOne()) {
                        flag2 = true;
                    }
                } else {
                    flag2 = true;
                }
                if (x - 1 >= 0) {
                    if (_board[x - 1][y + 1] instanceof Pawn && !_board[x - 1][y + 1].getOwner().isPlayerOne()) {
                        flag3 = true;
                    }
                } else {
                    flag3 = true;
                }
                if (!_board[x][y].getOwner().isPlayerOne()) {
                    flag4 = true;
                }
                kingCaptured = flag1 && flag2 && flag3 && flag4;
                return;
            }
        }
        if (y - 1 >= 0) {
            if (_board[x][y - 1] instanceof King) {
                if (x + 1 < BOARD_SIZE) {
                    if (_board[x + 1][y - 1] instanceof Pawn && !_board[x + 1][y - 1].getOwner().isPlayerOne()) {
                        flag1 = true;
                    }
                } else {
                    flag1 = true;
                }
                if (y - 2 >= 0) {
                    if (_board[x][y - 2] instanceof Pawn && !_board[x][y - 2].getOwner().isPlayerOne()) {
                        flag2 = true;
                    }
                } else {
                    flag2 = true;
                }
                if (x - 1 >= 0) {
                    if (_board[x - 1][y - 1] instanceof Pawn && !_board[x - 1][y - 1].getOwner().isPlayerOne()) {
                        flag3 = true;
                    }
                } else {
                    flag3 = true;
                }
                if (!_board[x][y].getOwner().isPlayerOne()) {
                    flag4 = true;
                }
                kingCaptured = flag1 && flag2 && flag3 && flag4;
            }
        }
    }

    private boolean kingInCorner() {
        return _board[0][0] instanceof King | _board[0][BOARD_SIZE - 1] instanceof King
                | _board[BOARD_SIZE - 1][0] instanceof King | _board[BOARD_SIZE - 1][BOARD_SIZE - 1] instanceof King;
    }

    @Override
    public void undoLastMove() {
        if (!undoStack.isEmpty() & !currentPosition.isEmpty()) {
            Position recentPos = new Position(undoStack.pop());
            Position currentPos = new Position(currentPosition.pop());
            removeLastPositionFromMovesArray(currentPos);
            _board[recentPos.getX()][recentPos.getY()] = _board[currentPos.getX()][currentPos.getY()];
            _board[currentPos.getX()][currentPos.getY()] = null;
            if (!killedPieces.isEmpty()) {
                ConcretePiece p = killedPieces.peek().getPiece();
                if (killedPieces.pop().gotKilled()) {
                    Position p_position = p.getPositiom();
                    _board[p_position.getX()][p_position.getY()] = p;
                }
            }
            firstPlayerTurn = !firstPlayerTurn;
        }
    }

    private void removeLastPositionFromMovesArray(Position p) {
        if (_board[p.getX()][p.getY()].getOwner().isPlayerOne()) {
            for (int i = 0; i < _firstPlayer.getArrayLength(); i++) {
                if (_firstPlayer.getPieceFromArray(i).getPiecePosition().equals(_board[p.getX()][p.getY()].getPiecePosition())) {
                    _firstPlayer.getPieceFromArray(i).removeLastMove();
                }
            }
        } else {
            for (int i = 0; i < _secondPlayer.getArrayLength(); i++) {
                if (_secondPlayer.getPieceFromArray(i).getPiecePosition().equals(_board[p.getX()][p.getY()].getPiecePosition())) {
                    _secondPlayer.getPieceFromArray(i).removeLastMove();
                }
            }
        }
    }

    @Override
    public int getBoardSize() {
        return BOARD_SIZE;
    }
    public static int getBoardSizeStatic() {
        return BOARD_SIZE;
    }
}
