package miweinst.tou;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import miweinst.engine.util.App;
import miweinst.engine.util.GameWorld;
import miweinst.engine.util.Vec2f;
import miweinst.engine.util.Vec2i;

public class TouWorld extends GameWorld {
	//Protected vars from superclass:
	/* protected int scale (in super) */
	/* protected Vec2f pxlgameloc (in super) */
	
///DEBUGGING VAR	
//		private TestCollisionVisualizer _testVisualizer;
///^^^
	
//	private Enemy _enemy;	
//	private Vec2f _mouseLast;
	private App _app;
	private Player _player;	
	private ArrayList<Enemy> _enemies;
	private Boss _boss;
	private boolean _reachedBoss;
	//Timer in milliseconds to time delay b/w spawns
	private int _spawnTimer;
	//Time between enemies spawning
	private int _spawnDelay;
	//Number of enemies that can be alive at once
	private int _numEnemy;
	//Speed at which enemy moves right-to-left
	private float _enemySpeed;
	//Number of enemies player has killed; 50 to boss
	private int _remaining;
	
	public TouWorld(App app, Vec2i dim) {
		super(app, new Vec2f(dim));
		_app = app;
		_spawnTimer = 0;
		//Initial rate of enemies; could change over game
		_spawnDelay = 800;
		_numEnemy = (int)(dim.y/2);
		_enemySpeed = -.1f;
		
		_player = new Player(this);		
		_enemies = new ArrayList<Enemy>(_numEnemy);
		_enemies.add(new Enemy(this));
		_boss = new Boss(this, _player);
		_reachedBoss = false;
//////	
		_remaining = 30;
///Collision Debugger
//		_testVisualizer = new TestCollisionVisualizer(this);
///^^^
	}
	
	/*Number of enemies at one time increases
	 * when the GameWorld size increases. Game
	 * World size changes when game is resized
	 * in GameScreen. i.e. without this message,
	 * fullscreen is too easy.*/
	@Override
	public void setDimensions(Vec2f newSize) {
		super.setDimensions(newSize);
		_numEnemy = (int)(newSize.y/2);
	}
	
	/*Forward tick method to necessary entities; each Entity
	 * handles its own movement. In this class, also check for
	 * deaths and spawn enemies at intervals.*/
	public void onTick(long nanosSincePreviousTick) {	
		//Player movement
		_player.onTick(nanosSincePreviousTick);
		
		//Collision Detections:
		Bullet[] bullets = _player.bullets();
		  
		//Boss fight
		if (_reachedBoss) {
				//Hit response (visualization)
				_boss.onTick(nanosSincePreviousTick);
				//Check Player's shots against Boss
				for (int i=0; i<bullets.length; i++) {
					Bullet b = bullets[i];
					if (_boss.collides(b)) {
						_boss.hit();
						_player.removeBullet(b);
					}
				}
				//Check for Win/Lose conditions 
				if (_boss.isDead()) {
					_app.setScreen(new WinScreen(_app));
				}
				//Player runs out of health
				if (_player.getHealth() <= 0) {
					_app.setScreen(new GameOverScreen(_app));
				}
				//Player runs into boss
				if (_boss.collides(_player)) {
					_app.setScreen(new GameOverScreen(_app));
				}
		}
		else {
			for (int i=0; i<_enemies.size(); i++) {
				Enemy e = _enemies.get(i);
				//Check  enemy with bullets
				for (int j=0; j<bullets.length; j++) {
					Bullet b = bullets[j];
					if (e.collides(b)) {
						boolean dead = e.hit();
						_player.removeBullet(b);
						if (dead) {
							if (_remaining != 0) _remaining -= 1;
						}
					}
				}
				//Check enemy with player
				if (e.collides(_player)) {
					_app.setScreen(new GameOverScreen(_app));
				}
			}
			//Check if player reached boss
			if (_remaining <= 0) {
				if (_enemies.size() <= 0) {
					_reachedBoss = true;
				}
			}	
			//Based on player progress: quicker spawn, faster movement
			else if (_remaining < 10) {
				_spawnDelay = 450;
				_enemySpeed = -.22f;
			}
			else if (_remaining < 25) {
				_spawnDelay = 620;
				_enemySpeed = -.16f;
			}
			else if (_remaining < 35) {
				_spawnDelay = 700;
				_enemySpeed = -.13f;
			}			
		}
		
		//Enemy movement and checking for death
		boolean[] indices = new boolean[_enemies.size()];
		for (int i=0; i<_enemies.size(); i++) {
			indices[i] = false;
			Enemy e = _enemies.get(i);
			if (e != null) {
				e.onTick(nanosSincePreviousTick, _enemySpeed);
				if (e.isDead()) {
					//Store indices so no ConcurrentModificationError
					indices[i] = true;
				}
			}
		}
		//Remove enemies that returned dead
		for (int i=0; i<indices.length; i++) {
			if (indices[i] == true) {
				_enemies.remove(i);
			}
		}
		//While still need to kill Enemies before boss
		if (_remaining > 0) {
			//New enemies spawning with constant delay
			_spawnTimer += nanosSincePreviousTick/1000000;
			if (_spawnTimer > _spawnDelay && _enemies.size() < _numEnemy) {
				_enemies.add(new Enemy(this));
				_spawnTimer = 0;
			}		
		}
///Collision Detection
//		_testVisualizer.onTick(nanosSincePreviousTick);
	}
	
