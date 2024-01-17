import java.util.*;

public class GameLogic implements PlayableLogic {
    private static final int BOARD_SIZE = 11;
    private final ConcretePlayer _firstPlayer = new ConcretePlayer(true);
    private final ConcretePlayer _secondPlayer = new ConcretePlayer(false);
    private final ConcretePiece[][] _board = new ConcretePiece[getBoardSize()][getBoardSize()];
    private final Stack<Position> undoStack = new Stack<>();
    private final Stack<Position> currentPosition = new Stack<>();
    private final Stack<Pawn_Killed> killedPiecesStack = new Stack<>();
    private static Position[][] numOfStepsBoard = new Position[BOARD_SIZE][BOARD_SIZE];
    private boolean firstPlayerTurn;
    private static boolean kingCaptured;
    private static boolean kingWin;
    private boolean killedApiece = false;
    private static int numOfMoves;

    /*
    Constructor for the GameLogic object which initializes all the parameters that are required in order to start
    a game.
    Including:
    - Initializing pieces on board.
    - initializing the boolean variables that say the king status (captured/win)
    - Initializing the turns of the players. setting the second player turns first.
     */
    public GameLogic() {
        InitialSetup();
        kingCaptured = false;
        kingWin = false;
        firstPlayerTurn = false;
    }
    /*
    A function that initializes all the pieces on board, with their starting positions.
    The function also calls another function which attaches each piece to its owner (Player 1/2).
     */
    public void InitialSetup() {
        clearPlayerPiecesArrays();      //Clears both players pieces arrays from previous games.
        clearStepsBoard();              //Clears the board which counts the number of steps each position has.
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
        InitializeStepsBoard();
        numOfMoves = 0;
    }

    /*
    This function adds each piece on the board to its owner.
    Each player has an array containing all of its pieces.
     */
    private void attachPiecesToPlayer() {
        for (int i = 0; i < getBoardSize(); i++) {
            for (int j = 0; j < getBoardSize(); j++) {
                Position position = new Position(i,j);
                if (getConcretePieceAtPosition(position) != null) {
                    if (getConcretePieceAtPosition(position).getOwner().isPlayerOne()) {
                        _firstPlayer.addPieceToStack(getConcretePieceAtPosition(position));
                    }
                    else {
                        _secondPlayer.addPieceToStack(getConcretePieceAtPosition(position));
                    }
                }
            }
        }
    }
    /*
    This function clears both player arrays of pieces.
    Is used to start a game from zero.
    */
    private void clearPlayerPiecesArrays() {
        _firstPlayer.emptyPiecesArray();
        _secondPlayer.emptyPiecesArray();
    }
    /*
    This function clears the board which counts the number of steps each position has by creating a new board.
     */
    public void clearStepsBoard()
    {
        numOfStepsBoard = new Position[BOARD_SIZE][BOARD_SIZE];
    }
    /*
    Initialize the board which counts the number of steps for each position.
     */
    private void InitializeStepsBoard()
    {
        for (int i = 0; i < numOfStepsBoard.length; i++) {
            for (int j = 0; j < numOfStepsBoard.length; j++) {
                Position position = new Position(i, j);
                numOfStepsBoard[i][j] = new Position(i,j);   //Adds the corresponding position for each cell in the board.
                if (getPieceAtPosition(position) instanceof ConcretePiece) {
                    //Add each piece to its location at the beginning of the game.
                    numOfStepsBoard[i][j].addPieceToArray(getConcretePieceAtPosition(position));
                }
            }
        }
    }


