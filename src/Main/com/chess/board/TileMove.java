package com.chess.board;

public class TileMove {
    public Tile tile;
    public String specialMoveName = null;
    public TileMove(Tile tile) {
        this.tile = tile;
    }
    public TileMove(Tile tile, String specialMoveName) {
        this.tile = tile;
        this.specialMoveName = specialMoveName;
    }
}
