package util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Util {
	
	
	public static double random(double low, double high){
		return low + Math.random() * (high - low);
		}
	
	
	public static float random(float high) {
		return (float) Math.random() * high;
	}

	
	public static double radians(double angle){
		return angle/180*Math.PI;		
	}
	
	
	public static float radians(float angle){
		return angle/180*(float)Math.PI;		
	}
	
	
}
