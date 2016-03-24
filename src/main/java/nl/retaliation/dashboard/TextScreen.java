package nl.retaliation.dashboard;

import nl.han.ica.OOPDProcessingEngineHAN.Dashboard.Dashboard;
import processing.core.PGraphics;

public class TextScreen extends Dashboard {
	private String content;

	public TextScreen(float x, float y, int fontSize, String text) {
		super(x, y, text.length() * fontSize, fontSize);
		this.content = text;
	}
	
	@Override
	public void draw(PGraphics g) {
		g.textSize(32);
		g.text(content, 0, height);
		g.textSize(10);
	}

}
