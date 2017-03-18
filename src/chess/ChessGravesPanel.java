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
                repaint();
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
        /*
        // create grave text areas
        whiteGraveTextArea = new JTextArea("");
        whiteGraveTextArea.setEditable(false);
        whiteGraveTextArea.setFont(new Font("TimesRoman", Font.PLAIN, (int)(pieceFontSize)));

        whiteGraveTextArea.setWrapStyleWord(true);
        whiteGraveTextArea.setLineWrap(true);

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
        */
    }

    // have graves of white and black display dead pieces
    private void drawGraves(Graphics g) {
        // try to get graves
        List<List<Piece>> grave = game.grave;
        if (blackGraveTextArea == null || whiteGraveTextArea == null || grave == null) return;

        List<Piece> whiteGrave = grave.get(0);
        List<Piece> blackGrave = grave.get(1);
        if (whiteGrave == null || blackGrave == null) return;

        Color pieceWhiteColor = boardPanel.pieceWhiteColor;
        Color pieceBlackColor = boardPanel.pieceBlackColor;
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(new Font("TimesRoman", Font.PLAIN, pieceFontSize));
        g2d.setStroke(new BasicStroke(1));

        // text setting only if change
        String whiteText = "";
        String blackText = "";
        g2d.setColor(pieceWhiteColor);
        for (int i = 0; i < whiteGrave.size(); ++i) {
            Piece piece = whiteGrave.get(i);
            whiteText += Character.toString(piece.getEncoding());
            if (i % 4 == 0) whiteText += "\n";
        }

        g2d.setColor(pieceBlackColor);
        for (int i = 0; i < blackGrave.size(); ++i) {
            Piece piece = blackGrave.get(i);
            blackText += Character.toString(piece.getEncoding());
            if (i % 4 == 0) blackText += "\n";
        }

        g2d.drawString(whiteText, 0, 0);
        g2d.drawString(blackText, 55, 0);
    }

    @Override
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      drawGraves(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(100, 500);
    }
}
