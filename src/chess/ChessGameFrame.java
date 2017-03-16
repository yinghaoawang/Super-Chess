package chess;
import chess.piece.*;
import javax.swing.JFrame;

public class ChessGameFrame extends JFrame {
    ChessGame game;
    public ChessGameFrame() {
        initUI();
    }

    private void initUI() {
        game = new ChessGame();
        ChessBoardPanel boardPanel = new ChessBoardPanel(game);

        add(boardPanel);
        setSize(600, 700);
        setTitle("Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // places the game in the middle of computer window
    }
}
