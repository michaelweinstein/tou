package miweinst.engine.util;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;


public class App extends SwingFrontEnd {
	
	private static final Vec2f DEFAULT_WINDOW_SIZE = new Vec2f(960, 540);

	//onResize takes in a Vec2i
	public Vec2f _currWindowSize;
	
	//Polymorphically calls game subclasses of Screen
		//Keep updating in setScreen method
	public Screen _currScreen;
	
	//Can load another Screen behind the visible one, to set visible on method call.
	public Screen _hiddenScreen;
	//Indicates whether a screen is hiding
	public boolean _haveHiddenScreen;
	

	public App(String title, boolean fullscreen) {
		super(title, fullscreen);
		
		_haveHiddenScreen = false;
		
		//Start with DEFAULT_WINDOW_SIZE defined in CS195NFrontEnd
		_currWindowSize = DEFAULT_WINDOW_SIZE;
	}
	
	/**
	 * Updates _currScreen reference.
	 * 
	 * @param screen
	 */
	public void setScreen(Screen screen) {
		_currScreen = screen;
	}
	
	public void setHiddenScreen(Screen screen) {
		_hiddenScreen = screen;
		_haveHiddenScreen = true;
	}
	
	public boolean haveHiddenScreen() {
		return _haveHiddenScreen;
	}
	
	public void showHiddenScreen() {
		_currScreen = _hiddenScreen;
		_hiddenScreen = null;
		_haveHiddenScreen = false;
	}
	

	public Vec2f getDimensions() {
		return _currWindowSize;
	}	

	@Override
	/**
	 * Engine updates the screen on every onTick
	 */
	protected void onTick(long nanosSincePreviousTick) {
		_currScreen.onTick(nanosSincePreviousTick);
	}

	@Override
	/**
	 * The engine draws the screen on every onDraw
	 */
	protected void onDraw(Graphics2D g) {
		_currScreen.onDraw(g);
	}

	@Override
	protected void onKeyTyped(KeyEvent e) {
		_currScreen.onKeyTyped(e);
	}

	@Override
	protected void onKeyPressed(KeyEvent e) {
		_currScreen.onKeyPressed(e);
	}

	@Override
	protected void onKeyReleased(KeyEvent e) {
		_currScreen.onKeyReleased(e);
	}

	@Override
	protected void onMouseClicked(MouseEvent e) {
		_currScreen.onMouseClicked(e);
	}

	@Override
	protected void onMousePressed(MouseEvent e) {
		_currScreen.onMousePressed(e);
	}

	@Override
	protected void onMouseReleased(MouseEvent e) {
		_currScreen.onMouseReleased(e);
	}

	@Override
	protected void onMouseDragged(MouseEvent e) {
		_currScreen.onMouseDragged(e);
	}

	@Override
	protected void onMouseMoved(MouseEvent e) {
		_currScreen.onMouseMoved(e);
	}

	@Override
	protected void onMouseWheelMoved(MouseWheelEvent e) {
		_currScreen.onMouseWheelMoved(e);
	}

	@Override
	protected void onResize(Vec2i newSize) {				
		_currScreen.onResize(newSize);		
		//Keep track of the updated window size
		_currWindowSize = new Vec2f(newSize);		
	}
	
}
