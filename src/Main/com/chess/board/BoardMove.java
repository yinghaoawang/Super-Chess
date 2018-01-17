package com.chess.board;
import com.chess.util.Utilities;
import com.chess.piece.Piece;

import java.awt.*;

/* Documents the history of a chess game. Holds which piece moved from source coordinate to destination coordinate */
public class BoardMove {
    Piece piece, victim;
    int srcRow, srcCol, destRow, destCol;
    String specialMoveName = null;
    public BoardMove(Piece piece, Piece victim, int srcRow, int srcCol, int destRow, int destCol, String specialMoveName) {
        this.piece = piece;
        this.srcRow = srcRow;
        this.srcCol = srcCol;
        this.destRow = destRow;
        this.destCol = destCol;
        this.specialMoveName = specialMoveName;
        this.victim = victim;
    }
    public BoardMove(Piece piece, Piece victim, int srcRow, int srcCol, int destRow, int destCol) {
        this(piece, victim, srcRow, srcCol, destRow, destCol, null);
    }
    public BoardMove(Piece piece, Piece victim, Point src, Point dest) {
        this(piece, victim, src.x, src.y, dest.x, dest.y);
    }
    public Piece getPiece() { return piece; }
    public Point getSrcCoord() { return new Point(srcRow, srcCol); }
    public Point getDestCoord() { return new Point(destRow, destCol); }
    public int getSrcRow() { return srcRow; }
    public int getSrcCol() { return srcCol; }
    public int getDestRow() { return destRow; }
    public int getDestCol() { return destCol; }
    public String getSpecialMoveName() { return specialMoveName; }
    public Piece getVictimPiece() { return victim; }
    public String toString() {
        return piece.getDisplaySequence() + Utilities.colToChar(srcCol) + Utilities.rowToChar(srcRow) + " "
            + Utilities.colToChar(destCol) + Utilities.rowToChar(destRow);
    }
}
