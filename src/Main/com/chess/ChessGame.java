package com.chess;

import com.chess.board.*;
import com.chess.move.*;
import com.chess.piece.*;
import com.chess.util.Utilities;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;



/* The logic of this program. It holds pieces together all the pieces
 * that include the moves, pieces, board, board moves, graves, and players */
public class ChessGame {
    public enum State {
        NIL, WHITEWIN, BLACKWIN, DRAW
    }
    State state = State.NIL;

    private Board board = null;
    private TileCollection tiles = null;
    private int rows, cols;

    private Tile selectedTile = null;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private TileMoveList selectedMoveTiles = null;
    private List<BoardMove> boardMoves = null;

    private String message = "";

    // player
    private Piece.Color[] playerColor = new Piece.Color[] { Piece.Color.WHITE, Piece.Color.BLACK };

    // grave
    private List<List<Piece>> grave = new LinkedList<>();
    private int currentPlayerIndex = 0;

    public ChessGame() {
        rows = cols = 8; // a chessboard has 8 rows and 8 cols
        board = new Board(rows);
        tiles = board.getTiles();
        init();
    }

    // getters
    public int getCols() { return this.cols; }
    public int getRows() { return this.rows; }
    public Board getBoard() { return board; }
    public Tile getSelectedTile() { return selectedTile; }
    public List<List<Piece>> getGrave() { return grave; }
    public List<BoardMove> getBoardMoves() { return boardMoves; }
    public String getMessage() { return message; }

    // on to the next player
    public void nextPlayer() {
        int index = currentPlayerIndex;
        if (++index >= playerColor.length)
            index = 0;
        toPlayer(index);

    }
    public void prevPlayer() {
        int index = currentPlayerIndex;
        if (--index < 0) index = playerColor.length - 1;
        toPlayer(index);
    }
    public void toPlayer(int index) {
        currentPlayerIndex = index;

        updateState();
    }

    public void updateState() {
        Piece.Color currColor = playerColor[currentPlayerIndex];
        String strColor = "White";
        if (currColor == Piece.Color.BLACK) strColor = "Black";
        if (isColorCheckmate(currColor)) {
            message = strColor + " is in checkmate.";
            if (strColor == "Black") state = State.WHITEWIN;
            else state = State.BLACKWIN;
        } else if (isColorCheck(currColor)) {
            message = strColor + " is in check.";
        } else if (isDraw()) {
            state = State.DRAW;
            message = "Draw.";
        } else {
            message = strColor + "'s move.";
        }
    }

    // gets the corresponding index for grave
    int getGraveIndex(Piece.Color color) {
        for (int i = 0; i < playerColor.length; ++i) {
            if (playerColor[i] == color) return i;
        }
        System.err.println("Color does not exist in player/grave index");
        return -1;
    }

    // puts piece into matching player's grave
    public void putToGrave(Piece piece) {
        Tile tile = board.findTile(piece);
        for (int i = 0; i < playerColor.length; ++i) {
            if (playerColor[i] == piece.getColor()) {
                grave.get(i).add(tile.remove(piece));
                break;
            }
        }
    }

    // finds piece in graveyard, removes it from graveyard and returns it
    public Piece takeFromGrave(Piece piece) {
        int graveIndex = getGraveIndex(piece.getColor());
        for (Piece p : grave.get(graveIndex)) {
            if (p == piece) {
                grave.get(graveIndex).remove(p);
                return p;
            }
        }
        return null;
    }
    public Piece takeFromGrave(String name, Piece.Color color) {
        int graveIndex = getGraveIndex(color);
        for (Piece p : grave.get(graveIndex)) {
            if (p.getName() == name) {
                grave.get(graveIndex).remove(p);
                return p;
            }
        }
        return null;
    }


