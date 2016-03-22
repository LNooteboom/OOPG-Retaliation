package nl.retaliation.networking;

import java.util.ArrayList;

import nl.han.ica.OOPDProcessingEngineHAN.Tile.TileMap;
import nl.retaliation.IRTSObject;

public class Packet {
	private ArrayList<IRTSObject> gameObjects;
	private TileMap tilemap;
	
	public Packet (ArrayList<IRTSObject> gameObjects, TileMap tilemap) {
		this.gameObjects = gameObjects;
		this.tilemap = tilemap;
	}
	
	public ArrayList<IRTSObject> getGameObjects() {
		return gameObjects;
	}
	public TileMap getTilemap() {
		return tilemap;
	}
}
