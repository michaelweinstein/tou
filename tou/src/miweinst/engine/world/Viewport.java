package miweinst.engine.world;

import java.awt.Color;
import java.awt.Graphics2D;

import miweinst.engine.gfx.shape.AARectShape;
import miweinst.engine.gfx.shape.Shape;
import miweinst.engine.util.GameWorld;
import miweinst.engine.util.Vec2f;
import miweinst.engine.util.Vec2i;

public class Viewport {	
	
	//Shape of Viewport screen, container with border
//	private Shape _screen;
	private AARectShape _screen;
	
	//World of game space in game units
	private GameWorld _game;
	
	//Dimensions of viewport, passed in constructor
	private Vec2f _screenDim;	
	
	//Dimensions of game world, passed in constructor
//	private Vec2f _gameDim;
	
	//Upper left of screen in pixels
	private Vec2f _screenLoc;
	
	//Upper left of screen in game units
	private Vec2f _screenGameLoc;
	
	/*Upper left of game world in pixels.
	 * (This is the reference sent to world 
	 * and used for entity unit conversion)*/
	private Vec2f _pxlGameLoc;
	
	//Scale in px/unit 
	private float _scale;
	
	//Container is the rectangle Viewport screen; (Not drawn, so can be generic superclass)
	//gameDim is the size of the game world, in game units
	public Viewport(Shape container, GameWorld game) {	
		//Reference to GameWorld
		_game = game;	
		
		//Constructor Default: 400x200
		_screenDim = container.getDimensions();
		
		//Constructor Default: container x and y
		_screenLoc = new Vec2f(container.getX(), container.getY());
		
		//Constructor Default: 480x240 (4800x2400px at scale=100; 12 screen sizes in game)
//		_gameDim = game.getDimensions();
		
		//Default scale; modified in mutator
		_scale = 20;
		
		//Default starting game view; upper left flush with world's
		_screenGameLoc = new Vec2f(0, 0); 
		
		//Initialize; calculated based on current _screenGameLoc 
		_pxlGameLoc = this.gamePointToScreen(new Vec2f(0, 0));
		//Send initial pxlGameLoc value to GameWorld
		_game.setPixelGameLocation(_pxlGameLoc);
		
		//Viewport screen; visible section of game map, in pixels
		_screen = new AARectShape(_screenLoc, _screenDim);
	}
	
	/**
	 * Mutator for scale. 
	 * This method
	 * can be used for zoom functionality.
	 * Scale is in pixels per game coordinate unit.
	 * 
	 * @param scale; conversion scalar from game units to pixels
	 */
	public void setScale(float scale) {
		_scale = scale;
		_game.setScale(scale);
	}
	/**Accessor for scale*/
	public float getScale() {
		return _scale;
	}
	
	/**
	 * This method takes in a point in the game world
	 * and translates it into a point on the screen.
	 * 
	 * @param Vec2f gamePoint; a point in the game world, in game units
	 * @return Vec2f; a point on the screen, in pixels
	 */
	public Vec2f gamePointToScreen(Vec2f gamePoint) {
		float newX = gamePoint.x - _screenGameLoc.x;
		float newY = gamePoint.y - _screenGameLoc.y;
		newX = newX*_scale;
		newY = newY*_scale;		
		newX += _screenLoc.x;
		newY += _screenLoc.y;
		
		return new Vec2f(newX, newY);
	}
	
	/**
	 * This method takes in a point on the screen
	 * and translates it into a point in the game.
	 * 
	 * @param Vec2f screenPoint; a point on the screen, in pixels
	 * @return Vec2f; a point in the game, in game units
	 */
	public Vec2f screenPointToGame(Vec2f screenPoint) {
		
		float dx = screenPoint.x - _screenLoc.x;
		float dy = screenPoint.y - _screenLoc.y;
		dx = dx/_scale;
		dy = dy/_scale;
		
		float newX = _screenGameLoc.x + dx;
		float newY = _screenGameLoc.y + dy;
		
		//In game units
		return new Vec2f(newX, newY);
	}
	
	/**
	 * These methods are called in GameWorld and
	 * TacMap draw methods to transform from
	 * game units to pixels. They are not functionally
	 * necessary but I wrote them for simplicity, because
	 * the game location and dimensions are how the
	 * draw methods convert to pixels.
	 * @return
	 */
/*	public Vec2f getPixelGameLoc() {
		return this.screenPointToGame(new Vec2f(0,0));
	}
	public Vec2f getPixelGameSize() {
		return new Vec2f(_gameDim.x*_scale, _gameDim.y*_scale);
	}*/
	
	/**
	 * This is a mutator for the size of the Viewport
	 * screen, which will be set depending on the 
	 * application. This value is passed into the
	 * constructor, but can modified later using
	 * this method. 
	 * The size of the screen is the square size 
	 * of the game map that is visible at any given time.
	 * The screen size is in pixels.
	 * 
	 * @param Vec2f newSize; size of visible viewport screen
	 */
	public void setScreenSize(Vec2f newSize) {
		_screenDim = newSize;
	}
	
