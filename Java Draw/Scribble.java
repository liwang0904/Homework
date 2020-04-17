import java.awt.*;
import java.util.ArrayList;

public class Scribble extends Shape {
    ArrayList<Point> points = new ArrayList<Point>();
    private Point end;
    private Color color;
    
    public Scribble(Point start, DrawingCanvas drawing_canvas) {
	canvas = drawing_canvas;
	bounds = new Rectangle(start);
	color = getCurrentColor();
	this.end = end;
    }


    @Override
    public void resize(Point anchor, Point end) {
	//resize each point 
    }

    private static Point resize(Rectangle old_bound,  Rectangle new_bound, Point orig){
	int cx = old_bound.getX() + old_bound.getWidth() / 2;
	int cy = old_bound.getY() + old_bound.getHeight() / 2;
	float scale_x = new_bound.getWith() * 1f / old_bound.getWeight();
	float scale_y = new_bound.getHeight() * 1f / new_bould.getHeight();
	int dx = orig.getX() - cx;
	int dy = orig.getY() - cy;
	int nx= cx + dx* scale_x;
	int ny = cy +dy * scale_y;
	return new Point(nx , ny);
    }
    
    @Override
    public void create(Point anchor, Point end) {
	System.out.println("CREATE");
	double minx = Integer.MAX_VALUE;
	double miny = Integer.MAX_VALUE;
	double maxx = -1;
	double maxy = -1;
	for(Point point : points) {
	    if (point.getX() < minx) {
		minx = point.getX();
	    }
	    if (point.getY() < miny) {
		miny = point.getY();
	    }
	    if (point.getX() > maxx) {
		maxx = point.getX();
	    }
	    if (point.getY() > maxy) {
		maxy = point.getY();
	    }
	}
	if (end.getX() < minx) {
	    minx = end.getX();
	    //System.out.println("minx " + minx);
	}
	if (end.getY() < miny) {
	    miny = end.getY();
	    //System.out.println("miny " + miny);
	}
	if (end.getX() > maxx) {
	    maxx = end.getX();
	    //System.out.println("maxx " + maxx);
	}
	if (end.getY() > maxy) {
	    maxy = end.getY();
	    //System.out.println("maxy " + maxy);
	}
	
	System.out.println("maxx " + maxx);
	System.out.println("maxy " + maxy);
	System.out.println("minx " + minx);
	System.out.println("miny " + miny);
	Point newend = new Point((int)minx, (int)miny);
	//super.resize(anchor, newend);
	points.add(end);
	Rectangle bounds = get_bounds(points);	
	this.end = end;
	setBounds(bounds);
       
    }

    public void draw(Graphics graphics, Rectangle clipRect) {
	//System.out.println("here");
	if (points.size() == 0) {
	    // System.out.println("Uh oh, no points");
	    return;
	}
	Point start = points.get(0);
	for (int i = 1; i < points.size(); i++) {
	    //System.out.println("here!");
	    Point point = points.get(i);
	    graphics.drawLine(start.x, start.y, point.x, point.y);
	    graphics.setColor(color);
	    start = point;
	}
	//System.out.println("here!!");
       draw_knobs(graphics);
	//System.out.println("redrew knobs!");
    }

    public void setSelected(boolean newState)
    {
	isSelected = newState;
	updateCanvas(bounds, true); 
    }

    private static Rectangle get_bounds(ArrayList<Point> points){
	double minx = Integer.MAX_VALUE;
	double miny = Integer.MAX_VALUE;
	double maxx = -1;
	double maxy = -1;
	for(Point point : points) {
	    if (point.getX() < minx) {
		minx = point.getX();
	    }
	    if (point.getY() < miny) {
		miny = point.getY();
	    }
	    if (point.getX() > maxx) {
		maxx = point.getX();
	    }
	    if (point.getY() > maxy) {
		maxy = point.getY();
	    }
	}
	    
	int width  = (int) maxx - (int) minx;
	int height = (int) maxy - (int) miny;
	//System.out.println("width " + width);
	//System.out.println("height " + height);
	//System.out.println("min " +  minx + ":" +miny + "  "+maxx+":"+maxy);
	//System.out.println("maxy " + maxy);
	return new Rectangle((int) minx - 2, (int) miny - 2, width + 4, height + 4);
    }
	
    @Override
    public void translate(int dx, int dy) {
	for (Point point : points) {
	    point.setLocation(point.getX() + dx, point.getY() + dy);
	}
	setBounds(get_bounds(points));
    }

    @Override
    public boolean inside(Point pt) {
	Point start = points.get(0);
	for (Point point : points) {
	    start = point;
	    int width = (int) (start.getX() - end.getX());
	    int height = (int) (start.getY() - end.getY());
	    Rectangle bounds = new Rectangle((int) start.getX(), (int) end.getY(), width, height);
	    if(Line.is_inside(start, end, pt, bounds)) {
		//System.out.println("selected!");
		return true;
	    }
	}
	//System.out.println("not selected!");
	return false;
    }
}
