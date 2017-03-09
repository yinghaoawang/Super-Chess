package chess.piece;
import chess.move.*;
public class Queen extends Piece {
    public Queen(Color color) {
        super(color);
    }
    void initSymbol() {
        symbol = 'Q';
    }
    void initName() {
        name = "Queen";
    }
    void initMoves() {
        moves.add(new StraightMove());
        moves.add(new DiagonalMove());
    }
}
