package org.sikuli.recorder.detector;

import java.awt.image.BufferedImage;

import org.sikuli.recorder.event.ScreenShotEvent;

public class ScreenshotEventDetector extends EventDetector {

	public void stop(){
		running = false;
		try {
			capturingThread.join();
		} catch (InterruptedException e) {
		}
	}

	volatile  boolean running = true;
	private Thread capturingThread;
	
	
	public void start(){		
		 
		
		capturingThread = new Thread(){
			public void run(){
				while (running){		
					running = true;
					performScreenCapture();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
				}
			}
		};
				
		capturingThread.start();
	}

	private void performScreenCapture(){		 
		BufferedImage image = getRegionOfInterest().capture();		
		ScreenShotEvent e = new ScreenShotEvent();
		e.setImage(image);
		eventDetected(e);
	}

}