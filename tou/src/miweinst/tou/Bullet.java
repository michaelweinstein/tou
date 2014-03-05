package miweinst.tou;

import java.awt.Color;

import miweinst.engine.entity.MovingEntity;
import miweinst.engine.gfx.shape.CircleShape;
import miweinst.engine.gfx.shape.PolygonShape;
import miweinst.engine.util.GameWorld;
import miweinst.engine.util.Vec2f;

public class Bullet extends MovingEntity {

//	private Shape _shape;
//	private Vec2f location;
//	private float radius;
	private boolean _isMissile;
	public Bullet(GameWorld world, boolean missile) {
		super(world);
		Vec2f location = new Vec2f(0, 0);
		float radius = .25f;	
		//Circular bullet, if missile is false
		CircleShape circle = new CircleShape(location, radius);
		circle.setColor(Color.LIGHT_GRAY);
		
		//Hexagonal missile, if missile is true	
		Vec2f[] verts = new Vec2f[6]; //counter-clockwise order
		//Upper right
		verts[0] = new Vec2f(location.x + radius*2, location.y);
		//Upper left
		verts[1] = new Vec2f(location.x, location.y);
		//Middle left
		verts[2] = new Vec2f(location.x - radius*2, location.y + radius*2);
		//Bottom left
		verts[3] = new Vec2f(location.x, location.y + 4*radius);
		//Bottom right
		verts[4] = new Vec2f(location.x + radius*2, location.y + 4*radius);
		//Middle right
		verts[5] = new Vec2f(location.x + 4*radius, location.y + 2*radius);
		PolygonShape poly = new PolygonShape(location, verts);
		poly.setColor(new Color(250, 30, 30));
		poly.setBorderWidth(.25f);
		poly.setBorderColor(Color.GRAY);
		
		//WHOA ternary operator!
		super.setShape((missile==false)? circle:poly);	
//		super.setShape(_shape);		
		_isMissile = missile;
	}
	
	/*Whether this is circular bullet or hexagonal missile.
	 * Set only in constructor.*/
	public boolean isMissile() {
		return _isMissile;
	}
}