    // returns a Tile List of all the possible moves on the board of the selected piece
    public TileMoveList getPieceMoveTiles(int row, int col) {
        Tile tile = tiles.get(row, col);
        Piece piece = tile.peek();
        if (piece == null) return null;
        return getPieceMoveTiles(piece);
    }
    public TileMoveList getPieceMoveTiles(Piece piece) {
        TileMoveList res = new TileMoveList();
        try {
            boolean[] allPathBlockedQuadrants = new boolean[] { false, false, false, false };
            for (Move move: piece.getMoves()) {
                boolean[] blockedQuadrants = new boolean[] { false, false, false, false };
                if (move.isAllPath()) blockedQuadrants = allPathBlockedQuadrants;
                // multiplier is needed for the pieces that move until it reaches corner of map (each iteration is for row/col increment)
                int multiplier = 1;
                do {
                    addMoveQuadrants(res, piece, move, multiplier, blockedQuadrants);
                    if (move.isTransposed()) addMoveQuadrants(res, piece, move.toTransposed(), multiplier, blockedQuadrants);
                } while (move.isUntilEnd() && multiplier++ < Math.max(rows, cols)); // only when piece moves until edge does this loop
            }
        } catch(Exception e) { Utilities.printException(e); }
        return res;
    }

    // addMoveQuadrants adds to the Tile List res the respective moves (helper for getTargetMoveTiles)
    private void addMoveQuadrants(TileMoveList res, Piece piece, Move move, int multiplier, boolean[] blockedQuadrants) {
        // the tile and coords of the given piece
        Tile pTile = board.findTile(piece);
        Point pCoord = board.findCoord(piece);

        // no moves if the tile could not be found
        if (pTile == null || pCoord == null) {
            return;
        }

        // used for converting direction of each quadrant for movement
        Point[] signConversion = new Point[] {
                new Point(multiplier, multiplier), // quadrant 1 (0)
                new Point(multiplier, -1 * multiplier), // quadrant 2 (1)
                new Point(-1 * multiplier, -1 * multiplier), // quadrant 3 (2)
                new Point(-1 * multiplier, multiplier) // quadrant 4 (3)
        };

        // changes direction of moves for different quadrants and gets the tiles that the piece can move to
        boolean[] quadrants = move.getQuadrants();
        for (int i = 0; i < quadrants.length; ++i) {
            // pre
            if (quadrants[i] == false) continue;
            if (move.isBlockable() && blockedQuadrants[i]) continue;

            int destRow, destCol, rowMove, colMove;
            rowMove = move.getRowMove();
            colMove = move.getColMove();

            if (Move.swapConversion[i] == true) {
                int tmp = rowMove;
                rowMove = colMove;
                colMove = tmp;
            }
            rowMove *= signConversion[i].x; // x is row, y is col
            colMove *= signConversion[i].y;

            destRow = rowMove + pCoord.x;
            destCol = colMove + pCoord.y;

            // TODO RULES
            // post
            if (!board.isWithinBorders(destRow, destCol)) continue; // make sure tile is within board borders

            Tile destTile = tiles.get(destRow, destCol);

            // set the quadrant blocked if is blocked
            if (move.isBlockable() && destTile.peek() != null) {
                blockedQuadrants[i] = true;
            }

            if (!move.isAttacking() && (destTile.peek() != null)) continue; // if cannot attack, make sure there is no piece on tile

            if (move.isAttackToMove() && (destTile.peek() == null || // if attackToMove, make sure there is a piece to attack
                    destTile.peek().getColor() == pTile.peek().getColor()))
                continue;

            if (!move.isTeamAttacking() && (destTile.peek() != null && // unless it is team attacking, it cannot move to tile with same color piece
                    destTile.peek().getColor() == pTile.peek().getColor()))
                continue;

            if (move.isFirstMove() && piece.moveCount != 0) continue;

            if (!res.contains(destTile)) res.add(new TileMove(destTile, move.getSpecialMoveName()));
        }
    }

    // action for mouse presses a tile
    public void tilePressed(int row, int col) {
        Tile destTile = tiles.get(row, col);
        if (selectedMoveTiles != null && selectedMoveTiles.contains(destTile) &&
                selectedTile != null && selectedTile != destTile) {

            Piece destPiece = destTile.peek();
            Piece selectedPiece = selectedTile.peek();
            Piece victimPiece = destPiece;
            if (destPiece != null) putToGrave(destPiece);

            if (tileIsSpecialMove(destTile)) {
                victimPiece = executeSpecialMove(selectedPiece, destTile);
            } else {
                board.movePiece(selectedPiece, destTile);
            }



            ++selectedPiece.moveCount;
            boardMoves.add(new BoardMove(selectedPiece, victimPiece, board.findCoord(selectedTile), board.findCoord(destTile), getSpecialMoveName(destTile)));

            // checks if a pawn reaches the end of the board, then you "should choose a piece" but i'm just giving a queen for now
            // todo: allow choice in pawn crossing
            // todo: change boardmove to proper move name on pawn crossing
            if (selectedPiece.getName() == "Pawn" && isEndOfBoard(selectedPiece.getColor(), row)) {
                board.removePiece(row, col);
                board.addPiece(copyPieceTypeTo(selectedPiece, "Queen"), row, col);
            }

            nextPlayer();
        }

        // if there is no selected tile on a click, then you select the tile. deselect if you do have a selected tile
        if (selectedTile == null && destTile.peek() != null) {
            selectTile(row, col);
        } else {
            deselectTile();
        }
    }

