package com.chess.piece;
import com.chess.move.*;

public class King extends Piece {
    public King(Color color) {
        super(color);
    }
    void initSymbol() {
        encoding = '\u265A';
        symbol = 'K';
    }
    void initName() {
        name = "King";
    }
    void initMoves() {
        addMove(new SingleStraightMove());
        addMove(new SingleDiagonalMove());
        addMove(new CastleMove(0));
        addMove(new CastleMove(2));
    }
}
