package nl.retaliation;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import nl.han.ica.OOPDProcessingEngineHAN.Engine.GameEngine;
import nl.han.ica.OOPDProcessingEngineHAN.Objects.GameObject;
import nl.han.ica.OOPDProcessingEngineHAN.Objects.Sprite;
import nl.han.ica.OOPDProcessingEngineHAN.Tile.TileMap;
import nl.han.ica.OOPDProcessingEngineHAN.Tile.TileType;
import nl.han.ica.OOPDProcessingEngineHAN.View.View;
import nl.han.ica.OOPDProcessingEngineHAN.View.Viewport;
import nl.retaliation.building.HQRed;
import nl.retaliation.dashboard.Minimap;
import nl.retaliation.dashboard.Selection;
import nl.retaliation.dashboard.TextScreen;
import nl.retaliation.level.GrassTile;
import nl.retaliation.level.WaterTile;
import nl.retaliation.logic.LevelGenerator;
import nl.retaliation.logic.Vector2;
import nl.retaliation.networking.Client;
import nl.retaliation.networking.Packet;
import nl.retaliation.networking.Server;
import nl.retaliation.players.IPlayer;
import nl.retaliation.players.Player;
import nl.retaliation.unit.SovIFV;
import nl.retaliation.unit.SovMiG;
import nl.retaliation.unit.Unit;
import processing.core.PApplet;

/**
 * Main in-game class
 * 
 * @author Luke Nooteboom
 * @author Jonathan Vos
 *
 */
@SuppressWarnings("serial")
public class Retaliation extends GameEngine { /* OOPG = Object Oriented Piece of Garbage */	
	private final int TILESIZE = 32;
	private View view;
	private Viewport viewport;

	//private Player currentPlayer;
	private Server currentServer;
	private Client currentClient;
	private boolean isServer = false;
	private boolean singlePlayer = true;

	private Minimap minimap;
	
	private TimerTask timerTask = new TimerTask() 
	{ 
	    public void run()  
	    { 
	         income();
	    } 
	};

	private Timer timer = new Timer(); 

	private ArrayList<IRTSObject> allObjects = new ArrayList<IRTSObject>();
	private ArrayList<IPlayer> players = new ArrayList<IPlayer>(8);

	private Vector2 corMousePressed = null, corMouseReleased = null;

	private IPlayer player;

	private TextScreen statusScreen;

	private String build = null;
	
	public static void main(String[] args) {
		PApplet.main(new String[]{"nl.retaliation.Retaliation"});
	}

	@Override
	public void setupGame() {
		if(singlePlayer){
			isServer = true;
		}

		players.add(new Player(0xFF0000FF, 0, this));
		players.get(0).addResources(1000000);
		players.get(0).makeIRTSObject(new SovMiG(3 * TILESIZE, 13 * TILESIZE, TILESIZE, players.get(0), this));
		players.get(0).makeIRTSObject(new SovIFV(3 * TILESIZE, 12 * TILESIZE, TILESIZE, players.get(0), this));
		players.get(0).makeIRTSObject(new HQRed(3 * TILESIZE, 14 * TILESIZE, TILESIZE, players.get(0), this));
		players.add(new Player(0xFFFF0000, 1, this));
		players.get(1).addResources(1000000);
		players.get(1).makeIRTSObject(new SovIFV(4 * TILESIZE, 13 * TILESIZE, TILESIZE, players.get(1), this));
		players.get(1).makeIRTSObject(new HQRed(4 * TILESIZE, 14 * TILESIZE, TILESIZE, players.get(1), this));

		createPlayerUnits();

		initTileMap();
		viewPort(800, 600);
		
		minimap = new Minimap(0, 0, this.getTileMap());
		addDashboard(minimap);
		setFPSCounter(true);
		
		timer.scheduleAtFixedRate(timerTask, 0, 1000);

		if (singlePlayer == false) {
			if (isServer) {
				player = players.get(0);
				currentServer = new Server(63530, this);
			} else {
				player = players.get(1);
				currentClient = new Client("localhost", 63530, this);
			}
		} else {
			player = players.get(0);
		}
	}

	@Override
	public void update() {
		checkStatus(players);

		if (statusScreen == null) {
			if (player.getState() == 1) {
				statusScreen = new TextScreen(0, 0, 32, "You lost!");
				addDashboard(statusScreen);
			} else if (player.getState() == 2) {
				statusScreen = new TextScreen(0, 0, 32, "You win!");
				addDashboard(statusScreen);
			}
		}

		deleteDeadGameObjects();
		allObjects = vectorToArrayList(getGameObjectItems());

		if (singlePlayer == false) {
			if (currentServer != null) {
				currentServer.sendData(allObjects, tileMap);
			}
			if (currentClient != null) {
				Packet packet = currentClient.transceiveData();
				if (packet != null) {
					if (packet.getTilemap() != null) {
						this.setTileMap(packet.getTilemap());
						minimap.updateMinimap(tileMap);
					}
					if (packet.getGameObjects() != null) {
						updateGameObjects(allObjects, packet.getGameObjects());
					}
				}
			}
		}

	}
	