    private Piece copyPieceTypeTo(Piece piece, String newPieceTypeName) {
        Piece newPiece;
        if (newPieceTypeName == "Queen") {
            newPiece = new Queen(piece.getColor());
        }
        // todo: the rest of this
        else {
            System.err.print("Cannot change piece to unknown type name: " + newPieceTypeName);
            return null;
        }
        newPiece.moveCount = piece.moveCount;
        return newPiece;
    }

    private boolean isEndOfBoard(Piece.Color color, int moveRow) {
        if (color == Piece.Color.BLACK) {
            if (moveRow == 0) return true;
            return false;
        } else {
            if (moveRow == getRows() - 1) return true;
            return false;
        }
    }

    // set selected tile to designated coordinate
    private void selectTile(int row, int col) {
        selectedTile = tiles.get(row, col);
        Piece piece = selectedTile.peek();

        // find and set all possible moves from all pieces for the current player
        if (state != State.DRAW) selectedMoveTiles = getPossibleMoves(piece, playerColor[currentPlayerIndex]);

        selectedRow = row;
        selectedCol = col;
    }

    // what to do when deselecting a tile
    private void deselectTile() {
        selectedTile = null;
        selectedRow = -1;
        selectedCol = -1;
        selectedMoveTiles = null;
    }

    // Gets the possible tiles a piece can move to
    private TileMoveList getPossibleMoves(Piece piece) {
        return getPossibleMoves(piece, piece.getColor());
    }
    private TileMoveList getPossibleMoves(Piece piece, Piece.Color color) {
        TileMoveList possibleMoves = new TileMoveList();
        Tile pTile = board.findTile(piece);

        if (piece != null && piece.getColor() == color) {
            possibleMoves = getPieceMoveTiles(piece);
            TileMoveList specialMoves = possibleMoves.getSpecialTileMoves();
            TileMoveList regularMoves = possibleMoves.getRegularTileMoves();

            // make sure special moves are doable (handles king safety)
            for (TileMove tm : specialMoves) {
                if (!canHandleSpecialMove(tm, piece)) possibleMoves.remove(tm);
            }

            // makes sure that when moving with regular moves, the king is not endangered
            for (int i = 0; i < regularMoves.size(); ++i) {
                Tile t = regularMoves.get(i).tile;
                // let them try moves temporarily to see if it protects king
                board.movePiece(piece, t);

                if (isColorCheck(color)) { // if it does not protect king, then remove it from possible moves
                    possibleMoves.remove(t);
                }

                // move piece back to original spot
                board.movePiece(piece, pTile);
            }

        }
        return possibleMoves;
    }

    // determine if the piece can make a special move
    private boolean canHandleSpecialMove(TileMove tm, Piece piece) {
        if (tm.specialMoveName == "Castle") {
            return canHandleCastling(tm, piece);
        } else if (tm.specialMoveName == "En Passant") {
            return canHandleEnPassant(tm, piece);
        }
        return false;
    }

