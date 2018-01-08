package com.chess.graphics;

import com.chess.ChessGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ChessInfoPanel extends JPanel {
    ChessGame game = null;
    JLabel label = null;
    ChessBoardPanel boardPanel = null;
    private int fontSize = 15;
    private String fontName = "Arial";

    ChessInfoPanel(ChessGame game, ChessBoardPanel boardPanel) {
        this.game = game;
        this.boardPanel = boardPanel;
        initComponents();
        initEventListeners(boardPanel);
    }

    void initComponents() {
        label = new JLabel("");
        label.setFont(new Font(fontName, Font.PLAIN, fontSize));
        label.setVerticalTextPosition(JLabel.CENTER);
        label.setHorizontalTextPosition(JLabel.CENTER);
        add(label);
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

    void drawMessage() {
        label.setText(game.getMessage());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawMessage();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 30);
    }
}
