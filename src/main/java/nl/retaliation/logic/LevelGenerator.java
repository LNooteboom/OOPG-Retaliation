package nl.retaliation.logic;

public class LevelGenerator {
	public static int[][] createNewTiles(int width, int height, float waterHeight) {
		float tiles[][] = new float[width][height];
		tiles[width/2][height/2] = 1; //zet het hoogste punt
		while (tiles[width - 1][height - 1] == 0 || tiles[0][0] == 0) {
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					if(tiles[x][y] != 0) {
						tiles = setTileNeighbors(x, y, tiles);
					}
				}
			}
		}
		int tileIndex[][] = new int[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (tiles[x][y] > waterHeight) { //water
					tileIndex[x][y] = 1;
				} else {
					tileIndex[x][y] = 0;
				}
			}
		}
		return tileIndex;
	}
	private static float[][] setTileNeighbors(int currentX, int currentY, float[][] tiles) {
		for (int xOffset = -1; xOffset <= 1; xOffset++) {
			for (int yOffset = -1; yOffset <= 1; yOffset++) {
				int x = currentX + xOffset;
				int y = currentY + yOffset;
				if (x >= 0 && x < tiles.length && y >= 0 && y < tiles[x].length && tiles[x][y] == 0) {
					//double mode = Math.random();
					float add = 1;
					//if (mode <= 0.33 && tiles[currentX][currentY] > 1) {
					add = (float) Math.random();
					//} else if (mode <= 0.66) {
					//	add = (float) Math.random();
					//}
					tiles[x][y] = tiles[currentX][currentY] + add;
				}
			}
		}

		return tiles;
	}
}
