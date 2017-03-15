package chess.piece;
import chess.move.*;
public class Rook extends Piece {
    public Rook(Color color) {
        super(color);
    }
    void initSymbol() {
        if (color == Color.WHITE)
            encoding = '\u2656';
        else if (color == Color.BLACK)
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
