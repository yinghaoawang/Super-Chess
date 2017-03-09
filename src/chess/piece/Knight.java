package chess.piece;
import chess.move.*;

public class Knight extends Piece {
    public Knight(Color color) {
        super(color);
    }
    void initSymbol() {
        symbol = 'N';
    }
    void initName() {
        name = "Knight";
    }
    void initMoves() {
        moves.add(new LMove());
    }
}
