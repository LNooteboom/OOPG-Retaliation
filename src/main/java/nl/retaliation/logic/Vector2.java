package nl.retaliation.logic;

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
	boolean equal(Vector2 object2) {
		return x == object2.x && y == object2.y;
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
}
