package org.sikuli.slides.actions;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.event.MouseEvent;
import java.io.IOException;

import org.jnativehook.NativeHookException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.robot.desktop.DesktopScreen;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.DesktopCanvas;
import org.sikuli.slides.actions.Action;
import org.sikuli.slides.actions.DoubleClickAction;
import org.sikuli.slides.actions.LeftClickAction;
import org.sikuli.slides.actions.RightClickAction;
import org.sikuli.slides.api.ActionRuntimeException;
import org.sikuli.slides.sikuli.NullScreenRegion;



public class ScreenRegionActionTest {


	private NullScreenRegion nullScreenRegion;
	private InputDetector detector;


	@Before
	public void setUp() throws NativeHookException{
		detector = new InputDetector();
		detector.start();
		nullScreenRegion = new NullScreenRegion(new DesktopScreen(0));
	}

	@After
	public void tearDown(){
		detector.stop();
	}

	@Test
	public void testLeftClickAction() throws IOException{
		Canvas canvas = new DesktopCanvas();		
		ScreenRegion screenRegion = new DesktopScreenRegion(100,100,500,500);
		Action action = new LeftClickAction(screenRegion);
		canvas.addLabel(screenRegion, "left click here")
		.withHorizontalAlignmentCenter().withVerticalAlignmentMiddle();;
		canvas.addBox(screenRegion);
		canvas.show();
		action.perform();
		canvas.hide();

		assertNotNull("last mouse event", detector.getLastMouseEvent());
		assertEquals("mouse button", MouseEvent.BUTTON1, detector.getLastMouseEvent().getButton());
		assertEquals("click count", 1, detector.getLastMouseEvent().getClickCount());		
		int x = detector.getLastMouseEvent().getX();
		int y = detector.getLastMouseEvent().getY();
		assertEquals("x", 350, x);
		assertEquals("y", 350, y);
	}

	@Test
	public void testRightClickAction() throws IOException{
		Canvas canvas = new DesktopCanvas();		
		ScreenRegion screenRegion = new DesktopScreenRegion(100,100,500,500);		
		Action action = new RightClickAction(screenRegion);
		canvas.addLabel(screenRegion, "right click here")
		.withHorizontalAlignmentCenter().withVerticalAlignmentMiddle();;
		canvas.addBox(screenRegion);		
		canvas.show();
		action.perform();
		canvas.hide();

		assertNotNull("last mouse event", detector.getLastMouseEvent());
		assertEquals("mouse button", MouseEvent.BUTTON2, detector.getLastMouseEvent().getButton());
		assertEquals("click count", 1, detector.getLastMouseEvent().getClickCount());
		int x = detector.getLastMouseEvent().getX();
		int y = detector.getLastMouseEvent().getY();
		assertEquals("x", 350, x);
		assertEquals("y", 350, y);
	}

	@Test
	public void testDoubleClickAction() throws IOException{
		Canvas canvas = new DesktopCanvas();		
		ScreenRegion screenRegion = new DesktopScreenRegion(100,100,500,500);
		Action action = new DoubleClickAction(screenRegion);
		canvas.addLabel(screenRegion, "double click here")
		.withHorizontalAlignmentCenter().withVerticalAlignmentMiddle();
		canvas.addBox(screenRegion);		
		canvas.show();
		action.perform();
		canvas.hide();	

		assertNotNull("last mouse event", detector.getLastMouseEvent());
		assertEquals("mouse button", MouseEvent.BUTTON1, detector.getLastMouseEvent().getButton());
		assertEquals("click count", 2, detector.getLastMouseEvent().getClickCount());
		int x = detector.getLastMouseEvent().getX();
		int y = detector.getLastMouseEvent().getY();
		assertEquals("x", 350, x);
		assertEquals("y", 350, y);
	}
	
	@Test
	public void testTypeAction(){
		Canvas canvas = new DesktopCanvas();		
		ScreenRegion screenRegion = new DesktopScreenRegion(100,100,500,500);
		TypeAction action = new TypeAction(screenRegion);
		action.setText("abcde");
		canvas.addLabel(screenRegion, "type here")
		.withHorizontalAlignmentCenter().withVerticalAlignmentMiddle();
		canvas.addBox(screenRegion);		
		canvas.show();
		action.perform();
		canvas.hide();	

		assertNotNull("last mouse event", detector.getLastMouseEvent());
		assertEquals("mouse button", MouseEvent.BUTTON1, detector.getLastMouseEvent().getButton());
		assertEquals("click count", 1, detector.getLastMouseEvent().getClickCount());
		
		assertEquals("last key typed", 'e', detector.getLastKeyEvent().getKeyChar());
		assertEquals("num keys typed", 5, detector.getNumKeyEvents());
	}
	
	@Test
	public void testLabelAction(){
		ScreenRegion screenRegion = new DesktopScreenRegion(100,100,500,200);
		LabelAction labelAction = new LabelAction(screenRegion);
		labelAction.setText("This is a test label");
		labelAction.setFontSize(15);
		labelAction.setDuration(1000);
		labelAction.perform();
	}	
	
	
	@Test
	public void testExistAction() {		
		ScreenRegion screenRegion = new DesktopScreenRegion(100,100,500,500);		
		Action action = new ExistAction(screenRegion);
		action.perform();
	}
	
	@Test(expected = ActionRuntimeException.class)
	public void testExistActionWithNullScreenRegion() {		
		Action action = new ExistAction(nullScreenRegion);
		action.perform();
	}
	
	
	@Test(expected = ActionRuntimeException.class)
	public void testNotExistAction() {	
		ScreenRegion screenRegion = new DesktopScreenRegion(100,100,500,500);		
		Action action = new NotExistAction(screenRegion);
		action.perform();
	}
	
	@Test	
	public void testNotExistActionWithNullScreenRegion() {				
		Action action = new NotExistAction(nullScreenRegion);
		action.perform();
	}
}
