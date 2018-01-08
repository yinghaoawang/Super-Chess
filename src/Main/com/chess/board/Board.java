package com.chess.board;
import com.chess.util.Utilities;
import com.chess.piece.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
        init();
    }

    // initiates board by calling other inits
    void init() {
        initTiles();
    }

    // initialize board of empty constructor tiles
    void initTiles() {
        tiles = new TileCollection(rows, cols);
        // make checkerboard design
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                if ((j + i) % 2 == 0) tiles.get(i, j).setColor(Tile.Color.BLACK);
            }
        }
    }

    // getters
    public TileCollection getTiles() { return tiles; }
    public int getRows() { return rows; }
    public int getCols() { return cols; }

    // moves piece at top of src to top of dest
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
    public void movePiece(Piece piece, int row, int col) {
        Point pCoord = findCoord(piece);
        movePiece(piece.getName(), pCoord.x, pCoord.y, row, col);
    }

    public Piece findPiece(String name, Piece.Color color) {
        for (Tile t : tiles) {
            for (Piece p : t.getPieces()) {
                if (p.getName() == name && p.getColor() == color) return p;
            }
        }
        return null;
    }

    // finds the tile that contains the parameter piece
    public Tile findTile(Piece piece) {
        for (Tile t : tiles) if (t.contains(piece)) return t;
        return null;
    }

    public Tile findTile(int row, int col) {
        return tiles.get(row, col);
    }

    public List<Tile> findTiles(String name, Piece.Color color) {
        List<Tile> res = new ArrayList<>();
        for (Tile t : tiles) if (t.contains(name, color)) res.add(t);
        return res;
    }

    // find the coordinate on the board (row, col)
    public Point findCoord(Piece piece) {
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                if (tiles.get(i, j).contains(piece)) return new Point(i, j);
            }
        }
        return null;
    }
    public Point findCoord(Tile tile) {
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                if (tiles.get(i, j) == tile) return new Point(i, j);
            }
        }
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
    public void movePiece(Piece piece, Tile tile) {
        Tile prevTile = findTile(piece);
        prevTile.remove(piece);
        tile.push(piece);
    }

    // puts a piece to the top of the tile
    public void addPiece(Piece piece, int row, int col) {
        tiles.get(row, col).push(piece);
    }

    // removes and returns the piece at the top of the tile
    public Piece removePiece(int row, int col) throws Exception {
        return tiles.get(row, col).remove();
    }

    // helpers, self explanatory names
    public boolean isWithinBorders(int row, int col) {
        if (row >= rows || col >= cols || row < 0 || col < 0) return false;
        return true;
    }
}
