import java.awt.*;

public class Oval extends Shape {
    private Point start;
    public Oval(Point start, DrawingCanvas drawing_canvas) {
	canvas = drawing_canvas;
	bounds = new Rectangle(start);
	this.start = start;
	color = getCurrentColor();
    }

    public void draw(Graphics g, Rectangle clipRect) {
	if (!bounds.intersects(clipRect)) {
	    return;
	}

	g.setColor(color);
	g.fillOval(bounds.x, bounds.y, bounds.width, bounds.height);
	draw_knobs(g);
    }
    
    protected Object clone() {
	Oval obj = new Oval(start, canvas);
	obj.color = color;
	obj.bounds = bounds;
	obj.isSelected= false;
	obj.canvas = canvas;
	obj.translate(50, 50);
	return obj;
    }

    @Override
    public boolean inside(Point pt){
	Point center = new Point();
	double centerX = start.getX() + (bounds.getWidth() / 2);
	double centerY = start.getY() + (bounds.getHeight() / 2);
	double A = bounds.getWidth() / 2;
	double B = bounds.getHeight() / 2;
	double x = centerX - pt.getX();
	double y = centerY - pt.getY();
	System.out.println( "center (" + centerX + ", " + centerY + ")");
	System.out.println(" pt ("  + pt.getX()+ " "+ pt.getY()+ ")");
	System.out.println( " xy ("+x + " " + y + ")");
	
	if (((x * x) / (A * A)) + ((y * y) / (B * B)) < 1) {	
	    System.out.println("true");
	    return true;
	}
	System.out.println("false");
	return false;
    }

    //FIX ME!
    public void setSelected(boolean newState) {
	isSelected = newState;
	updateCanvas(bounds, true);
    }
}
