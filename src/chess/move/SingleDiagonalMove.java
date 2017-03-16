package chess.move;

/* A single diagonal move of 1 in every direction. Like a chess King's diagonal move */
public class SingleDiagonalMove extends DiagonalMove {
    public SingleDiagonalMove() {
        untilEnd = false;
    }
}
