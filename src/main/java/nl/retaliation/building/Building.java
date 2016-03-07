package nl.retaliation.building;

import nl.han.ica.OOPDProcessingEngineHAN.Objects.*;
import nl.retaliation.IRTSObject;
import nl.retaliation.logic.Vector2;

public abstract class Building extends AnimatedSpriteObject implements IRTSObject{
	private Vector2 tilePosition;
	
	private boolean isIndestructible;
	
	private int health, armor;
	
	Building(float x, float y, Sprite sprite, int TILESIZE, int health, int armor){
		super(sprite, 1);
		
		this.setX(x * TILESIZE);
		this.setY(y * TILESIZE);
		tilePosition = new Vector2((int) x / TILESIZE, (int) y / TILESIZE);
		
		this.setWidth(TILESIZE);
		this.setHeight(TILESIZE);
		
		this.health = health;
		this.armor = armor;
	}
	
	public void update(){
		if (health <= 0 && isIndestructible == false) {
			destroy();
		}
	}
	
	public abstract void destroy();

	@Override
	public void damage(int amount) {
		health -= amount;
	}

	@Override
	public int getHealth() {
		return health;
	}

	@Override
	public int getArmor() {
		return armor;
	}
	
	public Vector2 getPos(){
		return tilePosition;
	}
}
