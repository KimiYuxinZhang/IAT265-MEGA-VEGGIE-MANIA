package environment;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
//generate a bunch of snowflakes to create snowing effect
public class Snowing {
    private ArrayList<Snowflake> snowflakes;
    private int panelWidth;
    private int panelHeight;

    public Snowing(int panelWidth, int panelHeight, int numberOfSnowflakes) {
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
        snowflakes = new ArrayList<>();

        // Initialize snowflakes
        for (int i = 0; i < numberOfSnowflakes; i++) {
            snowflakes.add(createRandomSnowflake());
        }
    }

    private Snowflake createRandomSnowflake() {
        Point2D.Double center = new Point2D.Double(Math.random() * panelWidth, -Math.random() * panelHeight);
        int size = (int) (Math.random() * 15 + 5); 
        int depth = (int) (Math.random() * 4 + 1); 
        return new Snowflake(center, size, depth);
    }

    public void update() {
        for (Snowflake snowflake : snowflakes) {
        	snowflake.move(); 
            if (snowflake.getCenter().y > panelHeight) {
                // Reset the snowflake to the top once it moves off the bottom of the screen
            	snowflake.getCenter().setLocation(Math.random() * panelWidth, -Math.random() * 50);
            }
        }
    }

    public void draw(Graphics2D g2) {
        for (Snowflake snowflake : snowflakes) {
            snowflake.draw(g2);
        }
    }
}

