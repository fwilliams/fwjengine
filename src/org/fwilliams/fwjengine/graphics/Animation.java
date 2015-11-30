package org.fwilliams.fwjengine.graphics;

import java.awt.Image;
import java.util.ArrayList;

/**
 * An animated image. Consists of multiple images that play for specified amounts of time. 
 * @author Francis Williams
 */
public class Animation {
	private ArrayList<AnimationFrame> frames;
	private int currentFrameIndex;
	private long animationTime;
	private long totalDuration;
	
	/**
	 * Creates a new, empty animation.
	 */
	public Animation() {
		this.frames = new ArrayList<AnimationFrame>();
		this.totalDuration = 0;
		this.start();
	}
	
	/**
	 * Returns a new, identical Animation as this one.
	 * @return An identical Animation to this one.
	 */
	public Animation clone() {
		Animation clone = new Animation();
		for(AnimationFrame f : this.frames) {
			clone.addFrame(f.image, f.duration);
		}
		return clone;
	}
	
	/**
	 * Adds a new frame to the animation.
	 * @param img The image to be added.
	 * @param duration The duration this image should be displayed for in the animation.
	 */
	public synchronized void addFrame(Image img, long duration) {
		this.totalDuration += duration;
		this.frames.add(new AnimationFrame(img, this.totalDuration));
		
	}
	
	/**
	 * Gets the current Image of the Animation.
	 * @return The Animation's current Image.
	 */
	public synchronized Image getCurrentImage() {
		if(this.frames.size() != 0) {
			return (Image) this.frames.get(this.currentFrameIndex).image;
		}
		return null;
	}

	/**
	 * Updates the current frame based on the elapsed time.
	 * @param elapsedTime The elapsed time since the last call to this method.
	 */
	public synchronized void update(long elapsedTime) {
		if(frames.size() > 1) {
			this.animationTime += elapsedTime;

			if(this.animationTime >= this.totalDuration) {
				this.animationTime = this.animationTime % this.totalDuration;
				this.currentFrameIndex = 0;
			}
			while(this.animationTime > this.getFrame(this.currentFrameIndex).duration) {
				this.currentFrameIndex++;
			}
		}
	}
	
	/**
	 * Gets the image at the specified zero-based based index in the Animation.
	 * @param index The zero-based image index.
	 * @return The image in the Animation at the specified index.
	 */
	private AnimationFrame getFrame(int index) {
		return this.frames.get(index);
	}
	
	/**
	 * Resets the the animation.
	 */
	private synchronized void start() {
		this.animationTime = 0;
		this.currentFrameIndex = 0;
	}
	
	/**
	 * A single animation frame. Has an image and a duration for which the frame should be displayed for.
	 * @author Francis Williams
	 */
	private class AnimationFrame {
		long duration;
		Image image;
		
		public AnimationFrame(Image img, long duration) {
			this.image = img;
			this.duration = duration;
		}
	}
	
}
