package object;

import static util.ImageLoader.loadImage;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import main.GardenPanel;

public class Umbrella extends DraggableObject{
	private BufferedImage open,closed;
	public Umbrella() {
		super();
		xPos=GardenPanel.W_WIDTH/2+250;
		yPos=GardenPanel.W_HEIGHT-110;
		scale=0.3;
		open= loadImage("assets/umbrella_open.png");
		closed= loadImage("assets/umbrella_closed.png");
		img =closed;
	}

	public void changeState(String state) {
		if (state=="open")img=open;
		else img=closed;
	}
}
