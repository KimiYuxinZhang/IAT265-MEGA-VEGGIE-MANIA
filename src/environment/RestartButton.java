package environment;

import static util.ImageLoader.loadImage;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import util.ImageLoader;
/*The RestartButton class handles the restart functionality in the application,
 * allowing users to play again after win/lose.
 */
public class RestartButton {
	protected double xPos;
	protected double yPos;
	protected double scale;

	protected BufferedImage img;
	
	public RestartButton(double x, double y) {
		xPos = x;
		yPos = y;
		scale=1;
		img = loadImage("assets/restart.png");

	}

	public boolean clicked(double x, double y){
		boolean clicked = false;
		
		if(x > (xPos - ((double) img.getWidth())/2*scale) && x < (xPos + ((double) img.getWidth())/2*scale) && y > (yPos - ((double) img.getHeight())/2*scale) && y < (yPos + ((double) img.getHeight())/2*scale)) 
			clicked = true;
		
		return clicked;
	}
	
	public void drawButton(Graphics2D g2) {
		AffineTransform transform = g2.getTransform();
		g2.translate(xPos, yPos);
		g2.scale(scale, scale);
		g2.drawImage(img, -img.getWidth() / 2, -img.getHeight() / 2, null);
		g2.setTransform(transform);
	}

}
