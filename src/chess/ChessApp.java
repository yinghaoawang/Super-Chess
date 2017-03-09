package chess;
import chess.piece.*;
import javax.swing.JFrame;

public class ChessApp extends JFrame {
    Board board;
    public ChessApp() {
        initUI();
        board.movePiece(0, 2, 2, 0); // move bishop
        board.movePiece(1, 0, 2, 0); // pawn crushes bishop
        board.movePiece(0, 0, 2, 0); // rook crushes pawn
        board.movePiece(1, 2, 0, 5, 0); // bishop escapes
        board.movePiece("Bishop", 2, 0, 3, 0); // pawn escapes
        board.movePiece("Dawn", 2, 0, 3, 0); // invalid name catch
        board.movePiece(-1, 2, 0, 3); // out of bounds error catch
        board.movePiece(3, 3, 0, 3); // out of bounds error catch
        board.selectTile(2, 0);
    }

    private void initUI() {
        board = new Board();
        add(board);
        setSize(600, 600);
        setTitle("Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // places the game in the middle of computer window
    }
}
