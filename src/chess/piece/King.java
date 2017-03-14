package chess.piece;
import chess.move.*;
public class King extends Piece {
    public King(Color color) {
        super(color);
    }
    void initSymbol() {
        if (color == Color.WHITE)
            encoding = '\u2654';
        else if (color == Color.BLACK)
            encoding = '\u265A';

        symbol = 'K';
    }
    void initName() {
        name = "King";
    }
    void initMoves() {
        moves.add(new SingleStraightMove());
        moves.add(new SingleDiagonalMove());
    }
}
