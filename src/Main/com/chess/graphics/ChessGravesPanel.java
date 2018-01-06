package com.chess.graphics;
import com.chess.ChessGame;
import com.chess.piece.Piece;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/* Displays two text areas representing the graves of the chess game */
public class ChessGravesPanel extends JPanel {
    // fields
    private ChessGame game = null;
    private ChessBoardPanel boardPanel = null;
    private JTextArea whiteGraveTextArea = null;
    private JTextArea blackGraveTextArea = null;
    private int pieceFontSize = 25;

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

    // standard initialization
    void init() {
        Color pieceBlackColor = boardPanel.pieceBlackColor;
        Color pieceWhiteColor = boardPanel.pieceWhiteColor;
    }
    
    // have graves of white and black display dead pieces
    private void drawGraves(Graphics g) {
        // try to get graves
        List<List<Piece>> grave = game.getGrave();
        if (grave == null) return;

        List<Piece> whiteGrave = grave.get(0);
        List<Piece> blackGrave = grave.get(1);
        if (whiteGrave == null || blackGrave == null) { return; } // error


        Color pieceWhiteColor = boardPanel.pieceWhiteColor;
        Color pieceBlackColor = boardPanel.pieceBlackColor;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(new Font("TimesRoman", Font.PLAIN, pieceFontSize));
        g2d.setStroke(new BasicStroke(1));

        // text setting only if change
        String whiteText = "";
        String blackText = "";

        for (int i = 0; i < whiteGrave.size(); ++i) {
            Piece piece = whiteGrave.get(i);
            whiteText += Character.toString(piece.getEncoding());
            if ((i+1) % 10 == 0) whiteText += "\n";
        }

        for (int i = 0; i < blackGrave.size(); ++i) {
            Piece piece = blackGrave.get(i);
            blackText += Character.toString(piece.getEncoding());
            if ((i+1) % 10 == 0) blackText += "\n";
        }

        g2d.setColor(pieceWhiteColor);
        int x, y;
        x = 10;
        y = 0;
        for (String line : whiteText.split("\n"))
            g2d.drawString(line, x, y += + g2d.getFontMetrics().getHeight());

        g2d.setColor(pieceBlackColor);
        x = 270;
        y = 0;
        for (String line : blackText.split("\n"))
            g2d.drawString(line, x, y += g2d.getFontMetrics().getHeight());
    }

    @Override
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      drawGraves(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 80);
    }
}
