package nl.retaliation.unit;

import java.util.ArrayList;

import nl.han.ica.OOPDProcessingEngineHAN.Objects.AnimatedSpriteObject;
import nl.han.ica.OOPDProcessingEngineHAN.Objects.GameObject;
import nl.han.ica.OOPDProcessingEngineHAN.Objects.Sprite;
import nl.han.ica.OOPDProcessingEngineHAN.Tile.TileMap;
import nl.retaliation.logic.Pathfind;
import nl.retaliation.logic.Trigonio;
import nl.retaliation.logic.Vector2;

/**
 * GroundUnit class
 * 
 * @author Luke Nooteboom
 *
 */
public abstract class GroundUnit extends AnimatedSpriteObject{
	//@SuppressWarnings("unused")
	//private float x, y;
	private Vector2 tilePosition;
	private Vector2 desiredTilePos;
	//private Vector2 nextTilePosition;
	private float currentDirection = 0; //0 is right, 0,5PI is bottom etc. IN GRAD
	
	private ArrayList<Vector2> currentPath;

	private float maxSpeed;
	
	private boolean canStepOnWater = false;
	private boolean canStepOnLand = true;
	
	private boolean isSelected = false;
	private boolean isMoving = false;
	
	private int health;
	
	public GroundUnit(float x, float y, Sprite sprite, int tileSize, float maxSpeed, int health) {
		super(sprite, 8);
		
		this.setX(x * tileSize);
		this.setY(y * tileSize);
		tilePosition = new Vector2((int) x / tileSize, (int) y / tileSize);
		
		this.setWidth(tileSize);
		this.setHeight(tileSize);
		
		this.maxSpeed = maxSpeed;
		this.health = health;
		
		System.out.println("created unit");
	}
	
	public void update() {
		tilePosition.setX((int) (x / width));
		tilePosition.setY((int) (y / height));
		
		if (isMoving) {
			moveNext();
		}
	}
	
	/* Moving */
	public void setPath(Vector2 desiredTilePos, TileMap terrain, GameObject[] gameobjects) {
		isMoving = true;
		this.desiredTilePos = desiredTilePos;
		this.currentPath = Pathfind.calcPath(tilePosition, desiredTilePos, terrain, this, gameobjects, canStepOnLand, canStepOnWater);
	}
	private void moveNext() {
		//TODO: finish this
		if (currentPath != null && currentPath.size() > 0) { //checks if path exists
			float nextX = currentPath.get(0).getX() * width;
			float nextY = currentPath.get(0).getY() * height;
			float deltaX = nextX - x;
			float deltaY = nextY - y;

			if (maxSpeed >= Trigonio.distance(deltaX, deltaY)) { //To prevent oscillation
				x = nextX;
				y = nextY;

				currentPath.remove(0); //sets target to next tile in path
			} else {
				currentDirection = Trigonio.angle(deltaX, deltaY);
				updateSpriteDirection(currentDirection);
				x = x + Trigonio.xSpeed(currentDirection, maxSpeed);
				y = y + Trigonio.ySpeed(currentDirection, maxSpeed);
				System.out.println(currentDirection / Trigonio.PI);

				Vector2 newTile = new Vector2((int) Math.floor(x / width), (int) Math.floor(y / height));
				if (newTile.equal(currentPath.get(0))) {
					tilePosition = currentPath.get(0);
				}
			}
		}
	}
	private void updateSpriteDirection(double trueDirection){
		double pi8 = Trigonio.PI / 8;
		if (trueDirection >= pi8 && trueDirection < Trigonio.QUARTER_PI + pi8) { //bottom-right
			setCurrentFrameIndex(1);
			return;
		}
		if (trueDirection >= Trigonio.QUARTER_PI + pi8 && trueDirection < Trigonio.HALF_PI + pi8) { //bottom
			setCurrentFrameIndex(2);
			return;
		}
		if (trueDirection >= Trigonio.HALF_PI + pi8 && trueDirection < Trigonio.PI - pi8) { //bottom-left
			setCurrentFrameIndex(3);
			return;
		}
		if ((trueDirection >= Trigonio.PI - pi8 && trueDirection < Trigonio.PI + pi8)|| (trueDirection <= -(Trigonio.PI - pi8) && trueDirection > -(Trigonio.PI + pi8))) { //left
			setCurrentFrameIndex(4);
			return;
		}
		if (trueDirection <= -(Trigonio.HALF_PI + pi8) && trueDirection > -(Trigonio.PI - pi8)) { //top-left
			setCurrentFrameIndex(5);
			return;
		}
		if (trueDirection <= -(Trigonio.QUARTER_PI + pi8) && trueDirection > -(Trigonio.HALF_PI + pi8)) { //top
			setCurrentFrameIndex(6);
			return;
		}
		if (trueDirection <= -pi8 && trueDirection > -(Trigonio.QUARTER_PI + pi8)) { //top-right
			setCurrentFrameIndex(7);
			return;
		}
		setCurrentFrameIndex(0); //right
	}

	
	/* Getters & Setters */
	public Vector2 getDesiredTilePos() {
		return desiredTilePos;
	}
	public void setDesiredTilePos(Vector2 desiredTilePos) {
		this.desiredTilePos = desiredTilePos;
	}

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
