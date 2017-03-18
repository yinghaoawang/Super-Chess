package chess;
import javax.swing.*;
import java.awt.Color;

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

        boardPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        boardMovesPanel.setBorder(BorderFactory.createCompoundBorder(
              BorderFactory.createEmptyBorder(15, 0, 0, 0),
              BorderFactory.createLineBorder(Color.black)
            ));
        gravesPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        add(boardPanel);
        add(boardMovesPanel);
        add(gravesPanel);
    }
}
