package chess;
import javax.swing.*;

/* This panel holds together all the other panels/displays of a
 * single instance of a Chess Game, which is also held by this panel */
public class ChessGamePanel extends JPanel {
    ChessGame game = null;
    ChessBoardPanel boardPanel = null;
    ChessBoardMovesPanel boardMovesPanel = null;
    ChessGravesPanel gravesPanel = null;

    public ChessGamePanel() {
        game = new ChessGame();
        boardPanel = new ChessBoardPanel(game);
        boardMovesPanel = new ChessBoardMovesPanel(game, boardPanel);
        gravesPanel = new ChessGravesPanel(game, boardPanel);
        add(boardPanel);
        add(boardMovesPanel);
        add(gravesPanel);
    }
}
