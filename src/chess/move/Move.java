package chess.move;
import chess.Utilities;
public class Move implements Cloneable {
    int rowMove;
    int colMove;
    boolean[] quadrants = new boolean[] { false, false, false, false };
    boolean transposed = false; // includes (col, row) move (i.e horse)
    /* TODO allPath is useless atm, thinking of removal */
    boolean allPath = false; // if every space it travels it can move to (no natural chess piece has this)
    boolean blockable = false; // if this piece can be blocked by other pieces (i.e bishop rook, pawn, not horse, etc.)
    boolean untilEnd = false; // continues the trend (i.e bishop, rook, queen, not horse, not pawn, etc.)
    boolean attacking = true; // whether if this move can be used to kill
    boolean attackToMove = false; // wthether the piece can only move here if enemy on here
    boolean teamAttacking = false; // whether this piece can eat teammates
    boolean firstMove = false; // if this rule only applies on first move of a piece (i.e pawn double move and en passant)

    // setters
    public void setRowMove(int rowMove) { this.rowMove = rowMove; }
    public void setColMove(int colMove) { this.colMove = colMove; }
    public void setTransposed(boolean transposed) { this.transposed = transposed; }
    public void setAttackToMove(boolean attackToMove) { this.attackToMove = attackToMove; }
    public void setAttacking(boolean attacking) { this.attacking = attacking; }
    public void setAllPath(boolean allPath) { this.allPath = allPath; }
    public void setBlockable(boolean blockable) { this.blockable = blockable; }
    public void setUntilEnd(boolean untilEnd) { this.untilEnd = untilEnd; }
    public void setFirstMove(boolean firstMove) { this.firstMove = firstMove; }
    public void setQuadrants(boolean one, boolean two, boolean three, boolean four) {
        this.quadrants[0] = one;
        this.quadrants[1] = two;
        this.quadrants[2] = three;
        this.quadrants[3] = four;
    }
    public void setQuadrants(boolean[] quadrants) {
        if (quadrants.length != 4) return;
        setQuadrants(quadrants[0], quadrants[1], quadrants[2], quadrants[3]);
    }

    // getters
    public boolean isTransposed() { return transposed; }
    public boolean isAllPath() { return allPath; }
    public boolean isBlockable() { return blockable; }
    public boolean isUntilEnd() { return untilEnd; }
    public boolean isAttacking() { return attacking; }
    public boolean isAttackToMove() { return attackToMove; }
    public boolean isTeamAttacking() { return teamAttacking; }
    public boolean isFirstMove() { return firstMove; }
    public boolean isInQ1() { return quadrants[0]; }
    public boolean isInQ2() { return quadrants[1]; }
    public boolean isInQ3() { return quadrants[2]; }
    public boolean isInQ4() { return quadrants[3]; }
    public int getRowMove() { return rowMove; }
    public int getColMove() { return colMove; }
    public boolean[] getQuadrants() { return quadrants.clone(); }
    public Move toTransposed() {
        Move transMove = null;
        try {
            transMove = (Move)this.clone();
        } catch (Exception e) { Utilities.printException(e); }
        transMove.rowMove = colMove;
        transMove.colMove = rowMove;
        return transMove;
    }
    public Move toInvertedQuadrants() {
        Move invertQuadMove = null;
        try {
            invertQuadMove = (Move)this.clone();
        } catch (Exception e) { Utilities.printException(e); }
        invertQuadMove.setQuadrants(
            quadrants[2],
            quadrants[3],
            quadrants[0],
            quadrants[1]
        );
        return invertQuadMove;
    }
}
