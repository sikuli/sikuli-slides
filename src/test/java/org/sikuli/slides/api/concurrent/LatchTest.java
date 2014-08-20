package org.sikuli.slides.api.concurrent;

import java.util.Timer;
import java.util.TimerTask;

import org.junit.Test;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.Relative;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.robot.Key;
import org.sikuli.api.robot.Keyboard;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopKeyboard;
import org.sikuli.api.robot.desktop.DesktopMouse;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.DesktopCanvas;
import org.sikuli.slides.api.Context;
import org.sikuli.slides.api.concurrent.EscapeKeyLatch;
import org.sikuli.slides.api.concurrent.Latch;
import org.sikuli.slides.api.concurrent.OrLatch;
import org.sikuli.slides.api.concurrent.ScreenRegionClickLatch;
import org.sikuli.slides.api.concurrent.ScreenRegionHoverLatch;

public class LatchTest {
	
	private OrLatch orLatch;
	private ScreenRegion region;

	@Test(timeout = 2000)
	public void testScreenRegionHoverLatchCanRelease() {
		final Context context = new Context();				
		final ScreenRegion s = context.getScreenRegion();
		final ScreenRegion r = Relative.to(s).region(0.4,0.4,0.5,0.5).getScreenRegion();
		
		Canvas canvas = new DesktopCanvas();
		canvas.addBox(r);
		canvas.addLabel(r.getCenter(), "Hover here").withHorizontalAlignmentCenter();
		canvas.show();		
		
		Timer timer = new Timer();		
		TimerTask task = new TimerTask(){
			@Override
			public void run() {				
				Mouse mouse = new DesktopMouse();
				mouse.hover(r.getCenter());
			}			
		};
		timer.schedule(task,  1000);
		
		Latch latch = new ScreenRegionHoverLatch(r);
		latch.await();
				
		canvas.hide();
	}
	
	@Test(timeout = 2000)
	public void testScreenRegionClickLatchCanRelease() {
		final Context context = new Context();				
		final ScreenRegion s = context.getScreenRegion();
		final ScreenRegion r = Relative.to(s).region(0.5,0.5,0.6,0.6).getScreenRegion();
		
		Canvas canvas = new DesktopCanvas();
		canvas.addBox(r);
		canvas.addLabel(r.getCenter(), "Click here").withHorizontalAlignmentCenter();
		canvas.show();		
		
		Timer timer = new Timer();		
		TimerTask task = new TimerTask(){
			@Override
			public void run() {				
				Mouse mouse = new DesktopMouse();
				mouse.click(r.getCenter());
			}			
		};
		timer.schedule(task,  1000);
		
		Latch latch = new ScreenRegionClickLatch(r);
		latch.await();
				
		canvas.hide();
	}

	@Test(timeout = 2000)
	public void testEscapeKeyLatchCanRelease() {
		Timer timer = new Timer();		
		TimerTask task = new TimerTask(){
			@Override
			public void run() {				
				Keyboard keyboard = new DesktopKeyboard();
				keyboard.type(Key.ESC);
			}			
		};
		timer.schedule(task,  1000);
		
		Latch latch = new EscapeKeyLatch();
		latch.await();				
	}
	
	
	public void setUpOrLatch(){
		final ScreenRegion s = new DesktopScreenRegion();
		region = Relative.to(s).region(0.4,0.4,0.5,0.5).getScreenRegion();
		orLatch = new OrLatch();
		Latch keyLatch = new EscapeKeyLatch();
		Latch mouseLatch = new ScreenRegionHoverLatch(region);
		orLatch.add(keyLatch);
		orLatch.add(mouseLatch);
	}
	
	@Test(timeout = 2000)
	public void testOrLatchCanReleaseOnEscapeKey() {
		setUpOrLatch();
		
		Timer timer = new Timer();		
		TimerTask task = new TimerTask(){
			@Override
			public void run() {				
				Keyboard keyboard = new DesktopKeyboard();
				keyboard.type(Key.ESC);
			}			
		};
		timer.schedule(task,  1000);				
		orLatch.await();				
	}
	
	@Test(timeout = 2000)
	public void testOrLatchCanReleaseOnMouseOver() {
		setUpOrLatch();
		
		Timer timer = new Timer();		
		TimerTask task = new TimerTask(){
			@Override
			public void run() {				
				Mouse mouse = new DesktopMouse();
				mouse.hover(region.getCenter());
			}			
		};
		timer.schedule(task,  1000);				
		orLatch.await();				
	}

}
