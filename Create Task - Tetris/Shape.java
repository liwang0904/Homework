import java.util.Random;

public class Shape {
    protected enum Tetromino { // Tetris pieces
	NoShape,
	LineShape,
	TShape,
	SquareShape,
	ZShape,
	SShape,
	LShape,
	JShape
    }

    private Tetromino pieceShape;
    private int[][] coordinates;

    public Shape() {
	coordinates = new int[4][2];
	setShape(Tetromino.NoShape);
    }

    void setShape(Tetromino shape) {
	int[][][] coordinatesTable = new int[][][] { // Coordinates of a Tetris piece
	    {{0, 0}, {0, 0}, {0, 0}, {0, 0}}, // NoShape
	    {{0, -1}, {0, 0}, {0, 1}, {0, 2}}, // LineShape
	    {{-1, 0}, {0, 0}, {1, 0}, {0, 1}}, // TShape
	    {{0, 0}, {1, 0}, {0, 1}, {1, 1}}, // SquareShape
            {{0, -1}, {0, 0}, {-1, 0}, {-1, 1}}, // ZShape
            {{0, -1}, {0, 0}, {1, 0}, {1, 1}}, // SShape
	    {{-1, -1}, {0, -1}, {0, 0}, {0, 1}}, // LShape
	    {{1, -1}, {0, -1}, {0, 0}, {0, 1}} // JShape
	};

	for (int i = 0; i < 4; i++) { // Tetris pieces take their coordinate values
	    System.arraycopy(coordinatesTable[shape.ordinal()], 0, coordinates, 0, 4);
	}
	
	pieceShape = shape;
    }

    private void setX(int index, int x) {
	coordinates[index][0] = x;
    }
    private void setY(int index, int y) {
	coordinates[index][1] = y;
    }

    int x(int index) {
	return coordinates[index][0];
    }
    int y(int index) {
	return coordinates[index][1];
    }

    public int minX() {
        int min = coordinates[0][0];
        for (int i = 0; i < 4; i++) {
           min = Math.min(min, coordinates[i][0]);
        }
        return min;
    }
    int minY() {
        int min = coordinates[0][1];
        for (int i = 0; i < 4; i++) {
            min = Math.min(min, coordinates[i][1]);
        }
        return min;
    }

    Shape rotateLeft() {
        if (pieceShape == Tetromino.SquareShape) { // The square is the same when rotates
            return this; // No change
        }
	
        var rotatedShape = new Shape();
        rotatedShape.pieceShape = pieceShape;
        for (int i = 0; i < 4; i++) {
            rotatedShape.setX(i, y(i));
            rotatedShape.setY(i, -x(i));
        }
        return rotatedShape;
    }
    Shape rotateRight() {
        if (pieceShape == Tetromino.SquareShape) {
            return this;
        }

        var rotatedShape = new Shape();
        rotatedShape.pieceShape = pieceShape;
        for (int i = 0; i < 4; i++) {
            rotatedShape.setX(i, -y(i));
            rotatedShape.setY(i, x(i));
        }
        return rotatedShape;
    }

    Tetromino getShape() {
	return pieceShape;
    }

    void setRandomShape() {
	var random = new Random();
	int x = Math.abs(random.nextInt()) % 7 + 1;

	Tetromino[] values = Tetromino.values();
	setShape(values[x]);
    }
}
