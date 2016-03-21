package nl.retaliation;

import java.util.ArrayList;

import nl.han.ica.OOPDProcessingEngineHAN.Engine.GameEngine;
import nl.han.ica.OOPDProcessingEngineHAN.Tile.TileMap;
import nl.retaliation.logic.Vector2;
import nl.retaliation.players.Player;
import nl.retaliation.unit.weapon.Weapon;

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
	
	public void addWeapon(Weapon newWeapon);
	
	public void target(IRTSObject enemy, TileMap terrain, ArrayList<IRTSObject> gameobjects);
}
