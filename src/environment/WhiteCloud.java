package environment;

import static util.ImageLoader.loadImage;

import main.GardenPanel;
//moving white cloud on the sky in the background
public class WhiteCloud extends Flowerpot{
	private float speed;
	public WhiteCloud(float x,float y,float scale,float speed) {
		super();
		this.xPos=x;
		this.yPos=y;
		this.scale=scale;
		this.speed=speed;
		img=loadImage("assets/cloud.png");
	}
	public void move() {
        xPos += speed;
        if (xPos >GardenPanel.W_WIDTH+width/2) {
            xPos=-width/2; // Change direction
        }
    }
}
