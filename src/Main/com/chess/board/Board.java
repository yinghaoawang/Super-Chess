package com.chess.board;
import com.chess.util.Utilities;
import com.chess.piece.*;

/* The setting where pieces interact with each other. Contains methods required to move them around */
public class Board {
    private int rows; // rows on board
    private int cols; // columns on board

    private TileCollection tiles;

    // constructor that calls init
    public Board(int cols) { this(cols, cols); }
    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        tiles = new TileCollection(rows, cols);
        init();
    }

    // initiates board by calling other inits
    void init() {
        initTiles();
    }

    // initialize board of empty constructor tiles
    void initTiles() {
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                tiles.set(i, j, new Tile());
            }
        }
    }

    // getters
    public TileCollection getTiles() { return tiles; }
    public int getRows() { return rows; }
    public int getCols() { return cols; }

    // moves piece at top of src to topd of dest
    public void movePiece(int srcRow, int srcCol, int destRow, int destCol) {
        movePiece(0, srcRow, srcCol, destRow, destCol);
    }
    // overloaded- finds the piece at src with the given name
    public void movePiece(String name, int srcRow, int srcCol, int destRow, int destCol) {
        try {
            int index = tiles.get(srcRow, srcCol).indexOfName(name);
            if (index < 0)
                throw new Exception("Piece with name \"" + name + "\" not on tile");
            movePiece(index, srcRow, srcCol, destRow, destCol);
        } catch (Exception e) {
            Utilities.printException(e);
        }
    }

    // finds the tile that contains the parameter piece
    public Tile findTile(Piece piece) {
        for (Tile t : tiles) if (t.contains(piece)) return t;
        return null;
    }
    // overloaded- finds the piece at src with the index and moves that one
    public void movePiece(int index, int srcRow, int srcCol, int destRow, int destCol) {
        try {
            Tile tile = tiles.get(srcRow, srcCol);
            Tile destTile = tiles.get(destRow, destCol);

            Piece piece = tile.remove(index);
            destTile.push(piece);
        } catch (Exception e) {
            Utilities.printException(e);
        }
    }

    // puts a piece to the top of the tile
    public void addPiece(Piece piece, int row, int col) {
        tiles.get(row, col).push(piece);
    }

    // removes and returns the piece at the top of the tile
    public Piece removePiece(int row, int col) throws Exception {
        return tiles.get(row, col).pop();
    }

    // helpers, self explanatory names
    public boolean isWithinBorders(int row, int col) {
        if (row >= rows || col >= cols || row < 0 || col < 0) return false;
        return true;
    }
}
