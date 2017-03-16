package chess;
import chess.piece.*;
import javax.swing.*;
import java.awt.*;

public class ChessGamePanel extends JPanel {
    ChessGame game = null;
    ChessBoardPanel bp = null;
    ChessBoardMovesPanel cbmp = null;
    public ChessGamePanel() {
        //super(new BorderLayout());
        game = new ChessGame();
        bp = new ChessBoardPanel(game);
        cbmp = new ChessBoardMovesPanel(game);
        add(bp);
        add(cbmp);
    }
}
