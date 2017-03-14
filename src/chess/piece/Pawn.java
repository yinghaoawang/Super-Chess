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
        move.setQuadrants(true, false, false, false);
        move.setAttacking(false);

        Move doubleMove = new Move();
        doubleMove.setRowMove(2);
        doubleMove.setQuadrants(true, false, false, false);
        doubleMove.setBlockable(true);
        doubleMove.setAttacking(false);
        doubleMove.setFirstMove(true);

        Move attack = new Move();
        attack.setRowMove(1);
        attack.setColMove(1);
        attack.setQuadrants(true, true, false, false);
        attack.setAttackToMove(true);

        if (color == Piece.Color.WHITE) {
            move = move.toInvertedQuadrants();
            doubleMove = doubleMove.toInvertedQuadrants();
            attack = attack.toInvertedQuadrants();
        }

        moves.add(move);
        moves.add(attack);
        moves.add(doubleMove);
    }
}
