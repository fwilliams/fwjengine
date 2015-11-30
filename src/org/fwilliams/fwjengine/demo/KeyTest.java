package org.fwilliams.fwjengine.demo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

import org.fwilliams.fwjengine.core.GameCore;


public class KeyTest extends GameCore implements KeyListener {

	private LinkedList<String> messages;
	private static final int FONT_SIZE = 12;
	
	public static void main(String[] args) {
		new KeyTest().run();
	}
	
	@Override
	public void init() {
		super.init();
		messages = new LinkedList<String>();
		Window window = screen.getFullScreenWindow();
		
		window.setFocusTraversalKeysEnabled(false);
		
		window.addKeyListener(this);
		
		addMessage("KeyTest: Press Esc to exit.");
		gameLoop();
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		int keyCode = arg0.getKeyCode();
		addMessage("Pressed: " + KeyEvent.getKeyText(keyCode));
		arg0.consume();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		int keyCode = arg0.getKeyCode();
		if(keyCode == KeyEvent.VK_ESCAPE) {
			exit();
		} else {
			addMessage("Released: " + KeyEvent.getKeyText(keyCode));
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		arg0.consume();
		
	}
	
	@Override
	public synchronized void draw(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.black);
		g.fillRect(0, 0, screen.getWidth(), screen.getHeight());
		
		g.setColor(Color.white);
		int y = FONT_SIZE;
		
		for(int i=0; i<messages.size(); i++) {
			g.drawString(messages.get(i), 5, y);
			y +=FONT_SIZE;
		}
	}
	
	public synchronized void addMessage(String message) {
		messages.add(message);
		if(messages.size() >= screen.getHeight()/FONT_SIZE) {
			messages.remove(0);
		}
	}
}
