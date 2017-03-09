package chess.move;
public class Move {
    int rowMove;
    int colMove;
    boolean[] quadrants = new boolean[] { false, false, false, false };
    boolean transposed = false; // includes (col, row) move (i.e horse)
    boolean allPath = false; // if every space it travels it can move to (no natural chess piece has this)
    boolean blockable = false; // if this piece can be blocked by other pieces (i.e bishop rook, pawn, not horse, etc.)
    boolean untilEnd = false; // continues the trend (i.e bishop, rook, queen, not horse, not pawn, etc.)
    boolean attacking = false; // whether if this move can be used to kill

    // setters
    public void setRowMove(int rowMove) { this.rowMove = rowMove; }
    public void setColMove(int colMove) { this.colMove = colMove; }
    public void setTransposed(boolean transposed) { this.transposed = transposed; }
    public void setAllPath(boolean allPath) { this.allPath = allPath; }
    public void setBlockable(boolean blockable) { this.blockable = blockable; }
    public void setUntilEnd(boolean untilEnd) { this.untilEnd = untilEnd; }
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
    public boolean isInQ1() { return quadrants[0]; }
    public boolean isInQ2() { return quadrants[1]; }
    public boolean isInQ3() { return quadrants[2]; }
    public boolean isInQ4() { return quadrants[4]; }
}
