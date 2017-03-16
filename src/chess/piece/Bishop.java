package chess.piece;
import chess.move.*;

public class Bishop extends Piece {
    public Bishop(Color color) {
        super(color);
    }
    void initSymbol() {
        encoding = '\u265D';
        symbol = 'B';
    }
    void initName() {
        name = "Bishop";
    }
    void initMoves() {
        addMove(new DiagonalMove());
    }
}
