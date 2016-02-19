package nl.retaliation.level;

import nl.han.ica.OOPDProcessingEngineHAN.Objects.Sprite;
import nl.han.ica.OOPDProcessingEngineHAN.Tile.Tile;

/**
 * Terrain tile
 * 
 * @author Luke Nooteboom
 *
 */
public class TerrainTile extends Tile {
	
	private boolean isWater;
	
	public TerrainTile(Sprite sprite, boolean isWater) {
		super(sprite);
		this.setWater(isWater);
	}

	public boolean isWater() {
		return isWater;
	}

	public void setWater(boolean isWater) {
		this.isWater = isWater;
	}
}
