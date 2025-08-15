package environment;

import java.awt.Graphics2D;

public interface DesignGarden {
	void draw(Graphics2D g2);
	void update();
	boolean isDaytime();
}
