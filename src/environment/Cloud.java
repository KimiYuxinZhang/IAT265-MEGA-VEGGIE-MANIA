package environment;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import main.GardenPanel;
import object.SoundEffectListener;
import processing.core.PApplet;
import util.Util;
/*The Cloud class is used to create and manage steam effects in the gardening application,
 * typically appearing during spring/fall season.
 */
public class Cloud {
	private float xPos, yPos;
	private int width, height;
	private float speed = 2.0f; 
	private float xstart;
	private float xnoise;
	private float ynoise;
	private PApplet pa;
	private int timer; 
    private boolean isVisible; 
    private static final int APPEARANCE_INTERVAL = 20000; // cloud shows up every 20 seconds
    private ArrayList<Waterdrop> waterdrops;
	public Cloud(int w, int h) {
		yPos = 20;
		width = w;
		xPos = - width / 2.0f;
		height = h;
		xstart = Util.random(10);
		xnoise = xstart;
		ynoise = Util.random(10);
		pa = new PApplet();
		this.timer = 0; 
        this.isVisible = false;
        waterdrops = new ArrayList<>();
	}
	
	public boolean isRaining() {
		return isVisible;
	}
	public void setVisible(boolean visible) {
		isVisible=visible;
	}
	public void update() {
        timer -= 30; 
        if (timer <= 0) {
            timer = APPEARANCE_INTERVAL; 
            isVisible = true; 
        }

        if (isVisible) {
        	move();
        }
        
        if (isVisible && Math.random() < 0.1) { 
            waterdrops.add(new Waterdrop(xPos + (float) Math.random() * width, yPos + height, 2,(int) (Math.random() * 5 + 10)));
        }

        // Update waterdrops
        for (int i=0;i<waterdrops.size();i++) {
        	Waterdrop w=waterdrops.get(i);
        	w.update();
        	if (w.isOffScreen(GardenPanel.W_HEIGHT)) waterdrops.remove(w);
        }
    }
	public void move() {
        xPos += speed; // Move cloud to the right
        if (xPos - width / 2.0f > GardenPanel.W_WIDTH) {
            // Reset cloud's position to the left side
            xPos = - width / 2.0f;
            isVisible = false; 
        }
    }
	public void drawSteam(Graphics2D g2) {
		if (isVisible) {
			float noiseFactor;
			AffineTransform at = g2.getTransform();
			g2.translate(xPos, yPos);
			float centerX = width / 2.0f;
			float centerY = height / 2.0f;
			for(int y=0; y <=height; y += 5) {
				ynoise += 0.1;
				xnoise = xstart;
				for(int x= 0; x<=width; x+=5) {
					xnoise+= 0.1;
					noiseFactor = pa.noise(xnoise,ynoise);

					float distFromCenter = PApplet.dist(x, y, centerX, centerY);
					float maxDist = PApplet.dist(0, 0, centerX, centerY);
					float edgeFactor = PApplet.map(distFromCenter, 0, maxDist, 1.0f, 0.1f);
					if (edgeFactor < 0.2f) continue;

					AffineTransform at1 = g2.getTransform();
					g2.translate(x, y);
					g2.rotate(noiseFactor*Util.radians(360));

					float edgeSize = noiseFactor * 50;
					int grey = (int) (150 + (noiseFactor*100));
					int alph = (int) (150 +(noiseFactor*100));
					g2.setColor(new Color(grey,grey,grey,alph));
					g2.fill(new Ellipse2D.Float(-edgeSize/2, -edgeSize/4, edgeSize, edgeSize/2* noiseFactor));
					g2.setTransform(at1);
				}

			}
			g2.setTransform(at);
			for (Waterdrop drop : waterdrops) {
	            drop.draw(g2);
	        }
		}
	}
}