    // determine if piece can castle
    private boolean canHandleCastling(TileMove tm, Piece piece) {
        // king must have never moved to castle
        if (piece.moveCount != 0) return false;

        // the tile and coords of the given piece
        Tile pTile = board.findTile(piece);
        Point pCoord = board.findCoord(piece);
        Point tCoord = board.findCoord(tm.tile);

        // no moves if the tile could not be found
        if (pTile == null || pCoord == null) {
            return false;
        }

        // set corresponding variables
        int pRow = pCoord.x;
        int pCol = pCoord.y;
        int tRow = tCoord.x;
        int tCol = tCoord.y;

        // make sure they're aligned horizontally (same row)
        if (pRow != tRow) return false;

        // make sure there is enough space to put rook behind the queen
        if (Math.abs(pCol - tCol) < 2) return false;

        // boolean to check if target tile is on left of king tile
        boolean leftOf = tCol < pCol;

        // loops thru left or right of piece depending on where target tile is
        //Piece rook = null;
        int delta = 1;
        if (leftOf) delta = -1;
        for (int i = pCol, iteration = 0; (leftOf && i >= 0) || (!leftOf && i < cols); i += delta, ++iteration) {
            /*
            if (i == pCol) {
                continue;
            }
            */
            Tile tile = board.findTile(pRow, i);
            // if tile is not occupied
            if (tile.isEmpty()) {
                // tile must be safe if queen is to walk on it
                if ((leftOf && i > 0) || (!leftOf && i < cols - 1)){
                    if (isInDanger(tile, piece.getColor())) return false;
                }
                // otherwise continue onto next time
                continue;
            }
            // if tile is occupied and
            // if the last piece occupying is not a rook, then cannot castle
            Piece currPiece = tile.peek();
            if ((leftOf && i == 0) || (!leftOf && i == cols - 1)) {
                if (currPiece.getName() != "Rook") return false;

                // make sure rook has space to castle to
                if (iteration < 2) return false;

                // the rook needs to have never moved
                if (currPiece.moveCount != 0) return false;
            }

        }

        /* Should not be needed since rooks are on corner of map, and castling implies the 2 spaces on side of king are safe
        // check if queen is in danger after castling
        boolean kingDanger = false;
        Tile rookTile = board.findTile(rook);
        board.movePiece(rook, board.findTile(pRow, pCol+delta));
        board.movePiece(piece, pTile);
        if (isColorCheck(piece.getColor())) kingDanger = true;
        board.movePiece(rook, rookTile);
        board.movePiece(piece, pTile);

        if (kingDanger) return false;
        */

        // if all the tiles until it encouters a rook are true, then castling is allowed
        return true;
    }

    // determines if piece can en passant
    private boolean canHandleEnPassant(TileMove tm, Piece piece) {
        // must be on 5th rank
        if (getPieceRank(piece) != 5) return false;

        // set vars
        Point tCoord = board.findCoord(tm.tile);
        Point pCoord = board.findCoord(piece);
        int pRow = pCoord.x;
        int pCol = pCoord.y;
        int tRow = tCoord.x;
        int tCol = tCoord.y;

        // find target pawn to capture
        Tile targetTile = board.findTile(pRow, tCol); // gets adjacent tile
        if (targetTile == null || targetTile.isEmpty()) return false;

        Piece targetPiece = targetTile.peek(); // gets target piece
        if (targetPiece.getColor() == piece.getColor()) return false; // cannot eat own piece
        if (targetPiece.getName() != "Pawn") return false; // must be a pawn to eat

        // target pawn must have just moved
        if (boardMoves.isEmpty()) return false;
        BoardMove lastMove = boardMoves.get(boardMoves.size()-1);
        if (lastMove.getPiece() != targetPiece) return false;

        // target pawn must have made a double move
        if (Math.abs(lastMove.getDestRow() - lastMove.getSrcRow()) != 2) return false;

        // make sure king is safe if en passant is made
        boolean kingDanger = false;
        /*
        board.removePiece(targetPiece);
        board.movePiece(piece, targetTile);
        */
        executeEnPassantMove(piece, targetTile);
        BoardMove move = new BoardMove(piece, targetPiece, pRow, pCol, pRow, tCol, tm.specialMoveName);
        if (isColorCheck(piece.getColor())) {
            kingDanger = true;
        }


        // put the pieces back
        /*
        board.movePiece(piece, tm.tile);
        board.addPiece(targetPiece, targetTile);
        */
        undoEnPassantMove(move);
        if (kingDanger) return false;

        return true;
    }

    // get the rank (layman's row) of a piece
    private int getPieceRank(Piece piece) {
        Point coord = board.findCoord(piece);
        if (piece.getColor() == Piece.Color.WHITE) {
            return coord.x + 1;
        } else {
            return rows - coord.x;
        }
    }

    // initializes board by calling other initializers
    void init() {
        initTiles();

        initBoardMoves();
        initGraves();

        newGame();
    }

