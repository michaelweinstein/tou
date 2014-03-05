package miweinst.engine.util;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import miweinst.engine.entity.Entity;
import miweinst.engine.gfx.shape.AARectShape;
import miweinst.engine.gfx.shape.Shape;

/**
 * This class should be subclassed for any
 * game with its own GameWorld, larger
 * than the visible area.
 */

public class GameWorld {
	
	/*Vars to PROTECTED because any GameWorld subclass
	 * should receive these values passed into world
	 * by Viewport, because GameWorld  has primary role
	 * of communicating between game units and pixels*/
	protected int scale;	//easier for subclass
	protected Vec2f pxlgameloc;		//easier for subclass

	//Factor for screen size (pxl) / game size (gu)
	private int _scale;	
	//Location of game world origin (upper left), converted to pixels
	private Vec2f _pxlGameLoc;	
	//Dimensions of game world
	private Vec2f _dim;	
	//The Shape (initial: AARectShape) containing game world 
	private Shape _container;	
	private Entity _entity;
	private Entity[] _entityArr;	
			
	public GameWorld(App app, Vec2f dim) {
		_dim = dim;		
		//This is a functionally abstract object, NOT DRAWN.
		_container = new AARectShape(new Vec2f(0,0), dim);
		//Value only set by in mutator by Viewport.
		_pxlGameLoc = new Vec2f(0,0);
		//Initialize scale
		_scale = scale = 20;
		
		_entity = null;
	}
	
	/*Returns Shape (Rect) of Game World. 
	 *Just used to store location/dimensions.
	 *Only generic class without curr shape set
	 *because it is never drawn.*/
	public Shape getContainer() {
		return _container;
	}
	
	/* Returns size of Game World in game units */
	public Vec2f getDimensions() {
		return _dim;
	}
	public void setDimensions(Vec2f dim) {
		_dim = dim;
	}
	
	public void setScale(float newScale) {
		_scale = scale = (int) newScale;
	}
	public int getScale() {
		return _scale;
	}
	
	/*Accessor/Mutator for screen location of the game origin,
	 * used for conversion between game units and pixels for all
	 * measurements inside the GameWorld. 
	 * 
	 * - Mutator called by Viewport to set this value.
	 * - Accessor allows any class with reference to GameWorld
	 * to get the value as well.*/
	public void setPixelGameLocation(Vec2f pxl) {
		_pxlGameLoc = pxlgameloc = pxl;
	}
	public Vec2f getPixelGameLocation() {
		return _pxlGameLoc;
	}
	
	/* Calculates pixel equivalent of specified point
	 * in game units, based on pixelGameLocation that
	 * is passed in by Viewport. GameWorld is the 
	 * container of all game objects, which are 
	 * all set in game units. */
	public Vec2f toPixels(Vec2f guLoc) {
		float newX = _pxlGameLoc.x + guLoc.x*_scale;
		float newY = _pxlGameLoc.y + guLoc.y*_scale;
		Vec2f pxlLoc = new Vec2f(newX, newY);
		return pxlLoc;
	}
	public Vec2f toUnits(Vec2f pxlLoc) {
		float newX = (pxlLoc.x - _pxlGameLoc.x)/_scale;
		float newY = (pxlLoc.y - _pxlGameLoc.y)/_scale;
		Vec2f unitLoc = new Vec2f(newX, newY);
		return unitLoc;
	}

	/** Protected: Subclasses can set Entity that 
	 * GameWorld draws in its AffineTransform. 
	 * So AffineTransform is generic, and does 
	 * not have to be defined every subclass.*/
	protected void setEntity(Entity curr) {
		_entity = curr;
	}
	protected void setEntityArray(Entity[] curr) {
		_entityArr = curr;
	}
	
	/* AffineTransform is defined by scale and 
	 * pixelGameOrigin in superclass. Draw
	 * passes each Entity to superclass to
	 * get drawn within the Transform. 
	 * The Graphics object does not have to be clipped in this method because
	 * the clipRect is taken care of in Viewport.*/
	public void draw(Graphics2D g) {	
			//Transform matrix: [[X scale, X shear, Y shear], [Y scale, X translate, Y translate]...]
			AffineTransform tx = new AffineTransform(_scale, 0, 0, _scale, _pxlGameLoc.x, _pxlGameLoc.y);
			AffineTransform tSave = g.getTransform();
			g.transform(tx);
			
			if (_entity != null) {
				_entity.draw(g);
			}
			
			if (_entityArr != null) {
				for (int i=0; i<_entityArr.length; i++) {
					if (_entityArr[i] != null) {
						_entityArr[i].draw(g);
					}
				}
			}
			
			g.setTransform(tSave);
	}
}
