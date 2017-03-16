package chess;
import chess.Utilities;
import chess.piece.Piece;

/* Documents the history of a chess game. Holds which piece moved from which position to which */
class BoardMove {
    Piece piece;
    int srcRow, srcCol, destRow, destCol;
    BoardMove(Piece piece, int srcRow, int srcCol, int destRow, int destCol) {
        this.piece = piece;
        this.srcRow = srcRow;
        this.srcCol = srcCol;
        this.destRow = destRow;
        this.destCol = destCol;
    }
    public String toString() {
        return piece.getDisplaySequence() + Utilities.colToChar(srcCol) + Utilities.rowToChar(srcRow) + " "
            + Utilities.colToChar(destCol) + Utilities.rowToChar(destRow);
    }
}