	/*Number of enemies to be killed 
	 * before boss arrives.*/
	public int getRemaining() {
		return _remaining;
	}

	/* AffineTransform is in superclass draw
	 * method. Superclass draws its current
	 * Entity or array of Entities. So this
	 * method sets the current Entity, calls
	 * super.draw, adds next Entity, etc... */
	public void draw(Graphics2D g) {
		//If player is at Boss
		if (_reachedBoss) {
			super.setEntity(_boss);
			super.draw(g);
		}
		//For most of game, before boss
		else {
			Enemy[] enemies = new Enemy[_enemies.size()];
			super.setEntityArray(_enemies.toArray(enemies));
			super.draw(g);
		}
		super.setEntity(_player);
		super.draw(g); 
		
///Collision Debugger
		/*Entity[] testarr = _testVisualizer.toArr();
		super.setEntityArray(testarr);
		super.draw(g);*/
///^^^
	}
	
/* USER INPUT LISTENERS:	*/
// forwarded from GameScreen
	
	public void mouseMoved(MouseEvent e) {
		Vec2f newLoc = super.toUnits(new Vec2f(e.getX(), e.getY()));	
		float dx = (newLoc.x - _player.getLocation().x)/25;
		float dy = (newLoc.y - _player.getLocation().y)/25;
		_player.setDx(dx);
		_player.setDy(dy);
//////
//		MovingEntity[] arr = _testVisualizer.toArr();
//		arr[arr.length-1].setDx(dx);
//		arr[arr.length-1].setDy(dy);
////^^^
}
	
	public void onKeyPressed(KeyEvent e) {
		//Left
/*		if (e.getKeyCode() == 37) {
//			_player.move(-.25f, 0);
			_player.setDx(-.2f);
			_player.setDy(0);
			_player.setFreeMoving(true);
		}
		//Up
		if (e.getKeyCode() == 38) {
//			_player.move(0, -.25f);
			_player.setDx(0);
			_player.setDy(-.2f);
			_player.setFreeMoving(true);
		}
		//Right
		if (e.getKeyCode() == 39) {
//			_player.move(.25f, 0);
			_player.setDx(.2f);
			_player.setDy(0);
			_player.setFreeMoving(true);
		}
		//Down
		if (e.getKeyCode() == 40) {
//			_player.move(0, .25f);
			_player.setDx(0);
			_player.setDy(.2f);
			_player.setFreeMoving(true);
		}*/
		if (e.getKeyChar() == ' ') {
			_player.shoot();
		}
	}
	
	public void onMousePressed(MouseEvent e) {
		//Toggle freeze of player movement
		_player.setFreeMoving(_player.isFreeMoving()? false: true);
//		Vec2f pointUnits = super.toUnits(new Vec2f(e.getX(), e.getY()));
/////DON'T WANT TO LOSE HEALTH WHEN CLICKING ON PLAYER/ENEMIES, RIGHT?
/*		if (_player.contains(pointUnits)) {
			_player.hit();
		}
		for (Enemy enem: _enemies) {
			if (enem.contains(pointUnits)) {
				enem.hit();
			}
		}*/
///		Pass in mouse location, converted to Game Units
//		_testVisualizer.onMousePressed(super.toUnits(new Vec2f(e.getX(), e.getY())));
	}	
	
	public void onMouseDragged(MouseEvent e) {
///		Pass in mouse location, converted to Game Units
//		_testVisualizer.onMouseDragged(super.toUnits(new Vec2f(e.getX(), e.getY())), super.scale);
	}
}




