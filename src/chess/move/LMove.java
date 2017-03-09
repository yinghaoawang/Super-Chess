package chess.move;
public class LMove extends Move {
    public LMove() {
        attacking = true;
        rowMove = 2;
        colMove = 1;
        transposed = true;
        setQuadrants(true, true, true, true);
    }
}
