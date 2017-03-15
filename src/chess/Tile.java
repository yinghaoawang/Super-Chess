package chess;
import chess.piece.Piece;
import java.util.List;
import java.util.LinkedList;

class Tile {
    enum Color {
        WHITE, BLACK
    }
    List<Piece> pieces = new LinkedList<>();
    Color color;
    Tile(Color color) {
        this.color = color;
    }
    boolean isBlack() { return color == Color.BLACK; }
    boolean isWhite() { return color == Color.WHITE; }
    boolean isEmpty() { return pieces.size() == 0; }
    Piece peek() {
        if (pieces.size() <= 0) return null;
        return (Piece)((LinkedList)pieces).peek();
    }
    Piece pop() throws Exception {
        if (peek() == null) throw new Exception("No pieces on tile");
        return (Piece)((LinkedList<Piece>)pieces).pop();
    }
    Piece get(int index) { return pieces.get(index); }
    void push(Piece piece) {
        ((LinkedList<Piece>)pieces).push(piece);
    }
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
    int size() { return pieces.size(); }
    int indexOf(Piece piece) { return pieces.indexOf(piece); }
    int indexOfName(Piece piece) {
        if (piece == null) return -1;
        return indexOfName(piece.getName());
    }
    int indexOfName(String name) {
        for (int i = 0; i < pieces.size(); ++i) {
            if (pieces.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }
}
