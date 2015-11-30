package org.fwilliams.fwjengine.input;


/**
 * <b>GameAction</b>
 * <br><br>
 * public class GameAction
 * <br>
 * <p>An abstraction for an action within the game.</p>
 * @author Francis Williams
 *
 */
public class GameAction {
	/**
	 * <b>MODE_NORMAL</b>
	 * <br><br>
	 * public static final int MODE_NORMAL = 0
	 * <br>
	 * <p>The default behavior. The isPressed() method will return true as long
	 * as the key is pressed.</p>
	 */
	public static final int MODE_NORMAL = 0;
	
	
	/**
	 * <b>MODE_INITIAL_KEY_PRESS_ONLY</b>
	 * <br><br>
	 * public static final int MODE_INITIAL_KEY_PRESS_ONLY = 1
	 * <br>
	 * <p>The isPressed() method will return true only when the key is initially
	 * pressed.</p>
	 */
	public static final int MODE_INITIAL_KEY_PRESS_ONLY = 1;
	
	/**
	 * <b>STATE_PRESSED</b>
	 * <br><br>
	 * public static final int STATE_PRESSED = 0
	 * <br>
	 * <p>The GameAction is in this state when a key is pressed.</p>
	 */
	public static final int STATE_PRESSED = 0;
	
	/**
	 * <b>STATE_RELEASED</b>
	 * <br><br>
	 * public static final int STATE_RELEASED = 1
	 * <br>
	 * <p>The GameAction is in this state after a key has been released.</p>
	 */
	public static final int STATE_RELEASED = 1;
	
	/**
	 * The GsmeAction is in this state after a key has been pressed and has not been released.
	 * This state only exists in MODE_INITIAL_PRESS_ONLY.
	 */
	public static final int STATE_WAIT_FOR_RELEASE = 2;
	
	private String name;
	private int mode;
	private int state;
	private int amount;
	
	/**
	 * <b>GameAction</b>
	 * <br><br>
	 * public GameAction(String)
	 * <br>
	 * <p>Creates a new GameAction in MODE_NORMAL with the given name.<p>
	 * @param name The name of the GameAction
	 */
	public GameAction(String name) {
		this(name, MODE_NORMAL);
	}
	
	/**
	 * <b>GameAction</b>
	 * <br><br>
	 * public GameAction()
	 * <br>
	 * <p>Creates a new GameAction with the given name and mode.</p>
	 * @param name The name of the GameAction
	 * @param mode The mode of the GameAction
	 */
	public GameAction(String name, int mode) {
		this.name = name;
		this.mode = mode;
		this.state = STATE_RELEASED;
	}
	
	/**
	 * <b>getName</b>
	 * <br><br>
	 * public String getName()
	 * <br>
	 * <p>Gets the name of the GameAction.</p>
	 * @return The name of the GameAction.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * <b>getMode</b>
	 * <br><br>
	 * public int getMode()
	 * <br>
	 * <p>Gets the mode the GameAction is in.</p>
	 * @return The GameAction's mode
	 */
	public int getMode() {
		return mode;
	}
	
	/**
	 * <b>getState</b>
	 * <br><br>
	 * public int getState()
	 * <br>
	 * <p>Gets the GameAction's current state.</p>
	 * @return The GameActions's state
	 */
	public int getState() {
		return state;
	}
	
	/**
	 * <b>getAmount</b>
	 * <br><br>
	 * private synchronized int getAmount()
	 * <br>
	 * <p>Gets the number of times a key was pressed or the distance moved by the mouse.</p>
	 * @return The number of times a key was pressed or the distance moved by the mouse.
	 */
	private synchronized int getAmount() {
		int ret = amount;
		if(ret != 0) {
			if(state == STATE_RELEASED) {
				amount = 0;
			} else if(mode == MODE_INITIAL_KEY_PRESS_ONLY) {
				state = STATE_WAIT_FOR_RELEASE;
				amount = 0;
			}
		}
		return ret;
	}
	
	/**
	 * <b>press</b>
	 * <br><br>
	 * public synchronized void press(int)
	 * <br>
	 * <p>Signal that a key was pressed a certain number of times or that 
	 * the mouse was moved a certain distance.</p>
	 * @param amount The amount of times the key was pressed or the amount the mouse has moved.
	 */
	public synchronized void press(int amount){
		if(state != STATE_WAIT_FOR_RELEASE) {
			this.amount += amount;
			state = STATE_PRESSED;
		}
	}
	
	/**
	 * <b>press</b>
	 * <br><br>
	 * public synchronized void press()
	 * <br>
	 * <p>Signals the key was pressed.</p>
	 */
	public synchronized void press() {
		press(1);
	}
	
	/**
	 * <b>release</b>
	 * <br><br>
	 * public synchronized void release()
	 * <br>
	 * <p>Signals the key was released.</p>
	 */
	public synchronized void release() {
		state = STATE_RELEASED;
	}
	
	/**
	 * <b>tap</b>
	 * <br><br>
	 * public synchronized void tap()
	 * <br>
	 * <p>Taps the game action by calling press() followed by release().</p>
	 */
	public synchronized void tap() {
		press();
		release();
	}
	
	/**
	 * <b>reset</b>
	 * <br><br>
	 * public void reset()
	 * <br>
	 * <p>Resets the game action to its initial state.</p>
	 */
	public void reset() {
		state = STATE_RELEASED;
		amount = 0;
	}
	
	/**
	 * <b>isPressed</b>
	 * <br><br>
	 * public boolean isPressed()
	 * <br>
	 * <p>Returns true if the action is pressed.</p>
	 * @return Whether or not the button is pressed
	 */
	public boolean isPressed() {
		return (getAmount() != 0);
	}
}
