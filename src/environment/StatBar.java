package environment;

import static util.ImageLoader.loadImage;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import main.GardenPanel;
import object.Veg;
/*The StatBar class displays veggie's state in the UI, such as health, moist, growth progress
 * of plants in the gardening simulation.
 */
public class StatBar extends Flowerpot{
	private Rectangle2D.Double moistBar,energyBar,healthBar;
	public StatBar() {
		super();
		img = loadImage("assets/statBar.png");
		scale=0.3;
		width=(int) (img.getWidth()*scale);
		height=(int) (img.getHeight()*scale);
		xPos=GardenPanel.W_WIDTH-width;
		yPos=height/2;
		
	}
	
	public void drawPot(Graphics2D g2,Veg veg) {
		super.drawPot(g2);
		AffineTransform at = g2.getTransform();
		g2.translate(xPos, yPos);
		
		if (veg.getMoist()>0) {
			moistBar=new Rectangle2D.Double(5,68,veg.getMoist(),12);
			if (veg.getMoist()>veg.getGoodMoist()+20||veg.getMoist()<veg.getGoodMoist())g2.setColor(Color.red);
			else g2.setColor(Color.blue);
			g2.fill(moistBar);
		}
		if (veg.getEnergy()>0) {
			energyBar=new Rectangle2D.Double(5,-9,veg.getEnergy(),12);
			g2.setColor(Color.yellow);
			g2.fill(energyBar);
		}
		if (veg.getHealth()>0) {
			healthBar=new Rectangle2D.Double(5,30,veg.getHealth(),12);
			g2.setColor(Color.pink);
			g2.fill(healthBar);
		}
		//add line to indicate good moist range
		g2.setColor(new Color(255,0,255));
		g2.drawRect(5+veg.getGoodMoist(), 68, 20, 12);
		g2.setTransform(at);
	}
}
