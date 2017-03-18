package chess;
import chess.BoardMove;
import chess.Utilities;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/* This panel is for display the list of move made on a board. It passes
 * in a ChessBoardPanel in order to interact with its mouse events */
public class ChessBoardMovesPanel extends JPanel {
    // fields
    int fontSize = 12;
    ChessGame game = null;
    JTextArea boardMovesTextArea = null;

    // constructor
    public ChessBoardMovesPanel(ChessGame game, ChessBoardPanel boardPanel) {
        this.game = game;
        init();
        initEventListeners(boardPanel);
    }

    // initialization invoker
    void init() {
        initBoardMoves();
    }

    // creates board moves
    void initBoardMoves() {
        boardMovesTextArea = new JTextArea(26, 8);
        boardMovesTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(boardMovesTextArea);
        //scrollPane.setViewportBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane);
    }

    // adds event listener(s), passes in a board panel to get when mouse is released on the board
    public void initEventListeners(ChessBoardPanel boardPanel) {
        boardPanel.addMouseListener(new MouseAdapter() {
            // when mouse is released, update board moves panel
            @Override
            public void mouseReleased(MouseEvent me) {
                super.mouseReleased(me);
                update();
            }
        });
    }

    // have the board move text area display the board moves
    void update() {
        boardMovesTextArea.setFont(new Font("Arial", Font.PLAIN, fontSize));
        List<BoardMove> boardMoves = game.boardMoves;
        if (boardMovesTextArea == null) return;
        boardMovesTextArea.setText("");
        if (boardMoves == null) return;
        int i = 1;
        for (BoardMove bm: boardMoves) {
            boardMovesTextArea.append(i++ + " " + bm + "\n");
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(150, 425);
    }
}
