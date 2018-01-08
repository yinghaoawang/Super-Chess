package com.chess.move;

public class EnPassantMove extends Move {
    public EnPassantMove(int quadrant) {
        specialMoveName = "En Passant";
        rowMove = 1;
        colMove = 1;
        boolean[] quadrants = { false, false, false, false };
        quadrants[quadrant] = true;
        setQuadrants(quadrants);
    }
}
