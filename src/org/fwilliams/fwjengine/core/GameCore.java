package org.fwilliams.fwjengine.core;

import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

import org.fwilliams.fwjengine.input.InputManager;

/**
 * Inherit this class to create a game. This sets up the basic framework to run a game: Loads assets, creates a full screen Window and draws and updates game objects.
 * @author Francis Williams
 *
 */
public abstract class GameCore {
    protected ScreenManager screen;
    protected InputManager inputManager;
    
    /**
     * Initializes the Game and runs it.
     */
    public void run() {  
        try {
        	init();
        }
        finally {
             screen.restoreScreen();
        }
    }

    /**
     * Initializes the game by setting up the screen with the appropriate resolution selected from the available resolutions and loading any assets set in the loadAssets() method.
     */
    public void init() {
    	screen = new ScreenManager();
        DisplayMode displayMode = new DisplayMode(1440, 900, 32, 60);
        screen.setFullScreen(displayMode);
        inputManager = new InputManager(screen.getFullScreenWindow());
        loadAssets();
    }
    
    /**
     * The game loop. Starts the drawing loop for the game.
     */
    public void gameLoop() {
        long startTime = System.currentTimeMillis();
        long currTime = startTime;

        while (true) {
            long elapsedTime =
                System.currentTimeMillis() - currTime;
            currTime += elapsedTime;

            // update sprite
            update(elapsedTime);

            // draw and update screen
            Graphics2D g = screen.getGraphics();
            draw(g);
            g.dispose();
            screen.update();

            // take a nap
            try {
                Thread.sleep(20);
            }
            catch (InterruptedException ex) { }
        }
    }
   
    /**
     * Loads an image from a file specified in the argument.
     * @param fileName The image file to load.
     * @return The loaded image.
     */
    public Image loadImage(String fileName) {
        return new ImageIcon(fileName).getImage();
    }

    /**
     * Exits the game.
     */
    public void exit() {
    	screen.restoreScreen();
    }
    
    /**
     * Gets the screen manager object for the game.
     * @return The screen manager object for the game.
     */
    public ScreenManager getScreen() {
    	return screen;
    }
    
    /**
     * Override this method to preload any assets into the game.
     */
    public void loadAssets() {
    }
    
    /**
     * This method gets called once per frame. Override it to draw game objects to the screen.
     * @param g The graphics object used to draw objects to the screen.
     */
    public void draw(Graphics2D g) {
    }
    
    /**
     * This method is called once per frame. Override it to update any game objects.
     * @param elapsedTime The amount of time since this method was last called.
     */
    public void update(long elapsedTime) {
    }

}

