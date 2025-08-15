package environment;

import static util.ImageLoader.loadImage;

import java.awt.Graphics2D;

import main.GardenPanel;
import util.Util;

public class MovingCloud extends Flowerpot implements DesignGarden{
	private Garden baseGarden;
	private WhiteCloud[]clouds;
	public MovingCloud(Garden baseGarden) {
		super();
		this.baseGarden=baseGarden;
		clouds=new WhiteCloud[3];
		initializeClouds();
	}
	private void initializeClouds() {
        // Initialize three clouds with random positions, limits, and speeds
		for (int i=0;i<clouds.length;i++) {
			float randomScale=(float)Util.random(0.25,1.2);
			float randomY=(float)Util.random(100,400);
			clouds[i]=new WhiteCloud((float)(100+i*150),randomY, randomScale,(float) (Math.random() * 2 + 1));
		}
    }
	
	public void update() {
		
	}
	public void draw(Graphics2D g2) {
		baseGarden.draw(g2);
		addCloud(g2);
	}
	private void addCloud(Graphics2D g2) {
		for (int i=0;i<clouds.length;i++) {
			clouds[i].move();
			clouds[i].drawPot(g2);
		}
	}
	@Override
	public boolean isDaytime() {

		return false;
	}
}
