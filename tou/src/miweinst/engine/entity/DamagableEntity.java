package miweinst.engine.entity;

import java.awt.Graphics2D;

import miweinst.engine.util.GameWorld;

public abstract class DamagableEntity extends MovingEntity {
	
	private float _health;
	private float _initialHealth;
	private boolean _hostile;
	private boolean _showHealth;

	public DamagableEntity(GameWorld world) {
		super(world);
		_initialHealth = 100;
		_health = 0;
		_hostile = false;
		_showHealth = false;
	}
	
	/*Methods to change and get entity health*/
	public float getHealth() {
		return _health;
	}
	public void setHealth(float health) {
		_health = health;
	}
	/*Methods to change health by specified amount.*/
	public void damage(float dhealth) {
		_health -= dhealth;
	}
	public void undamage(float dhealth) {
		_health += dhealth;
	}
	public void resetHealth() {
		_health = _initialHealth;
	}
	
	/*Methods to get or set the Entity's 
	 * starting health; does not correspond
	 * with current health, only default health.
	 * resetHealth resets curr health to initial health.*/
	public float getInitialHealth() {
		return _initialHealth;
	}
	public void setInitialHealth(float init) {
		_initialHealth = init;
	}
	
	/*Indicates membership to team, which
	 * is used if members of same team
	 * should not be able to injure teammates.*/
	public boolean getHostile() {
		return _hostile;
	}
	public void setHostile(boolean hostile) {
		_hostile = hostile;
	}
	
	/*If a health bar or visualizer is used,
	 * this boolean will store visibility*/
	public boolean isHealthVisible() {
		return _showHealth;
	}
	public void showHealth() {
		_showHealth = true;
	}
	public void hideHealth() {
		_showHealth = false;
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
	}

}
