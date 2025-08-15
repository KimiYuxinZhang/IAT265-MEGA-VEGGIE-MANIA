package main;

import static util.ImageLoader.loadImage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.Timer;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import environment.*;
import main.GardenPanel.MyMouseMotionListener;
import object.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

import util.GenerateText;
import util.ImageLoader;
import util.MinimHelper;
import util.Util;
/*ECO POINTS(12pt):sophisticated complex shapes(snowflake) - 2pt, meaningful seasonal change - 2pt,
 * decorator pattern (garden decorator) - 2 pt,sensible FSM with >4 different states (veg class) - 2pt,
 * self-created img - 2pt,day and night shift -2 pt.
 */
//main panel for the application, simulate the whole project
public class GardenPanel extends JPanel implements ActionListener,SoundEffectListener {
	public static int W_WIDTH = 1050;
	public static int W_HEIGHT = 750;
	private JFrame frame;
	private static final int SCREEN_DELAY = 5000; // win/lose screen will show 5 seconds after veg mature/dies
	private int delayCountdown = 0;
	private final int BUG_SPAWN = 300; // bug spawn every 10 seconds at night 
    private int bugSpawnCountdown;
    private static final int DAMAGE_INTERVAL = 15; // bug damage veg's health every second 
    private int damageCountdown ;
	private int season=0;
	// variables for holding mouse position
	private double mouseX;
	private double mouseY;
	private boolean hasPlanted;
	
	private Garden garden;
	private DesignGarden cloudGardenDesign,sunGardenDesign;
	private Flowerpot flowerPot;
	private ObjectConcreteFactory objectMaker;
	private DraggableObject[]tools=new DraggableObject[3];
	private DraggableObject seedbag,waterCan,umbrella;
	
	private Veg veg;
	private StatBar statBar;
	private ArrayList<Bug> bugs = new ArrayList<>();
    
	private Cloud cloud;
	private Snowing snowing;
	private BufferedImage startImg,endImg,successImg;
	private int state;

	private RestartButton restartButton;
	private StartButton startButton;

	//text
    private GenerateText welcomeText,introText,instructionText;
	private Rectangle2D.Double textBubble;
	private int bubbleW,bubbleH;
	private boolean showInstruction;
	
	private Timer timer;
	private Minim minim;
	private AudioPlayer click,watering,grow,pickup,bgm,raining,bug;
	private boolean musicStarted;
	GardenPanel(JFrame frame) {
		this.frame=frame;
		this.setBackground(Color.white);
		setPreferredSize(new Dimension(W_WIDTH, W_HEIGHT));
		musicStarted=false;
		garden = new Garden();
		cloudGardenDesign=new MovingCloud(garden);
		sunGardenDesign=new SunAndMoonDecorator(cloudGardenDesign);
		flowerPot = new Flowerpot();
		objectMaker=new ObjectConcreteFactory();
		tools[0] = objectMaker.createObject("seedbag");
		tools[1] = objectMaker.createObject("watercan");
		tools[2] = objectMaker.createObject("umbrella");
		seedbag = tools[0];
		waterCan = tools[1];
		umbrella=tools[2];
		veg=new Veg();
		veg.setSoundEffectListener(this); 
		statBar=new StatBar();
		startImg = loadImage("assets/startImg.png");
		endImg=loadImage("assets/gameover.png");
		successImg=loadImage("assets/success.png");
		state=0;
		hasPlanted=false;
		bugSpawnCountdown=BUG_SPAWN;
		damageCountdown= DAMAGE_INTERVAL;
		cloud = new Cloud(250, 90);
		snowing = new Snowing(W_WIDTH, W_HEIGHT, 50); 
		restartButton = new RestartButton(W_WIDTH / 2, W_HEIGHT / 2+150);
		startButton = new StartButton(W_WIDTH / 2, W_HEIGHT -80);
		//text
		welcomeText=new GenerateText("Welcome to \nMegaVeggie Mania!",20,W_HEIGHT/2,30,Color.white);
		introText = new GenerateText("Simply use your mouse to drag tools to your plant \n"
				+ "for sowing, watering, and protecting it from raining,\n"
				+ "and click to fend off pests and harvest vegetables. \n\n"
			        + "Experience the joy of planting and growing plants！",20, W_HEIGHT/2+70, 
			        16,Color.white);
		bubbleW=400;
		bubbleH=30;
		textBubble=new Rectangle2D.Double(0,0,bubbleW,bubbleH);

		
		
		minim = new Minim(new MinimHelper());
		click=minim.loadFile("click.mp3");
		pickup=minim.loadFile("pickup.mp3");
		grow=minim.loadFile("grow.mp3");
		watering=minim.loadFile("watering.mp3");
		bgm=minim.loadFile("bgm.mp3");
		bgm.setGain(-25.0f);
		raining=minim.loadFile("raining.mp3");
		bug=minim.loadFile("bug.mp3");
		
		MyMouseListener ml = new MyMouseListener();
		addMouseListener(ml);
		
		MyMouseMotionListener mml = new MyMouseMotionListener();
		addMouseMotionListener(mml);

		timer = new Timer(30, this);
		timer.start();
	}
	
