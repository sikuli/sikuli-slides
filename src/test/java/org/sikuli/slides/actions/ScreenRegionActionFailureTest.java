package org.sikuli.slides.actions;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.event.MouseEvent;
import java.io.IOException;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.NativeMouseEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.robot.desktop.DesktopScreen;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.DesktopCanvas;
import org.sikuli.slides.api.ActionRuntimeException;
import org.sikuli.slides.sikuli.NullScreenRegion;

public class ScreenRegionActionFailureTest {

	private MouseEventDetector detector;
	private NullScreenRegion nullScreenRegion;	

	private NativeMouseEvent getLastMouseEvent(){
		if (detector.events.size() == 0)
			return null;
		else
			return detector.events.get(detector.events.size()-1);		
	}

	@Before
	public void setUp() throws NativeHookException{
		GlobalScreen.registerNativeHook();
		detector = new MouseEventDetector();		
		GlobalScreen.getInstance().addNativeMouseListener(detector);
		
		nullScreenRegion = new NullScreenRegion(new DesktopScreen(0));
	}

	@After
	public void tearDown(){
		GlobalScreen.getInstance().removeNativeMouseListener(detector);
		GlobalScreen.unregisterNativeHook();
	}

	@Test(expected = ActionRuntimeException.class)  
	public void testLeftClickActionOnNullScreenRegion() {
		Action action = new LeftClickAction(nullScreenRegion);
		action.perform();
	}

	@Test(expected = ActionRuntimeException.class)  
	public void testRightClickActionOnNullScreenRegion() {
		Action action = new RightClickAction(nullScreenRegion);
		action.perform();
	}
	
	@Test(expected = ActionRuntimeException.class)  
	public void testDoubleClickActionOnNullScreenRegion() {
		Action action = new DoubleClickAction(nullScreenRegion);
		action.perform();
	}
	
	@Test
	public void testLabelActionOnNullScreenRegion(){
		LabelAction labelAction = new LabelAction(nullScreenRegion);
		labelAction.setText("This is a test label");
		labelAction.setFontSize(15);
		labelAction.setDuration(1000);
		labelAction.perform();
	}
	
	
	
}