package miweinst.tou;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import miweinst.engine.gfx.Text;
import miweinst.engine.gfx.shape.AARectShape;
import miweinst.engine.gfx.shape.Shape;
import miweinst.engine.util.App;
import miweinst.engine.util.Vec2f;
import miweinst.engine.util.Vec2i;
import miweinst.engine.world.Viewport;

public class GameScreen extends TouScreen {
	private App _app;
	private TouWorld _world;
	private Viewport _viewport;
	
	private Vec2i _viewportDim;
	private Vec2i _worldDim;
	
	private boolean _paused;
	
	private Text _remainingText;

	public GameScreen(App a) {
		super(a);
		_app = a;
		//Sets Viewport dimensions same as App frame
		_viewportDim = new Vec2i((int)(a.getDimensions().x), (int)(a.getDimensions().y));
		_worldDim = Defaults.GAME_DIMENSIONS;
		Shape viewportContainer = new AARectShape(new Vec2f(0, 0), new Vec2f(_viewportDim)); 
		
		_world = new TouWorld(a, _worldDim);
		_viewport = new Viewport(viewportContainer, _world);
		_viewport.setScale(Defaults.SCALE);
		_viewport.setScreenColor(Color.BLACK);
		
		_paused = false;
		
		int fontSize = 20;
		String s = "Enemies Remaining: " + _world.getRemaining();
		_remainingText = new Text(viewportContainer, s, new Vec2f(10, fontSize));
		_remainingText.setColor(Color.WHITE);
		_remainingText.setFontSize(fontSize);
		
		//If player STARTS in fullscreen, this condition avoids a bug
		if (a.isFullscreen()) this.onResize(new Vec2i((int)a.getDimensions().x, (int)a.getDimensions().y));
	}

	@Override
	public void onTick(long nanosSincePreviousTick) {
		if (_paused != true) _world.onTick(nanosSincePreviousTick);
		String s = "Enemies Remaining: " + _world.getRemaining();
		_remainingText.setString(s);
	}

	@Override
	public void onDraw(Graphics2D g) {
		_viewport.draw(g);
		_world.draw(g);
		_remainingText.draw(g);
	}

	@Override
	public void onKeyTyped(KeyEvent e) {
	}

	@Override
	public void onKeyPressed(KeyEvent e) {
		super.onKeyPressed(e);
		if (e.getKeyChar() == 'q') System.exit(0);
		if (e.getKeyChar() == 'r') {
			_app.setScreen(new GameScreen(_app));
		}
		if (e.getKeyChar() == 'm') {
			_app.setScreen(new MenuScreen(_app));
		}
		if (e.getKeyChar() == 'p') {
			_paused = _paused? false: true;
			if (_paused) _viewport.setScreenColor(Color.WHITE);
			else _viewport.setScreenColor(Color.BLACK);
		}
		_world.onKeyPressed(e);
	}

	@Override
	public void onKeyReleased(KeyEvent e) {
	}

	@Override
	public void onMouseClicked(MouseEvent e) {
	}

	@Override
	public void onMousePressed(MouseEvent e) {
		_world.onMousePressed(e);
	}

	@Override
	public void onMouseReleased(MouseEvent e) {
	}

	@Override
	public void onMouseDragged(MouseEvent e) {
		_world.onMouseDragged(e);
	}

	@Override
	public void onMouseMoved(MouseEvent e) {	
		_world.mouseMoved(e);
	}

	@Override
	public void onMouseWheelMoved(MouseWheelEvent e) {
	}

	@Override
	public void onResize(Vec2i newSize) {
		_viewport.setScreenSize(new Vec2f(newSize));
		_world.setDimensions(new Vec2f(newSize.x/_viewport.getScale(), newSize.y/_viewport.getScale()));
	}

}
