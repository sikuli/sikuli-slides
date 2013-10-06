package org.sikuli.slides.api.concurrent;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.concurrent.CountDownLatch;


import com.google.common.collect.Lists;

/**
 * An OrLatch takes multiple latches. It releases awaiting threads 
 * when ONE of the latch releases.
 */
class OrLatch implements Latch {	
	List<Latch> latches = Lists.newArrayList();
	
	private CountDownLatch latchReleaseSignal;	
	
	static class LatchThread extends Thread {
		private Latch latch;
		private CountDownLatch latchReleaseSignal;
		public LatchThread(Latch latch, CountDownLatch releaseSignal){
			this.latch = checkNotNull(latch);
			this.latchReleaseSignal = checkNotNull(releaseSignal);
		}
		
		@Override
		public void run(){			
			latch.await();		
			latchReleaseSignal.countDown();
		}
	}	
	
	public void add(Latch latch){
		latches.add(latch);
	}
	
	@Override
	public void await() {
		latchReleaseSignal = new CountDownLatch(1);
		System.out.println("await" + this.getClass().getCanonicalName());
		for (Latch latch : latches){
			LatchThread t = new LatchThread(latch, latchReleaseSignal);
			t.start();			
		}		
		try {
			latchReleaseSignal.await();
		} catch (InterruptedException e) {
		}
		for (Latch latch : latches){
			System.out.println("release" + latch.getClass().getCanonicalName());
			latch.release();			
		}				
	}	
	
	@Override
	public void release() {
		if (latchReleaseSignal != null)
			latchReleaseSignal.countDown();		
		for (Latch latch : latches){
			latch.release();			
		}				
	}	
}