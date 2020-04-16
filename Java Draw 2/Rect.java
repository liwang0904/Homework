import java.awt.*;

public class Rect extends Shape
{
    private Point start;
    public Rect(Point start, DrawingCanvas dcanvas)
    {
	canvas = dcanvas;
	bounds = new Rectangle(start);
	color = getCurrentColor();
	this.start = start;
    }
    
    public void translate(int dx, int dy)
    {
	Rectangle newRect = new Rectangle(bounds);
	newRect.translate(dx, dy);
	setBounds(newRect);
    }

    protected Object clone() {
	Rect obj = new Rect(start, canvas);
	obj.color = color;
	obj.bounds = bounds;
	obj.isSelected= false;
	obj.canvas = canvas;
	obj.translate(50, 50);
	return obj;
    }
	
    public void setSelected(boolean newState)
    {
	isSelected = newState;
	updateCanvas(bounds, true); 
    }
    
    public void draw(Graphics g, Rectangle clipRect)
    {
	if (!bounds.intersects(clipRect))
	    return;
			
	g.setColor(color);
	g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
	draw_knobs(g);

    }
}
