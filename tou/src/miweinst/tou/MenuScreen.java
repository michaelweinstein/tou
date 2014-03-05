package miweinst.tou;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import miweinst.engine.gfx.Text;
import miweinst.engine.gfx.shape.Shape;
import miweinst.engine.util.App;
import miweinst.engine.util.Vec2f;
import miweinst.engine.util.Vec2i;

public class MenuScreen extends TouScreen {
	private App _app;
	private Shape _background;
	private Text _startText;
	
	private float _dx;
	private float _dy;
	public MenuScreen(App a) {
		super(a);
		_app = a;
		_background = super.getBackground();
		_background.setColor(new Color(238, 246, 130));
		
		String s = "Click mouse to start";
		_startText = new Text(_background, s, _background.getLocation());
		_startText.setFontSize(75);
		this.onResize(new Vec2i((int)_background.getDimensions().x, (int)_background.getDimensions().y));
		//Initial rate of moving title
		_dx = .48f;
		_dy = -.9f;
	}

	@Override
	public void onTick(long nanosSincePreviousTick) {
		Vec2f oldLoc = _startText.getLocation();		
		if (oldLoc.x + _startText.getBoundingBox().x >= _background.getDimensions().x) {
			_dx = -1*(Math.abs(_dx) + .25f);
		}
		if (oldLoc.x <= 0) {
			_dx = Math.abs(_dx)+.25f;
		}
		if (oldLoc.y >= _background.getDimensions().y){
			_dy = -1*(Math.abs(_dy) +.25f);
		}
		if (oldLoc.y - _startText.getBoundingBox().y/2 <= 0) {
			_dy = Math.abs(_dy) + .25f;
		}
		_startText.setLocation(new Vec2f(oldLoc.x+_dx, oldLoc.y+_dy));
	}

	@Override
	public void onDraw(Graphics2D g) {
		super.onDraw(g);
		_startText.draw(g);
	}

	@Override
	public void onKeyTyped(KeyEvent e) {
		//Handles 'q' for quit
		super.onKeyTyped(e);
		//Because 'm' returns to MainMenu from GameScreen
		if (e.getKeyChar() != 'm') {
			_app.setScreen(new GameScreen(_app));
		}
	}

	@Override
	public void onKeyPressed(KeyEvent e) {
		
	}

	@Override
	public void onKeyReleased(KeyEvent e) {
		
	}

	@Override
	public void onMouseClicked(MouseEvent e) {
		_app.setScreen(new GameScreen(_app));
	}

	@Override
	public void onMousePressed(MouseEvent e) {
		
	}

	@Override
	public void onMouseReleased(MouseEvent e) {
		
	}

	@Override
	public void onMouseDragged(MouseEvent e) {
		
	}

	@Override
	public void onMouseMoved(MouseEvent e) {
		
	}

	@Override
	public void onMouseWheelMoved(MouseWheelEvent e) {
		
	}

	@Override
	public void onResize(Vec2i newSize) {
		super.onResize(newSize);
		_startText.centerTextHorizontal();
		_startText.centerTextVertical();
	}

}
