package miweinst.tou;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import miweinst.engine.entity.DamagableEntity;
import miweinst.engine.gfx.shape.CircleShape;
import miweinst.engine.gfx.shape.CompoundShape;
import miweinst.engine.gfx.shape.PolygonShape;
import miweinst.engine.gfx.shape.Shape;
import miweinst.engine.util.GameWorld;
import miweinst.engine.util.Vec2f;

public class Player extends DamagableEntity {
	private GameWorld _world;
	private CompoundShape _player;
	private Vec2f _location;	
	private CircleShape _gun;
	
	private boolean _hit;
	private float _timer;
	private Color _lastColor;
	
	private ArrayList<Bullet> _bullets;
	
	public Player(GameWorld world) {
		super(world);
		_world = world;
		_hit = false;
		_timer = 0;
		_lastColor = Color.BLUE;
		
		//Location and Dimensions are in game units
		_location = new Vec2f(10, 10);
//		CircleShape main = new CircleShape(_location, 1.5f);
		
		Vec2f[] verts = new Vec2f[3];
		verts[0] = new Vec2f(_location.x + 5, _location.y + 1.5f);
		verts[1] = _location;
		verts[2] = new Vec2f(_location.x, _location.y + 3f);
		PolygonShape poly = new PolygonShape(_location, verts);
		poly.setDimensions(new Vec2f(verts[0].x-verts[1].x, verts[2].y-verts[1].y));
		
//		Vec2f loc = new Vec2f(12.9f, 11.3f);
		CircleShape small = new CircleShape(new Vec2f(verts[0].x, verts[0].y-.25f), .25f);		
		Shape[] shapes = new Shape[2];
		shapes[0] = poly;
		shapes[1] = small;	
		_player = new CompoundShape(_lastColor, _location, shapes);
		_player.setComponentColor(Color.red, 1);

		_bullets = new ArrayList<Bullet>();
		_gun = small;
		this.setDimensions(poly.getDimensions());
		//Sets shape mutated in MovingEntity
		this.setInitialHealth(100);
		this.setHealth(100);
		this.setShape(_player);
		this.setFreeMoving(true);
	}
	
	/*Add bullets to List that is getting fired in onTick.*/
	public void shoot() {
		_location = super.getLocation();
		Bullet newBullet = new Bullet(_world, false);
		newBullet.setLocation(new Vec2f(_gun.getLocation().x, _gun.getLocation().y));
		_bullets.add(newBullet);
	}
	
	/*Returns all active bullets.*/
	public Bullet[] bullets() {
		Bullet[] bullets = new Bullet[_bullets.size()];
		return _bullets.toArray(bullets);
	}
	
	/*Called by TouWorld when bullet collides
	 * with an enemy, or in this class when
	 * bullet moves off the screen.*/
	public void removeBullet(Bullet b) {
		_bullets.remove(b);
	}
	
	/*When hit by bullet or enemy.*/
	public void hit(boolean missile) {
		//If missile, damage 20 hp. Else, 1 hp
		this.damage(missile? 10: 1);
		this.setShapeColor(Color.RED);
		_hit = true;
	}
	
	@Override
	public void setDx(float dx) {
		Vec2f bound = this.getLocation();
		Vec2f dim = _world.getDimensions();
		if (bound.x+this.getDimensions().x + dx < dim.x || bound.x + dx > 0) {
			super.setDx(dx);
		}
	}
	@Override
	public void setDy(float dy) {
		Vec2f bound = this.getLocation();
		Vec2f dim = _world.getDimensions();
		if (bound.y + dy < dim.y || bound.y + dy > 0) {
			super.setDy(dy);
		}
	}
	
	public void onTick(long nanosSincePreviousTick) {
		//Handles wrapping in the finite world
		Vec2f bound = _player.getLocation();
		Vec2f dim = _world.getDimensions();
		if (bound.x > dim.x) {
			this.setX(0);
		} else if (bound.x < 0) {
			this.setX(dim.x);
		}
		if (bound.y > dim.y){ 
			this.setY(0);
		} else if (bound.y < 0) {
			this.setY(dim.y);
		}
		
		super.onTick(nanosSincePreviousTick);
		if (_hit) {
			_timer += nanosSincePreviousTick/1000000;
			if (_timer > 75) {
				_player.setColor(_lastColor);
				_timer = 0;
				_hit = false;
			}
		}
		Bullet toRemove = null;
		for (Bullet b: _bullets) {
			b.move(.35f, 0);
			if (b.getLocation().x > _world.getDimensions().x){
				//Store ref to avoid ConcurrentModException
				toRemove = b;
			}
		}
		if (toRemove != null) this.removeBullet(toRemove);
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		for (Bullet b: _bullets) {
			b.draw(g);
		}
	}
}
