package miweinst.engine.sprite;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import miweinst.engine.util.Vec2f;
import miweinst.engine.util.Vec2i;

/**
 * This object is a wrapper that takes in a 
 * BufferedImage and basically stores it as a
 * Sprite for the game to use. It works as a 
 * wrapper for BufferedImage, and stores
 * the Sprite's main image along with all 
 * its animation frames.
 * It can take in multiple BufferedImages to store 
 * frames of the same Sprite, for example.
 * The first image in its array of 
 * BufferedImages is set to currImg,
 * which is the default image when
 * an animation is not being used.
 * 
 * This Sprite class 'knows' about all
 * the frames associated with the sprite. 
 * Each sprite is an image, but with references
 * to the other frames of the animation.
 * 
 * @author miweinst
 *
 */

public class Sprite {

	private ArrayList<BufferedImage> _frames;
	
	//Array holding original indices of frames from sprite sheet.
		//Never modified.
	private BufferedImage[] _originalArray;
	
	private BufferedImage _coverFrame;
	private BufferedImage _currFrame;
	
	//Stores the width and height of bounding box on canvas, in game units
	private Vec2f _dstSize;

	//Stores width and height of sprite bounding box from subimage, in pixels
	private Vec2i _srcSize;
	
	public Sprite(Vec2f dstSize, BufferedImage... frames) {
		_frames = new ArrayList<BufferedImage>();
		for (int i=0; i<frames.length; i++) {
			_frames.add(frames[i]);
		}
		_currFrame = _frames.get(0);	
		_coverFrame = _currFrame;
		
		_originalArray = new BufferedImage[_frames.size()];
		_originalArray = _frames.toArray(_originalArray);
		
		_dstSize = dstSize;
		//Dimensions of each frame in Sprite is identical, otherwise animation doesn't work
		_srcSize = new Vec2i(_currFrame.getWidth(), _currFrame.getHeight());
	}
	
	/**
	 * Changes the frame that acts as a 
	 * resting sprite image
	 * @param index
	 */
	public void setCurrFrame(int index) {
		if (_frames.get(index) != null){
			_currFrame = _frames.get(index);
		}
		else {
			System.out.println("Error: There is no frame at that index");
		}
	}
	
	public void setCoverFrame(int index) {
		if (_frames.get(index) != null) {
			_coverFrame = _frames.get(index);
		}
		else {
			System.out.println("Error: There is no frame at that index");
		}
	}
	
	public void showCoverFrame() {
		_currFrame = _coverFrame;
	}
	
	/**
	 * Sets the order of frames for the animation loop.
	 * Takes in an array of integers which specifies the
	 * order the indices in ArrayList _frames should be played.
	 * This method actually changes the order of storage in ArrayList
	 * to match the new order.
	 *  The int[] refers to the indices in the original frames
	 *  order for the Sprite, as in the order contained on 
	 *  sprite sheet. So even when the loop order is changed,
	 *  the int[] should refer to the indices of the original
	 *  sprite-sheet-order when specifying new loop order.
	 * @param order
	 */
	public void setLoopOrder(int[] order) {
		for (int i=0; i<order.length; i++) {
			_frames.set(i, _originalArray[order[i]]);
		}
		_currFrame = _frames.get(0);
		_coverFrame = _frames.get(0);
	}

	/**
	 * Combines the frames of this Sprite with the specified
	 * Sprite. If first is true, then the other sprite's frames
	 * are inserted before the current frames. If first is false,
	 * then the other Sprites frames are added to the end of the loop.
	 * @param other
	 * @param first
	 */
	public void joinSprites(Sprite other, boolean first) {
		if (first == false) {
			_frames.addAll(other.frames());
		}
		else {
			ArrayList<BufferedImage> newLoop = other.frames();
			newLoop.addAll(_frames);
			_frames = newLoop;
		}
	}
	
	/**
	 * Adds a new frame to the end of the loop
	 * @param newFrame
	 */
	public void addFrame(BufferedImage newFrame) {
		_frames.add(newFrame);
	}
	
	/**
	 * Returns the number of frames stored in this Sprite
	 * @return
	 */
	public int looplength() {
		return _frames.size();
	}
	
	/**
	 * Switches current frame to the next in loop arraylist. 
	 * Returns the new frame as a BufferedImage. 
	 * @return
	 */
	public BufferedImage nextFrame() {
		if (_frames.indexOf(_currFrame) != _frames.size()-1) {
			_currFrame = _frames.get(_frames.indexOf(_currFrame)+1);
		}
		else {
			_currFrame = _frames.get(0);
		}
		return _currFrame;
	}
	
	/**
	 * Returns the size of the Sprite in the destination canvas.
	 * @return
	 */
	public Vec2f size() {
		return _dstSize;
	}
	
	/**
	 * Returns the current frame of this Sprite.
	 * @return
	 */
	public BufferedImage getCurrFrame() {
		return _currFrame;
	}
	
	/**
	 * Returns an ArrayList of the frames associated with Sprite.
	 * @return
	 */
	public ArrayList<BufferedImage> frames() {
		return _frames;
	}
	
	/**
	 * This draw method takes in the location of the center of tile already
	 * converted into pixels. It must take in the current scale in order to calculate 
	 * its new width and height.
	 * @param g
	 * @param pxlLoc
	 * @param scale
	 */
	public void draw(Graphics2D g, Vec2f pxlLoc, int scale) {
		//Upper left in destination
		Vec2i dstLoc = new Vec2i((int)pxlLoc.x, (int)pxlLoc.y);
		
		//Upper right in destination
		int dstW = (int)(_dstSize.x*scale);
		int dstH = (int)(_dstSize.y*scale);		
		
		//Upper left in source image
		Vec2i srcLoc = new Vec2i(0, 0);
		
		//Upper right in source image
		Vec2i srcSize = new Vec2i((int)_srcSize.x, (int)_srcSize.y);
		
		//@params:		Image, dst x1, dst y1, dst x2, dst y2, src x1, src y1, src x2, src y2, null		
		g.drawImage(_currFrame, dstLoc.x, dstLoc.y, dstLoc.x+dstW, dstLoc.y+dstH, srcLoc.x, srcLoc.y, srcSize.x, srcSize.y, null);
	}
}























