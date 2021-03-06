package com.chess.graphics;
import com.chess.ChessGame;

import javax.swing.*;
import java.awt.*;

/* This panel holds together all the other panels/displays of a
 * single instance of a Chess Game, which is also held by this panel */
public class ChessGamePanel extends JPanel {
    private ChessGame game = null;
    private ChessBoardPanel boardPanel = null;
    private ChessBoardMovesPanel boardMovesPanel = null;
    private ChessGravesPanel gravesPanel = null;
    private ChessInfoPanel infoPanel = null;
    private ChessButtonsPanel buttonsPanel = null;

    public void repaintGame() {
        boardPanel.repaint();
        gravesPanel.repaint();
        infoPanel.repaint();
        boardMovesPanel.update();
    }

    ChessGamePanel() {
        game = new ChessGame();
        boardPanel = new ChessBoardPanel(game);
        boardMovesPanel = new ChessBoardMovesPanel(game, boardPanel);
        gravesPanel = new ChessGravesPanel(game, boardPanel);
        infoPanel = new ChessInfoPanel(game, boardPanel);
        buttonsPanel = new ChessButtonsPanel(game, this);

        /*
        boardPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        boardMovesPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        gravesPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        buttonsPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        */
        infoPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;

        add(infoPanel, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.gridheight = 2;

        add(boardPanel, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 2;

        add(boardMovesPanel, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;

        add(gravesPanel, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;

        add(buttonsPanel, gbc);
    }
}
