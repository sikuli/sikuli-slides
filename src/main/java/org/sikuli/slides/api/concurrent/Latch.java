package org.sikuli.slides.api.concurrent;

public interface Latch {
	/**
	 * Causes the current thread to wait until another thread invokes release()
	 */
	public void await();
	/**
	 * Releases all threads that are awaiting; nothing happens if no thread is awaiting
	 */	
	public void release();
}