    @Override
    public boolean move(Position a, Position b) {
        if (getConcretePieceAtPosition(a) == null) {
            return false;
        }
        if (getConcretePieceAtPosition(a).getOwner().isPlayerOne() &&  isSecondPlayerTurn()) {
            return false;
        }
        if (!isSecondPlayerTurn() && !getConcretePieceAtPosition(a).getOwner().isPlayerOne()) {
            return false;
        }
        if (!(legalMove(a, b))) {
            return false;
        }
        if (!(SpaceIsClear(a, b))) {
            return false;
        }
        if (getConcretePieceAtPosition(a) instanceof Pawn && Position.isCorner(b)) {
            return false;
        }
        undoStack.add(a);
        currentPosition.add(b);
        _board[a.getX()][a.getY()].setPosition(b);
        _board[b.getX()][b.getY()] = _board[a.getX()][a.getY()];
        _board[a.getX()][a.getY()] = null;
        addPositionToPieceArray(b);
        numOfStepsBoard[b.getX()][b.getY()].addPieceToArray(_board[b.getX()][b.getY()]);
        numOfMoves++;
        boolean aKillWasMade = eat((ConcretePiece) getPieceAtPosition(b));
        if (!aKillWasMade) {
            killedPiecesStack.add(new Pawn_Killed(_board[b.getX()][b.getY()], false, null,null,null, null));
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
    The function checks if the piece in the given position is player one's piece or the second player's piece.
    Afterward, it goes through the array of pieces inside the player.
    Until reaching the current piece (their names are the same).
    Then, the function adds the position to that piece's position array.
    */
    private void addPositionToPieceArray(Position b) {
        int x = b.getX();
        int y = b.getY();
        if (_board[x][y].getOwner().isPlayerOne()) {
            int i = 0;
            while (!_firstPlayer.getPieceFromArray(i).getPiecePosition().equals(_board[x][y].getPiecePosition())) {
                i++;
            }
            _firstPlayer.getPieceFromArray(i).addPositionToArray(b);
        }
        if (!_board[x][y].getOwner().isPlayerOne()) {
            int j = 0;
            while (!_secondPlayer.getPieceFromArray(j).getPiecePosition().equals(_board[x][y].getPiecePosition())) {
                j++;
            }
            _secondPlayer.getPieceFromArray(j).addPositionToArray(b);
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
        if (b.getX() > getBoardSize() || b.getY() > getBoardSize() || b.getX() < 0 || b.getY() < 0) {
            return false;
        }
        if ((a.getX() != b.getX()) && (a.getY() != b.getY())) { //Checks if the move is not only horizontal or only vertical.
            return false;
        }
        return getPieceAtPosition(b) == null;   //If position b has a piece on it, it is not a legal position to move to.
    }
    /*
    This function checks if all cells (the road) between position 'a' to position 'b' are clear.
    If the road is not clear, the function will return false.
    Else, the function returns true.
    This function is used to check if a given piece at position 'a' is able to move to position 'b' according
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
        //If the function did not return false until reaching here. The space between position a and position b is clear.
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
    This function returns the 'ConcretePiece' at a given position by referring to the board at that position
    Is used mainly where a 'ConcretePiece' is necessary, not just 'Piece' for activating functions on the piece.
     */
    public ConcretePiece getConcretePieceAtPosition(Position position)
    {
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
    /*
    This function returns true if the game is finished.
    The game is finished only if the king was captured or the king reached one of the corners of the board.
    The function adds a win to the player who won and updates each player's boolean value of "Won Last Game".
     */
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
        return kingCaptured || kingWin;
    }
    /*
    This function prints end game statistics according to the second part of the assignment.
    After each part of printing there will be 75 '*'.
     */
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
        //If the first player did not win the last game, the second player's array will be printed first.
        for (int i = 0; i < _secondPlayer.getArrayLength(); i++) {
            if (_secondPlayer.getPieceFromArray(i).getNumOfMoves() > 1) {
                System.out.print(_secondPlayer.getPieceFromArray(i).getPiecePosition() + ": ");
                System.out.print(_secondPlayer.getPieceFromArray(i).MovesToString());
                System.out.println();
            }
        }
        if (!_firstPlayer.getWonLastGame()) {       //Will be printed only if the second player won the last game.
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
        //MergedPlayersArray will have both player pieces merged into one array.
        for (int i = 0; i < _firstPlayer.getArrayLength(); i++) {
            MergedPlayersArray.add(_firstPlayer.getPieceFromArray(i));
        }
        for (int i = 0; i < _secondPlayer.getArrayLength(); i++) {
            MergedPlayersArray.add(_secondPlayer.getPieceFromArray(i));
        }
        Q2_2Sort(MergedPlayersArray);
        for (ConcretePiece piece : MergedPlayersArray) {
            if (piece.getNumberOfKills() > 0) { //Only pieces who have kills will be printed (kills > 0)
                System.out.print(piece.getPiecePosition() + ": ");
                System.out.println((piece).getNumberOfKills()+ " kills");
            }
        }
        System.out.println("***************************************************************************");
       //Q2_3
        Q2_3Sort(MergedPlayersArray);   //Uses the merged pieces array and sort it according to the assignment.
        for (ConcretePiece piece : MergedPlayersArray) {
            if (piece.getDistance() > 0) {  //Prints only pieces who moved in the game (squares moved > 0)
                System.out.println(piece.getPiecePosition() + ": " + piece.getDistance() + " squares");
            }
        }
        System.out.println("***************************************************************************");
        //Q2_4
        //calcNumOfSteps();
        ArrayList<Position> arr = Q2_4Sort();
        for (int i = 0; i < arr.size(); i++) {
            System.out.println(arr.get(i).toString() + arr.get(i).numOfSteps() + " pieces");
        }
        System.out.println("***************************************************************************");
    }
    /*
    This function sorts the array of ConcretePieces according to the Q2_2.
     */
    public void Q2_2Sort(ArrayList<ConcretePiece> array) {
        Comparator<ConcretePiece> KillComp = (o1, o2) -> {
            if (o1.getNumberOfKills() == o2.getNumberOfKills())
            {
                if (o1.getPieceNumber() == o2.getPieceNumber())
                {
                    //Will be sorted according to the piece owner
                    //only if the number of kills and the piece numbers are identical.
                    return -1 * Boolean.compare(o1.getConcreteOwner().getWonLastGame(), o2.getConcreteOwner().getWonLastGame());
                }
                //Will be sorted according to their unique number only if both pieces have the same number of kills.
                return Integer.compare(o1.getPieceNumber(), o2.getPieceNumber());
            }
            //Will be sorted on reverse according to the number of kills if both piece kills are not identical.
            return -1 * Integer.compare(o1.getNumberOfKills(), o2.getNumberOfKills());
        };
        array.sort(KillComp);   //Sorting the given array using the KillComp comparator.
    }
    /*
    This function sorts the array of ConcretePieces according to the Q2_3.
     */
    public void Q2_3Sort(ArrayList<ConcretePiece> arr) {
        Comparator<ConcretePiece> DistanceComp = new Comparator<ConcretePiece>() {
            @Override
            public int compare(ConcretePiece o1, ConcretePiece o2) {
                if (o1.getDistance() == o2.getDistance()) {
                    if (o1.getPieceNumber() == o2.getPieceNumber()) {
                        //Will be sorted according to who won the last game only
                        // if the distance passed by both players and the unique piece numbers are identical.
                        return -1 * Boolean.compare(o1.getConcreteOwner().getWonLastGame(),
                                o2.getConcreteOwner().getWonLastGame());
                    }
                    //Be sorted according to each piece unique number
                    //only if both pieces numbers of squares passed are identical.
                    return Integer.compare(o1.getPieceNumber(), o2.getPieceNumber());
                }
                //Will be sorted reversely according to the number of squares each piece passed
                //If that amount is not the same for both pieces.
                return -1 * Integer.compare(o1.getDistance(), o2.getDistance());
            }
        };
        arr.sort(DistanceComp); //Sorting given array using the DistanceComp comparator.
    }
    /*
    This function sorts the array of ConcretePieces according to the Q2_4.
     */
   public ArrayList<Position> Q2_4Sort()
    {
        ArrayList<Position> arr = new ArrayList<>();
        for (int i = 0; i < numOfStepsBoard.length; i++) {
            for (int j = 0; j < numOfStepsBoard.length; j++) {
                if (numOfStepsBoard[i][j].numOfSteps() > 1)  //Only positions with more than one step will be added
                {
                    arr.add(numOfStepsBoard[i][j]);
                }
            }
        }
        Comparator<Position> comp = new Comparator<>() {
            @Override
            public int compare(Position o1, Position o2) {
                if (o1.numOfSteps() == o2.numOfSteps()) {
                    if (o1.getX() == o2.getX()) {
                        return Integer.compare(o1.getY(), o2.getY());
                    }
                    return Integer.compare(o1.getX(), o2.getX());
                }
                return -1 * Integer.compare(o1.numOfSteps(), o2.numOfSteps());
            }
        };
        arr.sort(comp);
        return arr;
    }
    /*
    This function returns true if it is the second player turn.
     */
    @Override
    public boolean isSecondPlayerTurn() {
        return !firstPlayerTurn;
    }
    /*
    This function resets the game by doing the following:
    - reset the board by removing all of its pieces.
    - Changing both boolean variables linked to capturing the king and first player winning - to false.
        so that a new game could begin without ending immediately.
    - Clearing both stacks that are used for undoing a move.
    - Clearing the stack that contains all the killed pieces (is also used for the undo function).
    - Setting the board for a new game by placing pieces on the board (InitialSetup function).
     */
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
        killedPiecesStack.clear();
    }
    /*
    This function will return true if a piece was eaten by ConcretePiece 'Cpiece'.
    The function parameter is a ConcretePiece that made the last move.
    The function checks if ConcretePiece 'Cpiece' should eat a surrounding piece.
     */
    private boolean eat(ConcretePiece Cpiece) {
        int y = Cpiece.getPosition().getY();
        int x = Cpiece.getPosition().getX();
        int i = 0;
        int numOfKills = 0;
        boolean firstPlayer;
        Pawn p1 = null;
        Pawn p2 = null;
        Pawn p3 = null;
        Pawn p4 = null;
        killedApiece = false;
        //Reaching the piece from the player's array. Will be used later on the function to add the piece
        //The amount of kills it got on that move.
        if (Cpiece.getOwner().isPlayerOne()) {
            firstPlayer = true;
            while (!_firstPlayer.getPieceFromArray(i).getPiecePosition().equals(Cpiece.getPiecePosition())) {
                i++;
            }
        } else {
            firstPlayer = false;
            while (!_secondPlayer.getPieceFromArray(i).getPiecePosition().equals(Cpiece.getPiecePosition())) {
                i++;
            }
        }
        if (Cpiece instanceof Pawn) {    //If 'Cpiece' is the king, it cannot eat other pieces.
            //Check if he is close to the borders, so that only one piece is required to eat.
            //Also check if the piece owners are not the same and if the eaten piece is a pawn (because a king has
            // other parameters to be eaten).
            if (x == 1 && _board[x - 1][y] instanceof Pawn &&
                    !(Cpiece.getOwner().equals(_board[x - 1][y].getOwner()))) {
                //For each piece that would get ate, we would add it to a stack of 'Pawn_Killed' object, which has all
                //the pawns each piece has ate for a given move.
                p1 = new Pawn((Pawn) _board[x-1][y]);
                _board[x - 1][y] = null;    //Removing the killed piece from the board.
                killedApiece = true;        //Will be used later to add a kill to 'Cpiece' later.
                numOfKills++;  //Adds a kill to the counter. Will later be added to the piece 'kills' data member.
            }
            if (x == BOARD_SIZE - 2 && _board[x + 1][y] instanceof Pawn
                    && !(Cpiece.getOwner().equals(_board[x + 1][y].getOwner()))) {
                p2 = new Pawn((Pawn) _board[x+1][y]);
                _board[x + 1][y] = null;
                killedApiece = true;
                numOfKills++;
            }
            if (y == BOARD_SIZE - 2 && _board[x][y + 1] instanceof Pawn
                    && !(Cpiece.getOwner().equals(_board[x][y + 1].getOwner()))) {
                p3 = new Pawn((Pawn) _board[x][y+1]);
                _board[x][y + 1] = null;
                killedApiece = true;
                numOfKills++;
            }
            if (y == 1 && _board[x][y - 1] instanceof Pawn
                    && !(Cpiece.getOwner().equals(_board[x][y - 1].getOwner()))) {
                p4 = new Pawn((Pawn) _board[x][y-1]);
                _board[x][y - 1] = null;
                killedApiece = true;
                numOfKills++;
            }
            //Check his surroundings to see if he got another piece from the same player in the other side of the
            //opposing piece.
            if (x + 2 < BOARD_SIZE) {
                if (_board[x + 1][y] instanceof Pawn && _board[x + 2][y] instanceof Pawn) {
                    if (!_board[x + 1][y].getOwner().equals(_board[x][y].getOwner())
                            && _board[x][y].getOwner().equals(_board[x + 2][y].getOwner())) {
                        p1 = new Pawn((Pawn) _board[x+1][y]);
                        _board[x + 1][y] = null;
                        killedApiece = true;
                        numOfKills++;
                    }
                }
            }
            if (x - 2 >= 0) {
                if (_board[x - 1][y] instanceof Pawn && _board[x - 2][y] instanceof Pawn) {
                    if (!_board[x - 1][y].getOwner().equals(_board[x][y].getOwner())
                            && _board[x][y].getOwner().equals(_board[x - 2][y].getOwner())) {
                        p2 = new Pawn((Pawn) _board[x-1][y]);
                        _board[x - 1][y] = null;
                        killedApiece = true;
                        numOfKills++;
                    }
                }
            }
            if (y + 2 < BOARD_SIZE) {
                if (_board[x][y + 1] instanceof Pawn && _board[x][y + 2] instanceof Pawn) {
                    if (!_board[x][y + 1].getOwner().equals(_board[x][y].getOwner())
                            && _board[x][y].getOwner().equals(_board[x][y + 2].getOwner())) {
                        p3 = new Pawn((Pawn) _board[x][y+1]);
                        _board[x][y + 1] = null;
                        killedApiece = true;
                        numOfKills++;
                    }
                }
            }
            if (y - 2 >= 0) {
                if (_board[x][y - 1] instanceof Pawn && _board[x][y - 2] instanceof Pawn) {
                    if (!_board[x][y - 1].getOwner().equals(_board[x][y].getOwner())
                            && _board[x][y].getOwner().equals(_board[x][y - 2].getOwner())) {
                        p4 = new Pawn((Pawn) _board[x][y-1]);
                        _board[x][y - 1] = null;
                        killedApiece = true;
                        numOfKills++;
                    }
                }
            }
            if (killedApiece)
            {
                Pawn_Killed pawn = new Pawn_Killed(Cpiece,true,p1,p2,p3,p4);
                killedPiecesStack.add(pawn);
            }
            eatKing(Cpiece);    //Calls a function that checks if the king was eaten that turn.
            killedApiece = false;
            if (firstPlayer) {
                _firstPlayer.getPieceFromArray(i).addKills(numOfKills);   //Adds a kill to the piece who killed.
            } else {
                _secondPlayer.getPieceFromArray(i).addKills(numOfKills);
            }
            return true;    //Returning true because a piece was eaten.
        }
        return false;       //Returning false because no piece was eaten.
    }

    /*
    This function check if a king was eaten after ConcretePiece 'piece' made a move.
    The function has 4 boolean flags. All 4 of them need to be true in order for the king to be eaten.
    If the king is near one of the borders. The flag of the border that the king is close to will be true
    - so that only 3 flags will need to be true.
    The function checks all 4 directions of 'piece' for the king. If the king is found in one of the directions,
    the function will check the king's surroundings to see if it is captured.
     */
    public void eatKing(ConcretePiece piece) {
        int x = piece.getPosition().getX();
        int y = piece.getPosition().getY();
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
                    flag1 = true;   //If x + 2 > BOARD_SIZE, the king is near the boarder. only 3 flags needed.
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
                kingCaptured = flag1 && flag2 && flag3 && flag4;    //kingCaptured will be true only if all 4 flags are true.
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
                if (kingCaptured) {
                    killedApiece = true;
                }
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
                if (kingCaptured) {
                    killedApiece = true;
                }
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
                if (kingCaptured) {
                    killedApiece = true;
                }
            }
        }
    }

