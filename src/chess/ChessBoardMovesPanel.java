package chess;
import chess.BoardMove;
import chess.piece.*;
import chess.move.*;
import chess.Utilities;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class ChessBoardMovesPanel extends JPanel {
    // fields
    ChessGame game = null;
    JTextArea boardMovesTextArea = null;

    // constructor
    public ChessBoardMovesPanel(ChessGame game) {
        this.game = game;
        init();
    }

    // initialization invoker
    void init() {
        initBoardMoves();
        initEventListeners();
    }

    // creates board moves
    void initBoardMoves() {
        boardMovesTextArea = new JTextArea(20, 100);
        boardMovesTextArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(boardMovesTextArea);
        scroll.setBackground(Color.black);
        //scroll.setBounds(447, 37, 129, 402); // guessing i dont need this if i'm using relative placement
        add(scroll);
    }

    // adds event listener(s)
    void initEventListeners() {
        addMouseListener(new MouseAdapter() {
            // when mouse is released, update board moves panel
            @Override
            public void mouseReleased(MouseEvent me) {
                super.mouseReleased(me);
                System.out.println("release");
                update();
            }
        });
    }

    // have the board move text area display the board moves
    void update() {
        List<BoardMove> boardMoves = game.boardMoves;
        if (boardMovesTextArea == null) return;
        boardMovesTextArea.setText("");
        if (boardMoves == null) return;
        int i = 1;
        for (BoardMove bm: boardMoves) {
            boardMovesTextArea.append(i++ + " " + bm + "\n");
        }
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(100, 425);
    }
}
