package miweinst.tou;

import miweinst.engine.util.Vec2f;
import miweinst.engine.util.Vec2i;

public abstract class Defaults {

	//Set same as frame container for Tou
//	public static final Vec2i VIEWPORT_DIMENSIONS = new Vec2i(960, 540);
	
	public static final int SCALE = 20;
	
	//VIEWPORT / SCALE = GAME_DIMENSIONS : if Game Dimensions are exactly fit
	public static final Vec2i GAME_DIMENSIONS = new Vec2i(48, 27);
	
	//Left edge is end of world; enemies spawn right.
	public static final Vec2f PXL_GAME_LOCATION = new Vec2f(0, 0);

}
