package object;

import static util.ImageLoader.loadImage;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.GardenPanel;
import util.Util;
/*represents a vegetable in the garden simulation.
 * It manages the vegetable's growth stages, health, and any associated behaviors.
 */
public class Veg extends DraggableObject {
	private boolean isMoistStable,hasGrown,hasLevelUp;
	private int moist,health;
	protected int growthStage;
	private int energy;
	private int moistStableTime;
	private int timeSinceLastGrowth;
	private int timeSinceLevelUp;
    private final int LEVEL_UP_DURATION = 30 * 1000; // 30 seconds protection period for veg just level up
    private int updateInterval;
	private int growthCooldown;
	private int moistLossCountdown;
	private int goodMoist;
	private final int MAX_VALUE=120;
	private int RAIN_INTERVAL  = 2000; //gain moist every 2 sec when it's raining
	private int rainCounter = 0;
	private SoundEffectListener soundEffectListener;
	protected BufferedImage stage1,stage2,stage3,stage4,finalStage,stageDead;
	public Veg() {
		super();	
		stage1 = loadImage("assets/seed.png");
		stage3=loadImage("assets/stage3.png");
		stage2=loadImage("assets/stage2.png");
		stage4=loadImage("assets/stage4.png");
		finalStage=loadImage("assets/finalStage.png");
		stageDead=loadImage("assets/stageDead.png");
		setVariable();
	}
	
	private void setVariable() {
		xPos=GardenPanel.W_WIDTH/2;
		yPos=GardenPanel.W_HEIGHT-325;
		scale=0.2;
		isMoistStable=false;
		hasGrown=false;
		hasLevelUp=false;
		timeSinceLevelUp = 0;
        updateInterval = 30; 
		health=MAX_VALUE;
		moist=10;
		growthStage=0;
		energy=10;
		moistStableTime = 0;
		timeSinceLastGrowth=0;
		growthCooldown=120;
		moistLossCountdown = 60;
		goodMoist=(int) Util.random(MAX_VALUE-20);
		img=stage1;
	}
	public void update(int season,boolean isProtected){
		loseMoistOverTime(season);
		rainCounter += updateInterval; 
		if (rainCounter >= RAIN_INTERVAL) {
			rainCounter = 0;
			if (!isProtected) {
				raining(season== 2);
			}
		}
		moistStable();
		stageUp(season);
		if (die()) {
			img=stageDead;
			scale=0.4;
		}

		timeSinceLastGrowth++;
		timeSinceLevelUp += updateInterval;
		if (timeSinceLevelUp >= LEVEL_UP_DURATION) {
			hasLevelUp = false;
			System.out.println("reset protection");
			timeSinceLevelUp = 0; 
		}
		if (timeSinceLastGrowth >= growthCooldown) {
			timeSinceLastGrowth = 0;
			hasGrown = false; 
		}
	}
	
	private void loseMoistOverTime(int season) {
		int dmg=1;
		if (season==1)dmg=3;
        if (growthStage>0) {
        	moistLossCountdown--;
        	if (moistLossCountdown <= 0) {
        		moistLossCountdown = 60; 
        		if (moist > 0 && !hasLevelUp&& growthStage!=5&&!die()) {
        			System.out.println("loooosseee moist");
        			moist-=dmg;
        		}
        	}
        }
    }
	public int getGoodMoist() {
		return goodMoist;
	}
	public int getEnergy() {
		return energy;
	}
	private void moistStable(){
	    if (moist >= goodMoist && moist <= goodMoist+20) {
	      moistStableTime++; 
	    } else {
	      moistStableTime = 0; 
	      isMoistStable = false; 
	    }
	    if (moistStableTime >= 120) {
	      isMoistStable = true; 
	    }
	  }
	
	private void stageUp(int season){
	    //if at stage1 && moist stable, growth++
		int energyRate=10;
		if (season==3)energyRate=5;
		if (growthStage>0&&isMoistStable&&energy<MAX_VALUE&& !hasGrown) {
			energy+=energyRate;
			hasGrown=true;
			timeSinceLastGrowth = 0;
		}
		if (energy>=MAX_VALUE) {
			//rank up growthStage, larger the size and set moist to 1 & growth to 0
			growthStage++;
			System.out.println("level upppppp");
			if (soundEffectListener != null) {
                soundEffectListener.playGrowSound();
            }
			if (growthStage==2) {
				img=stage2;
				scale=0.4;
			}
			else if (growthStage==3) {
				img=stage3;
				scale=0.4;
			}else if (growthStage==4) {
				img=stage4;
				scale=0.4;
				yPos=GardenPanel.W_HEIGHT-335;
			}else if (growthStage==5) {
				img=finalStage;
				scale=0.4;
				yPos=GardenPanel.W_HEIGHT-335;
			}
			moist=10;
			energy=0;
			hasLevelUp=true;
		}
	}
	
	public void waterVeg(){
	    if (!die()&&growthStage>0) moist+=1; 
	  }
	public void raining(boolean isFall) {
		if (!die()&&growthStage>0&&isFall)moist+=3;
	}
	
	public void plantVeg() {
		growthStage=1;
	}
	public boolean die(){
		boolean died = (moist<=0||health<0||moist>MAX_VALUE);
		return died;
	}

	public int getMoist() {
		return moist;
	}

	public int getHealth() {
		return health;
	}

	public int growthStage() {
		return growthStage;
	}
	public void setSoundEffectListener(SoundEffectListener listener) {
        this.soundEffectListener = listener;
    }

	@Override
	public void changeState(String string) {
	}

	public void decreaseHealth(int i) {
		health -= i;
	}
}
