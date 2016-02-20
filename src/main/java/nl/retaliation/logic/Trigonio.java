package nl.retaliation.logic;

/**
 * A set of simple trigonometric functions
 * 
 * @author Luke Nooteboom
 *
 */
public class Trigonio {
	
	/**
	 * Calculates the distance between coordinates
	 * 
	 * @param deltaX Difference in x
	 * @param deltaY Difference in y
	 * @return Distance
	 */
	public static float distance(float deltaX, float deltaY) {
		return (float) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
	}
	/**
	 * Calculates the distance between coordinates
	 * 
	 * @param x1 X Coordinate of object 1
	 * @param y1 Y Coordinate of object 1
	 * @param x2 X Coordinate of object 2
	 * @param y2 Y Coordinate of object 2
	 * @return Distance
	 */
	public static float distance(float x1, float y1, float x2, float y2) {
		return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}
	
	/**
	 * Calculates the angle between coordinates
	 * 
	 * @param deltaX Difference in x
	 * @param deltaY Difference in y
	 * @return Angle in RADS
	 */
	public static float angle(float deltaX, float deltaY) {
		return (float) Math.atan2(deltaY, deltaX);
	}
	/**
	 * Calculates the angle between coordinates
	 * 
	 * @param x1 X Coordinate of object 1
	 * @param y1 Y Coordinate of object 1
	 * @param x2 X Coordinate of object 2
	 * @param y2 Y Coordinate of object 2
	 * @return Angle in RADS
	 */
	public static float angle(float x1, float y1, float x2, float y2) {
		float deltaX = x2 - x1;
		float deltaY = y2 - y1;
		return (float) Math.atan2(deltaY, deltaX);
	}
	
	/**
	 * Calculates the X speed from the direction and total speed
	 * 
	 * @param direction The direction the object is travelling
	 * @param speed The total speed
	 * @return X-axis speed
	 */
	public static float xSpeed(float direction, float speed) {
		return (float) Math.cos(direction) * speed;
	}
	/**
	 * Calculates the Y speed from the direction and total speed
	 * 
	 * @param direction The direction the object is travelling
	 * @param speed The total speed
	 * @return Y-axis speed
	 */
	public static float ySpeed(float direction, float speed) {
		return (float) Math.sin(direction) * speed;
	}
}
