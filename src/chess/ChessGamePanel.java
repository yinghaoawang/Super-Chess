package chess;
import chess.piece.*;
import javax.swing.*;
import java.awt.*;

public class ChessGamePanel extends JPanel {
    ChessGame game = null;
    ChessBoardPanel boardPanel = null;
    ChessBoardMovesPanel boardMovesPanel = null;
    public ChessGamePanel() {
        FlowLayout layout = new FlowLayout(FlowLayout.LEADING, 20, 10);
        game = new ChessGame();
        boardPanel = new ChessBoardPanel(game);
        boardMovesPanel = new ChessBoardMovesPanel(game, boardPanel);
        add(boardPanel);
        add(boardMovesPanel);
    }
}
