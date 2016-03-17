package nl.retaliation.unit.weapon;

import nl.han.ica.OOPDProcessingEngineHAN.Objects.GameObject;
import nl.retaliation.IRTSObject;
import nl.retaliation.logic.Trigonio;

public abstract class Weapon {
	protected float range;
	protected int cooldown;
	
	protected int damage;
	protected int armorDamage;
	
	private int timeSinceLastFire = 0;
	
	private GameObject parent;
	public IRTSObject enemy;
	
	private boolean antiAir;
	private boolean antiGround;
	
	protected boolean hasProjectile;
	
	public Weapon(GameObject parent, float range, int cooldown, boolean hasProjectile) {
		this.parent = parent;
		this.range = range;
		this.cooldown = cooldown;
		this.hasProjectile = hasProjectile;
	}
	
	public void update() {
		timeSinceLastFire++;
		if (enemy != null && Trigonio.distance(parent.getX(), parent.getY(), enemy.getX(), enemy.getY()) <= range && timeSinceLastFire > cooldown) {
			fire();
			timeSinceLastFire = 0;
		}
	}
	public abstract void fire();

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

}
