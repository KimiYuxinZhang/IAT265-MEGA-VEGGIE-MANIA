package environment;
import static util.ImageLoader.loadImage;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import main.GardenPanel;
//decorate garden with sun and moon images
public class SunAndMoonDecorator extends Flowerpot implements DesignGarden{
	private DesignGarden baseGarden;
	private BufferedImage sun,moon;
	private boolean isDaytime;
	public SunAndMoonDecorator(DesignGarden baseGarden) {
		super();
		this.baseGarden=baseGarden;
		scale=0.15;
		sun=loadImage("assets/sun.png");
		moon=loadImage("assets/moon.png");
		img=sun;
		width=img.getWidth()*scale;
		height=img.getHeight()*scale;
		xPos=GardenPanel.W_WIDTH-350;
		yPos=height/2-50;
		this.isDaytime = true; 
	}

	@Override
	public void draw(Graphics2D g2) {
		baseGarden.draw(g2);
		drawPot(g2);
	}

	@Override
	public void update() {
		xPos -= 1;
        if (xPos < -width/2) {
            xPos=width/2+GardenPanel.W_WIDTH;
            if (img==sun) {
            	img=moon;
            	isDaytime = false; 
            }
            else {
            	img=sun;
            	isDaytime = true; 
            }
        }
		
	}


	@Override
	public boolean isDaytime() {
		return isDaytime;
	}
}
