package com.chess.graphics;

import com.chess.ChessGame;

import javax.swing.*;
import java.awt.*;

public class ChessButtonsPanel extends JPanel {
    ChessGame game;
    ChessBoardPanel boardPanel;
    JButton newGameBtn;
    JButton undoBtn;

    ChessButtonsPanel(ChessGame game, ChessBoardPanel boardPanel) {
        this.game = game;
        this.boardPanel = boardPanel;
        initButtons();
    }

    private void initButtons() {
        newGameBtn = new JButton("New Game");
        undoBtn = new JButton("Undo");

        setLayout(new FlowLayout(FlowLayout.LEFT));

        add(newGameBtn);
        add(undoBtn);

        newGameBtn.addActionListener(e -> boardPanel.repaint());
        undoBtn.addActionListener(e -> {
            game.undoLastMove();
            boardPanel.repaint();
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 40);
    }
}
