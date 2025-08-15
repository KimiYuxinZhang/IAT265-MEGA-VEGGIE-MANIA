package object;

import static util.ImageLoader.loadImage;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import environment.Flowerpot;
import main.GardenPanel;
import processing.core.PVector;
/*DraggableObject is a base class for objects in the garden simulation that can be dragged
 * and dropped by the user, it's the superclass for various tools.
 */
public abstract class DraggableObject {
	protected double xPos;
	protected double yPos;
	protected double scale;
	protected BufferedImage img;
	
	public DraggableObject() {
		scale=1;
		img = loadImage("assets/seed.png");
	}

	public boolean hitPot(Flowerpot pot) {
		boolean hit = false;

		if (Math.abs(xPos - GardenPanel.W_WIDTH/2) < (pot.getWidth()/2)
				&& Math.abs(yPos- (GardenPanel.W_HEIGHT-pot.getHeight()/2)) < (pot.getHeight()/2))
			hit = true;
		
		return hit;
	}
	
	public boolean clicked(double x, double y){
		boolean clicked = false;
		
		if(x > (xPos - ((double) img.getWidth())/2*scale) && x < (xPos + ((double) img.getWidth())/2*scale) && y > (yPos - ((double) img.getHeight())/2*scale) && y < (yPos + ((double) img.getHeight())/2*scale)) 
			clicked = true;
		
		return clicked;
	}
	
	public void setXPos(double x){
		xPos = x;
	}
	
	public void setYPos(double y){
		yPos = y;
	}
	public double getWidth() {
		return img.getWidth()*scale;
	}
	public double getHeight() {
		return img.getHeight()*scale;
	}
	public double getX() {
		return xPos;
	}
	public double getY() {
		return yPos;
	}
	public void drawImg(Graphics2D g2) {
		AffineTransform transform = g2.getTransform();
		g2.translate(xPos, yPos);
		g2.scale(scale, scale);
		g2.drawImage(img, -img.getWidth() / 2, -img.getHeight() / 2, null);
		g2.setTransform(transform);
	}

	public abstract void changeState(String string);
	
}
