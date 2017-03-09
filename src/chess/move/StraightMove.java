package chess.move;
public class StraightMove extends Move {
    public StraightMove() {
        attacking = true;
        rowMove = 1;
        colMove = 0;
        untilEnd = true;
        setQuadrants(true, true, true, true);
    }
}
