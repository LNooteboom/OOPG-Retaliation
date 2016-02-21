package nl.retaliation;


import java.util.Vector;

import nl.han.ica.OOPDProcessingEngineHAN.Engine.GameEngine;
import nl.han.ica.OOPDProcessingEngineHAN.Objects.GameObject;
import nl.han.ica.OOPDProcessingEngineHAN.Objects.Sprite;
import nl.han.ica.OOPDProcessingEngineHAN.Tile.TileMap;
import nl.han.ica.OOPDProcessingEngineHAN.Tile.TileType;
import nl.han.ica.OOPDProcessingEngineHAN.View.View;
import nl.retaliation.groundUnit.*;
import nl.retaliation.level.*;
import nl.retaliation.logic.LevelGenerator;
import nl.retaliation.logic.Vector2;
import processing.core.PApplet;

/**
 * Main in-game class
 * 
 * @author Luke Nooteboom
 * @author Jonathan Vos
 *
 */
@SuppressWarnings("serial")
public class Retaliation extends GameEngine {
	
	private final int TILESIZE = 32;
	
	private GroundUnit u = new SovIFV(6, 6, TILESIZE);

	public static void main(String[] args) {
		PApplet.main(new String[]{"nl.retaliation.Retaliation"});
	}

	@Override
	public void setupGame() {
		addGameObject(u);
		initTileMap();
		tempViewPort(800, 600);
	}

	@Override
	public void update() {
		
	}
	
	@Override
	public void mouseClicked() {
		GameObject allUnits[] = vectorToArray(getGameObjectItems());
		u.setPath(new Vector2((int) (mouseX / TILESIZE), (int) (mouseY / TILESIZE)), tileMap, allUnits);
		System.out.println((int) Math.random() * 7);
	}
	
	private void initTileMap() {
		//temp
		Sprite grassSprite = new Sprite("src/main/java/nl/retaliation/media/sprites/grass.png");
		TileType<GrassTile> grassType = new TileType<>(GrassTile.class, grassSprite);
		Sprite waterSprite = new Sprite("src/main/java/nl/retaliation/media/sprites/water.png");
		TileType<WaterTile> waterType = new TileType<>(WaterTile.class, waterSprite);
		
		TileType[] tileTypes = {grassType, waterType};
		
		int tilesMap[][] = new int[8][8];
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				tilesMap[x][y] = 0;
				//System.out.println(x + ", " + y + ", I: " + tilesMap[x][y]);
			}
		}
		tilesMap[2][2] = 1;
		
		tileMap = new TileMap(TILESIZE, tileTypes, LevelGenerator.createNewTiles(16, 16, (float)3.4));
	}
	
	private void tempViewPort(int screenWidth, int screenHeight) {
		View view = new View(screenWidth,screenHeight);
		view.setBackground(loadImage("src/main/java/nl/han/ica/waterworld/media/background.jpg"));

		setView(view);
		size(screenWidth, screenHeight);
	}
	
	private GameObject[] vectorToArray(Vector<GameObject> gameObjects) {
		GameObject newGameObjects[] = new GameObject[gameObjects.size()];
		for (int i = 0; i < gameObjects.size(); i++) {
			newGameObjects[i] = gameObjects.get(i);
		}
		return newGameObjects;
	}

}