	private void setInstruction() {
		String instruction="";
		int textSize=16;
		if (!veg.die()){
			if (bugs.size()==0) {
				if (veg.growthStage()<1) {
					instruction="Drag seed bag to flowerpot to plant seed";
					showInstruction = true;
				}else if (veg.growthStage()==1){
					instruction="Drag water can to flowerpot to water the seed,\n"
							+ "Keep the soil's moisture level at optimal humidity to ensure the seed grows.";
					textBubble=new Rectangle2D.Double(0,0,bubbleW*1.5,bubbleH*2);
					showInstruction = true;
				}else if (season==1) {
					instruction="During summer time, plant lose moisture at an accelerated rate.\n"
							+"Don't forget to water your plant!!";
					showInstruction = true;
				}else if (season==2) {
					instruction="In autumn, it often rains. The moisture level of your plant will continuously\nincrease."
							+" Be careful not to let your plants get waterlogged!";
					showInstruction = true;
				}else if (season==3&&veg.growthStage()!=5) {
					instruction="In winter, plants grow more slowly.\n"
							+"Please be patient.";
					showInstruction = true;
				}else if (veg.growthStage()==5){
					instruction="Congratulations, your plant has fully grown!";
					textSize=20;
					showInstruction = true;
				}
			}else {
				instruction="At night, bug will spawn on the plant\n"
						+"Click on bug to kill it before it damage your plant!";
				showInstruction = true;
			}
		}else if (veg.die()){
			instruction="Sorry, your plant has died. Better luck next time.";
			textSize=20;
			showInstruction = true;
		}
		else showInstruction = false;
		instructionText=new GenerateText(instruction,20,20,textSize,Color.black);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (state==0) {//intro screen
			g2.drawImage(startImg, 0, 0, GardenPanel.W_WIDTH, GardenPanel.W_HEIGHT, null);
			startButton.drawButton(g2);
			welcomeText.draw(g2);
			introText.draw(g2);
		}else if (state==1){//game start
			sunGardenDesign.draw(g2);
			flowerPot.drawPot(g2);
			if (veg.growthStage()>0) {
				if (veg.growthStage()!=5)statBar.drawPot(g2,veg);
				veg.drawImg(g2);
			}
			for (int i=0;i<tools.length;i++)
				tools[i].drawImg(g2);
			if (season==2)cloud.drawSteam(g2);
			else if (season==3)snowing.draw(g2);
			if (showInstruction) {
				g2.setColor(new Color(255,255,255,200));
				g2.fill(textBubble);
				instructionText.draw(g2);
			}
			if (bugs.size()>0)for (Bug bug : bugs) {
	            bug.drawImg(g2);
	        }
			//night time
			if (!sunGardenDesign.isDaytime()) {
				g2.setColor(new Color(0, 0, 0, 150));
				g2.fill(new Rectangle2D.Double(0, 0, W_WIDTH, W_HEIGHT));
			}
		}else if (state==2) {//lose game
			g2.drawImage(endImg, 0, 0, GardenPanel.W_WIDTH, GardenPanel.W_HEIGHT, null);
			restartButton.drawButton(g2);
		}else if (state==3) {//win game
			g2.drawImage(successImg, 0, 0, GardenPanel.W_WIDTH, GardenPanel.W_HEIGHT, null);
			restartButton.drawButton(g2);
		}	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		setInstruction();
		updateSeasonalSounds();
		if (season==2) {
			cloud.update();
		}
		else if (season==3) {
			snowing.update();
			cloud.setVisible(false);
		}
		if (state==1) {
			bgm.play();
			sunGardenDesign.update();
			veg.update(season,umbrella.hitPot(flowerPot));
			if (seedbag.hitPot(flowerPot)&&!hasPlanted) {
				veg.plantVeg();
				hasPlanted=true;
			}
			if (waterCan.hitPot(flowerPot)) {
				waterCan.changeState("rotate");
				veg.waterVeg();
				watering.play(0);
			}else waterCan.changeState("reset");
			if (umbrella.hitPot(flowerPot)) {
				umbrella.changeState("open");
			}else umbrella.changeState("closed");
			//spawn bug at night only when veg has reached growthStage 2
			if ((veg.growthStage()>1)&&!sunGardenDesign.isDaytime()&&veg.growthStage()!=5) {
				handleBugSpawning();
			}
			if (bugs.size()>0) {
				handleBugDamage();
				removeDeadBugs();
			}
		}
		seasonChange();
		//after veg dies or matures, win/lose screen shows after a delay
		if (delayCountdown > 0) {
	        delayCountdown -= 30; 
	        if (delayCountdown <= 0) {
	            if (veg.die()) state = 2;
	            else if (veg.growthStage() == 5) state = 3;
	        }
	    } else {
	        gameStateChange();
	    }
		repaint();
	}
	private void handleBugDamage() {
		damageCountdown --;
		if (damageCountdown <= 0 ) {
			for (Bug bug:bugs)veg.decreaseHealth(1);
			damageCountdown = DAMAGE_INTERVAL;
		}
	}

