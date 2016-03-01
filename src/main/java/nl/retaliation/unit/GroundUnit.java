package nl.retaliation.unit;

import nl.han.ica.OOPDProcessingEngineHAN.Objects.GameObject;
import nl.han.ica.OOPDProcessingEngineHAN.Objects.Sprite;
import nl.han.ica.OOPDProcessingEngineHAN.Tile.TileMap;
import nl.retaliation.logic.Pathfind;
import nl.retaliation.logic.Vector2;

/**
 * GroundUnit class
 * 
 * @author Luke Nooteboom
 *
 */
public abstract class GroundUnit extends Unit{

	private boolean canStepOnWater = false;
	private boolean canStepOnLand = true;
	
	
	public GroundUnit(float x, float y, Sprite sprite, int tileSize, float maxSpeed, int health) {
		super(x, y, sprite, tileSize, maxSpeed, health);
	}
	
	@Override
	public void destroy() {
		//show explosion!!!
		
	}
	
	public void setPath(Vector2 desiredTilePos, TileMap terrain, GameObject[] gameobjects) {
		isMoving = true;
		this.desiredTilePos = desiredTilePos;
		this.currentPath = Pathfind.calcPath(tilePosition, desiredTilePos, terrain, this, gameobjects);
	}
	
	
	
	
	/* Getters & Setters */
	public boolean canStepOnWater() {
		return canStepOnWater;
	}

	public void setStepOnWater(boolean canStepOnWater) {
		this.canStepOnWater = canStepOnWater;
	}

	public boolean canStepOnLand() {
		return canStepOnLand;
	}

	public void setStepOnLand(boolean canStepOnLand) {
		this.canStepOnLand = canStepOnLand;
	}
	
}
