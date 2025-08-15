package object;

import static util.ImageLoader.loadImage;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import main.GardenPanel;
/*responsible for handling the seed bag in the gardening application.
 * It allows the user to drag the seed bag and plant seed in the pot.
 */
public class Seedbag extends DraggableObject {
	public Seedbag() {
		super();
		xPos=90;
		yPos=GardenPanel.W_HEIGHT-100;
		scale=0.2;
		img = loadImage("assets/seedbag.png");
	}

	@Override
	public void changeState(String string) {	
	}
	
}
