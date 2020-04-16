import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Tetris extends JFrame { // set up the game and create a board and statusbar
    private JLabel statusbar;
    
    public Tetris() {
        init();
    }
    
    private void init() {
	// score is displayed in a label at the bottom of the board:
        statusbar = new JLabel(" 0");
        add(statusbar, BorderLayout.SOUTH);

	//the board is created and added to the countainer:
        var board = new Board(this);
        add(board);
        board.start(); // starts the Tetris game

        setTitle("Tetris");
        setSize(200, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    JLabel getStatusBar() {
        return statusbar;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            var game = new Tetris();
            game.setVisible(true);
        });
    }
}
