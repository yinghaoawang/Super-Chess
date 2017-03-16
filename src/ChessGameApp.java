import chess.*;
import java.awt.EventQueue;
public class ChessGameApp {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                ChessGameFrame game = new ChessGameFrame();
                game.setVisible(true);
            }
        });
    }
}
