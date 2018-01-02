package com.chess.piece;
import com.chess.move.*;

public class Rook extends Piece {
    public Rook(Color color) {
        super(color);
    }
    void initSymbol() {
        encoding = '\u265C';
        symbol = 'R';
    }
    void initName() {
        name = "Rook";
    }
    void initMoves() {
        addMove(new StraightMove());
    }
}
