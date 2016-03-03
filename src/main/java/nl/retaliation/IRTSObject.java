package nl.retaliation;

import nl.retaliation.logic.Vector2;

public interface IRTSObject {
	
	public float getX();
	public float getY();
	
	public void damage(int amount);
	
	public int getHealth();
	
	public int getArmor();
	
	public Vector2 getPos();
}