	public Vec2f getScreenSize() {
		return _screenDim;
	}
	
	/**
	 * This is a mutator for the location of the Viewport
	 * screen. It is initialized t0 (200,200) by default,
	 * but should be set depending on the application from
	 * any subclass.
	 * The screen location is in pixels.
	 * @param Vec2f newLoc; upper left of viewport screen in pixels
	 */
	public void setScreenLoc(Vec2f newLoc) {
		_screenLoc = newLoc;
	}
	public Vec2f getScreenLoc() {
		return _screenLoc;
	}
	
	/*Background color of Viewport screen*/
	public void setScreenColor(Color col) {
		_screen.setColor(col);
	}
	
	/**
	 * Mutator to set upper left location of Viewport
	 * screen in game units. Defaults in Viewport constructor
	 * to 0,0. But should be able to be set for specific games
	 * outside of Viewport.
	 * @param screenInGameUnits
	 */
	public void setScreenInGameLoc(Vec2f screenInGameUnits) {
		_screenGameLoc = screenInGameUnits;
	}	
	public Vec2f getScreenInGameLoc() {
		return _screenGameLoc;
	}
	
	/**
	 * Updates the reference to world origin location in pixels.
	 * Returns the new value when updated. No accessors or 
	 * mutators really necessary for pixel game location 
	 * because this method is called to update the var
	 * and the GameWorld's var whenever it is modified (pan, zoom).
	 * 
	 * Must be called whenever _screenGameLoc is updated.
	 * @param pxl
	 */
	public void updatePixelGameLocation() {
		Vec2f pxl = gamePointToScreen(new Vec2f(0, 0));
		_pxlGameLoc = pxl;
		_game.setPixelGameLocation(pxl);
	}
	
	/**
	 * Accessor for pixelGameLocation for classes
	 * not passed it explicitly through updatePixel...() ^.
	 * @return
	 */
	public Vec2f getPixelGameLocation() {
		return _pxlGameLoc;
	}
	
	/**
	 * This method takes in the change in x and y
	 * between prev and curr (prev-curr) from mouseDragged 
	 * in GameScreen. It converts the values to their
	 * equivalent in game units, and then adds them
	 * to the current x and y location of the game world,
	 * which is the upper left of the screen mapped to game units.
	 * 
	 * @param float dx, change in x between prev and curr mouse position, pxls
	 * @param float dy, change in y between prev and curr mouse position, pxls
	 */
	public void pan(float dx, float dy) {	
		float gamedx = dx/_scale;
		float gamedy = dy/_scale;
		Vec2f currLoc = _screenGameLoc;
		_screenGameLoc = new Vec2f(currLoc.x+gamedx,currLoc.y+gamedy);
		this.updatePixelGameLocation();
	}
	
	/**
	 * This is called to zoom in and out on the game world from the 
	 * Viewport's center. Uses the conversion of scale (pxls/game unit) to
	 * change location of _screenGameLoc, which is the upper corner
	 * of Viewport in game world. This zooms from the center of
	 * the screen.
	 * 
	 * @param newScale
	 */
	public void zoom(float newScale, Vec2f mouseLoc) {
					
		float oldmapVisWidth = _screenDim.x/_scale;
		float oldmapVisHeight = _screenDim.y/_scale;		
		float mapVisWidth = _screenDim.x/newScale;
		float mapVisHeight = _screenDim.y/newScale;
		
		float dwidth = Math.abs(oldmapVisWidth - mapVisWidth);
		float dheight = Math.abs(oldmapVisHeight - mapVisHeight);			
		if (_scale <= newScale) {
			//Zoom in
			_screenGameLoc = new Vec2f(_screenGameLoc.x+dwidth/2, _screenGameLoc.y+dheight/2);
		}
		else {
			//Zoom out
			_screenGameLoc = new Vec2f(_screenGameLoc.x-dwidth/2, _screenGameLoc.y-dheight/2);
		}				
		this.setScale(newScale);	
		this.updatePixelGameLocation();
	}
		
	public void draw(Graphics2D g) {
		if (_game != null) {
			_screen.setDimensions(_screenDim);
			_screen.draw(g);	
			
			//Store reference to current clip
			java.awt.Shape clip = g.getClip();	
			
			//Clip graphics to the viewport screen rectangle
			Vec2i screenLoc = new Vec2i((int) _screenLoc.x, (int) _screenLoc.y);
			Vec2i screenDim = new Vec2i((int) _screenDim.x, (int) _screenDim.y);
			g.clipRect(screenLoc.x, screenLoc.y, screenDim.x, screenDim.y);
	
//			Vec2f pxlGameOrigin = gamePointToScreen(new Vec2f(0,0));				
			//Draw game with clip on, in pixels
			_game.draw(g);
			
			//Restore the clip
			g.setClip(clip);	
		}
	}
}
