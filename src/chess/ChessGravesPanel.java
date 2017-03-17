package chess;
import chess.piece.Piece;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/* Displays two text areas representing the graves of the chess game */
public class ChessGravesPanel extends JPanel {
    // fields
    ChessGame game = null;
    ChessBoardPanel boardPanel = null;
    JTextArea whiteGraveTextArea = null;
    JTextArea blackGraveTextArea = null;
    int pieceFontSize = 20;

    ChessGravesPanel(ChessGame game, ChessBoardPanel boardPanel) {
        this.game = game;
        this.boardPanel = boardPanel;
        init();
        initEventListeners(boardPanel);
    }

    // passes board to get when mouse is released on the board
    void initEventListeners(ChessBoardPanel boardPanel) {
        boardPanel.addMouseListener(new MouseAdapter() {
            @Override public void mouseReleased(MouseEvent me) {
                super.mousePressed(me);
                update();
            }
        });
    }

    // standard initialization invoking others
    void init() {
        initGraves();
    }

    // create text areas for graves of black and white
    void initGraves() {
        Color pieceBlackColor = boardPanel.pieceBlackColor;
        Color pieceWhiteColor = boardPanel.pieceWhiteColor;
        // create grave text areas
        whiteGraveTextArea = new JTextArea("");
        whiteGraveTextArea.setEditable(false);
        whiteGraveTextArea.setFont(new Font("TimesRoman", Font.PLAIN, (int)(pieceFontSize)));
        /*
        whiteGraveTextArea.setWrapStyleWord(true);
        whiteGraveTextArea.setLineWrap(true);
        */
        //whiteGraveTextArea.setBounds(37, 450, 100, 100);
        whiteGraveTextArea.setForeground(pieceWhiteColor);
        whiteGraveTextArea.setBackground(new Color(0,0,0,0)); // transparent background
        whiteGraveTextArea.setHighlighter(null);
        add(whiteGraveTextArea);

        blackGraveTextArea = new JTextArea("");
        blackGraveTextArea.setEditable(false);
        blackGraveTextArea.setFont(new Font("TimesRoman", Font.PLAIN, (int)(pieceFontSize)));
        //blackGraveTextArea.setLineWrap(true);
        //blackGraveTextArea.setBounds(157, 450, 100, 100);
        blackGraveTextArea.setForeground(pieceBlackColor);
        blackGraveTextArea.setBackground(new Color(0,0,0,0)); // transparent background
        blackGraveTextArea.setHighlighter(null);
        add(blackGraveTextArea);
    }

    // have graves of white and black display dead pieces
    void update() {
        List<List<Piece>> grave = game.grave;
        if (blackGraveTextArea == null || whiteGraveTextArea == null || grave == null) return;
        blackGraveTextArea.setText("");
        List<Piece> whiteGrave = grave.get(0);
        List<Piece> blackGrave = grave.get(1);
        if (whiteGrave == null || blackGrave == null) return;

        // text setting only if change is to remove this buggy thing on gui, also an optimization albeit minor
        String whiteText = "";
        String blackText = "";
        for (Piece piece: whiteGrave) {
            whiteText += Character.toString(piece.getEncoding());
        }
        for (Piece piece: blackGrave) {
            blackText += Character.toString(piece.getEncoding());
        }

        if (!whiteText.equals(whiteGraveTextArea.getText()))
            whiteGraveTextArea.setText(whiteText);
        if (!blackText.equals(blackGraveTextArea.getText()))
            blackGraveTextArea.setText(blackText);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(100, 500);
    }
}
