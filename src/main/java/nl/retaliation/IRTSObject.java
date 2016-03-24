package nl.retaliation;

import java.util.ArrayList;

import nl.han.ica.OOPDProcessingEngineHAN.Tile.TileMap;
import nl.retaliation.logic.Vector2;
import nl.retaliation.players.IPlayer;
import nl.retaliation.unit.weapon.Weapon;

/**
 * Interface for all objects in that are in the map
 * 
 * @author Luke Nooteboom
 *
 */
public interface IRTSObject {
	
	public float getX();
	public float getY();
	public void setX(float x);
	public void setY(float y);
	
	public void damage(int amount);
	
	public int getHealth();
	
	public float getHealthPercentage();
	
	public int getArmor();
	
	public IPlayer getOwner();
	
	public Vector2 getPos();
	
	/**
	 * Serialize this object into a string
	 * 
	 * @return this object serialized into a string
	 */
	public String serialize();
	
	public void addToEngine(Retaliation engine);
	
	/**
	 * Adds weapon to current unit
	 * 
	 * @param newWeapon
	 */
	public void addWeapon(Weapon newWeapon);
	
	/**
	 * Targets an enemy
	 * 
	 * @param enemy The enemy to be attacked
	 * @param terrain The tilemap to move around
	 * @param gameobjects The list of objects to avoid
	 */
	public void target(IRTSObject enemy, TileMap terrain, ArrayList<IRTSObject> gameobjects);
	
	/**
	 * Destroys this object
	 */
	public abstract void destroy();
}
