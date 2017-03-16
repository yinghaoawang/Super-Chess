package chess.piece;
import chess.move.*;

public class Queen extends Piece {
    public Queen(Color color) {
        super(color);
    }
    void initSymbol() {
        encoding = '\u265B';
        symbol = 'Q';
    }
    void initName() {
        name = "Queen";
    }
    void initMoves() {
        addMove(new StraightMove());
        addMove(new DiagonalMove());
    }
}
