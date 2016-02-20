package nl.retaliation.groundUnit;

import nl.han.ica.OOPDProcessingEngineHAN.Objects.AnimatedSpriteObject;
import nl.han.ica.OOPDProcessingEngineHAN.Objects.Sprite;
import nl.retaliation.logic.Trigonio;
import nl.retaliation.logic.Vector2;

/**
 * GroundUnit class
 * 
 * @author Luke Nooteboom
 *
 */
abstract class GroundUnit extends AnimatedSpriteObject{
	//@SuppressWarnings("unused")
	private float x, y;
	private Vector2 tilePosition;
	private Vector2 desiredTilePos;
	private Vector2 nextTilePosition;
	private float direction; //0 is rechts, 0,5PI is onder etc. IN GRAD

	private float maxSpeed;
	
	private Sprite sprite;
	private int spriteDirection;
	
	private boolean canStepOnWater = false;
	private boolean canStepOnLand = true;
	
	private boolean isSelected = false;
	private boolean isMoving = false;
	
	public GroundUnit(float x, float y, Sprite sprite, int tileSize, float maxSpeed) {
		super(sprite, 8);
		
		this.setX(x);
		this.setY(y);
		
		this.setWidth(tileSize);
		this.setHeight(tileSize);
		
		this.maxSpeed = maxSpeed;
	}
	
	public void update() {
		tilePosition.setX((int) (x / width));
		tilePosition.setY((int) (y / height));
		
		if (isMoving) {
			moveNext();
		}
	}
	
	/* Moving */
	private void moveNext() {
		//TODO: finish this
		float nextX = nextTilePosition.getX() * width;
		float nextY = nextTilePosition.getY() * height;
		float deltaX = nextX - x;
		float deltaY = nextY - y;

		if (maxSpeed >= Trigonio.distance(deltaX, deltaY)) { //To prevent oscillation
			x = nextX;
			y = nextY;
		} else {
			direction = Trigonio.angle(deltaX, deltaY);
			toSpriteDirection(direction);
			x = x + Trigonio.xSpeed(direction, maxSpeed);
			y = y + Trigonio.ySpeed(direction, maxSpeed);
			
			Vector2 newTile = new Vector2((int) Math.floor(x / width), (int) Math.floor(y / height));
			if (newTile.equal(nextTilePosition)) {
				tilePosition = nextTilePosition;
			}
		}
	}
	private int toSpriteDirection(double trueDirection){
		double pi8 = Trigonio.PI / 8;
		if (trueDirection >= pi8 && trueDirection < Trigonio.QUARTER_PI + pi8) { //Rechtsonder
			return 1;
		}
		if (trueDirection >= Trigonio.QUARTER_PI + pi8 && trueDirection < Trigonio.HALF_PI + pi8) { //Onder
			return 2;
		}
		if (trueDirection >= Trigonio.HALF_PI + pi8 && trueDirection < Trigonio.PI - pi8) { //Linksonder
			return 3;
		}
		if (trueDirection >= Trigonio.PI - pi8 && trueDirection < Trigonio.PI + pi8) { //Links
			return 4;
		}
		if (trueDirection >= Trigonio.PI + pi8 && trueDirection < Trigonio.PI + Trigonio.QUARTER_PI + pi8) { //Linksboven
			return 5;
		}
		if (trueDirection >= Trigonio.PI + Trigonio.QUARTER_PI + pi8 && trueDirection < Trigonio.PI + Trigonio.HALF_PI + pi8) { //Boven
			return 6;
		}
		if (trueDirection >= Trigonio.PI + Trigonio.HALF_PI + pi8 && trueDirection < Trigonio.TAU - pi8) { //Rechtsboven
			return 7;
		}
		return 0; //Rechts
	}

	
	/* Getters & Setters */
	public Vector2 getDesiredTilePos() {
		return desiredTilePos;
	}
	public void setDesiredTilePos(Vector2 desiredTilePos) {
		this.desiredTilePos = desiredTilePos;
	}
	
}
