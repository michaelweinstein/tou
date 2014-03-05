package miweinst.tou;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Random;

import miweinst.engine.gfx.Text;
import miweinst.engine.gfx.shape.Shape;
import miweinst.engine.util.App;
import miweinst.engine.util.Vec2f;
import miweinst.engine.util.Vec2i;

public class WinScreen extends TouScreen {
	private App _app;
	private Shape _background;
	private int _flickTimer;
	private Random _g;
	
	private Text _congratsText;
	private Text _winText;

	public WinScreen(App a) {
		super(a);
		_app = a;
		_background = super.getBackground();
		_flickTimer = 0;
		_g = new Random();
		
		String s = "Congratulations!";
		Vec2f loc = new Vec2f(_background.getDimensions().x/3, _background.getDimensions().y/3);
		_congratsText = new Text(_background, s, loc);
		_congratsText.setFontSize(100);
		_congratsText.centerTextHorizontal();
		
		s = "You win.";
		loc = new Vec2f(loc.x, loc.y+_background.getDimensions().y/3);
		_winText = new Text(_background, s, loc);
		_winText.setFontSize(80);
		_winText.centerTextHorizontal();
	}
	
	@Override
	public void onTick(long nanosSincePreviousTick) {
		_flickTimer += nanosSincePreviousTick/1000000;
		if (_flickTimer > 600) {
			//Generate random color
			float r = _g.nextFloat();
			float g = _g.nextFloat();
			float b = _g.nextFloat();
			Color col = new Color(r, g, b);
			super.setBackgroundColor(col);
			_flickTimer = 0;
		}
	}
	
	@Override
	public void onDraw(Graphics2D g) {
		super.onDraw(g);
		_congratsText.draw(g);
		_winText.draw(g);
	}
	
	@Override
	public void onKeyTyped(KeyEvent e) {
		//Handles 'q' for quit
		super.onKeyTyped(e);
		//Avoid accidentally pressing space when win during shooting
		if (e.getKeyChar() != ' ') {
			_app.setScreen(new MenuScreen(_app));
		}
	}
	
	@Override
	public void onMouseClicked(MouseEvent e) {
		_app.setScreen(new MenuScreen(_app));
	}


	@Override
	public void onResize(Vec2i newSize) {
		super.onResize(newSize);
		_winText.centerTextHorizontal();
		_congratsText.centerTextHorizontal();
	}
}
