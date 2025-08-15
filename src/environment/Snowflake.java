package environment;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import util.Util;
//create snowflakes during the winter season
public class Snowflake {
    private Point2D.Double center;
    private int size;
    private int depth;
    private int arms;
    private double fallingSpeed;

    public Snowflake(Point2D.Double center, int size, int depth) {
        this.center = center;
        this.size = size;
        this.depth = depth;
        arms=(int) Util.random(3,6);
        fallingSpeed=Util.random(1,3);
    }

    public void move() {
        center.y += fallingSpeed; 
    }

    public void draw(Graphics2D g2) {
    	g2.setColor(Color.white);
    	double angleIncrement = 2 * Math.PI / arms;
        for (int i = 0; i < arms; i++) {
            drawSnowflakeArm(g2, center, i * angleIncrement, size, depth);
        }
    }

    private void drawSnowflakeArm(Graphics2D g2, Point2D point, double angle, double size, int depth) {
        if (depth == 0) {
            Point2D end = new Point2D.Double(
                point.getX() + size * Math.cos(angle),
                point.getY() + size * Math.sin(angle)
            );
            g2.draw(new Line2D.Double(point, end));
        } else {
            Point2D mid1 = new Point2D.Double(
                point.getX() + size / 3 * Math.cos(angle),
                point.getY() + size / 3 * Math.sin(angle)
            );
            Point2D mid2 = new Point2D.Double(
                point.getX() + 2 * size / 3 * Math.cos(angle),
                point.getY() + 2 * size / 3 * Math.sin(angle)
            );

            //main segment
            drawSnowflakeArm(g2, point, angle, size / 3, depth - 1); 
            //left spike
            drawSnowflakeArm(g2, mid1, angle + Math.PI / 4, size / 4, depth - 1);
            //right spike
            drawSnowflakeArm(g2, mid1, angle - Math.PI / 4, size / 4, depth - 1);
            //Continue main segment
            drawSnowflakeArm(g2, mid2, angle, size / 3, depth - 1);
        }
    }



	public Double getCenter() {
		return center;
	}
}