	private void removeDeadBugs() {
		for (int i=0;i<bugs.size();i++)
			if (bugs.get(i).isDead())bugs.remove(bugs.get(i));
		
	}
	private void handleBugSpawning() {
        bugSpawnCountdown--;
        if (bugSpawnCountdown <= 0) {
            bugSpawnCountdown = BUG_SPAWN;
            spawnBugs();
        }
    }
	private void spawnBugs() {
		double bugX = Util.random(veg.getX()-veg.getWidth() / 2, veg.getX()+veg.getWidth() / 2);
		double bugY = Util.random(veg.getY()-veg.getHeight() / 2, veg.getY()+veg.getHeight() / 2);
		System.out.println("buggg spawnnnnn");
		bugs.add(new Bug(bugX,bugY)); 
		bug.play(0);
	}

	private void seasonChange() {
		if (veg.growthStage()==2) {
			season=1;
			garden.switchSeason(1);//when veg at stage2, season change to summer
		}
		else if (veg.growthStage()==3) {
			season=2;
			garden.switchSeason(2);//when veg at stage3, season change to fall
		}
		else if (veg.growthStage()==4) {
			season=3;
			garden.switchSeason(3);//when veg at stage4, season change to winter
		}
		
	}
	
	private void updateSeasonalSounds() {
	    // Handle rain sounds based on season
	    if (season == 2) {
	        if (!raining.isPlaying()) raining.loop(); 
	    } else stopRaining();
	}
	
	private void gameStateChange() {
		if ((veg.die() || veg.growthStage() == 5) && delayCountdown == 0) {
	        delayCountdown = SCREEN_DELAY;
	        stopRaining();
	    }
	}
	public void playGrowSound() {
        grow.play(0);
    }
	public void playRaining() {
        raining.play();
    }
	public void startBackgroundMusic() {
        if (!musicStarted) {
            bgm.play();
            musicStarted = true;
        }
    }
	public void stopRaining() {
	    if (raining.isPlaying()) raining.close();
	}
	private void resetGameState() {
        season = 0; 
    }
	private void pickupSound(DraggableObject object,double x, double y) {
		if (object.clicked(x, y))pickup.play(0);
	}
	//mouse interaction
	public class MyMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			mouseX = e.getX();
			mouseY = e.getY();
			if (state==0&&startButton.clicked(mouseX, mouseY)) {
				click.play(0);
				startBackgroundMusic();
				state+=1;
			}
			if (state>1&&restartButton.clicked(mouseX, mouseY)) {
				click.play(0);
				stopRaining();
				resetGameState();
				frame.dispose();
				new GardenApp("GardenApp");
			}
			for (int i=0;i<bugs.size();i++) {
				if (bugs.get(i).clicked(mouseX, mouseY)) {
					bugs.get(i).changeState("dead");
				}
			}
		} 
		public void mousePressed(MouseEvent e) {
	        mouseX = e.getX();
	        mouseY = e.getY();

	        // Play the sound if a draggable object is clicked
	        for (DraggableObject tool:tools)pickupSound(tool,mouseX,mouseY);
	    }
	}
	
	public class MyMouseMotionListener extends MouseMotionAdapter {
		public void mouseDragged(MouseEvent e) {
			mouseX = e.getX();
			mouseY = e.getY();
			if (seedbag.clicked(mouseX, mouseY)){
				seedbag.setXPos(mouseX);
				seedbag.setYPos(mouseY);
			}else if (waterCan.clicked(mouseX, mouseY)){
				waterCan.setXPos(mouseX);
				waterCan.setYPos(mouseY);
			}else if (umbrella.clicked(mouseX, mouseY)){
				umbrella.setXPos(mouseX);
				umbrella.setYPos(mouseY);
			}
			repaint();
		}
	}
	public int season() {
		return season;
	}

}
