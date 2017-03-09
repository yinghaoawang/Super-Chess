package chess.piece;
import chess.move.*;
public class King extends Piece {
    public King(Color color) {
        super(color);
    }
    void initSymbol() {
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
