package chess.piece;
import chess.move.*;
public class Rook extends Piece {
    public Rook(Color color) {
        super(color);
    }
    void initSymbol() {
        symbol = 'R';
    }
    void initName() {
        name = "Rook";
    }
    void initMoves() {
        moves.add(new StraightMove());
    }
}
