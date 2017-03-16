package chess.piece;
import chess.move.*;

public class Knight extends Piece {
    public Knight(Color color) {
        super(color);
    }
    void initSymbol() {
        //if (color == Color.WHITE)
            //encoding = '\u2658';
        //else if (color == Color.BLACK)
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
