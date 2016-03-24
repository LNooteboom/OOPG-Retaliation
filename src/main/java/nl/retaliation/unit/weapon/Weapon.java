package nl.retaliation.unit.weapon;

import nl.han.ica.OOPDProcessingEngineHAN.Objects.GameObject;
import nl.retaliation.IRTSObject;
import nl.retaliation.logic.Trigonio;

/**
 * 
 * @author Luke Nooteboom
 * @author Jonathan Vos
 *
 */

public abstract class Weapon extends GameObject{
	protected float range;
	protected int cooldown;
	
	protected int damage;
	//protected int armorDamage;
	
	private int timeSinceLastFire = 0;
	
	private GameObject parent;
	private IRTSObject enemy;
	
	//private boolean antiAir;
	//private boolean antiGround;
	
	protected boolean hasProjectile;
	
	public Weapon(GameObject parent, float range, int cooldown, boolean hasProjectile) {
		this.parent = parent;
		this.range = range;
		this.cooldown = cooldown;
		this.hasProjectile = hasProjectile;
	}
	
	public void update() {
		timeSinceLastFire++;
		if (canFire()) {
			fire();
			timeSinceLastFire = 0;
		}
	}
	
	private boolean canFire(){
		return enemy != null && enemy.getHealth() > 0 && enemyWithinRange() && timeSinceLastFire > cooldown;
	}
	
	private boolean enemyWithinRange(){
		return Trigonio.distance(parent.getX(), parent.getY(), enemy.getX(), enemy.getY()) <= range;
	}
	
	public void fire(){
		int damageDone = damage - enemy.getArmor();
		if(damageDone > 0){
			getEnemy().damage(damage);
		}
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}
	public void setEnemy(IRTSObject enemy) {
		this.enemy = enemy;
	}
	public IRTSObject getEnemy() {
		return enemy;
	}
}
