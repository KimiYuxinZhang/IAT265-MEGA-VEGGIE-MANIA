package environment;
import static util.ImageLoader.loadImage;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import main.GardenPanel;

//represents the background garden image
public class Garden implements DesignGarden{
	private BufferedImage img,spring,summer,fall,winter;
	public Garden() {
		spring= loadImage("assets/garden.png");
		summer=loadImage("assets/gardenSummer.png");
		fall=loadImage("assets/gardenFall.png");
		winter=loadImage("assets/gardenWinter.png");
		img=spring;
	}

	public void draw(Graphics2D g2) {
		AffineTransform at = g2.getTransform();
		g2.translate(0, 0);
		g2.drawImage(img, 0, 0, GardenPanel.W_WIDTH, GardenPanel.W_HEIGHT, null);
		g2.setTransform(at);
	}
	public void switchSeason(int season) {
		if (season==0)img=spring;
		else if (season==1)img=summer;
		else if (season==2)img=fall;
		else img=winter;
	}

	@Override
	public void update() {	
	}

	@Override
	public boolean isDaytime() {
		return false;
	}
}
