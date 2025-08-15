package environment;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import static util.ImageLoader.loadImage;

import main.GardenPanel;
import util.ImageLoader;
/*The FlowerPot class represents a flowerpot in the garden application.
 * It manages the planting and harvesting the veggie within the pot.
 */
public class Flowerpot {
	protected BufferedImage img;
	protected double width,height;
	protected double xPos,yPos;
	protected double scale;

	public Flowerpot() {
		img = loadImage("assets/flowerpot.png");
		scale=1;
		width=img.getWidth()*scale;
		height=img.getHeight()*scale;
		xPos=GardenPanel.W_WIDTH/2;
		yPos=GardenPanel.W_HEIGHT-height/2;
	}
	
	public void drawPot(Graphics2D g2) {
		AffineTransform at = g2.getTransform();
		g2.translate(xPos, yPos);
		g2.scale(scale, scale);
		g2.drawImage(img,(int) -width/2,(int) -height/2, null);
		g2.setTransform(at);
	}
	
	public double getWidth() {
		return width;
	}
	public double getHeight() {
		return height;
	}
}
