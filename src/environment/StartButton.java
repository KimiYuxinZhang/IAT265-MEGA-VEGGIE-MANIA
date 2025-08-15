package environment;

import static util.ImageLoader.loadImage;

public class StartButton extends RestartButton{
	public StartButton(double x, double y) {
		super(x,y);
		img = loadImage("assets/startButton.png");
	}
}
