package nl.han.ica.OOPDProcessingEngineHAN.Objects;

import nl.han.ica.OOPDProcessingEngineHAN.View.Viewport;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * The SpriteObject is a extension of GameObject. Instead of having a draw implementation you will need to provide an image/sprite.
 */
public abstract class SpriteObject extends GameObject {

	private Sprite sprite;
	
	/**
	 * Create a new SpriteObject with a Sprite object.
	 * @param sprite
	 */
	public SpriteObject(Sprite sprite) {
		super();
		
		this.sprite = sprite;
		
		setWidth(sprite.getWidth());
		setHeight(sprite.getHeight());
	}
	
	/**
	 * Draws the image on the PGraphics object, this is fired by the GameEngine.
	 */
	@Override
	public void drawWithViewport(PGraphics g, Viewport viewport)
	{
		g.image(sprite.getPImage(), x - viewport.getX(), y - viewport.getY());
	}
	public void draw(PGraphics g) {
		
	}
	
	/**
	 * Get the PImage of the Sprite.
	 * @return PImage
	 */
	public PImage getImage()
	{
		return sprite.getPImage();
	}
}
