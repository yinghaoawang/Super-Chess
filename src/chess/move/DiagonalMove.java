package chess.move;

/* A constant diagonal move of all quadrants; a bishop move in chess */
public class DiagonalMove extends Move {
    public DiagonalMove() {
        attacking = true;
        rowMove = 1;
        colMove = 1;
        blockable = true;
        untilEnd = true;
        setQuadrants(true, true, true, true);
    }
}
