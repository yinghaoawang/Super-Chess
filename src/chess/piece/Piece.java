package chess.piece;
import chess.*;
import chess.move.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.*;
import java.awt.geom.*;

/* A piece with its attribute. Contains moves that it is able to make. */
public abstract class Piece {
    public enum Color {
        BLACK("Black"), WHITE("White");
        private final String name;
        private Color(String name) { this.name = name; }
        public String toString() { return this.name; }
    }
    String name = null;
    char symbol = '\0';
    String displaySequence = null;
    char encoding = '\0';
    public int moveCount = 0;
    Color color;
    List<Move> moves = new ArrayList<>();
    public Piece(Color color) {
        this.color = color;
        init();
        //if (this.color == Color.WHITE) symbol = Utilities.charToLowerCase(symbol);
        //else symbol = Utilities.charToUpperCase(symbol);
    }
    public Color getColor() { return color; }
    public List<Move> getMoves() { return moves; }
    public String getName() { return name; }
    public char getSymbol() { return symbol; }
    public String getDisplaySequence() { if (displaySequence == null) return Character.toString(symbol); return displaySequence; }
    public char getEncoding() { if (encoding == '\0') return symbol; return encoding; }
    public String toString() { return color + " " + name; }
    public boolean isBlack() { return color == Color.BLACK; }
    public boolean isWhite() { return color == Color.WHITE; }
    abstract void initSymbol();
    abstract void initName();
    abstract void initMoves();
    void init() {
        initSymbol();
        initName();
        initMoves();
        assertIntegrity();
    }
    void assertIntegrity() {
        if (symbol == '\0') Utilities.printErrorAndExit("Piece has no symbol");
        if (name == null) Utilities.printErrorAndExit("Piece has no name");
        if (moves.size() <= 0) Utilities.printErrorAndExit("Piece has no moves");
        // if (g2d == null) throw new Exception("Piece has no g2d");
    }

    // add move to piece's moves
    protected void addMove(Move move) {
        int rowMove = move.getRowMove();
        int colMove= move.getColMove();
        int i = 0;
        int j = 0;
        Move newMove = null;
        if (move.isAllPath()) {
            while (++i <= rowMove) {
                newMove = new Move(move);
                newMove.setMove(i, j);
                moves.add(newMove);
            }
            i = rowMove;
            while (++j <= colMove) {
                newMove = new Move(move);
                newMove.setMove(i, j);
                moves.add(newMove);
            }
            return;
        }
        // for non all path
        moves.add(move);
    }
}
