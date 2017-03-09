import chess.*;
import java.awt.EventQueue;
public class Runner {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                ChessApp game = new ChessApp();
                game.setVisible(true);
            }
        });
    }
}
