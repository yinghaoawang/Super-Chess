package chess.piece;
import chess.move.*;
public class Pawn extends Piece {
    public Pawn(Color color) { super(color); }
    void initSymbol() {
        if (color == Color.WHITE)
            encoding = '\u2659';
        else if (color == Color.BLACK)
            encoding = '\u265F';

        symbol = 'P';
    }
    void initName() { name = "Pawn"; }
    void initMoves() {
        Move move = new Move();
        move.setRowMove(1);
        move.setQuadrants(true);
        move.setAttacking(false);
        moves.add(move);

        Move attack = new Move();
        attack.setRowMove(1);
        attack.setColMove(1);
        attack.setQuadrants(true, true);
        attack.setAttackToMove(true);
        moves.add(attack);
    }
}
