package org.fwilliams.fwjengine.demo;
import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.SwingUtilities;

import org.fwilliams.fwjengine.core.GameCore;


public class MouseLookDemo extends GameCore 
implements MouseMotionListener, KeyListener {

	private boolean mouseLookMode;
	private boolean centered;
	private Image background;
	private Image crosshair;
	private Robot robot;
	private Point bgPos;
	private Point mousePos;
	private Point center;
	private Cursor invisibleCursor;
	private Cursor defaultCursor;
	
	@Override
	public void init() {
		super.init();
		Window w = screen.getFullScreenWindow();
		
		w.addKeyListener(this);
		w.addMouseMotionListener(this);
		
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
		mouseLookMode = true;
		centered = false;
		
		center = new Point();
		bgPos = new Point();
		mousePos = new Point();
		
		invisibleCursor = 
			Toolkit.getDefaultToolkit().createCustomCursor(
				Toolkit.getDefaultToolkit().getImage(""), new Point(0,0), "invisible"
			);
		defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
		w.setCursor(invisibleCursor);
		
		restoreMouse();
		bgPos.x = center.x;
		bgPos.y = center.y;
		mousePos.x = center.x;
		mousePos.y = center.y;
		
		gameLoop();
	}
	
	@Override
	public synchronized void loadAssets() {
		background = loadImage("assets/background.png");
		crosshair = loadImage("assets/crosshair.png");
	}
	
	@Override
	public synchronized void draw(Graphics2D g) {
		int h = screen.getHeight();
		int w = screen.getWidth();
		
		if(bgPos.x < 0) {
			bgPos.x += w;
		}
		if(bgPos.y < 0) {
			bgPos.y += h;
		}
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		bgPos.x = bgPos.x % w;
		bgPos.y = bgPos.y % h;
		
		g.drawImage(background, bgPos.x, bgPos.y-h, null);
		g.drawImage(background, bgPos.x, bgPos.y, null);
		g.drawImage(background, bgPos.x-w, bgPos.y-h, null);
		g.drawImage(background, bgPos.x-w, bgPos.y, null);
		
		g.drawImage(crosshair, center.x-31, center.y-31, null);
	}
	
	public void restoreMouse() {
        Window window = screen.getFullScreenWindow();
        if (robot != null && window.isShowing()) {
            center.x = window.getWidth() / 2;
            center.y = window.getHeight() / 2;
            SwingUtilities.convertPointToScreen(bgPos,
                window);
            centered = true;
            robot.mouseMove(center.x, center.y);
        }
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		
		if(keyCode == KeyEvent.VK_ESCAPE) {
			exit();
		}
		if(keyCode == KeyEvent.VK_L) {
			mouseLookMode = !mouseLookMode;
			Window w = screen.getFullScreenWindow();
			if(mouseLookMode) {
				w.setCursor(invisibleCursor);
			} else {
				w.setCursor(defaultCursor);
			}
			restoreMouse();
		}	
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// Do nothing.
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// Do nothing
	}

	@Override
	public synchronized void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}

	@Override
	public synchronized void mouseMoved(MouseEvent e) {
		if(mouseLookMode) {
			if(e.getX() == center.x && e.getY() == center.y && centered) {
				centered = false;
			} else {
				bgPos.x += (e.getX() - mousePos.x);
				bgPos.y += (e.getY() - mousePos.y);
				restoreMouse();
			}
			mousePos.x = e.getX();
			mousePos.y = e.getY();
		}
	}
	
	public static void main(String[] args) {
		MouseLookDemo demo = new MouseLookDemo();
		demo.run();
	}
}
