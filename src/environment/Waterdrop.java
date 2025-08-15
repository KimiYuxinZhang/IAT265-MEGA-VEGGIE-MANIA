package environment;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
//draw waterdrops to create rainning effect
public class Waterdrop {
	private float x, y;
    private float fallingSpeed;
    private int size;

    public Waterdrop(float x, float y, float fallingSpeed, int size) {
        this.x = x;
        this.y = y;
        this.fallingSpeed = fallingSpeed;
        this.size = size;
    }

    public void update() {
        y += fallingSpeed; 
    }

    public void draw(Graphics2D g2) {
        g2.setColor(new Color(0,191,255));
        g2.fill(new Ellipse2D.Float(x - size / 2, y - size / 2, size, size));
    }

    public boolean isOffScreen(int panelHeight) {
        return y > panelHeight; 
    }
}