    public void newGame() {
        initPieces();
        state = State.NIL;
        toPlayer(0);
    }
    public void clear() {
        board.clear();
        boardMoves.clear();
        for (int i = 0; i < grave.size(); ++i) grave.get(i).clear();
    }

    // creates graveyard list for pieces
    void initGraves() {
        // create grave piece list
        for (int i = 0; i < playerColor.length; ++i) grave.add(new ArrayList<Piece>());
    }

    // places pieces on the chess board as they should be
    void initPieces() {
        board.addPiece(new Rook(Piece.Color.WHITE), 0, 0);
        board.addPiece(new Knight(Piece.Color.WHITE), 0, 1);
        board.addPiece(new Bishop(Piece.Color.WHITE), 0, 2);
        board.addPiece(new Queen(Piece.Color.WHITE), 0, 3);
        board.addPiece(new King(Piece.Color.WHITE), 0, 4);
        board.addPiece(new Bishop(Piece.Color.WHITE), 0, 5);
        board.addPiece(new Knight(Piece.Color.WHITE), 0, 6);
        board.addPiece(new Rook(Piece.Color.WHITE), 0, 7);
        for (int i = 0; i < 8; ++i) {
            board.addPiece(new Pawn(Piece.Color.WHITE), 1, i);
        }

        //board.addPiece(new Pawn(Piece.Color.WHITE), 5, 3);


        //board.addPiece(new Rook(Piece.Color.WHITE), 4, 4);
        /*
        board.addPiece(new Queen(Piece.Color.BLACK), 4, 7);
        board.addPiece(new Bishop(Piece.Color.BLACK), 3, 7);
        board.addPiece(new Pawn(Piece.Color.BLACK), 3, 5);
        */

        board.addPiece(new Rook(Piece.Color.BLACK), 7, 0);
        board.addPiece(new Knight(Piece.Color.BLACK), 7, 1);
        board.addPiece(new Bishop(Piece.Color.BLACK), 7, 2);
        board.addPiece(new Queen(Piece.Color.BLACK), 7, 3);
        board.addPiece(new King(Piece.Color.BLACK), 7, 4);
        board.addPiece(new Bishop(Piece.Color.BLACK), 7, 5);
        board.addPiece(new Knight(Piece.Color.BLACK), 7, 6);
        board.addPiece(new Rook(Piece.Color.BLACK), 7, 7);
        for (int i = 0; i < 8; ++i) {
            board.addPiece(new Pawn(Piece.Color.BLACK), 6, i);
        }
    }

    // create text areas for board moves
    void initBoardMoves() {
        boardMoves = new ArrayList<>();
    }

    // initializes and colors the tiles as a chessboard design
    void initTiles() {
        // begin first tile as black
        boolean blackRow = true;
        boolean blackCol = true;

        // go through the board
        for (int i = 0; i < rows; ++i) {
            if (blackRow == true) {
                blackCol = true;
            } else {
                blackCol = false;
            }
            for (int j = 0; j < cols ; ++j) {
                Tile t = tiles.get(i, j);
                if (blackCol) {
                    t = new Tile(Tile.Color.BLACK);
                } else {
                    t = new Tile(Tile.Color.WHITE);
                }
                blackCol = !blackCol;
            }
            blackRow = !blackRow;
        }
    }

    // determines if the tile is a tile in selectedMoveTiles
    public boolean isInMoveTiles(Tile tile) {
        if (selectedMoveTiles == null) return false;

        for (TileMove tm: selectedMoveTiles) {
            if (tm.tile == tile) return true;
        }
        return false;
    }

    // determines if a tile is a special move tile
    private boolean tileIsSpecialMove(Tile tile) {
        TileMoveList specialMoves = selectedMoveTiles.getSpecialTileMoves();
        return specialMoves.contains(tile);
    }

    // executes special move if it has one (assuming all prerequsites are met, will not make any checks)
    private Piece executeSpecialMove(Piece piece, Tile tile) {
        TileMoveList specialMoves = selectedMoveTiles.getSpecialTileMoves();
        TileMove tm = specialMoves.get(tile);
        String name = tm.specialMoveName;
        if (name == "Castle") return executeCastleMove(piece, tile);
        else if (name == "En Passant") return executeEnPassantMove(piece, tile);
        return null;
    }

