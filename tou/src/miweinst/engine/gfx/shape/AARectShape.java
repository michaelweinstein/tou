package miweinst.engine.gfx.shape;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import miweinst.engine.util.Vec2f;

/**
 * Axis-Aligned Rectangle Shape. Ensures that
 * this Rect cannot set rotation in superclass
 * Shape. Its own class so methods involved in 
 * algorithms like collision detection can 'know'
 * for certain that the incoming Rect is Axis-Aligned.
 * @author miweinst
 */

public class AARectShape extends Shape {
	
	private Rectangle2D _rect;

	public AARectShape(Vec2f loc, Vec2f dim) {
		super(loc, dim);
		_rect = new Rectangle2D.Float(loc.x, loc.y, dim.x, dim.y);
		super.setShape(_rect);
	}
	
	/*Returns Vec2f storage of point at center*/
	public Vec2f getCenter() {
		return new Vec2f((float)_rect.getCenterX(), (float)_rect.getCenterY());
	}
	
	/*Returns the Maximum and Minimum X and Y values on frame*/
	public float getMaxX() {
		return (float)_rect.getMaxX();
	}
	public float getMaxY() {
		return (float) _rect.getMaxY();
	}
	public float getMinX() {
		return (float) _rect.getMinX();
	}
	public float getMinY() {
		return (float) _rect.getMinY();
	}
	
	/*Returns a copy of this shape. Useful for repetitive features*/
	public Rectangle2D.Float clone() {
		return (Rectangle2D.Float) _rect.clone();
	}
	
	/*Returns whether point is within AA rectangle. Algorithm
	 * checks point relative to min/max of axis-aligned rectangle.*/
	public boolean contains(Vec2f v) {
		if (this.getMinX() < v.x && this.getMaxX() > v.x) {
			if (this.getMinY() < v.y && this.getMaxY() > v.y){
				return true;
			}
		}
		return false;
	}

	public void draw(Graphics2D g) {
		_rect.setFrame(super.getX(), super.getY(), super.getWidth(), super.getHeight());
		super.draw(g);
	}

	//Implementation of Collision Detection shapeCollisionDetection; double dispatch
	
	@Override
	public boolean collides(Shape s) {
		return s.collidesAAB(this);
	}

	@Override
	public boolean collidesCircle(CircleShape circ) {
		//Forward to where algorithm already defined
		return circ.collidesAAB(this);
	}

	@Override
	public boolean collidesAAB(AARectShape aab) {
		if (this.getMinX() < aab.getMaxX() && this.getMaxX() > aab.getMinX()) {
			if (this.getMinY() < aab.getMaxY() && this.getMaxY() > aab.getMinY()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean collidesCompound(CompoundShape c) {
		//Forward to where algorithm already defined
		return c.collidesAAB(this);
	}
	
	@Override
	public boolean collidesPolygon(PolygonShape p) {
		//Reuse code for algorithm
		return p.collidesAAB(this);
	}
}
