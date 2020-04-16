import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel {
    // define the size of the board:
    private final int BOARD_WIDTH = 10;
    private final int BOARD_HEIGHT = 22;

    // defines the speed of the game:
    private final int PERIOD_INTERVAL = 300;

    private Timer timer;

    private boolean isStarted = false; //check if the game has started
    private boolean isPaused = false; // check if the game is paused
    private boolean isFallingFinished = false; // determines if the shape has finished falling and a new shape needs to be created

    private int numLinesRemoved = 0; // counts the number of lines that have been removed

    // determine the position of the falling piece:
    private int currentX = 0;
    private int currentY = 0;

    private Shape currentPiece;

    private JLabel statusbar;
    private Shape.Tetromino[] board;

    private void initBoard(Tetris parent) {
	setFocusable(true);
        statusbar = parent.getStatusBar();
        addKeyListener(new TAdapter());
    }
    public Board(Tetris parent) {
        initBoard(parent);
    }

    // determine the width and height of a single square
    private int squareWidth() {
        return (int) getSize().getWidth() / BOARD_WIDTH;
    }
    private int squareHeight() {
        return (int) getSize().getHeight() / BOARD_HEIGHT;
    }

    // determine the shape at the given coordinates
    private Shape.Tetromino shapeAt(int x, int y) {
        return board[(y * BOARD_WIDTH) + x]; // shapes are stored in the array
    }

    void start() {
	isStarted = true;
	// new current shape and new board:
        currentPiece = new Shape();
        board = new Shape.Tetromino[BOARD_WIDTH * BOARD_HEIGHT];

	// the board is cleared and the new falling piece is initialize
        clearBoard();
        newPiece();

	//timer is executed at PERIOD_INTERVAL intervals, creating a game cycle
        timer = new Timer(PERIOD_INTERVAL, new GameCycle());
        timer.start();
    }
    private void pause() { // pauses/resumes the game
        isPaused = !isPaused;
        if (isPaused) { // when paused, display the paused message in the statusbad
            statusbar.setText("Paused");
        } else {
            statusbar.setText(String.valueOf(numLinesRemoved));
        }
        repaint();
    }

    private void draw(Graphics g) { // draw all objects on board
	var size = getSize();
	int boardTop = (int) size.getHeight() - BOARD_HEIGHT * squareHeight();

	// paint all shapes/remains of shapes that have been dropped to the bottom:
	for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                Shape.Tetromino shape = shapeAt(j, BOARD_HEIGHT - i - 1);
		if (shape != Shape.Tetromino.NoShape) {
                    drawSquare(g, j * squareWidth(), boardTop + i * squareHeight(), shape);
		}
	    }
	}
	// paint the falling piece:
	if (currentPiece.getShape() != Shape.Tetromino.NoShape) {
            for (int i = 0; i < 4; i++) {
                int x = currentX + currentPiece.x(i);
                int y = currentY - currentPiece.y(i);

                drawSquare(g, x * squareWidth(), boardTop + (BOARD_HEIGHT - y - 1) * squareHeight(), currentPiece.getShape());
            }
        }
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void clearBoard() { // fills board with empty Tetromino.NoShape (collision detection)
        for (int i = 0; i < BOARD_HEIGHT * BOARD_WIDTH; i++) {
            board[i] = Shape.Tetromino.NoShape;
        }
    }

    private boolean tryMove(Shape newPiece, int newX, int newY) { // tries to move the piece
        for (int i = 0; i < 4; i++) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);
            if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT) {
                return false; // if it has reached the board boundaries
            }
            if (shapeAt(x, y) != Shape.Tetromino.NoShape) {
                return false; // if it is adjacent the the already fallen pieces
            }
        }
        currentPiece = newPiece;
        currentX = newX;
        currentY = newY;
        repaint();
        return true;
    }

    private void newPiece() { // creates a new piece with a random shape
        currentPiece.setRandomShape();
	// compute the initial currentX and currentY values:
        currentX = BOARD_WIDTH / 2 + 1;
        currentY = BOARD_HEIGHT - 1 + currentPiece.minY();
        if (!tryMove(currentPiece, currentX, currentY)) { // if cannot move to the initial positions, the game is over
            currentPiece.setShape(Shape.Tetromino.NoShape);
	    // timer is stopped and the game over string containing the score is displayed in the statusbar
            timer.stop();
            var message = String.format("Game over. Your score is %d", numLinesRemoved);
            statusbar.setText(message);
        }
    }

    private void oneLineDown() { // try to move the piece down one line until fully dropped
        if (!tryMove(currentPiece, currentX, currentY - 1)) {
            pieceDropped();
        }
    }
    
    private void removeLines() { // check for a full row
        int numFullLines = 0;
        for (int i = BOARD_HEIGHT - 1; i >= 0; i--) {
            boolean lineIsFull = true;
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (shapeAt(j, i) == Shape.Tetromino.NoShape) {
                    lineIsFull = false;
                    break;
                }
            }
            if (lineIsFull) {
                numFullLines++; // increase the counter
                for (int k = i; k < BOARD_HEIGHT - 1; k++) {
                    for (int j = 0; j < BOARD_WIDTH; j++) {
                        board[(k * BOARD_WIDTH) + j] = shapeAt(j, k + 1); // move all lines above the full row down one line
                    }
                }
            }
        }
        if (numFullLines > 0) {
            numLinesRemoved += numFullLines;
            statusbar.setText(String.valueOf(numLinesRemoved));
            isFallingFinished = true;
            currentPiece.setShape(Shape.Tetromino.NoShape);
        }
    }

    private void pieceDropped() { // puts falling piece into board array
        for (int i = 0; i < 4; i++) {
            int x = currentX + currentPiece.x(i);
            int y = currentY - currentPiece.y(i);
            board[(y * BOARD_WIDTH) + x] = currentPiece.getShape(); // holds all the squares
        }
        removeLines();
        if (!isFallingFinished) {
            newPiece();
        }
    }
    
    private void dropDown() { // when the Space key is pressed, the piece is dropped to the bottom
        int newY = currentY;
        while (newY > 0) {
            if (!tryMove(currentPiece, currentX, newY - 1)) { // until it reaches the bottom or the top of another fallen piece
                break;
            }
            newY--;
        }
        pieceDropped(); // when the piece finishes falling
    }

    private void drawSquare(Graphics g, int x, int y, Shape.Tetromino shape) {
	// no shape, line, t, square, z, s, l, j
	
        Color colors[] = {
	    new Color(0, 0, 0), // NoShape
	    new Color(0, 245, 244), // LineShape
	    new Color(178, 0, 248), // TShape
	    new Color(236, 248, 0), // SquareShape
	    new Color(255, 0, 1), // ZShape
	    new Color(2, 252, 1), // SShape
	    new Color(250, 160, 0), // LShape
	    new Color(51, 0, 250) // JShape
        };

        var color = colors[shape.ordinal()];

        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);

        g.setColor(color.brighter()); // left and top sides are drawn with brighter colors
        g.drawLine(x, y + squareHeight() - 1, x, y);
        g.drawLine(x, y, x + squareWidth() - 1, y);

        g.setColor(color.darker()); // bottom and right sides are drawn with darker colors
        g.drawLine(x + 1, y + squareHeight() - 1, x + squareWidth() - 1, y + squareHeight() - 1);
        g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1, x + squareWidth() - 1, y + 1);
    }

    private class GameCycle implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            doGameCycle(); // create a game cycle
        }
    }
    private void doGameCycle() { // the game is divided into game cycles
	// each cycle updates the game and redraws the board:
        update();
        repaint();
    }
    private void update() {
        if (isPaused) {
            return;
        }
        if (isFallingFinished) { // new piece is created if the previous one has finished falling
            isFallingFinished = false;
            newPiece();
        } else { // the falling piece goes one line down
            oneLineDown();
        }
    }

    class TAdapter extends KeyAdapter { // check for key events in the KeyAdapter
	public void keyPressed(KeyEvent e) {
	    if (!isStarted || currentPiece.getShape() == Shape.Tetromino.NoShape) {
		return;
	    }
		       
	    int keycode = e.getKeyCode(); // get the key code

	    // bind key events to methods:
	    if (keycode == 'p' || keycode == 'P') {
		pause();
		return;
	    }	
	    if (isPaused)
		return;
	    
	    switch (keycode) {
	    case KeyEvent.VK_LEFT:
		tryMove(currentPiece, currentX - 1, currentY);
		break;
	    case KeyEvent.VK_RIGHT:
		tryMove(currentPiece, currentX + 1, currentY);
		break;
	    case KeyEvent.VK_DOWN:
		tryMove(currentPiece.rotateLeft(), currentX, currentY);
		break;
	    case KeyEvent.VK_UP:
		tryMove(currentPiece.rotateRight(), currentX, currentY);
		break;
	    case KeyEvent.VK_SPACE:
		dropDown();
		break;
	    case 'd':
		oneLineDown();
		break;
	    case 'D':
		oneLineDown();
		break;
	    }
	}	
    }
}
