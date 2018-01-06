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

    // list operations

    // returns the piece at given index
    public Piece get(int index) { return pieces.get(index); }

    // uses queue's peek
    public Piece peek() {
        if (pieces.size() <= 0) return null;
        return (Piece)((LinkedList)pieces).peek();
    }

    // uses linked list's pop
    public Piece pop() throws Exception {
        if (peek() == null) throw new Exception("No pieces on tile");
        return (Piece)((LinkedList<Piece>)pieces).pop();
    }

    // uses linked list's push
    public void push(Piece piece) {
        ((LinkedList<Piece>)pieces).push(piece);
    }

    // remove and returns the piece in the tile
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

    // get the index of a piece of the same name as the passed piece's name, -1 if not found
    public int indexOfName(Piece piece) {
        if (piece == null) return -1;
        return indexOfName(piece.getName());
    }

    // get the index of a piece of the same name as the passed name, -1 if not found
    public int indexOfName(String name) {
        for (int i = 0; i < pieces.size(); ++i) {
            if (pieces.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    public List<Piece> getPieces() { return pieces; }

    public boolean contains(Piece piece) {
        return indexOf(piece) != -1;
    }
}
