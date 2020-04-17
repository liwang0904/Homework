import java.awt.*;
import java.lang.*;

public class Line extends Shape{
    private Point start;
    private Point end;
    private Color color;
    public Line(Point start, DrawingCanvas drawing_canvas) {
	canvas = drawing_canvas;
	bounds = new Rectangle(start);
	this.start = start;
	color = getCurrentColor();
    }
    
    @Override
    public void resize(Point anchor, Point end){
	super.resize(anchor, end);
	this.end = end;
    }

    protected Object clone() {
	Line obj = new Line(start, canvas);
	obj.color = color;
	obj.bounds = bounds;
	obj.isSelected= false;
	obj.canvas = canvas;
	obj.translate(50, 50);
	return obj;
    }
	
    public void draw(Graphics g, Rectangle clipRect) {
	//System.out.println("bounds = "+bounds + "  clip=" + clipRect);
	if (!bounds.intersects(clipRect)){
	    //System.out.println("Does not intersect");
	    return;
	}
	/*if (start == null || end == null) {
	    System.out.println("ERRPR NO START");
	    return;
	    }*/
	g.setColor(color);
	System.out.println(start + " " + end);
	g.drawLine(start.x, start.y, end.x, end.y);
	draw_knobs(g);
    }

    /*public Color getCurrentColor() {
      return canvas.getToolbar().getCurrentColor();
      }*/

    public static boolean is_inside(Point start, Point end, Point pt, Rectangle bounds) {
	if (!bounds.contains(pt)) {
	    double start_distance = Math.sqrt(((start.getX() - pt.getX()) * (start.getX() - pt.getX())) + ((start.getY() - pt.getY()) * (start.getY() - pt.getY())));
	    double end_distance = Math.sqrt(((end.getX() - pt.getX()) * (end.getX() - pt.getX())) + ((end.getY() - pt.getY()) * (end.getY() - pt.getY())));
	    //System.out.println("start distance = " + start_distance);
	    //System.out.println("end distance = " + end_distance);
	    if (start_distance < 25.0) {
		return true;
	    } else if (end_distance < 25.0) {
		return true;
	    } else {
		return false;
	    }
	}

	if (end.getX() == start.getX()) {
	    System.out.println("It's vertical");
	    double distance = Math.abs(pt.getX() - end.getX());
	    System.out.println("distance = " + distance);
	    if (distance < 25.0) {
		return true;
	    } else {
		return false;
	    }
	}
	double slope = (end.getY() - start.getY()) / (end.getX() - start.getX());
	double y_intercept = start.getY() - (slope * start.getX());
	//System.out.println("slope = " + slope);
	//System.out.println("endY = " + end.getY() + "startY = " + start.getY() + "endX = " + end.getX() + "startX = " + start.getX());
	//System.out.println("y-intercept = " + y_intercept);]
	double a = slope;
	double b = -1.0;
	double c = y_intercept;
	double x = pt.getX();
	double y = pt.getY();
	double d = (Math.abs((a * x) + (b * y) + c)) / (Math.sqrt((a * a) + (b * b)));
	//System.out.println(d);
	if (d < 25.0) {
	    //System.out.println("true");
	    return true;
	}
	//System.out.println("false");
	return false;
    }
    
    //FIXME FIXME
    @Override
    public boolean inside(Point pt)
    {
	return is_inside(start, end, pt, bounds);
    }

    @Override
    public void translate(int dx, int dy) {
	start.setLocation(start.getX() + dx, start.getY() + dy);
	end.setLocation(end.getX() + dx, end.getY() + dy);
	super.translate(dx, dy);
    }
    public void setSelected(boolean newState) {
	isSelected = newState;
	updateCanvas(bounds, true);
    }
}
