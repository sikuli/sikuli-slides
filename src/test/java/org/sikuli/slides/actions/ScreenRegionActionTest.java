package org.sikuli.slides.actions;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.robot.desktop.DesktopScreen;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.DesktopCanvas;
import org.sikuli.recorder.detector.EventDetector;
import org.sikuli.slides.actions.Action;
import org.sikuli.slides.actions.DoubleClickAction;
import org.sikuli.slides.actions.LeftClickAction;
import org.sikuli.slides.actions.RightClickAction;
import org.sikuli.slides.api.ActionRuntimeException;
import org.sikuli.slides.sikuli.NullScreenRegion;

class MouseEventDetector extends EventDetector 
implements NativeMouseInputListener {

	List<NativeMouseEvent> events = new ArrayList<NativeMouseEvent>();

	public void nativeMouseClicked(NativeMouseEvent e) {
		System.out.println("Mosue Clicked: x = " + e.getX() + ", y = " + e.getY() + ", button =  " + e.getButton() + ", count = " + e.getClickCount());
		events.add(e);
	}

	public void nativeMousePressed(NativeMouseEvent e) {
		//System.out.println("Mosue Pressed: " + e.getButton());
	}

	public void nativeMouseReleased(NativeMouseEvent e) {
		//System.out.println("Mosue Released: " + e.getButton());
	}

	public void nativeMouseMoved(NativeMouseEvent e) {
		//System.out.println("Mosue Moved: " + e.getX() + ", " + e.getY());
	}

	public void nativeMouseDragged(NativeMouseEvent e) {
		//System.out.println("Mosue Dragged: " + e.getX() + ", " + e.getY());
	}

	public void start(){
		;
		GlobalScreen.getInstance().addNativeMouseMotionListener(this);
	}

	public void stop(){

		GlobalScreen.getInstance().removeNativeMouseMotionListener(this);
	}


}

public class ScreenRegionActionTest {

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

		assertNotNull("last mouse event", getLastMouseEvent());
		assertEquals("mouse button", MouseEvent.BUTTON1, getLastMouseEvent().getButton());
		assertEquals("click count", 1, getLastMouseEvent().getClickCount());		
		int x = getLastMouseEvent().getX();
		int y = getLastMouseEvent().getY();
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

		assertNotNull("last mouse event", getLastMouseEvent());
		assertEquals("mouse button", MouseEvent.BUTTON2, getLastMouseEvent().getButton());
		assertEquals("click count", 1, getLastMouseEvent().getClickCount());
		int x = getLastMouseEvent().getX();
		int y = getLastMouseEvent().getY();
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

		assertNotNull("last mouse event", getLastMouseEvent());
		assertEquals("mouse button", MouseEvent.BUTTON1, getLastMouseEvent().getButton());
		assertEquals("click count", 2, getLastMouseEvent().getClickCount());
		int x = getLastMouseEvent().getX();
		int y = getLastMouseEvent().getY();
		assertEquals("x", 350, x);
		assertEquals("y", 350, y);
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
