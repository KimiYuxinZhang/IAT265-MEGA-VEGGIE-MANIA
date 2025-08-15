package object;

import static util.ImageLoader.loadImage;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import environment.Waterdrop;
import main.GardenPanel;
import util.Util;
/*The WaterCan class is used to water plants, affecting their growth moist level and growth stage.
 */
public class WaterCan extends DraggableObject {
	private double angle;
	private ArrayList<Waterdrop> waterdrops;
	public WaterCan() {
		super();
		xPos=GardenPanel.W_WIDTH-100;
		yPos=GardenPanel.W_HEIGHT-110;
		scale=0.3;
		img = loadImage("assets/watercan.png");
		angle=0;
		waterdrops = new ArrayList<>();
	}

	public void drawImg(Graphics2D g2) {
		AffineTransform transform = g2.getTransform();
		g2.translate(xPos, yPos);
		g2.scale(scale, scale);
		g2.rotate(angle);
		g2.drawImage(img, -img.getWidth() / 2, -img.getHeight() / 2, null);
		g2.setTransform(transform);
		for (Waterdrop drop : waterdrops) {
            drop.draw(g2);
        }
	}
	@Override
	public void changeState(String string) {
		if (string=="rotate") {
			angle=-45;
			addWaterDrop();
			// Update waterdrops
	        for (int i=0;i<waterdrops.size();i++) {
	        	Waterdrop w=waterdrops.get(i);
	        	w.update();
	        	if (w.isOffScreen((int) (yPos+getHeight()))) waterdrops.remove(w);
	        }
		}
		else if (string=="reset") {
			angle=0;
			waterdrops.clear();
		}
	}
	private void addWaterDrop(){
		waterdrops.add(new Waterdrop((float)(Util.random(xPos-getWidth()/2-10,xPos-30)), 
				(float)yPos+30, 2,(int) (Math.random() * 3 + 5)));
	}

}
