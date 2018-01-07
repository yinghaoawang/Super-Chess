package com.chess;

import com.chess.board.*;
import com.chess.move.*;
import com.chess.piece.*;
import com.chess.util.Utilities;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/* The logic of this program. It holds pieces together all the pieces
 * that include the moves, pieces, board, board moves, graves, and players */
public class ChessGame {
    private Board board = null;
    private TileCollection tiles = null;
    private int rows, cols;

    private Tile selectedTile = null;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private List<TileMove> selectedMoveTiles = null;
    private List<BoardMove> boardMoves = null;

    // player
    private Piece.Color[] playerColor = new Piece.Color[] { Piece.Color.WHITE, Piece.Color.BLACK };

    // grave
    private List<List<Piece>> grave = new ArrayList<List<Piece>>();
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

    // on to the next player
    public void nextPlayer() {
        if (++currentPlayerIndex >= playerColor.length)
            currentPlayerIndex = 0;
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
            rowMove *= signConversion[i].x; // not really x, just using point for 2 ints
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

            if (!res.contains(destTile)) res.add(new TileMove(destTile));
        }
    }

    // action for mouse presses a tile
    public void tilePressed(int row, int col) {
        Tile tile = tiles.get(row, col);
        // if clicked on a move tile, then move the piece and update stuff
        if (selectedMoveTiles != null && selectedMoveTilesContains(tile) &&
                selectedTile != null && selectedTile != tile) {


            Piece destPiece = tile.peek();
            if (destPiece != null) putToGrave(destPiece);

            board.movePiece(selectedRow, selectedCol, row, col);

            ++tile.peek().moveCount;
            boardMoves.add(new BoardMove(tile.peek(), selectedRow, selectedCol, row, col));
            nextPlayer();
        }

        // if there is no selected tile on a click, then you select the tile. deselect if you do have a selected tile
        if (selectedTile == null && tile.peek() != null) {
            selectTile(row, col);
        } else {
            deselectTile();
        }
    }

    // set selected tile to designated coordinate
    private void selectTile(int row, int col) {
        selectedTile = tiles.get(row, col);
        Piece piece = selectedTile.peek();
        selectedMoveTiles = getPossibleMoves(piece);

        // TODO ENPASSANT
        selectedRow = row;
        selectedCol = col;
    }

/*
    private List<Move> getCastleMoves(Piece piece) {
        List<Move> res = new ArrayList<>();
        if (piece.moveCount != 0) return res;
        // search horizontally
        Point coord = board.findCoord(piece);
        Piece.Color pColor = piece.getColor();
        int pRow = coord.x;
        int pCol = coord.y;
        // get each tile horizontal to the piece
        for (int i = pCol - 1; i >= 0; --i) {
            Tile tile = board.findTile(pRow, i);
            // if tile is empty, then check if that tile is threatened by enemy (cannot castle if it is)
            if (tile.isEmpty()) {
                if (isInDanger(tile, pColor)) break;
            }
            // if tile is not empty, make sure it meets criteria before adding move
            else {
                System.out.println("asdf");
                Piece tilePiece = tile.peek();
                if (tilePiece.getName() != "Rook") break;
                // if it is a rook, make sure it is not right next to king (space to castle)
                if (i == pCol - 1) break;
                // if it is a rook, make sure it has never moved
                if (tilePiece.moveCount != 0) break;
                // if it meets criteria, then
                res.add(new CastleMove(Math.abs(pCol - i), 3));
            }
        }
        return res;
    }
*/
    // Gets the possible tiles a piece can move to
    private List<TileMove> getPossibleMoves(Piece piece) {
        TileMoveList possibleMoves = new TileMoveList();
        Tile pTile = board.findTile(piece);

        Piece.Color currPlayerColor = playerColor[currentPlayerIndex];

        if (piece != null && piece.getColor() == currPlayerColor) {
            possibleMoves = getPieceMoveTiles(piece);

            // makes sure that when moving, the king is not endangered
            for (int i = 0; i < possibleMoves.size(); ++i) {
                Tile t = possibleMoves.get(i).tile;
                // let them try moves temporarily to see if it protects king
                board.movePiece(piece, t);

                if (isColorCheck(currPlayerColor)) { // if it does not protect king, then remove it from possible moves
                    possibleMoves.remove(t);
                    --i;
                }

                // move piece back to original spot
                board.movePiece(piece, pTile);
            }

        }
        return possibleMoves;
    }

    // what to do when deselecting a tile
    private void deselectTile() {
        selectedTile = null;
        selectedRow = -1;
        selectedCol = -1;
        selectedMoveTiles = null;
    }

    // initiates board by calling other inits
    void init() {
        initTiles();
        initPieces();
        initBoardMoves();
        initGraves();
    }

    void initGraves() {
        // create grave piece list
        for (int i = 0; i < playerColor.length; ++i) grave.add(new ArrayList<Piece>());
    }

    // places pieces on the chess board as they should be
    void initPieces() {
        board.addPiece(new Rook(Piece.Color.WHITE), 0, 0);
       // board.addPiece(new Knight(Piece.Color.WHITE), 0, 1);
        //board.addPiece(new Bishop(Piece.Color.WHITE), 0, 2);
        //board.addPiece(new Queen(Piece.Color.WHITE), 0, 3);
        board.addPiece(new King(Piece.Color.WHITE), 0, 4);
        //board.addPiece(new Bishop(Piece.Color.WHITE), 0, 5);
        //board.addPiece(new Knight(Piece.Color.WHITE), 0, 6);
        board.addPiece(new Rook(Piece.Color.WHITE), 0, 7);
        for (int i = 0; i < 8; ++i) {
            board.addPiece(new Pawn(Piece.Color.WHITE), 1, i);
        }

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

    // check if the tilemoves contain the tile
    boolean selectedMoveTilesContains(Tile tile) {
        for (TileMove tm : selectedMoveTiles) if (tm.tile == tile) return true;
        return false;
    }

    // create text areas for board moves
    void initBoardMoves() {
        boardMoves = new ArrayList<BoardMove>();
    }

    // inits and colors the tiles as a chessboard design
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
