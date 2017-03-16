package chess.piece;
import chess.move.*;

public class Knight extends Piece {
    public Knight(Color color) {
        super(color);
    }
    void initSymbol() {
        encoding = '\u265E';
        symbol = 'N';
    }
    void initName() {
        name = "Knight";
    }
    void initMoves() {
        addMove(new LMove());
    }
}
