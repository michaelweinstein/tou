package miweinst.engine.gfx.shape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import miweinst.engine.util.Vec2f;

public class CompoundShape extends Shape {
	
	private ArrayList<Shape> _shapes;
	private Vec2f _refLocation;

	/*Take in uniform color of compound shape and 
	* already formed non-Compound subclasses of Shape. Reference
	* Location is the set location for CompoundShape; it can be any
	* point, does not have to be a location of one of Shapes passed in.
	* But when new Location is set, all the shapes stay at the same
	* x and y positions relative to the reference location.*/
	public CompoundShape(Color color, Vec2f refLoc, Shape...shapes) {
		super(new Vec2f(0, 0), new Vec2f(0, 0));
		_shapes = new ArrayList<Shape>(shapes.length);
		for (int i=0; i<shapes.length; i++) {
			shapes[i].setColor(color);
			_shapes.add(shapes[i]);
		}
		_refLocation = refLoc;
		super.setLocation(refLoc);
	}
	
	/*Get array of shapes stored in CompoundShape*/
	public Shape[] getShapes() {
		Shape[] shapes = new Shape[_shapes.size()];
		return _shapes.toArray(shapes);
	}
	
	/*Gets/Sets location of each Shape relative to
	 * the new reference location.*/
	@Override
	public Vec2f getLocation() {
		return super.getLocation();
	}
	@Override 
	public void setLocation(Vec2f newLoc) {	
		float dx = newLoc.x - _refLocation.x;
		float dy = newLoc.y - _refLocation.y;	
		for (Shape s: _shapes) {
			Vec2f currLoc = s.getLocation();
			s.setLocation(new Vec2f(currLoc.x+dx, currLoc.y+dy));
		}
		super.setLocation(newLoc);
		_refLocation = newLoc;
	}
	/*X/Y separated accessors/mutators used by
	 * MovingEntity in move(dx,dy) method. Sets
	 * X of every Shape maintaining each of their
	 * positions relative to reference curr location.x.*/
	@Override
	public float getX() {
		return _refLocation.x;
	}
	@Override
	public void setX(float x) {
		this.setLocation(new Vec2f(x, _refLocation.y));
		super.setX(x);
	}

	@Override
	public float getY() {
		return _refLocation.y;
	}
	public void setY(float y) {
		this.setLocation(new Vec2f(_refLocation.x, y));
	}
	
	/*Sets/Gets color of each Shape in Compound,
	 * and updates references in superclass.*/
	@Override
	public Color getColor() {
		return super.getColor();
	}
	@Override
	public void setColor(Color col) {
		super.setColor(col);
		for (Shape s: _shapes) {
			s.setColor(col);
		}
	}
	
	/*Takes in index of component shape
	 * in the _shapes list and sets the 
	 * color of only that shape.*/
	public void setComponentColor(Color col, int index) {
		_shapes.get(index).setColor(col);
	}
	
	@Override
	public void setBorderWidth(float width) {
		super.setBorderWidth(width);
		for (Shape s: _shapes) {
			s.setBorderWidth(width);
		}
	}
	@Override
	public void setBorderColor(Color color) {
		super.setBorderColor(color);
		for (Shape s: _shapes) {
			s.setBorderColor(color);
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		for (miweinst.engine.gfx.shape.Shape s: _shapes) {
			s.draw(g);
		}
	}
	
	@Override 
	public void setOutline(Color col, float width) {
		super.setOutline(col, width);
		this.setBorderColor(col);
		this.setBorderWidth(width);
		this.setColor(new Color(0, 0, 0, 0));
	}
	
////
	public CompoundShape generateRandom(Vec2f loc, Vec2f bounds) {
		
		return null;
	}
	
	/*True if point contained by any Shape*/
	@Override
	public boolean contains(Vec2f pnt) {
		for (Shape s: _shapes) {
			if (s.contains(pnt)) return true;
		}
		return false;
	}

	
	//Implementation of Collision Detection shapeCollisionDetection; double dispatch

	@Override
	public boolean collides(Shape s) {
		return s.collidesCompound(this);
	}

	@Override
	public boolean collidesCircle(CircleShape c) {
 		for (Shape s: _shapes) {
			if (c.collides(s)) return true;
		}
		return false;
	}

	@Override
	public boolean collidesAAB(AARectShape aab) {
		for (Shape s: _shapes) {
			if (aab.collides(s)) return true;
		}
		return false;
	}

	@Override
	public boolean collidesCompound(CompoundShape c) {
		// TODO Auto-generated method stub
		for (Shape is: c.getShapes()) {
			for (Shape js: _shapes) {
				if (is.collides(js)) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean collidesPolygon(PolygonShape p) {
		//Reuse code for equivalent algorithm
		return p.collidesCompound(this);
	}
}
