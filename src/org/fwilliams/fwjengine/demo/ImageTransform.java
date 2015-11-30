package org.fwilliams.fwjengine.demo;

import org.fwilliams.fwjengine.core.*;
import org.fwilliams.fwjengine.entity.*;
import org.fwilliams.fwjengine.graphics.*;
import org.fwilliams.fwjengine.input.*;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import javax.swing.ImageIcon;


public class ImageTransform {
    public static void main(String args[]) {
        ImageTransform test = new ImageTransform();
        test.run();
    }

    private static final long DEMO_TIME = 100000;
    private static final long FADE_TIME = 2000;
    private static final int NUMBER_OF_SPRITES = 20;

    private ScreenManager screen;
    private Image bgImage;
    private Animation anim;
    private Sprite cloud;
    private Sprite[] smileys;
    
    public void loadImages() {
        // load images
        bgImage = loadImage("assets/bg.png");
        Image player1 = loadImage("assets/cloud1.png");
        Image player2 = loadImage("assets/cloud2.png");
        Image player3 = loadImage("assets/cloud3.png");
        Image smiley = loadImage("assets/smile.png");
        
        // create animation
        anim = new Animation();
        anim.addFrame(player1, 150);
        anim.addFrame(player2, 150);
        anim.addFrame(player1, 150);
        anim.addFrame(player2, 150);
        anim.addFrame(player3, 150);
        anim.addFrame(player2, 150);
        
        cloud = new Sprite(anim);
        cloud.setXVelocity(0.2f);
        cloud.setYVelocity(0.3f);
        
        Animation happyAnimation = new Animation();
        happyAnimation.addFrame(smiley, 10);
        
        smileys = new Sprite[NUMBER_OF_SPRITES];
        for(int i=0; i<smileys.length; i++) {
        	smileys[i] = new Sprite(happyAnimation.clone());
        	smileys[i].setX((float) Math.random()*screen.getWidth());
        	smileys[i].setY((float) Math.random()*screen.getHeight());
        	smileys[i].setXVelocity((float) Math.random()* 0.5f);
        	smileys[i].setYVelocity((float) Math.random()* 0.5f);
        }
        
    }


    
    private Image loadImage(String fileName) {
        return new ImageIcon(fileName).getImage();
    }


    public void run() {
        screen = new ScreenManager();
        try {
            DisplayMode displayMode = new DisplayMode(800, 600, 32, 60);
                //screen.findFirstCompatibleDisplayMode(POSSIBLE_MODES);
            screen.setFullScreen(displayMode);
            loadImages();
            animationLoop();
        }
        finally {
             screen.restoreScreen();
        }
    }
    
    public void animationLoop() {
        long startTime = System.currentTimeMillis();
        long currTime = startTime;

        while (currTime - startTime < DEMO_TIME) {
            long elapsedTime =
                System.currentTimeMillis() - currTime;
            currTime += elapsedTime;

            // update sprite
            update(elapsedTime);

            // draw and update screen
            Graphics2D g = screen.getGraphics();
            draw(g);
            drawFade(g, currTime-startTime);
            g.dispose();
            screen.update();

            // take a nap
            try {
                Thread.sleep(20);
            }
            catch (InterruptedException ex) { }
        }
    }
    
    private void drawFade(Graphics2D g, long currentTime) {
    	long time = 0;
    	if(currentTime <= FADE_TIME) {
    		time = FADE_TIME - currentTime;
    	} else if(currentTime > (DEMO_TIME - FADE_TIME)) {
    		time = currentTime - (DEMO_TIME - FADE_TIME);
    	} else {
    		return;
    	}
    	
    	byte numBars = 8;
    	int barHeight = screen.getHeight()/numBars;
    	int fillHeight = (int)(time * barHeight / FADE_TIME);
    	g.setColor(Color.black);
    	for(int i=0; i<numBars; i++) {
    		int y = i*barHeight + (barHeight - fillHeight)/2;
    		g.fillRect(0, y, screen.getWidth(), fillHeight);
    	}
    	
    }
    
    private void update(long elapsedTime) {
    	cloud.update(elapsedTime);
    	for(Sprite s : smileys) {
    		s.update(elapsedTime);
    		if(s.getX() + s.getWidth() >= screen.getWidth()) {
        		s.setXVelocity(-Math.abs(s.getXVelocity()));
        	}
        	if(s.getX() <= 0) {
        		s.setXVelocity(Math.abs(s.getXVelocity()));
        	}
        	if(s.getY() + s.getHeight() >= screen.getHeight()) {
        		s.setYVelocity(-Math.abs(s.getYVelocity()));
        	}
        	if(s.getY() <= 0) {
        		s.setYVelocity(Math.abs(s.getYVelocity()));
        	}
    	}
    	if(cloud.getX() + cloud.getWidth() >= screen.getWidth()) {
    		cloud.setXVelocity(-Math.abs(cloud.getXVelocity()));
    	}
    	if(cloud.getX() <= 0) {
    		cloud.setXVelocity(Math.abs(cloud.getXVelocity()));
    	}
    	if(cloud.getY() + cloud.getHeight() >= screen.getHeight()) {
    		cloud.setYVelocity(-Math.abs(cloud.getYVelocity()));
    	}
    	if(cloud.getY() <= 0) {
    		cloud.setYVelocity(Math.abs(cloud.getYVelocity()));
    	}
    }

    public void draw(Graphics g) {
        // draw background
        g.drawImage(bgImage, 0, 0, null);
        // draw image
        g.drawImage(cloud.getImage(), Math.round(cloud.getX()), Math.round(cloud.getY()), null);
        
        for(Sprite s : smileys) {
        	AffineTransform transform = new AffineTransform();
        	transform.setToTranslation(s.getX(), s.getY());
        	transform.rotate(s.getVelocityDirection(), (double) s.getWidth()/2, (double) s.getHeight()/2);
        	((Graphics2D)g).drawImage(s.getImage(), transform, null);
        }
    }
}
