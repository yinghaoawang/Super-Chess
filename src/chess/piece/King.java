package chess.piece;
import chess.move.*;

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
    }
}
