package org.sikuli.recorder;

import java.awt.Color;
import java.io.File;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.Relative;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.DesktopCanvas;
import org.sikuli.recorder.detector.EventDetector;
import org.sikuli.recorder.detector.MouseEventDetector;
import org.sikuli.recorder.detector.ScreenshotEventDetector;
import org.sikuli.recorder.pptx.PPTXGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;


public class Recorder {
	
	static Logger logger = LoggerFactory.getLogger(Recorder.class);

	// used to draw a red box to visualize the region of interest
	private Canvas canvas;
	private ScreenRegion regionOfInterest;

	public Recorder(){
		EventDetector d1 = new MouseEventDetector();
		EventDetector d2 = new ScreenshotEventDetector();
		canvas = new DesktopCanvas();
		addEventDetector(d1);
		addEventDetector(d2);
		setRegionOfInterest(new DesktopScreenRegion());
	}	

	public File getEventDir() {
		return writer.getEventDir();
	}

	public void setEventDir(File dir) {
		writer.setEventDir(dir);

	}

	private List<EventDetector> detectors = Lists.newArrayList();


	DefaultEventWriter writer = new DefaultEventWriter();
	public void addEventDetector(EventDetector d) {
		d.setWriter(writer);
		detectors.add(d);
	}

	public void startRecording(){
		logger.info("Start Recording");
		for (EventDetector d : detectors){
			d.start();
		}
	}



	public void setRegionOfInterest(ScreenRegion screenRegion) {
		regionOfInterest = screenRegion;
		for (EventDetector d : detectors){
			d.setRegionOfInterest(screenRegion);
		}
	}


	CountDownLatch escapeSignal = new CountDownLatch(1);

	public void stopRecording(){	
		for (EventDetector d : detectors){
			d.stop();
		}		
	}

	public void start(){

		ScreenRegion outsideBorder = Relative.to(regionOfInterest).taller(7).wider(7).getScreenRegion();
		// draw bigger so the red lines won't be captured in screenshots
		canvas.addBox(outsideBorder).withColor(Color.red).withLineWidth(3);
		canvas.show();

		try {
			GlobalScreen.registerNativeHook();
		}
		catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());
			return;
			//System.exit(1);
		}

		//Construct the example object and initialze native hook.
		GlobalScreen.getInstance().addNativeKeyListener(new HotKeyListener());

		try {
			escapeSignal.await();
		} catch (InterruptedException e) {
		}

		stopRecording();

		canvas.hide();
		System.out.println("Recording is stopped.");

	}

	class HotKeyListener implements NativeKeyListener {

		private Logger logger = LoggerFactory.getLogger(HotKeyListener.class); 

		public void nativeKeyPressed(NativeKeyEvent e) {

			boolean isMetaPressed = (e.getModifiers() & NativeKeyEvent.META_MASK) > 0;
			boolean isAltPressed = (e.getModifiers() & NativeKeyEvent.ALT_MASK) > 0;
			boolean isShiftPressed = (e.getModifiers() & NativeKeyEvent.SHIFT_MASK) > 0;
			boolean isCtrlPressed = (e.getModifiers() & NativeKeyEvent.CTRL_MASK) > 0;


			// CTRL+SHIFT+2
			if (e.getKeyCode() == NativeKeyEvent.VK_2 && isShiftPressed && isCtrlPressed){                	
				logger.trace("CTRL+SHIFT+2 is pressed");
				startRecording();
			}

			// CTRL+SHIFT+ESC
			if (e.getKeyCode() == NativeKeyEvent.VK_ESCAPE && isShiftPressed && isCtrlPressed){
				logger.trace("CTRL+SHIFT+ESC is pressed");
				GlobalScreen.unregisterNativeHook();
				escapeSignal.countDown();
			}

		}

		public void nativeKeyReleased(NativeKeyEvent e) {
			//	                System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
		}

		public void nativeKeyTyped(NativeKeyEvent e) {
			//	                System.out.println("Key Typed: " + e.getKeyText(e.getKeyCode()));
		}

	}



}