package com.chess.board;

import java.util.Iterator;

/* Iterator for 2d array of tiles */
public class TileCollection implements Iterable<Tile> {
    private Tile[][] tiles;
    private int rows, cols;
    public TileCollection(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        tiles = new Tile[rows][cols];
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                tiles[i][j] = new Tile();
            }
        }
    }

    public Tile get(int row, int col) {
        if (row >= this.rows || col >= this.cols || row < 0 || col < 0) return null;
        return tiles[row][col];
    }

    public void set(int row, int col, Tile tile) {
        if (row >= this.rows || col > this.cols || row < 0 || col < 0) return;
        tiles[row][col] = tile;
    }

    @Override
    public Iterator<Tile> iterator() {
        Iterator it = new Iterator() {
            private int i = 0;
            private int j = 0;
            @Override
            public boolean hasNext() {
                return i < rows && j < cols;
            }

            @Override
            public Tile next() {
                if (!hasNext()) return null;
                while (i < rows) {
                    while (j < cols) {
                        Tile t = tiles[i][j];
                        ++j;

                        if (j == cols) {
                            j = 0;
                            ++i;
                        }
                        if (t == null) continue;
                        return t;
                    }
                    ++i;
                }
                return null;
            }
        };
        return it;
    }
}
