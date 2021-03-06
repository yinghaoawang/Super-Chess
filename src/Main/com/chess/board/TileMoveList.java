package com.chess.board;

import java.util.ArrayList;

public class TileMoveList extends ArrayList<TileMove> {
    public boolean remove(Tile tile) {
        for (TileMove tm : this) {
            if (tm.tile == tile) return remove(tm);
        }
        return false;
    }

    /*
    @Override
    public int size() {
        int count = 0;
        for (TileMove tm : this) ++count;
        return count;
    }
    */

    public boolean add(Tile tile) {
        return add(new TileMove(tile));
    }

    public boolean contains(Tile tile) {
        for (TileMove tm : this) {
            if (tm.tile == tile) return true;
        }
        return false;
    }

    public TileMove get(Tile tile) {
        for (TileMove tm :  this) {
            if (tm.tile == tile) return tm;
        }
        return null;
    }

    public TileMoveList getSpecialTileMoves() {
        TileMoveList res = new TileMoveList();
        for (TileMove tm : this) {
            if (tm.specialMoveName != null) res.add(tm);
        }
        return res;
    }

    public TileMoveList getRegularTileMoves() {
        TileMoveList res = new TileMoveList();
        for (TileMove tm : this) {
            if (tm.specialMoveName == null) res.add(tm);
        }
        return res;
    }
}
