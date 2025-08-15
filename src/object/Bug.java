package object;

import static util.ImageLoader.loadImage;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
//bug will spawn on veg to damage veg's health
public class Bug extends DraggableObject{
	boolean isDead;
	public Bug(double x,double y) {
		super();
		this.xPos=x;
		this.yPos=y;
		scale=0.1;
		img = loadImage("assets/bug.png");
		isDead=false;
	}

	@Override
	public void changeState(String string) {
		if (string=="dead")isDead=true;
		
	}

	public boolean isDead() {
		return isDead;
	}
}
