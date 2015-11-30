package org.fwilliams.fwjengine.core;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

/**
 * This class manages a full screen window. It has some automation to find the best fitting resolution for the device it runs on and can create images compatible with the device's settings.
 * @author Francis Williams
 *
 */
public class ScreenManager {
	private GraphicsDevice device;
	
	/**
	 * Creates a new screen manager object representing the local graphics environment.
	 */
	public ScreenManager() {
		GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		this.device = environment.getDefaultScreenDevice();
	}
	
	/**
	 * Gets a list of the compatible display modes for the current system's screen device.
	 * @return An array of all compatible display modes for the system's display device.
	 */
	public DisplayMode[] getCompatibleModes() {
		return device.getDisplayModes();
	}
	
	/**
	 * Given an array of DisplayModes, this method will return the first one that is compatible with the display device.
	 * If no compatible display mode is found, this method will return null
	 * @param modes A list of DisplayModes
	 * @return The first compatible DisplayMode with this system's display device.
	 */
	public DisplayMode findFirstCompatibleDisplayMode(DisplayMode[] modes) {
		for(DisplayMode inputMode : modes) {
			for(DisplayMode compatibleMode: this.getCompatibleModes()) {
				if(displayModesMatch(inputMode, compatibleMode)) {
					return inputMode;
				}
			}
			return null;
		}
		return null;
	}
	
	/**
	 * Compares the resolution, bit depth and refresh rate of two DisplayMode objects. If they are compatible, this method returns true.
	 * Note: If the bit depth of one of the DisplayMode objects is BIT_DEPTH_MULTI, the bit depths will be treated as identical.
	 * 		 If the refresh rate of one of the DisplayMode objects is REFRESH_RATE_UNKOWN, the refresh rates will be treated as identical.
	 * @param dm1 The first DisplayMode object to be compared.
	 * @param dm2 The second DisplayMode object to be compared.
	 * @return Whether or not the two display modes match.
	 */
	public boolean displayModesMatch(DisplayMode dm1, DisplayMode dm2) {
		if(dm1.getWidth() != dm2.getWidth()) {
			return false;
		}
		if(dm1.getHeight() != dm2.getHeight()) {
			return false;
		}
		if(dm1.getBitDepth() != dm2.getBitDepth() && 
		   dm1.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI && 
		   dm2.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI) {
			return false;
		}
		if(dm1.getRefreshRate() != dm2.getRefreshRate() &&
		   dm1.getBitDepth() != DisplayMode.REFRESH_RATE_UNKNOWN &&
		   dm2.getBitDepth() != DisplayMode.REFRESH_RATE_UNKNOWN) {
			return false;
		}
		return true;
	}
	
	/**
	 * Creates a new Window and makes it full screen. Sets up 2 buffers to blit or flip depending on the device.
	 * @param dm The DisplayMode object to be used for the full screen Window.
	 */
	public void setFullScreen(DisplayMode dm) {
		JFrame frame = new JFrame();
		frame.setBackground(Color.black);
		frame.setUndecorated(true);
		frame.setIgnoreRepaint(true);
		frame.setResizable(false);
		
		device.setFullScreenWindow(frame);
		if(dm != null && device.isDisplayChangeSupported()) {
			try {
				device.setDisplayMode(dm);
			} catch(IllegalArgumentException e) {/*Fail Silently - Illegal mode for this device*/}
		}
		frame.createBufferStrategy(2);
	}
	
	/**
	 * Gets the Graphics2D object associated with the current BufferStrategy. This Graphics2D object will draw to the appropriate buffer.
	 * @return The Graphics2D object associated with the current BufferStrategy.
	 */
	public Graphics2D getGraphics() {
		Window window = device.getFullScreenWindow();
		if(window != null) {
			BufferStrategy strategy = window.getBufferStrategy();
			return (Graphics2D) strategy.getDrawGraphics();
		}
		return null;
	}
	
	/**
	 * Gets the Window object filling the screen.
	 * @return The Window object filling the screen.
	 */
	public Window getFullScreenWindow() {
		return device.getFullScreenWindow();
	}
	
	/**
	 * Updates the screen with the appropriate buffer.
	 */
	public void update() {
		Window window = device.getFullScreenWindow();
		if(window != null) {
			BufferStrategy strategy = window.getBufferStrategy();
			if(!strategy.contentsLost()) {
				strategy.show();
			}
			Toolkit.getDefaultToolkit().sync();
		}
	}
	
	/**
	 * Gets the horizontal resolution of the Window. This method returns 0 if the Window is null;
	 * @return The horizontal resolution of the window.
	 */
	public int getWidth() {
		Window window = device.getFullScreenWindow();
		if(window != null) {
			return window.getWidth();
		}
		return 0;
	}
	
	/**
	 * Gets the vertical resolution of the Window. This method returns 0 if the Window is null;
	 * @return The vertical resolution of the window.
	 */
	public int getHeight() {
		Window window = device.getFullScreenWindow();
		if(window != null) {
			return window.getHeight();
		}
		return 0;
	}
	
	/**
	 * Frees the memory occupied by the full screen Window and restores the original screen resolution if changed.
	 */
	public void restoreScreen() {
		Window window = device.getFullScreenWindow();
		if(window != null) {
			window.dispose();
		}
		device.setFullScreenWindow(null);
	}
	
	/**
	 * Creates an image compatible with the current display.
	 * @param width The width of the image.
	 * @param height The height of the image.
	 * @param transparency The transperency state of the image.
	 * @return An image compatible with the current display.
	 */
	public BufferedImage createCompatibleImage(int width, int height, int transparency) {
		Window window = device.getFullScreenWindow();
		if(window != null) {
			GraphicsConfiguration gc = window.getGraphicsConfiguration();
			return gc.createCompatibleImage(width, height, transparency);
		}
		return null;
	}
}
