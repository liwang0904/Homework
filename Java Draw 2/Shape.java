import java.awt.*;

public abstract class Shape implements java.io.Serializable{
    static final long serialVersionUID = 0L;
    
    protected Color color;
    public DrawingCanvas canvas;
    protected Rectangle bounds;
    protected boolean isSelected;
    protected static final int KNOB_SIZE = 6;

    public Color getColor() {
	return color;
    }
    

    public Color getCurrentColor() {
	return canvas.getToolbar().getCurrentColor();
    }

    public void setSelected(boolean newState) {}
    
    protected void setBounds(Rectangle newBounds)
    {
	Rectangle oldBounds = bounds; 
	bounds = newBounds;
	updateCanvas(oldBounds.union(bounds));
    }

    protected void updateCanvas(Rectangle areaOfChange, boolean enlargeForKnobs)
    {
	Rectangle toRedraw = new Rectangle(areaOfChange);
	if (enlargeForKnobs)
	    toRedraw.grow(KNOB_SIZE/2, KNOB_SIZE/2);
	canvas.repaint(toRedraw);
    }

    protected void updateCanvas(Rectangle areaOfChange)
    {
	updateCanvas(areaOfChange, isSelected);
    }

    public void create(Point anchor, Point end)
    {//QQ
	resize(anchor, end);
    }
    
    public void resize(Point anchor, Point end)
    {
	Rectangle newRect = new Rectangle(anchor);
	newRect.add(end);		// creates smallest rectange which includes both anchor & end
	if(newRect.getWidth() == 0)newRect.setSize(1, (int)newRect.getHeight());
	if(newRect.getHeight() == 0)newRect.setSize((int)newRect.getWidth(), 1);
	setBounds(newRect);  	// reset bounds & redraw affected areas
    }
	
    public abstract void draw(Graphics g, Rectangle clipRect);

    protected Rectangle[] getKnobRects()
    {
	Rectangle[] knobs = new Rectangle[4];
	knobs[NW] = new Rectangle(bounds.x - KNOB_SIZE/2, bounds.y - KNOB_SIZE/2, KNOB_SIZE, KNOB_SIZE);
	knobs[SW] = new Rectangle(bounds.x - KNOB_SIZE/2, bounds.y + bounds.height - KNOB_SIZE/2, KNOB_SIZE, KNOB_SIZE);
	knobs[SE] = new Rectangle(bounds.x + bounds.width - KNOB_SIZE/2, bounds.y + bounds.height - KNOB_SIZE/2, KNOB_SIZE, KNOB_SIZE);
	knobs[NE] = new Rectangle(bounds.x + bounds.width - KNOB_SIZE/2, bounds.y - KNOB_SIZE/2, KNOB_SIZE, KNOB_SIZE);
	return knobs;
    }

    protected int getKnobContainingPoint(Point pt)
    {
	if (!isSelected)	// if we aren't selected, the knobs aren't showing and thus there are no knobs to check
	    return NONE;
			
	Rectangle[] knobs = getKnobRects();
	for (int i = 0; i < knobs.length; i++)
	    if (knobs[i].contains(pt)) 
		return i;
	return NONE;
    }

    public boolean inside(Point pt)
    {
	return bounds.contains(pt);
    }
    
    public void translate(int dx, int dy) {
	Rectangle newRect = new Rectangle(bounds);
	newRect.translate(dx, dy);
	setBounds(newRect);
    }
    
    public Point getAnchorForResize(Point mouseLocation)
    {
	int whichKnob = getKnobContainingPoint(mouseLocation);
		
	if (whichKnob == NONE) // no resize knob is at this location
	    return null;
	switch (whichKnob) {
	case NW: return new Point(bounds.x + bounds.width, bounds.y + bounds.height);
	case NE: return new Point(bounds.x, bounds.y + bounds.height);
	case SW: return new Point(bounds.x + bounds.width, bounds.y);
	case SE: return new Point(bounds.x, bounds.y);
	}
	return null;
    }

    public Shape makeCopy(){
	try{
	    return (Shape) this.clone();
	}catch(Exception e){
	    System.err.println("THe sky is falling");
	    return this;
	}
    }
    protected static final int NONE = -1, NW = 0, SW = 1, SE = 2 , NE = 3;

    protected void draw_knobs(Graphics g){
	if (isSelected) { // if selected, draw the resizing knobs along the 4 corners
	    System.out.println("selected");
	    Rectangle[] knobs = getKnobRects();
	    for (int i = 0; i < knobs.length; i++) {
		System.out.println(knobs[i].x + " knobs[i].x");
		System.out.println(knobs[i].y + " knobs[i].y");
		g.fillRect(knobs[i].x, knobs[i].y, knobs[i].width, knobs[i].height);
	    }  
	}
	System.out.println("not selected");
    }
    
}
