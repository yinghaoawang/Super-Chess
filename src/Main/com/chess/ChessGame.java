package com.chess;

import com.chess.board.Board;
import com.chess.board.BoardMove;
import com.chess.board.Tile;
import com.chess.board.TileCollection;
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
    private List<Tile> moveTiles = null;
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

    // finds first player with same color as piece, then puts it into that grave
    public void putToGrave(Tile tile, Piece piece) {
        for (int i = 0; i < playerColor.length; ++i) {
            if (playerColor[i] == piece.getColor()) {
                grave.get(i).add(tile.remove(piece));
                break;
            }
        }
    }

    // returns a Tile List of all the possible moves on the board of the selected piece
    public List<Tile> getMoveTiles(int row, int col) {
        List<Tile> res = new ArrayList<Tile>();
        try {
            Tile tile = tiles.get(row, col);
            Piece piece = tile.peek();
            boolean[] allPathBlockedQuadrants = new boolean[] { false, false, false, false };
            for (Move move: piece.getMoves()) {
                int multiplier = 1;
                boolean[] blockedQuadrants = new boolean[] { false, false, false, false };
                if (move.isAllPath()) blockedQuadrants = allPathBlockedQuadrants;
                do {
                    addMoveQuadrants(res, piece, row, col, move, multiplier, blockedQuadrants);
                    if (move.isTransposed()) addMoveQuadrants(res, piece, row, col, move.toTransposed(), multiplier, blockedQuadrants);
                } while (move.isUntilEnd() && multiplier++ < Math.max(rows, cols));
            }
        } catch(Exception e) { Utilities.printException(e); }
        return res;
    }

    // addMoveQuadrants adds to the Tile List res the respective moves a multiplier times its movement
    private void addMoveQuadrants(List<Tile> res, Piece piece, int row, int col, Move move) {
        addMoveQuadrants(res, piece, row, col, move, 1);
    }
    private void addMoveQuadrants(List<Tile> res, Piece piece, int row, int col, Move move, int multiplier) {
        addMoveQuadrants(res, piece, row, col, move, multiplier, new boolean[] { false, false, false, false });
    }
    private void addMoveQuadrants(List<Tile> res, Piece piece, int row, int col, Move move, int multiplier, boolean[] blockedQuadrants) {
        // scale here
        Point[] signConversion = new Point[] {
            new Point(multiplier, multiplier), // quadrant 1 (0)
            new Point(multiplier, -1 * multiplier), // quadrant 2 (1)
            new Point(-1 * multiplier, -1 * multiplier), // quadrant 3 (2)
            new Point(-1 * multiplier, multiplier) // quadrant 4 (3)
        };
        boolean[] swapConversion = new boolean[] {
            false, // quadrant 1
            true, // quadrant 2
            false, // quadrant 3
            true // quadrant 4
        };
        boolean[] quadrants = move.getQuadrants();
        for (int i = 0; i < quadrants.length; ++i) {
            // TODO RULES -- HOW DO I MODULARIZE THIS?!?!
            // pre
            if (quadrants[i] == false) continue;
            if (move.isBlockable() && blockedQuadrants[i]) continue;

            int destRow, destCol, rowMove, colMove;
            rowMove = move.getRowMove();
            colMove = move.getColMove();

            if (swapConversion[i] == true) {
                int tmp = rowMove;
                rowMove = colMove;
                colMove = tmp;
            }
            rowMove *= signConversion[i].x; // not really x, just using point for 2 ints
            colMove *= signConversion[i].y;

            destRow = rowMove + row;
            destCol = colMove + col;

            // TODO RULES
            // post
            if (!board.isWithinBorders(destRow, destCol)) continue; // make sure tile is within board borders

            Tile destTile = tiles.get(destRow, destCol);

            // set the qudrant blocked if is blocked
            if (move.isBlockable() && destTile.peek() != null) {
                blockedQuadrants[i] = true;
            }

            if (!move.isAttacking() && (destTile.peek() != null)) continue; // if cannot attack, make sure there is no piece on tile

            if (move.isAttackToMove() && (destTile.peek() == null || // if attackToMove, make sure there is a piece to attack
                    destTile.peek().getColor() == selectedTile.peek().getColor()))
                continue;

            if (!move.isTeamAttacking() && (destTile.peek() != null && // unless it is team attacking, it cannot move to tile with same color piece
                    destTile.peek().getColor() == selectedTile.peek().getColor()))
                continue;

            if (move.isFirstMove() && piece.moveCount != 0) continue;

            if (!res.contains(destTile)) res.add(destTile);
        }
    }

    // set selected tile to designated coordinate
    private void selectTile(int row, int col) {
        try {
            selectedTile = tiles.get(row, col);
            if (selectedTile.peek() != null && selectedTile.peek().getColor() == playerColor[currentPlayerIndex])
                moveTiles = getMoveTiles(row, col);
            selectedRow = row;
            selectedCol = col;
        } catch (Exception e) {}
    }

    // what to do when deselecting a tile
    private void deselectTile() {
        selectedTile = null;
        selectedRow = -1;
        selectedCol = -1;
        moveTiles = null;
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
        boardMoves = new ArrayList<BoardMove>();
    }
    // action for mouse presses a tile
    public void tilePressed(int row, int col) {
        Tile tile = tiles.get(row, col);
        // if clicked on a move tile, then move the piece and update stuff
        if (moveTiles != null && moveTiles.contains(tile) &&
                selectedTile != null && selectedTile != tile) {

            Piece destPiece = tile.peek();
            if (destPiece != null) putToGrave(tile, destPiece);

            board.movePiece(selectedRow, selectedCol, row, col);

            ++tile.peek().moveCount;
            boardMoves.add(new BoardMove(tile.peek(), selectedRow, selectedCol, row, col));
            nextPlayer();
        }

        // if there is no selected tile on a click, then you select the tile. deselect if you do have a selected tile
        if (selectedTile == null && tile.peek() != null)
            selectTile(row, col);
        else
            deselectTile();
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

    // determines if the tile is a tile in moveTiles
    public boolean isInMoveTiles(Tile tile) {
        if (moveTiles == null) return false;

        for (Tile t: moveTiles) {
            if (t == tile) return true;
        }
        return false;
    }

    private boolean isInCheck(Piece piece) {
        Tile target = board.findTile(piece);
        if (target == null) return false;
        for (Tile t : tiles) {
            if (t == target) continue;
            for (Piece p : t.getPieces()) {
                // TODO asdf
                return true;
            }
        }
        return false;
    }

}
