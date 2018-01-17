package com.chess.graphics;

import com.chess.ChessGame;

import javax.swing.*;
import java.awt.*;

public class ChessButtonsPanel extends JPanel {
    ChessGame game;
    ChessGamePanel gamePanel;
    JButton newGameBtn;
    JButton undoBtn;

    ChessButtonsPanel(ChessGame game, ChessGamePanel gamePanel) {
        this.game = game;
        this.gamePanel = gamePanel;
        initButtons();
    }

    private void initButtons() {
        newGameBtn = new JButton("New Game");
        undoBtn = new JButton("Undo");

        setLayout(new FlowLayout(FlowLayout.LEFT));

        add(newGameBtn);
        add(undoBtn);

        newGameBtn.addActionListener(e -> gamePanel.repaintGame());
        undoBtn.addActionListener(e -> {
            game.undoLastMove();
            gamePanel.repaintGame();
        });
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 40);
    }
}
