package chess;
import chess.piece.*;
import chess.Utilities;

/* The setting where pieces interact with each other. Contains methods required to move them around */
public class Board {
    int rows; // rows on board
    int cols; // columns on board

    Tile[][] tiles;

    // constructor that calls init
    public Board(int cols) { this(cols, cols); }
    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        tiles = new Tile[rows][cols];
        init();
    }

    // initiates board by calling other inits
    void init() {
        initTiles();
    }

    // initialize board of empty constructor tiles
    void initTiles() {
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols ; ++j) {
                tiles[i][j] = new Tile();
            }
        }
    }

    // getters
    public Tile[][] getTiles() { return tiles; }
    public int getRows() { return rows; }
    public int getCols() { return cols; }

    // moves piece at top of src to top of dest
    void movePiece(int srcRow, int srcCol, int destRow, int destCol) {
        movePiece(0, srcRow, srcCol, destRow, destCol);
    }
    // overloaded- finds the piece at src with the given name
    void movePiece(String name, int srcRow, int srcCol, int destRow, int destCol) {
        try {
            int index = tiles[srcRow][srcCol].indexOfName(name);
            if (index < 0)
                throw new Exception("Piece with name \"" + name + "\" not on tile");
            movePiece(index, srcRow, srcCol, destRow, destCol);
        } catch (Exception e) {
            Utilities.printException(e);
        }
    }
    // overloaded- finds the piece at src with the index and moves that one
    void movePiece(int index, int srcRow, int srcCol, int destRow, int destCol) {
        try {
            Tile tile = tiles[srcRow][srcCol];
            Tile destTile = tiles[destRow][destCol];

            Piece piece = tile.remove(index);
            destTile.push(piece);
        } catch (Exception e) {
            Utilities.printException(e);
        }
    }

    // puts a piece to the top of the tile
    void addPiece(Piece piece, int row, int col) {
        tiles[row][col].push(piece);
    }

    // removes and returns the piece at the top of the tile
    Piece removePiece(int row, int col) throws Exception {
        return tiles[row][col].pop();
    }

    // helpers, self explanatory names
    public boolean isWithinBorders(int row, int col) {
        if (row >= rows || col >= cols || row < 0 || col < 0) return false;
        return true;
    }
}
