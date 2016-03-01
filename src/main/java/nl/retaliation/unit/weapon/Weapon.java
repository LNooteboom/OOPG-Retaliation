package nl.retaliation.unit.weapon;

import nl.han.ica.OOPDProcessingEngineHAN.Objects.GameObject;
import nl.retaliation.IRTSObject;
import nl.retaliation.logic.Trigonio;

public abstract class Weapon {
	private float range;
	private int cooldown;
	private int timeSinceLastFire = 0;
	
	private GameObject parent;
	public IRTSObject enemy;
	
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
		}
	}
	public abstract void fire();

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

}
