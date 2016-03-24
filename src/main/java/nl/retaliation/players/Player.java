package nl.retaliation.players;

import java.util.ArrayList;

import nl.han.ica.OOPDProcessingEngineHAN.Engine.GameEngine;
import nl.han.ica.OOPDProcessingEngineHAN.Objects.GameObject;
import nl.han.ica.OOPDProcessingEngineHAN.Tile.TileMap;
import nl.retaliation.IRTSObject;
import nl.retaliation.building.Building;
import nl.retaliation.dashboard.Selection;
import nl.retaliation.logic.Vector2;
import nl.retaliation.unit.AirUnit;
import nl.retaliation.unit.Unit;

/**
 * 
 * @author Jonathan Vos
 *
 */

public class Player implements IPlayer{
	private static final int PLAYING = 0;
	private static final int LOSE = 1;
	private static final int WIN = 2;
	private int state = PLAYING;
	
	private int id;
	private int color;
	private GameEngine engine;
	
	private ArrayList<IRTSObject> objects;
	private ArrayList<Unit> units;
	private ArrayList<Building> buildings;
	private ArrayList<Selection> selections;
	
	//private boolean networked;
	//private Client client;
	
	
	public Player(int color, int id, GameEngine engine) {
		this.color = color;
		this.id = id;
		this.engine = engine;
		
		objects = new ArrayList<IRTSObject>(100);
		units = new ArrayList<Unit>(100);
		buildings = new ArrayList<Building>(100);
		selections = new ArrayList<Selection>(20);
		//this.networked = false;
	}

	public Player(int port, String host) {
		//this.networked = true;
	}
	
	public void setID(int id){
		this.id = id;
	}

	public int getID() {
		return id;
	}
	
	@Override
	public boolean makeIRTSObject(IRTSObject object) {
		objects.add(object);
		if(object instanceof GameObject){
			if(object instanceof Unit){
				units.add((Unit)object);
			}
			if(object instanceof Building){
				buildings.add((Building)object);
			}
			return true;
		}
		return false;
	}
	
	public void removeIRTSObject(IRTSObject object){
		objects.remove(object);
		if(object instanceof Unit){
			units.remove((Unit)object);
		}
		if(object instanceof Building){
			buildings.remove((Building)object);
		}
		
		for(Selection selection : selections){
			if(selection.getObject() == object){
				selection.removeSelf(engine);
				selections.remove(selection);
				
				break;
			}
		}
		engine.deleteGameObject((GameObject)object);
	}
	
	@Override
	public void selectIRTSObjects(Vector2 cor1, Vector2 cor2, GameEngine gameEngine, int TILESIZE){
		removeSelection(gameEngine);
		ArrayList<IRTSObject> selectedObjects = vectorsToIRTSObjects(cor1, cor2);
		
		selections = new ArrayList<Selection>(selectedObjects.size());
		
		for(IRTSObject object : selectedObjects){
			if(object.getPos().between(cor1, cor2)){
				selections.add(new Selection(gameEngine, TILESIZE, object));
			}
		}
		
		updateSelection(gameEngine);
	}
	
	private ArrayList<IRTSObject> vectorsToIRTSObjects(Vector2 cor1, Vector2 cor2){
		ArrayList<IRTSObject> selectedObjects = new ArrayList<IRTSObject>(30);
		
		if(cor1.equal(cor2)){
			selectedObjects.add(null);
			
			for(IRTSObject object: objects){
				if(object.getPos().equal(cor1)){
					if(object instanceof AirUnit){
						selectedObjects.set(0, object);
						return selectedObjects;
					}
					else{
						selectedObjects.set(0, object);
					}
				}
			}
			
			if(selectedObjects.get(0) == null){
				return new ArrayList<IRTSObject>(0);
			}
			else{
				return selectedObjects;
			}
		}
		else{
			for(IRTSObject object: objects){
				if(object.getPos().between(cor1, cor2)){
					selectedObjects.add(object);
				}
			}
			
			return selectedObjects;
		}
	}
	
	private void removeSelection(GameEngine gameEngine){
		if(selections != null){
			for(Selection selection : selections){
				selection.removeSelf(gameEngine);
			}
		}
	}
	
	private void updateSelection(GameEngine gameEngine){
		if(selections != null){
			for(Selection selection : selections){
				gameEngine.addGameObject(selection);
			}
		}
	}
	
	@Override
	public void setPathOfSelection(Vector2 desiredTilePos, TileMap terrain, ArrayList<IRTSObject> objects) {
		IRTSObject target = vectorToIRTSObject(desiredTilePos, objects);
		
		if(target != null){
			for(Selection selection : selections){
				selection.getObject().target(target, terrain, objects);
			}
		}
		else{
			for(Selection selection : selections){
				if(selection.getObject() instanceof Unit){
					((Unit)selection.getObject()).setPath(desiredTilePos, terrain, objects, 0.1f);
				}
			}
		}
	}
	
	private IRTSObject vectorToIRTSObject(Vector2 desiredTilePos, ArrayList<IRTSObject> objects){
		for(IRTSObject object : objects){
			if(object.getPos().between(desiredTilePos, desiredTilePos)){
				return object;
			}
		}
		
		return null;
	}
	
	@Override
	public void letSelectionAttack() {
		System.out.println("Attaaack!");
		return;
	}
	
	@Override
	public ArrayList<Building> getBuildings() {
		return buildings;
	}
	
	@Override
	public ArrayList<Unit> getUnits() {
		return units;
	}
	
	@Override
	public ArrayList<IRTSObject> getIRTSObjects() {
		return objects;
	}
	
	public int getColor(){
		return color;
	}
	
	@Override
	public ArrayList<Selection> getSelection() {
		return selections;
	}
	@Override
	public void setWin() {
		// TODO Auto-generated method stub
		state = WIN;
	}
	@Override
	public void setLose() {
		// TODO Auto-generated method stub
		state = LOSE;
	}
	@Override
	public int getState() {
		// TODO Auto-generated method stub
		return state;
	}
}
