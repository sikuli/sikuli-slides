package org.sikuli.slides.api.slideshow;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.sikuli.api.robot.Key;
import org.sikuli.api.robot.Keyboard;
import org.sikuli.api.robot.desktop.DesktopKeyboard;
import org.sikuli.slides.api.slideshow.SlideShowController;

import static org.mockito.Mockito.*;

public class SlideShowControllerHotkeyTest {

	SlideShowController slideshow;
	private SlideShowHotkeyManager hotkeys;
		
	void pause(long msecs){
		try {
			Thread.sleep(msecs);
		} catch (InterruptedException e) {
		}
	}
	
	@Before
	public void setUp(){
		slideshow = mock(SlideShowController.class);
		hotkeys = new SlideShowHotkeyManager(slideshow);
		hotkeys.start();
	}
	
	@After
	public void tearDown(){
		hotkeys.stop();
	}
	
	@Test
	public void testHotKeysCanTriggerActions() throws InterruptedException {
		
		Thread t = new Thread(){
			public void run(){
				pause(100);
				Keyboard keyboard = new DesktopKeyboard();
				keyboard.keyDown(Key.CTRL);
				keyboard.keyDown(Key.ALT);
				keyboard.keyDown(Key.LEFT);				
				keyboard.keyUp(Key.LEFT);				
				keyboard.keyUp();
				
				keyboard.keyDown(Key.CTRL);
				keyboard.keyDown(Key.ALT);
				keyboard.keyDown(Key.RIGHT);				
				keyboard.keyUp(Key.RIGHT);				
				keyboard.keyUp();
				
				keyboard.keyDown(Key.CTRL);
				keyboard.keyDown(Key.ALT);
				keyboard.keyDown(Key.ESC);				
				keyboard.keyUp(Key.ESC);				
				keyboard.keyUp();
			}
		};
		t.start();
		t.join();
		pause(100);
		
		InOrder inOrder = inOrder(slideshow);
		inOrder.verify(slideshow).previous();
		inOrder.verify(slideshow).next();
		inOrder.verify(slideshow).quit();
	}

}
