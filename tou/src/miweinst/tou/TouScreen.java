package miweinst.tou;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import miweinst.engine.gfx.shape.AARectShape;
import miweinst.engine.gfx.shape.Shape;
import miweinst.engine.util.App;
import miweinst.engine.util.Screen;
import miweinst.engine.util.Vec2f;
import miweinst.engine.util.Vec2i;

public class TouScreen extends Screen {
	
	private Shape _background;

	public TouScreen(App a) {
		super(a);
		_background = new AARectShape(new Vec2f(0,0), new Vec2f(a.getDimensions().x,a.getDimensions().y));
		_background.setColor(new Color(204, 229, 210));
	}
	
	public Shape getBackground() {
		return _background;
	}
	public void setBackgroundColor(Color col) {
		_background.setColor(col);
	}
	
	@Override
	public void onTick(long nanosSincePreviousTick) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onDraw(Graphics2D g) {
		_background.draw(g);
	}

	@Override
	public void onKeyTyped(KeyEvent e) {
		if (e.getKeyChar() == 'q') {
			System.exit(0);
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
		// TODO Auto-generated method stub

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
		_background.setDimensions(new Vec2f(newSize));
	}

}
