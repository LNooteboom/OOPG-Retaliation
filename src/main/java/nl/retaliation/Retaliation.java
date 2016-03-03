package nl.retaliation;


import java.util.ArrayList;
import java.util.Vector;

import nl.han.ica.OOPDProcessingEngineHAN.Engine.GameEngine;
import nl.han.ica.OOPDProcessingEngineHAN.Objects.GameObject;
import nl.han.ica.OOPDProcessingEngineHAN.Objects.Sprite;
import nl.han.ica.OOPDProcessingEngineHAN.Tile.Tile;
import nl.han.ica.OOPDProcessingEngineHAN.Tile.TileMap;
import nl.han.ica.OOPDProcessingEngineHAN.Tile.TileType;
import nl.han.ica.OOPDProcessingEngineHAN.View.View;
import nl.han.ica.OOPDProcessingEngineHAN.View.Viewport;
import nl.retaliation.logic.*;
import nl.retaliation.unit.*;
import nl.retaliation.level.*;
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
	
	private GameObject allObjects[];
	private ArrayList<Unit> units = new ArrayList<Unit>(100);
	
	private Unit selectedUnit = null;

	public static void main(String[] args) {
		PApplet.main(new String[]{"nl.retaliation.Retaliation"});
	}

	@Override
	public void setupGame() {
		units.add(new SovIFV(6, 6, TILESIZE));
		units.add(new SovIFV(10, 10, TILESIZE));
		units.add(new SovMiG(13, 13, TILESIZE));
		for(Unit unit : units){
			addGameObject(unit);
		}
		initTileMap();
		tempViewPort(800, 600);
		setFPSCounter(true);
	}

	@Override
	public void update() {
		allObjects = vectorToArray(getGameObjectItems());
		deleteDeadGameObjects();
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
	public void mouseClicked() {
		int xTile = (int) ((viewport.getX() + mouseX) / TILESIZE);
		int yTile = (int) ((viewport.getY() + mouseY) / TILESIZE);
		Vector2 tileCor = new Vector2(xTile, yTile);
		Tile clickedTile = tileMap.getTileOnIndex(xTile, yTile);
		
		Unit clickedUnit = this.vectorToUnit(tileCor);
		
		if(mouseButton == LEFT){
			if(clickedUnit != null){
				selectedUnit = clickedUnit;
				System.out.println(selectedUnit.getCor().toString());
			}
		}
		if(mouseButton == RIGHT){
			selectedUnit.setPath(tileCor, tileMap, this.getGameObjectItems().toArray(new GameObject[this.getGameObjectItems().size()]));
		}
		
		if(clickedTile instanceof WaterTile){
			System.out.println("Je moeder!");
		}
		if(clickedTile instanceof GrassTile){
			System.out.println("Je vader!");
		}
		
		//u.setPath(new Vector2((int) ((viewport.getX() + mouseX) / TILESIZE), (int) ((viewport.getY() + mouseY) / TILESIZE)), tileMap, allObjects);
	}
	
	private Unit vectorToUnit(Vector2 cor){
		for(Unit unit : units){
			if(unit.getCor().equal(cor)){
				return unit;
			}
		}
		
		return null;
	}
	
	private void initTileMap() {
		//temp
		Sprite grassSprite = new Sprite("src/main/java/nl/retaliation/media/sprites/grass.png");
		TileType<GrassTile> grassType = new TileType<>(GrassTile.class, grassSprite);
		Sprite waterSprite = new Sprite("src/main/java/nl/retaliation/media/sprites/water.png");
		TileType<WaterTile> waterType = new TileType<>(WaterTile.class, waterSprite);
		
		TileType<?>[] tileTypes = {grassType, waterType};
		
		LevelGenerator noise = new LevelGenerator(0.5f, 128, 128);
		tileMap = new TileMap(TILESIZE, tileTypes, noise.generateNoise(0.0f));
		tileMap.setTile(3, 3, 1);
	}
	
	private void tempViewPort(int screenWidth, int screenHeight) {
		viewport = new Viewport(0, 0, screenWidth, screenHeight);
		view = new View(viewport, screenWidth, screenHeight);

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
