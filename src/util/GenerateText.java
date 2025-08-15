package util;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.RenderingHints;
import java.awt.Color;
//generate instruction text on the screen
public class GenerateText {
	private String[] lines;
    private int xPos;
    private int yPos;
    private Font font;
    private Color color;

    public GenerateText(String text, int x, int y, int fontSize, Color color) {
    	this.lines = text.split("\n");
        this.xPos = x;
        this.yPos = y;
        this.font = new Font("SansSerif", Font.BOLD, fontSize);
        this.color = color;
    }

    public void draw(Graphics g) {
    	Graphics2D g2 = (Graphics2D) g;

        g2.setFont(font);
        g2.setColor(color);

        FontMetrics metrics = g2.getFontMetrics(font);

        int lineHeight = metrics.getHeight();
        int y = yPos;
        for (String line : lines) {
            g2.drawString(line, xPos, y);
            y += lineHeight; // Move y-position down for the next line
        }

    }



    public int getX() {
        return xPos;
    }

    public void setX(int x) {
        this.xPos = x;
    }

    public int getY() {
        return yPos;
    }

    public void setY(int y) {
        this.yPos = y;
    }
}

