package com.chess.board;

import java.util.ArrayList;

public class TileMoveList extends ArrayList<TileMove> {
    public boolean remove(Tile tile) {
        for (TileMove tm : this) {
            if (tm.tile == tile) return remove(tm);
        }
        return false;
    }
    public boolean add(Tile tile) {
        return add(new TileMove(tile));
    }
}
