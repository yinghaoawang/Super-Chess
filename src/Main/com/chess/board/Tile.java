package com.chess.board;
import com.chess.util.Utilities;
import com.chess.piece.Piece;
import java.util.List;
import java.util.LinkedList;

/* The tile of a chess board. Has a color (represented by a nested enum), and has
 * lists of pieces such that they are able to stack on top of each other */
public class Tile {
    public enum Color {
        WHITE, BLACK
    }

    // fields
    private List<Piece> pieces = new LinkedList<>();
    private Color color;

    // constructor
    public Tile() {
        this(Color.WHITE);
    }
    public Tile(Color color) {
        this.color = color;
    }

    // getters
    public boolean isBlack() { return color == Color.BLACK; }
    public boolean isWhite() { return color == Color.WHITE; }
    public boolean isEmpty() { return pieces.size() == 0; }
    public int size() { return pieces.size(); }
    public List<Piece> getPieces() { return pieces; }

    // setters
    public void setColor(Color color) { this.color = color; }

    /* list operations */

    // returns the piece at given index
    public Piece get(int index) { return pieces.get(index); }

    // gets front of tile's pieces
    public Piece peek() {
        if (pieces.size() <= 0) return null;
        return (Piece)((LinkedList)pieces).peek();
    }

    // pushes piece to front of pieces
    public void push(Piece piece) {
        ((LinkedList<Piece>)pieces).push(piece);
    }
    // adds a piece to designated index
    public void add(int index, Piece piece) { ((LinkedList<Piece>)pieces).add(index, piece);}

    // remove and returns the piece in front of tile or specified
    public Piece remove() { return remove(0); }
    public Piece remove(Piece piece) {
        return remove(indexOf(piece));
    }
    public Piece remove(int index) {
        Piece res = null;
        try {
            res = pieces.remove(index);
        } catch (Exception e) { Utilities.printException(e); }
        return res;
    }

    // get the index of a piece if it exists in the tile
    public int indexOf(Piece piece) { return pieces.indexOf(piece); }

    // get the index of a piece of the same specialMoveName as the passed piece's specialMoveName, -1 if not found
    public int indexOfName(Piece piece) {
        if (piece == null) return -1;
        return indexOfName(piece.getName());
    }

    // get the index of a piece of the same specialMoveName as the passed specialMoveName, -1 if not found
    public int indexOfName(String name) {
        for (int i = 0; i < pieces.size(); ++i) {
            if (pieces.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    // determines if the tile contains a piece
    public boolean contains(Piece piece) {
        return indexOf(piece) != -1;
    }
    public boolean contains(String name, Piece.Color color) {
        int index = indexOfName(name);
        if (index == -1) return false;
        return pieces.get(index).getColor() == color;
    }
}
