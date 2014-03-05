package miweinst.tou;

import java.awt.Color;
import java.util.Random;

import miweinst.engine.entity.DamagableEntity;
import miweinst.engine.gfx.shape.AARectShape;
import miweinst.engine.gfx.shape.CircleShape;
import miweinst.engine.gfx.shape.CompoundShape;
import miweinst.engine.gfx.shape.PolygonShape;
import miweinst.engine.gfx.shape.Shape;
import miweinst.engine.util.Vec2f;

public class Enemy extends DamagableEntity {
//	private TouWorld _world;
	private Vec2f _gameDim;
	private CompoundShape _enemy;
	
	private int _timer;
	private boolean _hit;
	private Color _lastColor;
	
	private boolean _dead;
	
	public Enemy(TouWorld world) {
		super(world);
//		_world = world;
		_gameDim = world.getDimensions();
		_enemy = this.makeEnemy();		
		_timer = 0;
		_hit = false;
		_lastColor = _enemy.getColor();
		_dead = false;
		
		super.setHealth(250);
		super.setShape(_enemy);
	}
	
	/*Generates a CompoundShape with up to three 
	 * components. Generated close together within
	 * a small bounding box that specifies constraints
	 * of random number generation for location (relative to
	 * a reference location randomized on the y axis) and 
	 * dimensions (kept within bounds). Color is totally 
	 * randomized with r, g, b float values for outline color.*/
	public CompoundShape makeEnemy() {
		//Random generator for color and constrained location & dimensions
		Random gen = new Random();	
		//Generate random color
		float r = gen.nextFloat();
		float g = gen.nextFloat();
		float b = gen.nextFloat();
		Color col = new Color(r, g, b);	
		//Stores component shapes of CompoundShape
		Shape[] components = new Shape[4];
		//Bounds dimensions for CompoundShape
		Vec2f bounds = new Vec2f(4, 3);
		//Ref loc for CompoundShape; x off screen, y randomized w/in world height
		Vec2f spawnLoc = new Vec2f(_gameDim.x+10, gen.nextFloat()*(_gameDim.y-bounds.y));		
		//Create constant visible bounds so shapes connected
		components[0] = new AARectShape(spawnLoc, bounds);	
		
		for (int i=1; i<components.length; i++) {
			//Constrained randomized component location
			float x = spawnLoc.x + gen.nextFloat()*(bounds.x-.5f);
			float y = spawnLoc.y + gen.nextFloat()*(bounds.y-.5f);
			Vec2f loc = new Vec2f(x, y);
			//Constrained randomized component dimension
			float w = gen.nextFloat()*2*bounds.x;
			float h = gen.nextFloat()*2*bounds.y;
			int n = gen.nextInt(5);
			switch (n) {
				case 0:
					components[i] = new AARectShape(loc, new Vec2f(w, h));				
					break;
				case 1:
					components[i] = new AARectShape(loc, new Vec2f(w, h));
					break;
				case 2:
					components[i] = new CircleShape(loc, w/2);
					break;
				case 3:
					//Right triangle, counter-clockwise verts
					Vec2f[] triVerts = new Vec2f[3];
					triVerts[0] = new Vec2f(loc.x + w, loc.y);
					triVerts[1] = loc;
					triVerts[2] = new Vec2f(loc.x, loc.y + h);
					components[i] = new PolygonShape(loc, triVerts);
					break;
				case 4:
					//Parallelogram, counter-clockwise verts
					Vec2f[] quadVerts = new Vec2f[4];
					quadVerts[0] = new Vec2f(loc.x + w, loc.y);
					quadVerts[1] = loc;
					quadVerts[2] = new Vec2f(loc.x - w/2, loc.y + h);
					quadVerts[3] = new Vec2f(loc.x + 3*w/2, loc.y + h);
					components[i] = new PolygonShape(loc, quadVerts);
					break;
				default:
					components[i] = new CircleShape(loc, h/2);
					break;
			}
		}
		CompoundShape shape = new CompoundShape(col, spawnLoc, components);
		shape.setOutline(col, .25f);	
		return shape;
	}
	
	/*Handle ticks within Entity.*/
	public void onTick(long nanosSincePreviousTick, float speed) {
		this.move(speed, 0);
		if (_hit) {
			_timer += nanosSincePreviousTick/1000000;
			if (_timer > 75) {
				_enemy.setColor(_lastColor);
				_timer = 0;
				_hit = false;
			}
		}
		//When the enemy moves off the screen/world
		if (_enemy.getX() + _enemy.getWidth() < 0) {
			_dead = true;
		}
	}
	
	/*When hit by bullet or enemy. Returns
	 * whether or not this hit killed
	 * the Enemy.*/
	public boolean hit() {
		this.damage(10);
		_enemy.setColor(Color.RED);
		_hit = true;
		if (this.getHealth() <= 0) {
			_dead = true;
			return true;
		}
		return false;
	}
	
	/*Returns whether enemy is off screen or killed.*/
	public boolean isDead() {
		return _dead;
	}
}
