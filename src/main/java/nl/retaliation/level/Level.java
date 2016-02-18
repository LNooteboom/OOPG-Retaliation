package nl.retaliation.level;

public class Level {
	private int gridWidth, gridHeight, gridSize;
	private byte[][] terrain;

	Level(int gridWidth, int gridHeight, int gridSize) {
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		this.gridSize = gridSize;
		terrain = new byte[gridWidth][gridHeight];
	}
	
	void modifyTerrain(int x, int y, int type) {
		terrain[x][y] = (byte)type;
	}
	
	public int getGridSize() {
		return gridSize;
	}

	public void setGridSize(int gridSize) {
		this.gridSize = gridSize;
	}

	public int getGridWidth() {
		return gridWidth;
	}

	public int getGridHeight() {
		return gridHeight;
	}

	public byte[][] getTerrain() {
		return terrain;
	}
}
