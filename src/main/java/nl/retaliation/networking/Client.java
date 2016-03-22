package nl.retaliation.networking;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import nl.han.ica.OOPDProcessingEngineHAN.Objects.Sprite;
import nl.han.ica.OOPDProcessingEngineHAN.Tile.TileMap;
import nl.han.ica.OOPDProcessingEngineHAN.Tile.TileType;
import nl.retaliation.IRTSObject;
import nl.retaliation.level.GrassTile;
import nl.retaliation.level.WaterTile;
import nl.retaliation.logic.Vector2;
import nl.retaliation.unit.Unit;

public class Client {
	private int hostPort;
	private String hostName;
	private BufferedReader input;
	private PrintWriter output;
	private Socket socket;
	
	private TileMap clientTilemap;
	
	public Client(String hostName, int hostPort, TileMap tilemap) {
		this.hostName = hostName;
		this.hostPort = hostPort;
		this.clientTilemap = tilemap;
		
		createClient();
		//System.out.println("c: "+socket.isClosed());
	}
	
	private void createClient() {
		try {
			Socket socket = new Socket(hostName, hostPort);
			//socket.setKeepAlive(true);
			socket.setTcpNoDelay(true);
			this.socket = socket;
			
			this.input = new BufferedReader(new InputStreamReader(new BufferedInputStream(socket.getInputStream())));
			//this.output = new PrintWriter(socket.getOutputStream(), true);
			
			System.out.println("connected!");
			
		} catch (UnknownHostException e){
			System.out.println("Could not find host");
		} catch (IOException e) {
			System.out.println("failed to connect");
		}
	}
	
	public Packet transceiveData() {
		try {
			
			String line = input.readLine();
			System.out.println(line);
			if (line.charAt(0) == '#' || line.charAt(1) == '#') {
				System.out.println("tm");
				return new Packet(null, deserializeTileMap(line));
			} else {
				return new Packet(deserializeGameObjects(line), null);
			}
		} catch (IOException e) {
			System.out.println(e);
		}
		return null;
	}
	private ArrayList<IRTSObject> deserializeGameObjects(String input) {
		ArrayList<IRTSObject> gameobjects = new ArrayList<IRTSObject>();
		char objectSeperator = '%';
		int prevSepPos = 0;
		
		for (int i = 1; i < input.length(); i++) {
			if (input.charAt(i) == objectSeperator) {
				String objectString = input.substring(prevSepPos + 1, i);
				gameobjects.add(deserializeGameObject(objectString));
				
				prevSepPos = i;
				//break;
			} else if (i == input.length() - 1) {
				String objectString = input.substring(prevSepPos + 1, input.length());
				gameobjects.add(deserializeGameObject(objectString));
			}
		}
		return gameobjects;
	}
	@SuppressWarnings("unchecked")
	private IRTSObject deserializeGameObject(String input) {
		Class<IRTSObject> objectClass = null;
		boolean classExists = false;
		
		IRTSObject newObject;
		int xPos = 0;
		int yPos = 0;
		int direction;
		
		int prevSepPos = 0;
		char seperator = '$';
		for (int i = 1; i < input.length(); i++) {
			if (input.charAt(i) == seperator) {
				String className = input.substring(7, i);
				
				try {
					objectClass = (Class<IRTSObject>) Class.forName(className);
					classExists = true;
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				prevSepPos = i;
				break;
			}
		}
		for (int i = prevSepPos + 1; i < input.length(); i++) {
			if (input.charAt(i) == '$') {
				xPos = Integer.parseInt(input.substring(prevSepPos + 1, i));
				prevSepPos = i;
				break;
			}
		}
		for (int i = prevSepPos + 1; i < input.length(); i++) {
			if (input.charAt(i) == '$') {
				yPos = Integer.parseInt(input.substring(prevSepPos + 1, i));
				prevSepPos = i;
				break;
			}
		}
		direction = Integer.parseInt(input.substring(prevSepPos + 1, input.length()));
		if (classExists) {
			
			Class<?>[] pars = {Float.TYPE, Float.TYPE, Integer.TYPE};
			Object[] initargs = {xPos, yPos, 32};
			try {
				Constructor<?> constructor = objectClass.getConstructor(pars);
				try {
					newObject = (IRTSObject) constructor.newInstance(initargs);
					//Holy shit het werkt!!
					if (newObject instanceof Unit) {
						((Unit) newObject).forceSpriteDirection(direction);
					}
					return newObject;
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	private TileMap deserializeTileMap(String input) {
		int prevSepPos = 0;
		ArrayList<Integer> mapData = new ArrayList<Integer>();
		input = input.substring(1, input.length());
		for (int i = prevSepPos + 1; i < input.length(); i++) {
			if (input.charAt(i) == '$') {
				mapData.add(Integer.parseInt(input.substring(prevSepPos + 1, i)));
				//System.out.println("ss "+Integer.parseInt(input.substring(prevSepPos + 1, i)));
				prevSepPos = i;
			}
		}
		
		int mapWidthCounter = 0;
		int mapHeightCounter = 0;
		int[][] indexMap = new int[mapData.get(1)][mapData.get(0)];
		for (int i = 2; i < mapData.size(); i++) {
			if (mapHeightCounter < indexMap.length) {
				if (mapWidthCounter >= indexMap[0].length) {
					mapWidthCounter = 0;
					mapHeightCounter++;
				}
				indexMap[mapHeightCounter][mapWidthCounter] = mapData.get(i);
				mapWidthCounter++;
			}
		}
//		for (int x = 0; x < indexMap[0].length; x++) {
//			String line = "";
//			for (int y = 0; y < indexMap.length; y++) {
//				line += indexMap[y][x];
//			}
//			System.out.println(line);
//		}
		
		Sprite grassSprite = new Sprite("src/main/java/nl/retaliation/media/sprites/grass.png");
		TileType<GrassTile> grassType = new TileType<>(GrassTile.class, grassSprite);
		Sprite waterSprite = new Sprite("src/main/java/nl/retaliation/media/sprites/water.png");
		TileType<WaterTile> waterType = new TileType<>(WaterTile.class, waterSprite);
		
		TileType<?>[] tileTypes = {grassType, waterType};
		
		TileMap tilemap = new TileMap(32, tileTypes, indexMap);
		
		
		return tilemap;
	}
	public void sendClick(Vector2 pos) {
		output.println(pos.getX() + "$" + pos.getY());
	}
	public void disconnect() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
