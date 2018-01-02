package com.chess.graphics;
import javax.swing.*;

/* The frame holding the Chess Game panel */
public class ChessGameFrame extends JFrame {
    public ChessGameFrame() {
        init();
    }
    void init() {
        ChessGamePanel panel = new ChessGamePanel();
        getContentPane().add(panel);
        setVisible(true);
        setSize(600, 700);
        setTitle("Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // places the game in the middle of computer window
    }
}
