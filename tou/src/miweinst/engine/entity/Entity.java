package miweinst.engine.entity;

import java.awt.Graphics2D;

import miweinst.engine.util.GameWorld;
import miweinst.engine.util.Vec2f;

/**
 * Single logical object within the game. 
 * 
 * Everything within Entity and its subclasses 
 * are set in game units. GameWorld will convert units
 * to pixels based on the value passed to it by Viewport
 * indicating game world origin in pixels.
 * 
 * @author miweinst
 *
 */

public abstract class Entity {
	private Vec2f _location;
	private Vec2f _dimensions;	
	private boolean _visible;
	
	public Entity(GameWorld world) {
		_location = new Vec2f(0, 0);
		_dimensions = new Vec2f(1, 1);
		_visible = true;
	}
	
	public boolean isVisible() {
		return _visible;
	}
	public void setVisible(boolean visible) {
		_visible = visible;
	}
	
	public Vec2f getLocation() {
		return _location;
	}
	public void setLocation(Vec2f loc) {
		_location = loc;
	}
	
	public float getX() {
		return _location.x;
	}
	public void setX(float x) {
		_location = new Vec2f(x, _location.y);
	}
	public float getY() {
		return _location.y;
	}
	public void setY(float y) {
		_location = new Vec2f(_location.x, y);
	}
	
	public Vec2f getDimensions() {
		return _dimensions;
	}
	public void setDimensions(Vec2f dim) {
		_dimensions = dim;
	}
	
	public float getWidth() {
		return _dimensions.x;
	}
	public void setWidth(float w) {
		_dimensions = new Vec2f(w, _dimensions.y);
	}
	public float getHeight() {
		return _dimensions.y;
	}
	public void setHeight(float h) {
		_dimensions = new Vec2f(_dimensions.x, h);
	}
	
	/*Depends entirely on shape of bounds of entity*/
	public abstract boolean contains(Vec2f pnt);
	
	/*Has to draw the shapes or sprites, not declared in superclass*/
	public abstract void draw(Graphics2D g);
}
