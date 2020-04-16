import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

class Tile {
    private boolean merged;
    private int value;
 
    Tile(int val) {
        value = val;
    }
 
    int getValue() {
        return value;
    }
 
    void setMerged(boolean m) {
        merged = m;
    }
 
    boolean canMergeWith(Tile other) {
        return !merged && other != null && !other.merged && value == other.getValue();
    }
 
    int mergeWith(Tile other) {
        if (canMergeWith(other)) {
            value *= 2;
            merged = true;
            return value;
        }
        return -1;
    }
}

public class Game2048 extends JPanel {

    final static int target = 2048;

    static int highest;
    static int score;

    // Colors courtesy of www.color-hex.com
    final Color[] colorTable = {
        new Color(0x701710),
	new Color(0xFFE4C3),
	new Color(0xfff4d3),
        new Color(0xffdac3),
	new Color(0xe7b08e),
	new Color(0xe7bf8e),
        new Color(0xffc4c3),
	new Color(0xE7948e),
	new Color(0xbe7e56),
        new Color(0xbe5e56),
	new Color(0x9c3931),
	new Color(0x701710)
    };
 
    private Color gridColor = new Color(0xBBADA0);
    private Color emptyColor = new Color(0xCDC1B4);
    private Color startColor = new Color(0xFFEBCD);

    enum State {
        start, won, running, over
    }
 
    private Random random = new Random();
 
    private Tile[][] tiles;
    private int side = 4;
    private State gamestate = State.start;
    private boolean checkingAvailableMoves;
 
    public Game2048() {
	// Courtesy of www.programcreek.com
        setPreferredSize(new Dimension(900, 700));
        setBackground(new Color(0xFAF8EF));
        setFont(new Font("SansSerif", Font.BOLD, 48));
        setFocusable(true);
 
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startGame();
                repaint();
            }
        });
 
        addKeyListener(new KeyAdapter() {
            @Override
	    // Courtesy of docs.oracle.com
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        moveUp();
                        break;
                    case KeyEvent.VK_DOWN:
                        moveDown();
                        break;
                    case KeyEvent.VK_LEFT:
                        moveLeft();
                        break;
                    case KeyEvent.VK_RIGHT:
                        moveRight();
                        break;
                }
                repaint();
            }
        });
    }
 
    @Override
    public void paintComponent(Graphics gg) {
        super.paintComponent(gg);
        Graphics2D grid = (Graphics2D) gg;
        grid.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
 
        drawGrid(grid);
    }
 
    void startGame() {
        if (gamestate != State.running) {
            score = 0;
            highest = 0;
            gamestate = State.running;
            tiles = new Tile[side][side];
            addRandomTile();
            addRandomTile();
        }
    }
 
    void drawGrid(Graphics2D grid) {
        grid.setColor(gridColor);
        grid.fillRoundRect(200, 100, 499, 499, 15, 15);
 
        if (gamestate == State.running) {
 
            for (int row = 0; row < side; row++) {
                for (int col = 0; col < side; col++) {
                    if (tiles[row][col] == null) {
                        grid.setColor(emptyColor);
                        grid.fillRoundRect(215 + col * 121, 115 + row * 121, 106, 106, 7, 7);
                    } else {
                        drawTile(grid, row, col);
                    }
                }
            }
        } else {
            grid.setColor(startColor);
            grid.fillRoundRect(215, 115, 469, 469, 7, 7);
 
            grid.setColor(gridColor.darker());
            grid.setFont(new Font("SansSerif", Font.BOLD, 128));
            grid.drawString("2048", 290, 350);
 
            grid.setFont(new Font("SansSerif", Font.BOLD, 20));
 
            if (gamestate == State.won) {
                grid.drawString("YOU MADE IT!", 390, 385);
 
            } else if (gamestate == State.over)
                grid.drawString("Game Over", 400, 385);
 
            grid.setColor(gridColor);
            grid.drawString("Click to start a new game", 320, 410);
            grid.drawString("(use arrow keys to move tiles)", 292, 450);
        }
    }
 
    void drawTile(Graphics2D grid, int row, int col) {
        int value = tiles[row][col].getValue();
 
        grid.setColor(colorTable[(int) (Math.log(value) / Math.log(2)) + 1]);
        grid.fillRoundRect(215 + col * 121, 115 + row * 121, 106, 106, 7, 7);
        String s = String.valueOf(value);
 
        grid.setColor(value < 128 ? colorTable[0] : colorTable[1]);

	// Courtesy of docs.oracle.com
        FontMetrics fm = grid.getFontMetrics();
        int asc = fm.getAscent();
        int dec = fm.getDescent();
 
        int x = 215 + col * 121 + (106 - fm.stringWidth(s)) / 2;
        int y = 115 + row * 121 + (asc + (106 - (asc + dec)) / 2);
 
        grid.drawString(s, x, y);
    }
 
 
    private void addRandomTile() {
        int pos = random.nextInt(side * side);
        int row, col;
        do {
            pos = (pos + 1) % (side * side);
            row = pos / side;
            col = pos % side;
        } while (tiles[row][col] != null);
 
        int val = random.nextInt(10) == 0 ? 4 : 2;
        tiles[row][col] = new Tile(val);
    }
 
    private boolean move(int countDownFrom, int yIncr, int xIncr) {
        boolean moved = false;
 
        for (int i = 0; i < side * side; i++) {
            int j = Math.abs(countDownFrom - i);
 
            int row = j / side;
            int col = j % side;
 
            if (tiles[row][col] == null)
                continue;
 
            int nextR = row + yIncr;
            int nextC = col + xIncr;
 
            while (nextR >= 0 && nextR < side && nextC >= 0 && nextC < side) {
 
                Tile next = tiles[nextR][nextC];
                Tile curr = tiles[row][col];
 
                if (next == null) {
 
                    if (checkingAvailableMoves)
                        return true;
 
                    tiles[nextR][nextC] = curr;
                    tiles[row][col] = null;
                    row = nextR;
                    col = nextC;
                    nextR += yIncr;
                    nextC += xIncr;
                    moved = true;
 
                } else if (next.canMergeWith(curr)) {
 
                    if (checkingAvailableMoves)
                        return true;
 
                    int value = next.mergeWith(curr);
                    if (value > highest)
                        highest = value;
                    score += value;
                    tiles[row][col] = null;
                    moved = true;
                    break;
                } else
                    break;
            }
        }
 
        if (moved) {
            if (highest < target) {
                clearMerged();
                addRandomTile();
                if (!movesAvailable()) {
                    gamestate = State.over;
                }
            } else if (highest == target)
                gamestate = State.won;
        }
 
        return moved;
    }
 
    boolean moveUp() {
        return move(0, -1, 0);
    }
 
    boolean moveDown() {
        return move(side * side - 1, 1, 0);
    }
 
    boolean moveLeft() {
        return move(0, 0, -1);
    }
 
    boolean moveRight() {
        return move(side * side - 1, 0, 1);
    }
 
    void clearMerged() {
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile != null)
                    tile.setMerged(false);
	    }
	}
    }
 
    boolean movesAvailable() {
        checkingAvailableMoves = true;
        boolean hasMoves = moveUp() || moveDown() || moveLeft() || moveRight();
        checkingAvailableMoves = false;
        return hasMoves;
    }
 
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setTitle("2048");
            f.setResizable(true);
            f.add(new Game2048(), BorderLayout.CENTER);
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}
