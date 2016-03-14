package nl.retaliation;

import nl.han.ica.OOPDProcessingEngineHAN.Engine.GameEngine;
import nl.retaliation.logic.Vector2;
import nl.retaliation.players.Player;

public interface IRTSObject {
	
	public float getX();
	public float getY();
	
	public void damage(int amount);
	
	public int getHealth();
	
	public float getHealthPercentage();
	
	public int getArmor();
	
	public Player getOwner();
	
	public Vector2 getPos();
	
	public String serialize();
	
	public void addToEngine(GameEngine engine);
}
