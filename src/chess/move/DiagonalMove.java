package chess.move;
public class DiagonalMove extends Move {
    public DiagonalMove() {
        attacking = true;
        rowMove = 1;
        colMove = 1;
        untilEnd = true;
        setQuadrants(true, true, true, true);
    }
}
