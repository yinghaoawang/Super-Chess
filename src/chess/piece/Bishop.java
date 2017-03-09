package chess.piece;
import chess.move.*;
public class Bishop extends Piece {
    public Bishop(Color color) {
        super(color);
    }
    void initSymbol() {
        symbol = 'B';
    }
    void initName() {
        name = "Bishop";
    }
    void initMoves() {
        moves.add(new DiagonalMove());
    }
}
