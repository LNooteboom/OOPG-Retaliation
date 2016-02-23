package nl.retaliation.logic;

import java.util.Random;

public class Noise {
	
	private Random random;
	private float roughness;
	private float[][] tiles;
	private int totalSize;
	
	public Noise(float roughness, int width, int height) {
		this.roughness = roughness / width;
		this.tiles = new float[width][height];
		this.totalSize = width + height;
		this.random = new Random();
	}
	
	public int[][] generateNoise(float waterHeight) {
		int maxX = tiles.length - 1;
		int maxY = tiles[0].length - 1;
		
		//set corners
		tiles[0][0] = randomMinHalf();
		tiles[maxX][0] = randomMinHalf();
		tiles[0][maxY] = randomMinHalf();
		tiles[maxX][maxY] = randomMinHalf();
		
		generateFractal(0, 0, maxX + 1, maxY + 1, tiles[0][0], tiles[maxX][0], tiles[maxX][maxY], tiles[0][maxY]);
		return toTileIndex(waterHeight);
	}
	
	private void generateFractal(int x, int y, int width, int height, float cornerValue1, float cornerValue2, float cornerValue3, float cornerValue4) {
		/*
		 * 1-1-2
		 * |4c |2
		 * 4-3-3
		 */
		
		int middlePointX = width / 2;
		int middlePointY = height / 2;
		
		if (width > 1 && height > 1) {
			//Diamond step
			//The center value is the average of all corners
			float centerValue = (cornerValue1 + cornerValue2 + cornerValue3 + cornerValue4) / 4;
			centerValue += shift(middlePointX + middlePointY);
			
			//Square step
			// a side is the average of its connected corners
			float side1 = normalize((cornerValue1 + cornerValue2) / 2);
			float side2 = normalize((cornerValue2 + cornerValue3) / 2);
			float side3 = normalize((cornerValue3 + cornerValue4) / 2);
			float side4 = normalize((cornerValue4 + cornerValue1) / 2);
			
			//repeat this for new squares
			generateFractal(x, y, middlePointX, middlePointY, cornerValue1, side1, centerValue, side4);
			generateFractal(x + middlePointX, y, width - middlePointX, middlePointY, side1, cornerValue2, side2, centerValue);
			generateFractal(x + middlePointX, y + middlePointY, width - middlePointX, height - middlePointY, centerValue, side2, cornerValue3, side3); 
			generateFractal(x, y + middlePointY, middlePointX, height - middlePointY, side4, centerValue, side3, cornerValue4);
			
		} else {
			//store the new value in the array
			//if (x <= tiles.length && y <= tiles[0].length) {
				tiles[x][y] = (cornerValue1 + cornerValue2 + cornerValue3 + cornerValue4) / 4;
			//}
		}
	}
	private float normalize(float input) {
		if (input < 0) {
			return 0;
		} else if (input > 1) {
			return 1;
		} else {
			return input;
		}
	}
	private float shift(int smallSize) {
		return (float) ((Math.random() - 0.5) * smallSize / totalSize * roughness);
	}
	private float randomMinHalf() {
		return random.nextFloat();
	}
	private int[][] toTileIndex(float waterHeight) {
		int[][] tileIndex = new int[tiles.length][tiles[0].length];
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[0].length; y++) {
				if (tiles[x][y] > waterHeight) { //land
					tileIndex[x][y] = 0;
				} else {
					tileIndex[x][y] = 1;
				}
			}
		}
		return tileIndex;
	}
}
