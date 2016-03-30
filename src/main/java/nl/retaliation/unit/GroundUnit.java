package nl.retaliation.unit;

import java.util.ArrayList;

import nl.han.ica.OOPDProcessingEngineHAN.Engine.GameEngine;
import nl.han.ica.OOPDProcessingEngineHAN.Objects.Sprite;
import nl.han.ica.OOPDProcessingEngineHAN.Tile.TileMap;
import nl.retaliation.IRTSObject;
import nl.retaliation.logic.Pathfind;
import nl.retaliation.logic.Vector2;
import nl.retaliation.players.IPlayer;

/**
 * GroundUnit for all Units that move on land or water
 * 
 * @author Luke Nooteboom
 *
 */
public abstract class GroundUnit extends Unit{

	private boolean canStepOnWater = false;
	private boolean canStepOnLand = true;
	
	
	public GroundUnit(float x, float y, Sprite sprite, int tileSize, float maxSpeed, int health, int armor, int cost, IPlayer player, GameEngine engine) {
		super(x, y, sprite, tileSize, maxSpeed, health, armor, cost,  player, engine);
	}
	
	@Override
	public void setPath(Vector2 desiredTilePos, TileMap terrain, ArrayList<IRTSObject> gameobjects, float targetRadius) {
		isMoving = true;
		this.gameobjects = gameobjects;
		this.terrain = terrain;
		this.desiredTilePos = desiredTilePos;
		this.currentPath = Pathfind.calcPath(tilePosition, desiredTilePos, terrain, this, gameobjects, targetRadius);
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
