package com.chess;
import javax.swing.*;
import java.awt.*;

/* This panel holds together all the other panels/displays of a
 * single instance of a Chess Game, which is also held by this panel */
public class ChessGamePanel extends JPanel {
    ChessGame game = null;
    ChessBoardPanel boardPanel = null;
    ChessBoardMovesPanel boardMovesPanel = null;
    ChessGravesPanel gravesPanel = null;

    public ChessGamePanel() {
        game = new ChessGame();
        boardPanel = new ChessBoardPanel(game);
        boardMovesPanel = new ChessBoardMovesPanel(game, boardPanel);
        gravesPanel = new ChessGravesPanel(game, boardPanel);

        boardPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        boardMovesPanel.setBorder(BorderFactory.createCompoundBorder(
              BorderFactory.createEmptyBorder(0, 0, 0, 0),
              BorderFactory.createLineBorder(Color.black)
            ));
        gravesPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 2;

        add(boardPanel, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 2;

        add(boardMovesPanel, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;

        add(gravesPanel, gbc);
    }
}
