package chess.move;
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
