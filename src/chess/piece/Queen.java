package chess.piece;
import chess.move.*;
public class Queen extends Piece {
    public Queen(Color color) {
        super(color);
    }
    void initSymbol() {
        if (color == Color.WHITE)
            encoding = '\u2655';
        else if (color == Color.BLACK)
            encoding = '\u265B';

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
