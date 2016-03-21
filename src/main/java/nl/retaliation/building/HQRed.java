package nl.retaliation.building;

import nl.han.ica.OOPDProcessingEngineHAN.Engine.GameEngine;
import nl.han.ica.OOPDProcessingEngineHAN.Objects.Sprite;
import nl.retaliation.players.IPlayer;

public class HQRed extends Building{
	public HQRed(float x, float y, int TILESIZE, IPlayer player){
		super(x, y, new Sprite("nl/retaliation/media/sprites/HQ.png"), TILESIZE, 1000, 300, player);
		
	}

	@Override
	public void destroy() {
		// Boom!
	}

	@Override
	public void addToEngine(GameEngine engine) {
		// TODO Auto-generated method stub
		
	}
}