	private void income(){
		//System.out.println("Yay! Moneyzzz!");
		for(int i = 0; i < players.size(); i++){
			//System.out.println("Player " + i + " has a state of: " + players.get(i).getState());
			if(players.get(i).getState() == 0){
				players.get(i).addResources(1);
				//System.out.println("Player " + i + " has " + players.get(i).getResources() + " resources.");
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
		case 'q':
			if(player.getSelection().size() > 0){
				player.getSelection().get(0).getObject().destroy();
			}
			break;
		case 'b':
			build = "hq";
			break;
		case '1':
			System.out.println("1");
			build = "ifv";
			break;
		case '2':
			System.out.println("2");
			build = "mig";
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
			if (isServer) {
				if(build != null){
					IRTSObject object;
					switch(build){
					case "hq":
						object = new HQRed(xTile * TILESIZE, yTile * TILESIZE, TILESIZE, player, this);
						break;
					case "ifv":
						System.out.println("ifv");
						object = new SovIFV(xTile * TILESIZE, yTile * TILESIZE, TILESIZE, player, this);
						break;
					case "mig":
						System.out.println("mig");
						object = new SovMiG(xTile * TILESIZE, yTile * TILESIZE, TILESIZE, player, this);
						break;
					default:
						object = null;
					}
					
					if(player.makeIRTSObject(object)){
						this.addGameObject((GameObject)object);
					}
					build = null;
				}
				else{
					player.setPathOfSelection(corMouseReleased, tileMap, allObjects);
				}
			} else {
				currentClient.sendSelection(corMouseReleased);
			}
		}
	}

	private void initTileMap() {
		//temp
		Sprite grassSprite = new Sprite("src/main/java/nl/retaliation/media/sprites/grass.png");
		TileType<GrassTile> grassType = new TileType<>(GrassTile.class, grassSprite);
		Sprite waterSprite = new Sprite("src/main/java/nl/retaliation/media/sprites/water.png");
		TileType<WaterTile> waterType = new TileType<>(WaterTile.class, waterSprite);

		TileType<?>[] tileTypes = {grassType, waterType};

		LevelGenerator noise = new LevelGenerator(8000f, 128, 128);
		tileMap = new TileMap(TILESIZE, tileTypes, noise.generateNoise(0.1f));
		tileMap.setTile(3, 3, 1);
	}

	private void viewPort(int screenWidth, int screenHeight) {
		viewport = new Viewport(0, 0, screenWidth, screenHeight);
		view = new View(viewport, screenWidth, screenHeight);

		setView(view);
		size(screenWidth, screenHeight);
	}

	private ArrayList<IRTSObject> vectorToArrayList(Vector<GameObject> gameObjects) {
		ArrayList<IRTSObject> newGameObjects = new ArrayList<IRTSObject>();
		for (GameObject gameObject : gameObjects) {
			if (gameObject instanceof IRTSObject) {
				newGameObjects.add((IRTSObject) gameObject);
			}
		}
		return newGameObjects;
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
	private void createPlayerUnits() {
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
	}
	
	public ArrayList<IRTSObject> getObjects() {
		return allObjects;
	}
	public ArrayList<IPlayer> getPlayers() {
		return players;
	}
	public IPlayer getPlayer() {
		return player;
	}
	public ArrayList<Selection> getSelection() {
		return player.getSelection();
	}
	private void endGame() {
		//System.exit(0);
	}
	private void checkStatus(ArrayList<IPlayer> players) {
		int losers = 0;
		for (IPlayer player : players) {
			if (player.getBuildings().size() == 0) {
				player.setLose();
				losers++;
			}
		}
		if (losers == players.size() - 1) {
			for (IPlayer player : players) {
				if (player.getBuildings().size() != 0) {
					player.setWin();
					endGame();
				}
			}
		}
	}
	private void updateGameObjects(ArrayList<IRTSObject> oldObjects, ArrayList<IRTSObject> newObjects) {
		for (IRTSObject oldObject : oldObjects) {
			if (getObjectFromID(oldObject.getID(), newObjects) == null) {
				oldObject.destroy();
			}
		}
		for (IRTSObject newObject : newObjects) {
			IRTSObject matchingObject = getObjectFromID(newObject.getID(), oldObjects);
			if (matchingObject == null) {
				oldObjects.add(newObject);
			} else {
				matchingObject.setX(newObject.getX());
				matchingObject.setY(newObject.getY());
				matchingObject.setHealth(newObject.getHealth());
				
				if (matchingObject instanceof Unit) {
					((Unit) matchingObject).forceSpriteDirection(((Unit) newObject).getSpriteDirection());
				}
			}
		}
	}
	public IRTSObject getObjectFromID(int id, ArrayList<IRTSObject> objects) {
		for (IRTSObject object : objects) {
			if (object.getID() == id) {
				return object;
			}
		}
		return null;
	}
}
