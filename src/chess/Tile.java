package chess;
import chess.piece.Piece;
import java.util.List;
import java.util.LinkedList;

/* The tile of a chess board. Has a color (represented by a nested enum), and has
 * lists of pieces such that they are able to stack on top of each other */
class Tile {
    enum Color {
        WHITE, BLACK
    }

    // fields
    List<Piece> pieces = new LinkedList<>();
    Color color;

    // constructor
    Tile() {
        this(Color.WHITE);
    }
    Tile(Color color) {
        this.color = color;
    }

    // getters
    boolean isBlack() { return color == Color.BLACK; }
    boolean isWhite() { return color == Color.WHITE; }
    boolean isEmpty() { return pieces.size() == 0; }
    int size() { return pieces.size(); }

    // list operations

    // returns the piece at given index
    Piece get(int index) { return pieces.get(index); }

    // uses queue's peek
    Piece peek() {
        if (pieces.size() <= 0) return null;
        return (Piece)((LinkedList)pieces).peek();
    }

    // uses linked list's pop
    Piece pop() throws Exception {
        if (peek() == null) throw new Exception("No pieces on tile");
        return (Piece)((LinkedList<Piece>)pieces).pop();
    }

    // uses linked list's push
    void push(Piece piece) {
        ((LinkedList<Piece>)pieces).push(piece);
    }

    // remove and returns the piece in the tile
    Piece remove() { return remove(0); }
    Piece remove(Piece piece) {
        return remove(indexOf(piece));
    }
    Piece remove(int index) {
        Piece res = null;
        try {
            res = pieces.remove(index);
        } catch (Exception e) { Utilities.printException(e); }
        return res;
    }

    // get the index of a piece if it exists in the tile
    int indexOf(Piece piece) { return pieces.indexOf(piece); }

    // get the index of a piece of the same name as the passed piece's name, -1 if not found
    int indexOfName(Piece piece) {
        if (piece == null) return -1;
        return indexOfName(piece.getName());
    }

    // get the index of a piece of the same name as the passed name, -1 if not found
    int indexOfName(String name) {
        for (int i = 0; i < pieces.size(); ++i) {
            if (pieces.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }
}
