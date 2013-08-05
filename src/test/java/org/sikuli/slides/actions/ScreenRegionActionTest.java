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
import org.sikuli.api.Target;
import org.sikuli.api.robot.desktop.DesktopScreen;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.DesktopCanvas;
import org.sikuli.slides.actions.Action;
import org.sikuli.slides.actions.DoubleClickAction;
import org.sikuli.slides.actions.LeftClickAction;
import org.sikuli.slides.actions.RightClickAction;
import org.sikuli.slides.api.Context;
import org.sikuli.slides.mocks.IdentiyScreenRegionTarget;
import org.sikuli.slides.mocks.NotFoundScreenTarget;
import org.sikuli.slides.sikuli.NullScreenRegion;




public class ScreenRegionActionTest {


	private NullScreenRegion nullScreenRegion;
	private InputDetector detector;
	private Context context;
	private DesktopCanvas canvas;
	private DesktopScreenRegion screenRegion;


	@Before
	public void setUp() throws NativeHookException{
		detector = new InputDetector();
		detector.start();
		nullScreenRegion = new NullScreenRegion(new DesktopScreen(0));
		screenRegion = new DesktopScreenRegion(100,100,500,500);

		context = new Context();
		context.setScreenRegion(screenRegion);
		
		canvas = new DesktopCanvas();		
		canvas.addLabel(screenRegion, "here")
		.withHorizontalAlignmentCenter().withVerticalAlignmentMiddle();;
		canvas.addBox(screenRegion);
	}

	@After
	public void tearDown(){
		detector.stop();
	}

	@Test
	public void testFindDoLeftClick() throws ActionExecutionException {
		canvas.show();
		Action action = new FindDoAction(new IdentiyScreenRegionTarget(), new LeftClickAction());
		action.execute(context);
		
		assertNotNull("last mouse event", detector.getLastMouseEvent());
		assertEquals("mouse button", MouseEvent.BUTTON1, detector.getLastMouseEvent().getButton());
		assertEquals("click count", 1, detector.getLastMouseEvent().getClickCount());		
		int x = detector.getLastMouseEvent().getX();
		int y = detector.getLastMouseEvent().getY();
		assertEquals("x", screenRegion.getCenter().getX(), x);
		assertEquals("y", screenRegion.getCenter().getY(), y);

	}
	
	@Test
	public void testLeftClickAction() throws IOException, ActionExecutionException{
		TargetScreenRegionAction action = new LeftClickAction();
		action.execute(context, screenRegion);
		
		int x = detector.getLastMouseEvent().getX();
		int y = detector.getLastMouseEvent().getY();
		assertEquals("x", screenRegion.getCenter().getX(), x);
		assertEquals("y", screenRegion.getCenter().getY(), y);	}

	@Test
	public void testRightClickAction() throws IOException, ActionExecutionException{
		Canvas canvas = new DesktopCanvas();		
		ScreenRegion screenRegion = new DesktopScreenRegion(100,100,500,500);		
		Action action = new RightClickAction(screenRegion);
		canvas.addLabel(screenRegion, "right click here")
		.withHorizontalAlignmentCenter().withVerticalAlignmentMiddle();;
		canvas.addBox(screenRegion);		
		canvas.show();
		action.execute(null);
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
	public void testDoubleClickAction() throws IOException, ActionExecutionException{
		Canvas canvas = new DesktopCanvas();		
		ScreenRegion screenRegion = new DesktopScreenRegion(100,100,500,500);
		Action action = new DoubleClickAction(screenRegion);
		canvas.addLabel(screenRegion, "double click here")
		.withHorizontalAlignmentCenter().withVerticalAlignmentMiddle();
		canvas.addBox(screenRegion);		
		canvas.show();
		action.execute(null);
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
	public void testTypeAction() throws ActionExecutionException{
		Canvas canvas = new DesktopCanvas();		
		ScreenRegion screenRegion = new DesktopScreenRegion(100,100,500,500);
		TypeAction action = new TypeAction(screenRegion);
		action.setText("abcde");
		canvas.addLabel(screenRegion, "type here")
		.withHorizontalAlignmentCenter().withVerticalAlignmentMiddle();
		canvas.addBox(screenRegion);		
		canvas.show();
		action.execute(context);
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
		labelAction.execute(context);
	}	
	
	@Test
	public void testExistAction() throws ActionExecutionException {	
		Action action = new ExistAction(new IdentiyScreenRegionTarget());
		action.execute(context);
	}
	
	@Test(expected = ActionExecutionException.class)
	public void testExistActionFailed() throws ActionExecutionException {		
		Action action = new ExistAction(new NotFoundScreenTarget());
		action.execute(context);
	}
	
	@Test(expected = ActionExecutionException.class)
	public void testNotExistActionFailed() throws ActionExecutionException {			
		Action action = new NotExistAction(new IdentiyScreenRegionTarget());
		action.execute(context);
	}
	
	@Test	
	public void testNotExistAction() throws ActionExecutionException {				
		Action action = new NotExistAction(new NotFoundScreenTarget());
		action.execute(context);
	}
}
