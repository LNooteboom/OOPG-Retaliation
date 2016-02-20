package nl.retaliation;

import nl.han.ica.OOPDProcessingEngineHAN.Engine.GameEngine;
import nl.han.ica.OOPDProcessingEngineHAN.Objects.Sprite;
import nl.han.ica.OOPDProcessingEngineHAN.Tile.TileMap;
import nl.han.ica.OOPDProcessingEngineHAN.Tile.TileType;
import nl.han.ica.OOPDProcessingEngineHAN.View.View;
import nl.retaliation.groundUnit.*;
import nl.retaliation.level.*;
import processing.core.PApplet;

/**
 * Main in-game class
 * 
 * @author Luke Nooteboom
 * @author Jonathan Vos
 *
 */
public class Retaliation extends GameEngine {
	
	private final int TILESIZE = 64;
	
	private GroundUnit u = new SovIFV(0, 0);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PApplet.main(new String[]{"nl.retaliation.Retaliation"});
	}

	@Override
	public void setupGame() {
		// TODO Auto-generated method stub
		initTileMap();
		tempViewPort(800, 600);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	
	private void initTileMap() {
		//temp
		//TileType<WaterTile> waterType = new TileType<>(WaterTile.class, WaterTile.SPRITE);
		Sprite grassSprite = new Sprite("src/main/java/nl/retaliation/media/sprites/grass.png");
		TileType<GrassTile> grassType = new TileType<>(GrassTile.class, grassSprite);
		TileType[] tileTypes = {grassType};
		
		int tilesMap[][] = new int[8][8];
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				tilesMap[x][y] = 0;
				System.out.println(x + ", " + y + ", I: " + tilesMap[x][y]);
			}
		}
		
		tileMap = new TileMap(64, tileTypes, tilesMap);
	}
	
	private void tempViewPort(int screenWidth, int screenHeight) {
		View view = new View(screenWidth,screenHeight);
		view.setBackground(loadImage("src/main/java/nl/han/ica/waterworld/media/background.jpg"));

		setView(view);
		size(screenWidth, screenHeight);
	}

}
