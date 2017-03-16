package chess.piece;
import chess.move.*;
public class Bishop extends Piece {
    public Bishop(Color color) {
        super(color);
    }
    void initSymbol() {
        //if (color == Color.WHITE)
            //encoding = '\u2657';
        //else if (color == Color.BLACK)
            encoding = '\u265D';

        symbol = 'B';
    }
    void initName() {
        name = "Bishop";
    }
    void initMoves() {
        addMove(new DiagonalMove());
    }
}
