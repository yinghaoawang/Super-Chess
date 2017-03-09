package chess.piece;
import chess.move.*;
public class Pawn extends Piece {
    public Pawn(Color color) { super(color); }
    void initSymbol() { symbol = 'P'; }
    void initName() { name = "Pawn"; }
    void initMoves() {
        // TODO this
        moves.add(new SingleStraightMove());
    }
}