    /*
    This function returns true only if one of the borders of the board contains the king.
     */
    private boolean kingInCorner() {
        return _board[0][0] instanceof King || _board[0][BOARD_SIZE - 1] instanceof King
                || _board[BOARD_SIZE - 1][0] instanceof King || _board[BOARD_SIZE - 1][BOARD_SIZE - 1] instanceof King;
    }
    /*
    This function is used to undo the last move made.
    Can go as long as the start of the game.
    It Will be used when the "Undo" button is pressed.
     */
    @Override
    public void undoLastMove() {
        if (!undoStack.isEmpty() && !currentPosition.isEmpty()) {
            Position recentPos = new Position(undoStack.pop());
            Position currentPos = new Position(currentPosition.pop());
            removeLastPositionFromMovesArray(currentPos); //Removes the last position of the piece from its moves array.
            //Put the piece at its last position.

            //After removing the position from the position array of the piece.
            //We want to see if it stepped there more than once.
            //We will do so by checking if his array of moves still has that position.
            //If it does, that means he stepped there more than once, and we should not remove him from the array of
            //pieces who stepped on that position.
            if (!getConcretePieceAtPosition(currentPos).steppedThere(currentPos)) {
                numOfStepsBoard[currentPos.getX()][currentPos.getY()].removePieceFromArray(getConcretePieceAtPosition(currentPos));
            }
            _board[recentPos.getX()][recentPos.getY()] = _board[currentPos.getX()][currentPos.getY()];
            //Remove the piece from its current position.
            _board[currentPos.getX()][currentPos.getY()] = null;
            numOfMoves--;

            //If in the last move a piece was killed, it should be brought back.
            if (!killedPiecesStack.isEmpty()) {
                Pawn_Killed maybeKilledPiece = killedPiecesStack.pop();
                if (maybeKilledPiece.gotKilled()) {   //If the last piece from the array was killed, it will be brought back.
                    //Retrieving the position which the piece was last located

                    //Checking which of the surrounding pieces was killed and should be brought back.
                    if (maybeKilledPiece.getPiece1() != null)
                    {
                        Position pos = new Position(maybeKilledPiece.getPiece1().getPosition());
                        _board[pos.getX()][pos.getY()] = (ConcretePiece) maybeKilledPiece.getPiece1();
                    }
                    if (maybeKilledPiece.getPiece2() != null)
                    {
                        Position pos = new Position(maybeKilledPiece.getPiece2().getPosition());
                        _board[pos.getX()][pos.getY()] = (ConcretePiece) maybeKilledPiece.getPiece2();
                    }
                    if (maybeKilledPiece.getPiece3() != null)
                    {
                        Position pos = new Position(maybeKilledPiece.getPiece3().getPosition());
                        _board[pos.getX()][pos.getY()] = (ConcretePiece) maybeKilledPiece.getPiece3();
                    }
                    if (maybeKilledPiece.getPiece4() != null)
                    {
                        Position pos = new Position(maybeKilledPiece.getPiece4().getPosition());
                        _board[pos.getX()][pos.getY()] = (ConcretePiece) maybeKilledPiece.getPiece4();
                    }
                    _board[recentPos.getX()][recentPos.getY()].undoKill();  //Removes 1 kill from the kill counter of that piece.
                }
            }
            //Setting the turn back to the last player.
            firstPlayerTurn = !firstPlayerTurn;
        }
    }
    /*
    This function removes the last position from the piece's move array.
    The function first finds the owner of the piece which is located at position 'p'.
    Then the function reaches the corresponding piece from the player pieces array (by checking if its String of player
    symbol (A/D/K) + unique number is identical.
    Afterward, the function removes the last move from the array.
     */
    private void removeLastPositionFromMovesArray(Position p) {
        if(getConcretePieceAtPosition(p) == null)
        {
            return;
        }
        if (getConcretePieceAtPosition(p).getOwner().isPlayerOne()) {
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
    /*
    This function returns the board size.
     */
    @Override
    public int getBoardSize() {
        return BOARD_SIZE;
    }
    /*
    This function returns the board size. But it is static.
    Is used in the position class.
     */
    public static int getBoardSizeStatic() {
        return BOARD_SIZE;
    }
}
