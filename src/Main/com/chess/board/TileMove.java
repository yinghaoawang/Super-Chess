package com.chess.board;

public class TileMove {
    public Tile tile;
    public String name = null;
    public TileMove(Tile tile) {
        this.tile = tile;
    }
    public TileMove(Tile tile, String name) {
        this.tile = tile;
        this.name = name;
    }
}
