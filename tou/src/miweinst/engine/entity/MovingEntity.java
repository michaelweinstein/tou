package miweinst.engine.entity;

import java.awt.Color;
import java.awt.Graphics2D;

import miweinst.engine.gfx.shape.Shape;
import miweinst.engine.util.GameWorld;
import miweinst.engine.util.Vec2f;

/**
 * This subclass defines a game object that
 * moves. That means that this Entity has a 
 * Shape, set with methods that mutate properties.
 * @author miweinst
 *
 */

public abstract class MovingEntity extends Entity {
	
	private Shape _shape;
	
	private float _dx;
	private float _dy;
	private boolean _freeMoving;
	
	public MovingEntity(GameWorld world) {
		super(world);
		_shape = null;
		_dx = 0;
		_dy = 0;
		_freeMoving = false;
	}
	
	/* This method is only used by subclasses
	 * to set the shape that is mutated by
	 * the methods in this class. Also matches
	 * the location and dimensions references in
	 * Entity to the Shape's location.*/
	protected void setShape(Shape shape) {
		if (shape != null) {
			_shape = shape;
			this.setLocation(shape.getLocation());
			this.setDimensions(shape.getDimensions());
		}
		else System.out.println("Shape is null (MovingEntity.setShape");
	}
	protected Shape getShape() {
		return _shape;
	}
	protected void voidShape() {
		_shape = null;
	}
	
	/*Partial override of super methods
	 *  in order to set attributes of shape.*/
	public void setLocation(Vec2f loc) {
		super.setLocation(loc);
		if (_shape != null) _shape.setLocation(loc);
	}
	public void setX(float x) {
		super.setX(x);
		if (_shape != null)_shape.setX(x);
	}
	public void setY(float y) {
		super.setY(y);
		if (_shape != null)_shape.setY(y);
	}
	public void setDimensions(Vec2f dim) {
		super.setDimensions(dim);
		if (_shape != null)_shape.setDimensions(dim);
	}
	public void setWidth(float w) {
		super.setWidth(w);
		if (_shape != null)_shape.setWidth(w);
	}
	public void setHeight(float h) {
		super.setHeight(h);
		if (_shape != null)_shape.setHeight(h);
	}
	/*If MovingEntity is using Shape, color accessor/mutator.*/
	public Color getShapeColor() {
		if (_shape != null) return _shape.getColor();
		else return null;
	}
	public void setShapeColor(Color color) {
		if (_shape != null) _shape.setColor(color);
	}
	
	public boolean isFreeMoving() {
		return _freeMoving;
	}
	//Sets free move; i.e. move by dx/dy vars on tick
	public void setFreeMoving(boolean moving) {
		_freeMoving = moving;
	}
	//Accessors and Mutators for Free Move
	public float getDx() {
		return _dx;
	}
	public void setDx(float dx) {
		_dx = dx;
	}
	public float getDy() {
		return _dy;
	}
	public void setDy(float dy) {
		_dy = dy;
	}
	
	/*Moving entities need tick*/
	public void onTick(long nanosSincePreviousTick) {
		if (_freeMoving) {
			this.move(_dx, _dy);
		}
	}
	
	/*Takes in a dx and dy corresponding
	 * to the change in location on each tick.*/
	public void move(float dx, float dy) {
		this.setX(this.getX()+dx);
		this.setY(this.getY()+dy);
	}
	
	/*Whether specified point is inside the
	 * Shape in this Entity. Game Units. */
	@Override
	public boolean contains(Vec2f pnt) {
		boolean isIn = false;
		if (_shape != null) {
			isIn = _shape.contains(pnt);
		}
		return isIn;
	}
	
	/*Forwards collision detection to associated shape. If
	 * one the shapes of one of the MovingEntity 
	 * instances is null, returns false and prints error.*/
	public boolean collides(MovingEntity s) {
		if (_shape != null && s.getShape() != null) {
			return _shape.collides(s.getShape());
		}
		System.out.println("Error: One of the MovingEntity shapes is null. (MovingEntity.collides)");
		return false;
	}

	@Override
	public void draw(Graphics2D g) {
		if (_shape != null) {
			_shape.draw(g);
		}
	}
}
