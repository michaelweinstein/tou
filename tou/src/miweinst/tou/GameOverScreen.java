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

public class GameOverScreen extends TouScreen {
	private App _app;
	private Shape _background;
	private Text _loseText;
	private Text _instructionText;
	private Text _extraText;
	public GameOverScreen(App a) {
		super(a);
		_app = a;
		super.setBackgroundColor(Color.RED);
		_background = super.getBackground();
		String s = "You lose.";
		_loseText = new Text(_background, s, new Vec2f(0,_background.getHeight()/3));
		_loseText.setFontSize(80);
		_loseText.centerTextHorizontal();
		
		s = "Press any key to get back to the start.";
		_instructionText = new Text(_background, s, new Vec2f(0, _background.getHeight()*2/3));
		_instructionText.setFontSize(30);
		_instructionText.centerTextHorizontal();
		
		s =  "Except for the one that self destructs.";
		_extraText = new Text(_background, s, new Vec2f(0, _background.getHeight()*3/4));
		_extraText.setFontSize(18);
		_extraText.centerTextHorizontal();
	}

	@Override
	public void onTick(long nanosSincePreviousTick) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onDraw(Graphics2D g) {
		super.onDraw(g);
		_loseText.draw(g);
		_instructionText.draw(g);
		_extraText.draw(g);
	}

	@Override
	public void onKeyTyped(KeyEvent e) {
		super.onKeyTyped(e);
		if (e.getKeyChar() != ' ') {
			_app.setScreen(new MenuScreen(_app));
		}
	}

	@Override
	public void onKeyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onKeyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMouseClicked(MouseEvent e) {
		_app.setScreen(new MenuScreen(_app));
	}

	@Override
	public void onMousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResize(Vec2i newSize) {
		super.onResize(newSize);
		_loseText.centerTextHorizontal();
		_loseText.setLocation(new Vec2f(_loseText.getLocation().x, _background.getHeight()/3));
		_instructionText.centerTextHorizontal();
		_instructionText.setLocation(new Vec2f(_instructionText.getLocation().x, _background.getHeight()*2/3));
		_extraText.centerTextHorizontal();
		_extraText.setLocation(new Vec2f(_extraText.getLocation().x, _background.getHeight()*3/4));
	}
}
