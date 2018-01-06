package com.chess.piece;
import com.chess.util.Utilities;
import com.chess.move.*;
import java.util.List;
import java.util.ArrayList;

/* A piece with its attribute. Contains moves that it is able to make. */
public abstract class Piece {
    public enum Color {
        BLACK("Black"), WHITE("White");
        private final String name;
        Color(String name) { this.name = name; }
        public String toString() { return this.name; }
    }

    String name = null;
    char symbol = '\0';
    String displaySequence = null;
    char encoding = '\0';
    public int moveCount = 0;
    Color color;
    List<Move> moves = new ArrayList<>();

    // constructor
    public Piece(Color color) {
        this.color = color;
        init();
    }

    // Getters
    public Color getColor() { return color; }
    public List<Move> getMoves() { return moves; }
    public String getName() { return name; }
    public char getSymbol() { return symbol; }
    public String getDisplaySequence() { if (displaySequence == null) return Character.toString(symbol); return displaySequence; }
    public char getEncoding() { if (encoding == '\0') return symbol; return encoding; }
    public boolean isBlack() { return color == Color.BLACK; }
    public boolean isWhite() { return color == Color.WHITE; }
    // methods to be defined in sublasses
    abstract void initSymbol();
    abstract void initName();
    abstract void initMoves();
    // calls other inits to be defined in subclasses
    void init() {
        initSymbol();
        initName();
        initMoves();
        assertIntegrity();
    }
    // asserts if piece has required variables
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

    @Override
    public String toString() { return color + " " + name; }
}
