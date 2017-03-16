package chess.move;

/* A constant straight move of all quadrants; a rook move in chess */
public class StraightMove extends Move {
    public StraightMove() {
        attacking = true;
        rowMove = 1;
        colMove = 0;
        blockable = true;
        untilEnd = true;
        setQuadrants(true, true, true, true);
    }
}
