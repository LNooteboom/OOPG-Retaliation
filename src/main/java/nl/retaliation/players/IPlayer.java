package nl.retaliation.players;

import java.util.ArrayList;

import nl.han.ica.OOPDProcessingEngineHAN.Engine.GameEngine;
import nl.han.ica.OOPDProcessingEngineHAN.Tile.TileMap;
import nl.retaliation.IRTSObject;
import nl.retaliation.building.Building;
import nl.retaliation.dashboard.Selection;
import nl.retaliation.logic.Vector2;
import nl.retaliation.unit.Unit;

/**
 * 
 * @author Jonathan Vos
 *
 */

public interface IPlayer {
	public boolean makeIRTSObject(IRTSObject object);
	public void removeIRTSObject(IRTSObject object);
	public void selectIRTSObjects(Vector2 cor1, Vector2 cor2, GameEngine gameEngine, int TILESIZE);
	public void setPathOfSelection(Vector2 desiredTilePos, TileMap terrain, ArrayList<IRTSObject> objects);
	public void letSelectionAttack();
	
	public int getColor();
	public int getID();
	
	public ArrayList<Building> getBuildings();
	public ArrayList<Unit> getUnits();
	public ArrayList<IRTSObject> getIRTSObjects();
	public ArrayList<Selection> getSelection();
	
	public void setWin();
	public void setLose();
	public int getState();
}
