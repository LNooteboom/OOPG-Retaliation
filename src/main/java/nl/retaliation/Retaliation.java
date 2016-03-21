package nl.retaliation;


import java.util.ArrayList;
import java.util.Vector;

import nl.han.ica.OOPDProcessingEngineHAN.Engine.GameEngine;
import nl.han.ica.OOPDProcessingEngineHAN.Objects.GameObject;
import nl.han.ica.OOPDProcessingEngineHAN.Objects.Sprite;
import nl.han.ica.OOPDProcessingEngineHAN.Tile.TileMap;
import nl.han.ica.OOPDProcessingEngineHAN.Tile.TileType;
import nl.han.ica.OOPDProcessingEngineHAN.View.View;
import nl.han.ica.OOPDProcessingEngineHAN.View.Viewport;

import nl.retaliation.dashboard.Minimap;
import nl.retaliation.unit.*;
import nl.retaliation.level.*;
import nl.retaliation.logic.LevelGenerator;
import nl.retaliation.logic.Vector2;
import nl.retaliation.networking.Client;
import nl.retaliation.networking.Server;
import nl.retaliation.players.IPlayer;
import nl.retaliation.players.Player;

import processing.core.PApplet;

/**
 * Main in-game class
 * 
 * @author Luke Nooteboom
 * @author Jonathan Vos
 *
 */
@SuppressWarnings("serial")
public class Retaliation extends GameEngine { /* OOPG = Object oriented piece of garbage */	
	private final int TILESIZE = 32;
	private View view;
	private Viewport viewport;
	
	//private Player currentPlayer;
	private Server currentServer;
	private Client currentClient;
	private boolean isServer = false;
	
	private Minimap minimap;
	
	private ArrayList<IRTSObject> allObjects = new ArrayList<IRTSObject>(200);
	private ArrayList<IPlayer> players = new ArrayList<IPlayer>(8);
	
	private Vector2 corMousePressed = null, corMouseReleased = null;
	
	private IPlayer player;

	public static void main(String[] args) {
		PApplet.main(new String[]{"nl.retaliation.Retaliation"});
	}

	@Override
	public void setupGame() {
		if (isServer) {
			currentServer = new Server(63530);
		} else {
			currentClient = new Client("localhost", 63530);
		}
		
		players.add(new Player(0xFF0000FF));
		players.get(0).makeIRTSObject(this, new SovMiG(3, 13, TILESIZE, players.get(0)));
		players.get(0).makeIRTSObject(this, new SovMiG(3, 12, TILESIZE, players.get(0)));
		players.add(new Player(0xFFFF0000));
		players.get(1).makeIRTSObject(this, new SovMiG(4, 13, TILESIZE, players.get(1)));
		players.add(new Player(0xFF00FF00));
		players.get(2).makeIRTSObject(this, new SovMiG(5, 13, TILESIZE, players.get(2)));
		
		player = players.get(0);
		
		for(IPlayer player : players){
			ArrayList<IRTSObject> objects = player.getIRTSObjects();
			for(IRTSObject object : objects){
				if(object instanceof GameObject){
					addGameObject((GameObject)object);
				}
				else{
					System.out.println("Code should never get here!");
				}
			}
		}
		
		initTileMap();
		viewPort(800, 600);
		minimap = new Minimap(0, 0, this.getTileMap());
		addDashboard(minimap);
		setFPSCounter(true);
	}

	@Override
	public void update() {
		allObjects = vectorToArrayList(getGameObjectItems());
		deleteDeadGameObjects();
		
		if (currentServer != null) {
			currentServer.sendData(allObjects, tileMap);
		}
		if (currentClient != null) {
			ArrayList<IRTSObject> newObjects = currentClient.transceiveData(tileMap);
			if (newObjects != null) {
				deleteAllGameOBjects();
				for (IRTSObject newObject : newObjects) {
					newObject.addToEngine(this);
				}
			}
		}
		
	}
	
	@Override
	public void keyPressed() {
		int moveSpeed = 32;
		switch (key) {
		case 'w':
			viewport.setY(viewport.getY() - moveSpeed);
			break;
		case 's':
			viewport.setY(viewport.getY() + moveSpeed);
			break;
		case 'a':
			viewport.setX(viewport.getX() - moveSpeed);
			break;
		case 'd':
			viewport.setX(viewport.getX() + moveSpeed);
			break;
		}
	}
	
	@Override
	public void mousePressed(){
		if(mouseButton == LEFT){
			int xTile = (int) ((viewport.getX() + mouseX) / TILESIZE);
			int yTile = (int) ((viewport.getY() + mouseY) / TILESIZE);
		
			corMousePressed = new Vector2(xTile, yTile);
		}
	}
	
	@Override
	public void mouseReleased(){
		int xTile = (int) ((viewport.getX() + mouseX) / TILESIZE);
		int yTile = (int) ((viewport.getY() + mouseY) / TILESIZE);
		corMouseReleased = new Vector2(xTile, yTile);
		
		if(mouseButton == LEFT){
			player.selectIRTSObjects(corMousePressed, corMouseReleased, this, TILESIZE);
		}
		else if(mouseButton == RIGHT){
			player.setPathOfSelection(corMouseReleased, tileMap, allObjects);
		}
	}
	
	private ArrayList<IRTSObject> vectorToArrayList(Vector<GameObject> gameObjects) {
		ArrayList<IRTSObject> newGameObjects = new ArrayList<IRTSObject>();
		for (int i = 0; i < gameObjects.size(); i++) {
			if (gameObjects.get(i) instanceof IRTSObject) {
				newGameObjects.add((IRTSObject) gameObjects.get(i));
			}
		}
		return newGameObjects;
	}
	
	private void initTileMap() {
		//temp
		Sprite grassSprite = new Sprite("src/main/java/nl/retaliation/media/sprites/grass.png");
		TileType<GrassTile> grassType = new TileType<>(GrassTile.class, grassSprite);
		Sprite waterSprite = new Sprite("src/main/java/nl/retaliation/media/sprites/water.png");
		TileType<WaterTile> waterType = new TileType<>(WaterTile.class, waterSprite);
		
		TileType<?>[] tileTypes = {grassType, waterType};
		
		LevelGenerator noise = new LevelGenerator(0000f, 128, 128);
		tileMap = new TileMap(TILESIZE, tileTypes, noise.generateNoise(0.0f));
		tileMap.setTile(3, 3, 1);
	}
	
	private void viewPort(int screenWidth, int screenHeight) {
		viewport = new Viewport(0, 0, screenWidth, screenHeight);
		view = new View(viewport, screenWidth, screenHeight);

		setView(view);
		size(screenWidth, screenHeight);
	}
	
	private void deleteDeadGameObjects() {
		GameObject currentObject;
		for (int i = 0; i < getGameObjectItems().size(); i++) {
			currentObject = getGameObjectItems().get(i);
			if (currentObject.isVisible() == false) {
				deleteGameObject(currentObject);
			}
		}
	}
}
