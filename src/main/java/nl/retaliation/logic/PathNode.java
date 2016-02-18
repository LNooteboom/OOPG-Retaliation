package nl.retaliation.logic;


class PathNode {
	private Vector2 pos;
	private double gCost;
	private double fCost;
	private Vector2 parent;

	public PathNode(Vector2 pos, double gCost) {
		this.pos = pos;
		this.gCost = gCost;
	}
	public PathNode(Vector2 pos, double gCost, Vector2 parent) {
		this.pos = pos;
		this.gCost = gCost;
		this.parent = parent;
	}
	public PathNode(Vector2 pos, double gCost, double fCost, Vector2 parent) {
		this.pos = pos;
		this.gCost = gCost;
		this.fCost = fCost;
		this.parent = parent;
	}
	
	
	public Vector2 getPos() {
		return pos;
	}
	public void setPos(Vector2 pos) {
		this.pos = pos;
	}
	public double getgCost() {
		return gCost;
	}
	public void setgCost(double gCost) {
		this.gCost = gCost;
	}
	public double getfCost() {
		return fCost;
	}
	public void setfCost(double fCost) {
		this.fCost = fCost;
	}
	public Vector2 getParent() {
		return parent;
	}
	public void setParent(Vector2 parent) {
		this.parent = parent;
	}
	
}
