package org.fwilliams.fwjengine.demo;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;

import org.fwilliams.fwjengine.core.GameCore;


public class MouseTest extends GameCore 
implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener{

	
	public static final Color[] COLORS = {Color.blue,  Color.cyan,      Color.darkGray, 
							  Color.gray,   Color.green, Color.lightGray, Color.magenta, 
							  Color.orange, Color.pink,  Color.red,       Color.white, 
							  Color.yellow};
	public static final int TRAIL_LENGTH = 10;
	
	private int scrollIndex;
	private LinkedList<Point> trail;
	private boolean trailMode;
	
	public static void main(String[] args) {
		MouseTest test = new MouseTest();
		test.run();
	}
	
	public synchronized void mouseEvent(MouseEvent e) {
		trail.addFirst(new Point(e.getX(), e.getY()));
		while(trail.size() > TRAIL_LENGTH) {
			trail.removeLast();
		}
	}
	
	@Override
	public void init() {
		super.init();
		Window w = screen.getFullScreenWindow();
		w.addKeyListener(this);
		w.addMouseListener(this);
		w.addMouseMotionListener(this);
		w.addMouseWheelListener(this);
		w.setFocusTraversalKeysEnabled(false);
		scrollIndex = 0;
		trail = new LinkedList<Point>();
		trailMode = false;
		gameLoop();
	}
	
	@Override
	public synchronized void draw(Graphics2D g) {
		Window w = screen.getFullScreenWindow();
		
		g.setColor(w.getBackground());
		g.fillRect(0, 0, w.getWidth(), w.getHeight());
		
		g.setColor(COLORS[scrollIndex]);
		if(trailMode) {
			try {
				for(Point p : trail) {
					g.drawString("Hello World", p.x, p.y);
				}
			} catch(ConcurrentModificationException e) {
				// The AWT event thread is modifying the linked list. 
				// We don't care since any faulty results will be fixed on the next redraw.
				// TODO: Find a solution around this.
			}

		} else {
			if(trail.size() != 0) {
				Point p = trail.get(0);
				g.drawString("Hello World", p.x, p.y);
			}
		}
	}
	
	
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		int keyCode = arg0.getKeyCode();
		if(keyCode == KeyEvent.VK_ESCAPE) {
			exit();
		}
		arg0.consume();
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		trailMode = !trailMode;	
		System.out.println("trailMode: " + trailMode);
	}
	@Override
	public void mouseMoved(MouseEvent arg0) {
		trail.addFirst(new Point(arg0.getX(), arg0.getY()));
		while(trail.size() > TRAIL_LENGTH) {
			trail.removeLast();
		}
	}
	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		scrollIndex = (scrollIndex +  arg0.getWheelRotation()) % COLORS.length;
		
		if(scrollIndex < 0) {
			scrollIndex += COLORS.length;
		}
		Window w = screen.getFullScreenWindow();
		w.setForeground(COLORS[scrollIndex]);
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		//Do nothing.
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		mouseMoved(arg0);
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		mouseMoved(arg0);
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		mouseMoved(arg0);
	}
	@Override
	public void mouseDragged(MouseEvent arg0) {
		mouseMoved(arg0);
		
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
		// Do nothing
	}
	@Override
	public void keyPressed(KeyEvent arg0) {
		// Do nothing
	}

}
