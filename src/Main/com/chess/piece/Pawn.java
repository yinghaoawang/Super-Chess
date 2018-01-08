package com.chess.piece;
import com.chess.move.*;

// TODO en passant.
public class Pawn extends Piece {
    public Pawn(Color color) { super(color); }
    void initSymbol() {
        encoding = '\u265F';

        symbol = 'P';
        displaySequence = "";
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
        doubleMove.setAllPath(true);

        Move attack = new Move();
        attack.setRowMove(1);
        attack.setColMove(1);
        attack.setQuadrants(true, true, false, false);
        attack.setAttackToMove(true);

        Move ep1 = new EnPassantMove(0);
        Move ep2 = new EnPassantMove(1);

        if (color == Piece.Color.BLACK) {
            move = move.toInvertedQuadrants();
            doubleMove = doubleMove.toInvertedQuadrants();

            attack = attack.toInvertedQuadrants();
            ep1.setQuadrants(new boolean[] { false, false, true, false });
            ep2.setQuadrants(new boolean[] { false, false, false, true });
        }

        addMove(move);
        addMove(attack);
        addMove(doubleMove);
        addMove(ep1);
        addMove(ep2);
    }
}
