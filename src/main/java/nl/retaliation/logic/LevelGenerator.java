package nl.retaliation.logic;

/**
 * A simple random island level generator
 * Using an algorithm I found out myself (not very optimised, it should only be used at the start of a match)
 * 
 * @author Luke Nooteboom
 *
 */
public class LevelGenerator {
	/**
	 * Generates an island level
	 * 
	 * @param width
	 * @param height
	 * @param waterHeight Invert this
	 * @return New level
	 */
	public static int[][] createNewTiles(int width, int height, float waterHeight) {
		float tiles[][] = new float[width][height]; //heightmap, higher numbers mean lower terrain.
		tiles[width/2][height/2] = 1; //Puts the highest point in the middle.
		while (tiles[width - 1][height - 1] == 0 || tiles[0][0] == 0) { //while the corners of the map are not assigned
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					if(tiles[x][y] != 0) { //if this tile is already assigned
						tiles = setTileNeighbors(x, y, tiles); //set this tile's neighbours
					}
				}
			}
		}
		//convert the heightmap into a tileIndex array
		int tileIndex[][] = new int[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (tiles[x][y] > waterHeight) {
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
					tiles[x][y] = tiles[currentX][currentY] + add; //for each neighbour of tile: assign the value of parent + a random number from 0 to 1;
				}
			}
		}

		return tiles;
	}
}
