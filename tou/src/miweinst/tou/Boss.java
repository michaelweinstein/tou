package miweinst.tou;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import miweinst.engine.behavior_tree.BehaviorNode;
import miweinst.engine.behavior_tree.BehaviorTree;
import miweinst.engine.behavior_tree.SequenceNode;
import miweinst.engine.entity.DamagableEntity;
import miweinst.engine.gfx.shape.AARectShape;
import miweinst.engine.gfx.shape.CircleShape;
import miweinst.engine.gfx.shape.CompoundShape;
import miweinst.engine.gfx.shape.PolygonShape;
import miweinst.engine.gfx.shape.Shape;
import miweinst.engine.util.GameWorld;
import miweinst.engine.util.Vec2f;

public class Boss extends DamagableEntity {
	private GameWorld _world;
	private CompoundShape _boss;
	private Player _player;
	//M/secs timer for hit visualization 
	private float _hitTimer;
	private boolean _dead;
	private boolean _hit;
	private Color _lastColor;
////
	private BehaviorTree _bt;
	private Vec2f _location;
	private ArrayList<Bullet> _bullets;
	private AARectShape[] _guns;
	private CircleShape _eye;

	public Boss(GameWorld world, Player player) {
		super(world);
		_world = world;
		_player = player;
		
		Shape[] components = new Shape[5];
		Vec2f worldDim = world.getDimensions();
		
		Vec2f polyLoc;
		Vec2f[] verts = new Vec2f[5];
		verts[0] = new Vec2f(worldDim.x-1, worldDim.y/3);
		verts[1] = new Vec2f(worldDim.x*4/5, worldDim.y/7);
		verts[2] = polyLoc = new Vec2f(worldDim.x*3/4, worldDim.y/3);
		verts[3] = new Vec2f(worldDim.x*3/4, worldDim.y*2/3);
		verts[4] = new Vec2f(worldDim.x-1, worldDim.y*2/3);
		PolygonShape poly = new PolygonShape(polyLoc, verts);
		//Set dimensions to only the main polygon box
		poly.setDimensions(new Vec2f(Math.abs(verts[0].x-verts[2].x), Math.abs(verts[4].y - verts[0].y)));
		
		float eyeRad = 1;
		Vec2f eyeLoc = new Vec2f(polyLoc.x-eyeRad*3/4, polyLoc.y-eyeRad/4);
		_eye = new CircleShape(eyeLoc, .8f);
		
		Vec2f topFaceDim = new Vec2f(3.5f, .9f);
		Vec2f topFaceLoc = new Vec2f(polyLoc.x - topFaceDim.x*3/5, polyLoc.y + worldDim.y/4);
		AARectShape topFace = new AARectShape(topFaceLoc, topFaceDim);
		Vec2f midFaceLoc = new Vec2f(topFaceLoc.x, topFaceLoc.y-2*topFaceDim.y);
		AARectShape midFace = new AARectShape(midFaceLoc, topFaceDim);
		Vec2f botFaceLoc = new Vec2f(midFaceLoc.x, midFaceLoc.y-2*topFaceDim.y);
		AARectShape botFace = new AARectShape(botFaceLoc, topFaceDim);
		
		components[0] = poly;
		components[1] = _eye;
		components[2] = topFace;
		components[3] = midFace;
		components[4] = botFace;		
		//Ref location is upper left of Polygon
		_boss = new CompoundShape(Color.WHITE, polyLoc, components); 
		_boss.setBorderColor(Color.BLACK);
		_boss.setDimensions(poly.getDimensions());
		
		_hitTimer = 0;
		_dead = false;
		_hit = false;
		_lastColor = Color.WHITE;
		
		_location = polyLoc;
		_boss.setLocation(polyLoc);
		this.setLocation(polyLoc);
		
		this.setShape(_boss);
		
		this.setInitialHealth(350);
		this.setHealth(350);
			
		_bt = this.generateBT();	
		_bullets = new ArrayList<Bullet>();	
		_guns = new AARectShape[3];
		_guns[0] = topFace;
		_guns[1] = midFace;
		_guns[2] = botFace;		
	}
	
	public void onTick(long nanosSincePreviousTick) {
		//Hit visualization; Boss turns red
		if (_hit) {
			_hitTimer += nanosSincePreviousTick/1000000;
			if (_hitTimer > 75) {
				_boss.setColor(_lastColor);
				_hitTimer = 0;
				_hit = false;
			}
		}
		//Update BehaviorTree
		_bt.update(nanosSincePreviousTick);
		
		//Move the bullets, and check for removal
		Bullet toRemove = null;
		for (Bullet b: _bullets) {
			b.move(-.35f, 0);
			if (b.getLocation().x < 0){
				//Store ref to avoid ConcurrentModException
				toRemove = b;
			}
			//Boss checks for its own bullet collisions, because he's a bamf
			if (_player.collides(b)) {
				_player.hit(b.isMissile());
				toRemove = b;
			}
		}
		if (toRemove != null) this.removeBullet(toRemove);
	}
	
