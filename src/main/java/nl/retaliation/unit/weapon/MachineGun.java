package nl.retaliation.unit.weapon;

import processing.core.PGraphics;
import nl.han.ica.OOPDProcessingEngineHAN.Objects.GameObject;
import nl.han.ica.OOPDProcessingEngineHAN.View.Viewport;

/**
 * 
 * @author Luke Nooteboom
 *
 */

public class MachineGun extends Weapon{
	public MachineGun(GameObject parent, int tileSize) {
		super(parent, 10 * tileSize, 10, false);
		this.damage = 10;
	}

	@Override
	public void draw(PGraphics g) {
		// Don't do shit
	}

	@Override
	public void drawWithViewport(PGraphics g, Viewport viewport) {
		// Don't do shit
	}
}
