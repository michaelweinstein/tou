package miweinst.engine.gfx.shape;

import miweinst.engine.util.Vec2f;

	/**
	 * Extends EllipseShape as a guaranteed
	 * circle. Allows collision detection algorithms
	 * to assume that the shape is a perfect circle.
	 * Overrides methods that would manipulate dimensions
	 * of EllipseShape, and adds a setRadius method.
	 * @author miweinst
	 *
	 */

public class CircleShape extends EllipseShape {
	
	private float _radius;

	//Takes radius (float) in constructor, not Dimensions
	public CircleShape(Vec2f loc, float radius) {
		super(loc, new Vec2f(2*radius, 2*radius));
		_radius = radius;
	}
	
	public float getRadius() {
		return _radius;
	}
	/*Only circle-specific method.*/
	public void setRadius(float radius) {
		this.setDimensions(new Vec2f(2*radius, 2*radius));
		_radius = radius;
	}
	
	@Override
	public void setWidth(float w) {
		this.setDimensions(new Vec2f(w, w));
		_radius = w/2;
	}
	@Override
	public void setHeight(float h) {
		this.setDimensions(new Vec2f(h, h));
		_radius = h/2;
	}
	@Override
	public void setDimensions(Vec2f dim) {
		if (dim.x == dim.y) {
			super.setDimensions(dim);
			_radius = dim.x/2;
		}
		else System.out.println(dim + "Method Override: Use Circle.setRadius instead... (Circle.setDimensions)");
	}
	
	
	/*Implements algorithm for point-circle collision. 
	 * Returns true if point is contained in circle. */
	@Override
	public boolean contains(Vec2f v) {
		float dist = this.getCenter().dist(v);
		if (dist < this.getRadius()) {
			return true;
		}
		else return false;
	}
	
	//Implementation of Collision Detection shapeCollisionDetection; double dispatch
	
	@Override
	public boolean collides(Shape s) {
		return s.collidesCircle(this);
	}

	@Override
	public boolean collidesCircle(CircleShape c) {
		boolean collides = false;
		//Distance between centers
		float dist = this.getCenter().dist(c.getCenter());
		float sumRad = this.getRadius() + c.getRadius();
		//If distance b/w centers is less than sum rads
		if (dist < sumRad) {
			collides = true;
		}
		else collides = false;
		return collides;
	}

	@Override
	public boolean collidesAAB(AARectShape aab) {
		//If contains, return true
		if (aab.contains(this.getCenter())) {
			return true;
		}
		Vec2f c = this.getCenter();
		float px = c.x;
		float py = c.y;
		//Clamp point values to min and max
		if (c.x < aab.getMinX()) px = aab.getMinX();
		if (c.x > aab.getMaxX()) px = aab.getMaxX();
		if (c.y < aab.getMinY()) py = aab.getMinY();
		if (c.y > aab.getMaxY()) py = aab.getMaxY();
		
		//Point-circle collision factored out to contains() above^
		return this.contains(new Vec2f(px, py));
	}

	/*Reuse Compound-Circle collision code in CompoundShape*/
	@Override
	public boolean collidesCompound(CompoundShape c) {
		return c.collidesCircle(this);
	}

	/*Reuse Polygon-Circle collision code in PolygonShape*/
	@Override
	public boolean collidesPolygon(PolygonShape p) {
		return p.collidesCircle(this);
	}
}