	/*When hit by bullet or enemy.*/
	public void hit() {
		this.damage(1);
		_boss.setColor(Color.RED);
		//Health check and update
		if (this.getHealth() <= 0) {
			_dead = true;
		}
		//Uses transparency (alpha val) as health indicator; becomes transparent at 30% hp remaining
		else {
			float percentHealth = this.getHealth()/this.getInitialHealth();
			int alpha = _lastColor.getAlpha();
			if (percentHealth < .30f) {
				alpha = 0;
			}
			else {				
				alpha = (int)(255*percentHealth - 255*.27f);
			}
			_lastColor = new Color(_lastColor.getRed(), _lastColor.getGreen(), _lastColor.getBlue(), alpha);
		}
		//Condition in onTick, turns Boss red momentarily
		_hit = true;
	}
	
	/*Accessor called on TouWorld's tick to
	 * check if Boss is dead. The _dead boolean
	 * is set true when the Boss's health is 0.*/
	public boolean isDead() {
		return _dead;
	}
	
	/*Add bullets to List that is getting fired in onTick.
	 * Boss shoots three bullets at once, one from each gun.*/
	public void shoot() {
		_location = super.getLocation();
		//Three bullets at a time
		for (AARectShape gun: _guns) {		
			Bullet newBullet = new Bullet(_world, false);
			newBullet.setLocation(new Vec2f(gun.getLocation().x, gun.getLocation().y));
			_bullets.add(newBullet);
		}
	}
	/*Shoot from the Boss's eye.*/
	public void shootFromEye() {
		_location = super.getLocation();
		Bullet newBullet = new Bullet(_world, true);
		newBullet.setLocation(new Vec2f(_eye.getLocation().x, _eye.getLocation().y));
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
	
	/*Generates simple BehaviorTree for the Boss.
	 * Just includes trying to shoot if in range,
	 * and moving towards the Player's y-pos if not.
	 * The boss only moves in the Y axis.*/
	public BehaviorTree generateBT() {
		//Instantiate tree and SelectorNode root
		BehaviorTree bt = new BehaviorTree(4);
		bt.addRoot();		
		//Attack sequence
		SequenceNode attackSequence = new SequenceNode();
		PlayerInRangeCondition inRangeCond = new PlayerInRangeCondition();
		ShootPlayerAction attackAction = new ShootPlayerAction();
		attackSequence.addChildStart(inRangeCond);
		attackSequence.addChildEnd(attackAction);		
		//Movement sequence
		SequenceNode moveSequence = new SequenceNode();
		FollowPlayerAction followAction = new FollowPlayerAction();		
		moveSequence.addChildEnd(followAction);
		
		//Add SequenceNodes to tree
		bt.root().addChildStart(attackSequence);
		bt.root().addChildEnd(moveSequence);
		
		return bt;
	}
	
	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		for (Bullet bullet: _bullets) {
			bullet.draw(g);
		}
	}

///Inner Classes
private class PlayerInRangeCondition extends BehaviorNode {

	@Override
	public Status update(long nanos) {
		Vec2f playerLoc = _player.getLocation();
		if (playerLoc.y > _boss.getLocation().y && playerLoc.y < _boss.getLocation().y + _boss.getDimensions().y){
			return Status.SUCCESS;
		}
		return Status.FAILED;
	}

	@Override
	public void reset() {
		//N.A.
	}
}
private class ShootPlayerAction extends BehaviorNode {
	
	private int shotTimer;
	private int eyeTimer;
	
	@Override
	public Status update(long nanos) {
		shotTimer += nanos/1000000;
		eyeTimer += nanos/1000000;
		//Store how many bullets
		int before = _bullets.size();
		
		//Shoot from the eye with lower frequency
		if (eyeTimer > 400) {
			Vec2f playerLoc = _player.getLocation();
			if (playerLoc.y >= _boss.getLocation().y && playerLoc.y <= _boss.getLocation().y+_guns[1].getLocation().y) {
				Boss.this.shootFromEye();
			}
			eyeTimer = 0;
		}
		
		//Otherwise it is a solid line of bullets
		if (shotTimer > 18) {
			shotTimer = 0;
			Boss.this.shoot();
			if (_bullets.size() > before) {
				return Status.SUCCESS;
			}	
		}
			return Status.FAILED;
	}

	@Override
	public void reset() {
	}	
}
private class FollowPlayerAction extends BehaviorNode {

	@Override
	public Status update(long nanos) {
		//Wrap around if Boss follows player off screen
		if (_boss.getLocation().y + _boss.getDimensions().y < 0) {
			Boss.this.setLocation(new Vec2f(Boss.this.getX(), _world.getDimensions().y));
		}
		if (_boss.getLocation().y > _world.getDimensions().y) {
			Boss.this.setLocation(new Vec2f(Boss.this.getX(), 0));
		}
		
		//Move to get player in range
		Vec2f playerLoc = _player.getLocation();
		if (playerLoc.y < _location.y) {
			Boss.this.move(0, -.5f);
			return Status.SUCCESS;
		}
		if (playerLoc.y > _location.y + _boss.getDimensions().y) {
			Boss.this.move(0, .5f);
			return Status.SUCCESS;
		}
		
		return Status.FAILED;
	}

	@Override
	public void reset() {
		//N.A.
	}
	
}
}
