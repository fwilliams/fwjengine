package org.fwilliams.fwjengine.util;
import java.util.LinkedList;


public class ThreadPool extends ThreadGroup {
	
	private boolean isAlive;
	private LinkedList taskQueue;
	private int threadID;
	private static int threadPoolID;
	
	/**
	 * Creates a new ThreadPool object. 
	 * @param numThreads The number of threads in the pool.
	 */
	public ThreadPool(int numThreads) {
		super("ThreadPool-" + (threadPoolID++));
		this.setDaemon(true);
		
		isAlive = true;
		taskQueue = new LinkedList();
		
		for(int i=0; i<numThreads; i++) {
			new PooledThread().start();
		}
	}
	
	/**
	 * Returns true if the ThreadPool is alive.
	 */
	public boolean isAlive() {
		return isAlive;
	}
	
	/**
	 * Requests a new task to run. The task executes on the next available idle thread in this ThreadPool.
	 * @param task The task to run. If null, no action is taken.
	 * @throws IllegalStateException if the 
	 */
	public synchronized void runTask(Runnable task) {
		if(!isAlive) {
			throw new IllegalStateException();
		}
		if(task != null) {
			taskQueue.add(task);
			notify();
		}
	}
	
	/**
	 * Gets the task at the top of the ThreadPool's task queue. If there are no tasks in the queue, the method waits until one is added.
	 * @return null if the ThreadPool is dead.
	 */
	public synchronized Runnable getTask() throws InterruptedException {
		while(taskQueue.size() == 0) {
			if(!isAlive) {
				return null;
			}
			wait();
		}
		return (Runnable) taskQueue.removeFirst();
	}
	
	/**
	 * Kills the ThreadPool and returns immdiately. All threads running in the ThreadPool are stopped and any waiting tasks are not executed.
	 * Once a ThreadPool has been closed, no more tasks can run on it. 
	 */
	public synchronized void close() {
		if(isAlive) {
			isAlive = false;
			taskQueue.clear();
			interrupt();
		}
	}
	
	/**
	 * Closes this ThreadPool and waits for all running threads to finish. Any waiting tasks are not executed.
	 */
	public void join() {
		synchronized(this) {
			this.isAlive = false;
			notifyAll();
		}
		
		Thread[] threads = new Thread[activeCount()];
		int count = enumerate(threads);
		for(int i=0; i<count; i++) {
			try {
				threads[i].join();
			} catch(InterruptedException e) {}
		}
	}
	
	/**
	 * A PooledThread is a Thread in a ThreadPool. It is designed to run a task (Runnable).
	 */
	private class PooledThread extends Thread {
		public PooledThread() {
			super(ThreadPool.this, "PooledThread-" + (threadID++));
		}
		@Override
		public void run() {
			while(!isInterrupted()) {
				Runnable task = null;
				
				try {
					task = getTask();
				} catch(InterruptedException e) {}
				
				if(task == null) {
					return;
				}
				try {
					task.run();
				} catch(Throwable t) {
					uncaughtException(this, t);
				}
 			}
		}
	}	
}
