package org.fwilliams.fwjengine.input;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;


import javax.swing.SwingUtilities;


public class InputManager implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
	
	/**
	 * <b>INVISIBLE_CURSOR</b>
	 * <br><br>
	 * public static final Cursor INVISIBLE_CURSOR
	 * <br>
	 * <p>An invisible mouse cursor.
	 */
	public static final Cursor INVISIBLE_CURSOR = 
		Toolkit.getDefaultToolkit().createCustomCursor(
				Toolkit.getDefaultToolkit().createImage(""), 
				new Point(0,0),
				"invisible");
	
	/**
	 * <b>MOUSE_LEFT</b>
	 * <br><br>
	 * public static final int MOUSE_LEFT = 0
	 * <br>
	 * <p>Represents a mouse motion to the left.
	 */
	public static final int MOUSE_LEFT = 0;
	
	/**
	 * <b>MOUSE_RIGHT</b>
	 * <br><br>
	 * public static final int MOUSE_RIGHT = 1
	 * <br>
	 * <p>Represents a mouse motion to the right.
	 */
	public static final int MOUSE_RIGHT = 1;
	
	/**
	 * <b>MOUSE_UP</b>
	 * <br><br>
	 * public static final int MOUSE_UP = 2
	 * <br>
	 * <p>Represents a mouse motion upwards.
	 */
	public static final int MOUSE_UP = 2;
	
	
	/**
	 * <b>MOUSE_DOWN</b>
	 * <br><br>
	 * public static final int MOUSE_DOWN = 3
	 * <br>
	 * <p>Represents a mouse motion downwards.
	 */
	public static final int MOUSE_DOWN = 3;
	
	/**
	 * <b>MOUSE_WHEEL_UP</b>
	 * <br><br>
	 * public static final int MOUSE_WHEEL_UP = 4
	 * <br>
	 * <p>Represents the mouse wheel scrolling up.
	 */
	public static final int MOUSE_WHEEL_UP = 4;
	
	/**
	 * <b>MOUSE_WHEEL_DOWN</b>
	 * <br><br>
	 * public static final int MOUSE_WHEEL_DOWN = 5
	 * <br>
	 * <p>Represents the mouse wheel scrolling down.
	 */
	public static final int MOUSE_WHEEL_DOWN = 5;
	
	/**
	 * <b>MOUSE_BUTTON_1</b>
	 * <br><br>
	 * public static final int MOUSE_BUTTON_1 = 6
	 * <br>
	 * <p>Represents the left mouse button.
	 */
	public static final int MOUSE_BUTTON_1 = 6;
	
	/**
	 * <b>MOUSE_BUTTON_2</b>
	 * <br><br>
	 * public static final int MOUSE_BUTTON_2 = 7
	 * <br>
	 * <p>Represents the center mouse button.
	 */
	public static final int MOUSE_BUTTON_2 = 7;
	
	/**
	 * <b>MOUSE_BUTTON_3</b>
	 * <br><br>
	 * public static final int MOUSE_BUTTON_3 = 8
	 * <br>
	 * <p>Represents the right mouse button.
	 */
	public static final int MOUSE_BUTTON_3 = 8;
	
	private static final int NUM_MOUSE_CODES = 9;
	private static final int NUM_KEY_CODES = 600;
	
	private GameAction[] keyMap = new GameAction[NUM_KEY_CODES];
	private GameAction[] mouseMap = new GameAction[NUM_MOUSE_CODES];
	
	private Component component;
	private Robot robot;
	private Point center;
	private Point mousePosition;
	private boolean isRecentering;
	
	/**
	 * <b>InputManager</b>
	 * <br><br>
	 * public IntputManager(Component)
	 * <br>
	 * <p>Creates a new Input manager and attaches it to the given component.</p>
	 * @param c The component this input manager is attached to.
	 */
	public InputManager(Component c) {
		this.component = c;
		
		this.mousePosition = new Point();
		this.center = new Point();
		
		c.addKeyListener(this);
		c.addMouseListener(this);
		c.addMouseMotionListener(this);
		c.addMouseWheelListener(this);
		
		c.setFocusTraversalKeysEnabled(false);
	}
	
	/**
	 * <b>setCursor</b>
	 * <br><br>
	 * public void setCursor(Cursor)
	 * <br>
	 * <p>Sets the mouse cursor to the one specified.</p>
	 * @param cursor The cursor to set.
	 */
	public void setCursor(Cursor cursor) {
		component.setCursor(cursor);
	}
	
	/**
	 * <b>setRelativeMouseMode</b>
	 * <br><br>
	 * public void setRelativeMouseMode(boolean)
	 * <br>
	 * <p>Sets relative mouse mode.</p>
	 * <br>
	 * <b>Note</b>
	 * <p>If relative mouse mode is set. The mouse cursor will be centered every frame 
	 * and the distance it has moved during that frame will be be stored as the amount value in the GameAction
	 * for the mouse movement in that direction (MOUSE_UP, MOUSE_DOWN, MOUSE_LEFT, MOUSE_RIGHT).</p>
	 * @param isMouseModeSet Whether or not relative mouse mode is set.
	 */
	public void setRelativeMouseMode(boolean isMouseModeSet) {
		if(isMouseModeSet == isRelativeMouseMode()) {
			return;
		}
		if(isMouseModeSet) {
			try {
				robot = new Robot();
				recenterMouse();
			} catch (AWTException e) {
				robot = null;
			}
		} else {
			robot = null;
		}
	}
	
	/**
	 * <b>recenterMouse</b>
	 * <br><br>
	 * public synchronized void recenterMouse()
	 * <br>
	 * <p>If the Component this InputManager is attached to is visible, this method will reposition the mouse to 
	 * the center of the component.</p>
	 */
	public synchronized void recenterMouse() {
		if(robot != null && component.isShowing()) {
			center.x = component.getWidth()/2;
			center.y = component.getHeight()/2;
			SwingUtilities.convertPointToScreen(center, component);
			isRecentering = true;
			robot.mouseMove(center.x, center.y);
		}
	}
	
	/**
	 * <b>isRelativeMouseMode</b>
	 * <br><br>
	 * public boolean isRelativeMouseMode() 
	 * <br>
	 * <p>If this InputManager is in relative mouse mode, this method will return true. Otherwise it will return false.</p>
	 * <br>
	 * <b>Note:</b>
	 * <p>If relative mouse mode is set. The mouse cursor will be centered every frame 
	 * and the distance it has moved during that frame will be be stored as the amount value in the GameAction
	 * for the mouse movement in that direction (MOUSE_UP, MOUSE_DOWN, MOUSE_LEFT, MOUSE_RIGHT).</p>
	 */
	public boolean isRelativeMouseMode() {
		return (robot != null);
	}
	
	/**
	 * <b>mapToKey</b>
	 * <br><br>
	 * public void mapToKey(int, GameAction)
	 * <p>Maps the given GameAction to the given virtual key code.
	 * @param keyCode The virtual keyCode to to map the GameAction to.
	 * @param action The GameAction to map.
	 */
	public void mapToKey(int keyCode, GameAction action) {
		keyMap[keyCode] = action;
	}
	
	/**
	 * <b>mapToKey</b>
	 * <br><br>
	 * public void mapToKey(int, GameAction)
	 * <p>Maps the given GameAction to the given mouse action.
	 * @param mouseCode The mouse action to to map the GameAction to.
	 * @param action The GameAction to map.
	 */ 
	void mapToMouse(int mouseCode, GameAction action) {
		mouseMap[mouseCode] = action;
	}
	
	 /**
	  * <b>clearMap</b>
	  * <br><br>
	  * <p>Clears the specified GameAction if it is mapped to a key or mouse event.
	  * @param action The GameAction to clear.
	  */
	public void clearMap(GameAction action) {
		for(int i=0; i<keyMap.length; i++) {
			if(keyMap[i] == action) {
				keyMap[i] = null;
			}
		}
		for(int i=0; i<mouseMap.length; i++) {
			if(mouseMap[i] == action) {
				mouseMap[i] = null;
			}
		}
	}
	
	/**
	 * <b>getKeyName</b>
	 * <br><br>
	 * public static String getKeyName(int)
	 * <p>
	 * @param keyCode
	 * @return
	 */
	public static String getKeyName(int keyCode) {
	    return KeyEvent.getKeyText(keyCode);
	}
	
	
	/**
	    Gets the name of a mouse code.
	*/
	public static String getMouseName(int mouseCode) {
	    switch (mouseCode) {
	        case MOUSE_LEFT: return "Mouse Left";
	        case MOUSE_RIGHT: return "Mouse Right";
	        case MOUSE_UP: return "Mouse Up";
	        case MOUSE_DOWN: return "Mouse Down";
	        case MOUSE_WHEEL_UP: return "Mouse Wheel Up";
	        case MOUSE_WHEEL_DOWN: return "Mouse Wheel Down";
	        case MOUSE_BUTTON_1: return "Mouse Button 1";
	        case MOUSE_BUTTON_2: return "Mouse Button 2";
	        case MOUSE_BUTTON_3: return "Mouse Button 3";
	        default: return "Unknown mouse code " + mouseCode;
	    }
	}


	
	/**
	 * <b>getMouseButtonCode</b>
	 * <br><br>
	 * static int getMouseButtonCode(MouseEvent)
	 * <br>
	 * <p>Gets the mouse button code(InputManaget.MOUSE_BUTTON_1, InputManaget.MOUSE_BUTTON_2, InputManaget.MOUSE_BUTTON_3) from a MouseEvent.</p>
	 * @param e The MouseEvent to retrieve the code from.
	 * @return the mouse button code.
	 */
	public static int getMouseButtonCode(MouseEvent e) {
		switch(e.getButton()) {
			case MouseEvent.BUTTON1:
				return MOUSE_BUTTON_1;
			case MouseEvent.BUTTON2:
				return MOUSE_BUTTON_1;
			case MouseEvent.BUTTON3:
				return MouseEvent.BUTTON3;
			default:
				return -1;
		}
	}
	
	private GameAction getKeyAction(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(keyCode < keyMap.length) {
			return keyMap[keyCode];
		} else {
			return null;
		}
	}
	
	private GameAction getMouseButtonAction(MouseEvent e) {
		int mouseCode = getMouseButtonCode(e);
		if(mouseCode != -1) {
			return mouseMap[mouseCode];
		} else {
			return null;
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}

	@Override
	public synchronized void mouseMoved(MouseEvent e) {
		if(isRecentering && center.x == e.getX() && center.y == e.getY()) {
			isRecentering = false;
		} else {
			int dx = e.getX() - mousePosition.x;
			int dy = e.getY() - mousePosition.y;
			mouseMovedHelper(MOUSE_LEFT, MOUSE_RIGHT, dx);
			mouseMovedHelper(MOUSE_UP, MOUSE_DOWN, dy);
			
			if(isRelativeMouseMode()) {
				recenterMouse();
			}
		}
		
		mousePosition.x = e.getX();
		mousePosition.y = e.getY();	
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		e.consume();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		mouseMoved(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		mouseMoved(e);
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		GameAction action = getMouseButtonAction(e);
		if(action != null) {
			action.press();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		GameAction action = getMouseButtonAction(e);
		if(action != null) {
			action.release();
		}	
	}

	@Override
	public void keyPressed(KeyEvent e) {
		GameAction action = getKeyAction(e);
		if(action != null) {
			action.press();
		}	
	}

	@Override
	public void keyReleased(KeyEvent e) {
		GameAction action = getKeyAction(e);
		if(action != null) {
			action.release();
		}		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		e.consume();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private void mouseMovedHelper(int negCode, int posCode, int amount) {
		GameAction action;
		if(amount < 0) {
			action = mouseMap[negCode];
		} else {
			action = mouseMap[posCode];
		}
		
		if(action != null) {
			action.press(amount);
			action.release();
		}
	}

}