    private String getSpecialMoveName(Tile tile) {
        TileMoveList specialMoves = selectedMoveTiles.getSpecialTileMoves();
        TileMove tm = specialMoves.get(tile);
        if (tm == null) return null;
        return tm.specialMoveName;
    }

    private void undoSpecialMove(BoardMove move) {
        String specialName = move.getSpecialMoveName();
        if (specialName == "Castle") {
            undoCastleMove(move);
        } else if (specialName == "En Passant") {
            undoEnPassantMove(move);
        }
    }

    // assuming can execute castle move, will move rook behind king, and move king 2 steps
    private Piece executeCastleMove(Piece piece, Tile tile) {
        Point pCoord = board.findCoord(piece);
        Point tCoord = board.findCoord(tile);

        int pRow = pCoord.x;
        int pCol = pCoord.y;
        int tRow = tCoord.x;
        int tCol = tCoord.y;

        boolean leftOf = tCol < pCol;
        int delta = 1;
        if (leftOf) delta = -1;
        for (int i = pCol; (leftOf && i >= 0) || (!leftOf && i < cols); i += delta) {
            if (i == pCol) continue;
            Tile currTile = board.findTile(pRow, i);
            if (currTile.isEmpty()) continue;
            // if tile is not empty, then it must be a rook, so do castle
            // move rook
            board.movePiece(currTile.peek(), pRow, pCol + delta);
            // move king
            board.movePiece(piece, tile);
        }
        return null;
    }

    // undo the previous move if it is a castling move
    private void undoCastleMove(BoardMove move) {
        Piece king = move.getPiece();
        // col dir the rook is in in respect to the king after castling, 1 if king moved to left, -1 if king moved to right
        boolean rookLeftOf = move.getDestCol() > move.getSrcCol();
        int rookDir = rookLeftOf ? -1 : 1;
        int rookCol = move.getDestCol() + rookDir;
        Piece rook = board.findPiece(move.getDestRow(), rookCol);
        assert(rook != null);
        board.movePiece(king, move.getSrcRow(), move.getSrcCol());
        if (rookLeftOf) {
            board.movePiece(rook, move.getSrcRow(), cols - 1);
        } else {
            board.movePiece(rook, move.getSrcRow(), 0);
        }
    }

    // assuming can execute en passant move, will kill target piece, and move current pawn to diagonal
    private Piece executeEnPassantMove(Piece piece, Tile tile) {
        Point pCoord = board.findCoord(piece);
        Point tCoord = board.findCoord(tile);

        int pRow = pCoord.x;
        int pCol = pCoord.y;
        int tRow = tCoord.x;
        int tCol = tCoord.y;

        Tile targetTile = board.findTile(pRow, tCol);
        Piece targetPiece = targetTile.peek();
        putToGrave(targetPiece);
        board.movePiece(piece, tile);
        return targetPiece;
    }

    // undoes last move
    private void undoMove(BoardMove move) {
        --move.getPiece().moveCount;
        // undo special move
        if (move.getSpecialMoveName() != null) {
            undoSpecialMove(move);
            return;
        }
        // undo regular move
        Piece piece = move.getPiece();
        board.movePiece(piece, move.getSrcRow(), move.getSrcCol());
        Piece victim = move.getVictimPiece();
        if (victim == null) return;
        takeFromGrave(victim);
        board.addPiece(victim, move.getDestRow(), move.getDestCol());
    }

    // public function for undoing last move (removes from board moves)
    public void undoLastMove() {
        if (boardMoves.size() == 0) return;
        BoardMove lastMove = boardMoves.get(boardMoves.size() - 1);
        undoMove(lastMove);
        boardMoves.remove(lastMove);
        prevPlayer();
    }

    // undoes last move if it is an enpassant move
    private void undoEnPassantMove(BoardMove move) {
        Piece killer = move.getPiece();
        Piece victim = takeFromGrave(move.getVictimPiece());
        board.removePiece(killer);
        board.addPiece(killer, move.getSrcRow(), move.getSrcCol());
        board.addPiece(victim, move.getSrcRow(), move.getDestCol());
    }

