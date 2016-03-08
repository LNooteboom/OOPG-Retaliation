package nl.retaliation.unit;

import java.util.ArrayList;

import nl.han.ica.OOPDProcessingEngineHAN.Objects.AnimatedSpriteObject;
import nl.han.ica.OOPDProcessingEngineHAN.Objects.GameObject;
import nl.han.ica.OOPDProcessingEngineHAN.Objects.Sprite;
import nl.han.ica.OOPDProcessingEngineHAN.Tile.TileMap;

import nl.retaliation.IRTSObject;
import nl.retaliation.logic.Trigonio;
import nl.retaliation.logic.Vector2;
import nl.retaliation.players.Player;

public abstract class Unit extends AnimatedSpriteObject implements IRTSObject{
	
	protected Vector2 tilePosition;
	protected Vector2 desiredTilePos;
	private float currentDirection = 0; //0 is right, 0,5PI is bottom etc. IN RAD
	
	protected ArrayList<Vector2> currentPath;

	private float maxSpeed;
	
	protected boolean isMoving = false;
	private boolean isIndestructible = false;
	
	private int health;
	private int armor;
	
	public Unit(float x, float y, Sprite sprite, int tileSize, float maxSpeed, int health, int armor) {
		super(sprite, 8);
		
		this.setX(x * tileSize);
		this.setY(y * tileSize);
		tilePosition = new Vector2((int) x / tileSize, (int) y / tileSize);
		
		this.setWidth(tileSize);
		this.setHeight(tileSize);
		
		this.maxSpeed = maxSpeed;
		this.health = health;
		this.armor = armor;
	}
	
	public abstract void destroy();
	
	public void update() {
		tilePosition.setX((int) (x / width));
		tilePosition.setY((int) (y / height));
		
		if (health <= 0 && isIndestructible == false) {
			destroy();
		}
		if (isMoving) {
			moveNext();
		}
	}
	
	public abstract void setPath(Vector2 desiredTilePos, TileMap terrain, ArrayList<IRTSObject> gameobjects);
	
	private void moveNext() {
		if (currentPath != null && currentPath.size() > 0) { //checks if path exists
			float nextX = currentPath.get(0).getX() * width;
			float nextY = currentPath.get(0).getY() * height;
			float deltaX = nextX - x;
			float deltaY = nextY - y;

			if (maxSpeed >= Trigonio.distance(deltaX, deltaY)) { //To prevent oscillation
				x = nextX;
				y = nextY;

				currentPath.remove(0); //sets target to next tile in path
				if (currentPath.size() == 0) { //reached destination
					isMoving = false;
				}
			} else {
				currentDirection = Trigonio.angle(deltaX, deltaY);
				updateSpriteDirection(currentDirection);
				x = x + Trigonio.xSpeed(currentDirection, maxSpeed);
				y = y + Trigonio.ySpeed(currentDirection, maxSpeed);

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
	
	public void damage(int damage) {
		health -= damage;
	}
	public int getHealth() {
		return health;
	}
	public int getArmor() {
		return armor;
	}
	
	public Vector2 getPos(){
		return tilePosition;
	}
	public Player getOwner() {
		return null;
	}
}
