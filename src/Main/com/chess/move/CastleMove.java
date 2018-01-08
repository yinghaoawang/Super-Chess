package com.chess.move;

public class CastleMove extends Move {
    public CastleMove(int quadrant) {
        specialMoveName = "Castle";
        colMove = 2;
        blockable = true;
        boolean[] quadrants = { false, false, false, false };
        quadrants[quadrant] = true;
        setQuadrants(quadrants);
    }
}
