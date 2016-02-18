package nl.retaliation.logic;

public class Vector2 {
	private int x, y;
	
	public Vector2() {
		
	}
	public Vector2(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
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
