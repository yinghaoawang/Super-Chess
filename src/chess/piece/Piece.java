package chess.piece;
import chess.*;
import chess.move.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.*;
import java.awt.geom.*;

public abstract class Piece {
    public enum Color {
        BLACK("Black"), WHITE("White");
        private final String name;
        private Color(String name) { this.name = name; }
        public String toString() { return this.name; }
    }
    String name = null;
    char symbol = '\0';
    char encoding = '\0';
    Color color;
    List<Move> moves = new ArrayList<>();
    // Graphics2D g2d;
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
    public char getEncoding() { if (encoding == '\0') return symbol; return encoding; }
    public String toString() { return color + " " + name; }
    // Graphics2D getG2D() { return g2d; }
    public boolean isBlack() { return color == Color.BLACK; }
    public boolean isWhite() { return color == Color.WHITE; }
    abstract void initSymbol();
    abstract void initName();
    abstract void initMoves();
    // abstract void initGraphics();
    void init() {
        initSymbol();
        initName();
        initMoves();
        // initGraphics();
        assertIntegrity();
    }
    void assertIntegrity() {
        if (symbol == '\0') Utilities.printErrorAndExit("Piece has no symbol");
        if (name == null) Utilities.printErrorAndExit("Piece has no name");
        if (moves.size() <= 0) Utilities.printErrorAndExit("Piece has no moves");
        // if (g2d == null) throw new Exception("Piece has no g2d");
    }
}
