package nl.retaliation.logic;

import nl.retaliation.logic.Trigonio;

/**
 * This class stores coordinates for a 2D environment
 * 
 * @author Luke Nooteboom
 *
 */
public class Vector2 {
	private int x, y;
	
	/**
	 * Create an empty position object
	 */
	public Vector2() {
		
	}
	/**
	 * Create a position object with contents
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public Vector2(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Check if the contents are equal
	 * 
	 * @param object2 the object to compare it to
	 * @return True if the objects are equal
	 */
	public boolean equal(Vector2 object2) {
		return x == object2.x && y == object2.y;
	}
	public boolean withinRadius(Vector2 object2, float radius) {
		int deltaX = x - object2.getX();
		int deltaY = y - object2.getY();
		float distance = Trigonio.distance(deltaX, deltaY);
		if (distance <= radius) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean between(Vector2 vector1, Vector2 vector2){
		if( (x <= vector1.getX() && x >= vector2.getX()) || (x >= vector1.getX() && x <= vector2.getX()) ){
			if( (y <= vector1.getY() && y >= vector2.getY()) || (y >= vector1.getY() && y <= vector2.getY()) ){
				return true;
			}
		}
		
		return false;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	public String toString(){
		return x + " " + y;
	}
}
