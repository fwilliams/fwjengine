package org.fwilliams.fwjengine.entity;



import java.awt.Image;

import org.fwilliams.fwjengine.graphics.Animation;


/**
 * This class represents a moving Animation. 
 * @author Francis Williams
 */
public class Sprite {
	private Animation animation;
	private float x;
	private float y;
	
	private float dx;
	private float dy;
	
	/**
	 * Creates a new Sprite with the specified Animation.
	 * @param animation The Animation representing the sprite.
	 */
	public Sprite(Animation animation) {
		this.animation = animation;
	}
	
	/**
	 * Updates the Sprite's position based on the velocity and the Animation.
	 * @param elapsedTime The time elapsed since the last call to this method.
	 */
	public void update(long elapsedTime) {
		animation.update(elapsedTime);
		
		x += dx*elapsedTime;
		y += dy*elapsedTime;
	}
	
	/**
	 * The current horizontal position of the top left corner of the sprite.
	 * @return The horizontal pixel value of the Sprite's top left corner.
	 */
	public float getX() {
		return x;
	}
	
	/**
	 * The current vertical position of the top left corner of the sprite.
	 * @return The vertical pixel value of the Sprite's top left corner.
	 */
	public float getY() {
		return y;
	}
	
	/**
	 * Gets the Sprite's horizontal velocity in pixels per millisecond.
	 * @return The Sprite's horizontal velocity in pixels per millisecond.
	 */
	public float getXVelocity() {
		return dx;
	}
	
	/**
	 * Gets the Sprite's vertical velocity in pixels per millisecond.
	 * @return The Sprite's vertical velocity in pixels per millisecond.
	 */
	public float getYVelocity() {
		return dy;
	}
	
	/**
	 * Gets the counter clockwise angle (in Radians) relative to the horizontal axis representing the direction of the Sprite's velocity.
	 * @return The angle of the Sprite's velocity.
	 */
    public double getVelocityDirection() {
    	double theta = Math.abs(Math.atan((double)(dy/dx)));
    	if(dx >= 0 && dy >= 0) {
    		return theta;
    	} else if(dx < 0 && dy >= 0) {
    		return (Math.PI - theta);
    	} else if(dx < 0 && dy < 0) {
    		return (Math.PI + theta);
    	} else if(dx >= 0 && dy <0) {
    		return (2*Math.PI - theta);
    	}
    	return (Double) null;
    }
    
	/**
	 * Gets the width of the Sprite's current image.
	 * @return The width of the Sprite's current image. 
	 */
	public int getWidth() {
		return animation.getCurrentImage().getWidth(null);
	}
	
	/**
	 * Gets the height of the Sprite's current image.
	 * @return The height of the Sprite's current image. 
	 */
	public int getHeight() {
		return animation.getCurrentImage().getHeight(null);
	}
	
	/**
	 * Gets the Sprite's current image.
	 * @return The Sprite's current image.
	 */
	public Image getImage() {
		return animation.getCurrentImage();
	}
	
	/**
	 * Sets the Sprite's horizontal position to the specified value.
	 * @param value The new horizontal position for the sprite.
	 */
	public void setX(float value) {
		x = value;
	}
	
	/**
	 * Sets the Sprite's vertical position to the specified value.
	 * @param value The new vertical position for the Sprite.
	 */
	public void setY(float value) {
		y = value;
	}
	
	/**
	 * Sets the Sprite's horizontal velocity to the specified value.
	 * @param value The new horizontal velocity for the Sprite.
	 */
	public void setXVelocity(float value) {
		dx = value;
	}
	
	/**
	 * Sets the Sprite's vertical velocity to the specified value.
	 * @param value The new vertical velocity for the Sprite.
	 */
	public void setYVelocity(float value) {
		dy = value;
	}
}