    // determine if the game is in a draw
    public boolean isDraw() {
        // stalemate (if the player on turn has no legal move but is not in check, this is stalemate and the game is automatically a draw.)
        Piece.Color color = playerColor[currentPlayerIndex];
        if ((getPossibleMoveCount(color) == 0) && !isColorCheck(color)) {
            return true;
        }

        // threefold repetition
        for (int i = boardMoves.size() - 1; i >= 4 && i > boardMoves.size() - 5; --i) {
            BoardMove move = boardMoves.get(i);
            BoardMove prev = boardMoves.get(i - 4);


            // 3 fold repetition involves matching moves 4 moves ago
            if (move.getVictimPiece() != null) break; // eating does not count
            if (move.getSrcRow() != prev.getSrcRow()) break;
            if (move.getSrcCol() != prev.getSrcCol()) break;
            if (move.getDestRow() != prev.getDestRow()) break;
            if (move.getDestCol() != prev.getDestCol()) break;

            // if past 4th iteration, is 3fold rep
            if (i == boardMoves.size() - 4) return true;
        }
        System.out.println();

        // no capture or pawn move (50 move rule)
        /*
        for (int i = boardMoves.size() - 1; i >= 0 && i > boardMoves.size() - 51; --i) {
            BoardMove move = boardMoves.get(i);
            if (move.getPiece().getName() == "Pawn") break;
            if (move.getVictimPiece() != null) break;

            if (i == boardMoves.size() - 50) return true;
        }
        */


        /* // TODO? (not completely sure about the draw)
        // impossible checkmate
        if (boardOnlyHas(new String[] {"King"})) return true;
        if (boardOnlyHas(new String[] {"King", "Knight"}, new String[] {"King"})) return true;
        if (boardOnlyHas(new String[] {"King", "Bishop"}, new String[] {"King"})) return true;
        // if (king +bishsamecolor v king+bishsamecolor) return true;
        */

        // mutual agreement draw (TODO?)

        return false;
    }

    // checks if the current color player is in checkmate
    public boolean isColorCheckmate(Piece.Color color) {
        return (getPossibleMoveCount(color) == 0) && isColorCheck(color);
    }

    public int getPossibleMoveCount(Piece.Color color) {
        int count = 0;
        for (Tile tile : tiles) {
            if (tile.isEmpty()) continue;
            if (!tile.peek().getColor().equals(color)) continue;
            int possibleMoveCount = getPossibleMoves(tile.peek()).size();
            count += possibleMoveCount;
        }
        return count;
    }

    // determines if white or black king(s) is in danger
    public boolean isAnyCheck() {
        return isWhiteCheck() || isBlackCheck();
    }
    // determines if white king is in danger
    public boolean isWhiteCheck() {
        return isColorCheck(Piece.Color.WHITE);
    }
    // determines if black king is in danger
    public boolean isBlackCheck() {
        return isColorCheck(Piece.Color.BLACK);
    }
    // determines if a king of a certain color is in danger
    public boolean isColorCheck(Piece.Color color) {
        return isAnyInDanger("King", color);
    }

    // determines if a piece can be eaten by any piece on the boards
    public boolean isInDanger(Piece piece) {
        Tile target = board.findTile(piece);
        if (target == null) return false;
        for (Tile t : tiles) {
            if (t == target || t.isEmpty()) continue;
            Piece p = t.peek();
            for (TileMove tm : getPieceMoveTiles(p)) {
                if (tm.tile == target) return true;
            }
        }
        return false;
    }

    // checks if tile can be eaten (ignores the parameter color pieces)
    public boolean isInDanger(Tile tile, Piece.Color color) {
        if (tile == null) return false;
        for (Tile t : tiles) {
            if (t == tile || t.isEmpty()) continue;
            Piece p = t.peek();
            // piece of same color cannot eat the tile
            if (p.getColor() == color) continue;
            for (TileMove tm : getPieceMoveTiles(p)) {
                if (tm.tile == tile) return true;
            }
        }
        return false;
    }

    // determines if any piece of a certain type and color can be eaten by any piece on the boards
    public boolean isAnyInDanger(String name, Piece.Color color) {
        List<Tile> targets = board.findTiles(name, color);
        if (targets.isEmpty()) return false;

        for (Tile target : targets) {
            if (target == null) continue;
            for (Tile t : tiles) {
                if (t == target || t.isEmpty()) continue;
                Piece p = t.peek();
                for (TileMove tm : getPieceMoveTiles(p)) {
                    if (tm.tile == target) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
