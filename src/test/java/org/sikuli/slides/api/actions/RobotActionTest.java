package org.sikuli.slides.api.actions;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.event.MouseEvent;
import java.io.IOException;

import org.jnativehook.NativeHookException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.visual.DesktopCanvas;
import org.sikuli.slides.api.Context;
import org.sikuli.slides.api.mocks.AlwaysFoundTarget;


public class RobotActionTest {

	private InputDetector detector;
	private Context context;
	private DesktopCanvas canvas;
	private DesktopScreenRegion screenRegion;

	@Before
	public void setUp() throws NativeHookException{
		detector = new InputDetector();
		detector.start();
		screenRegion = new DesktopScreenRegion(100,100,500,500);

		context = new Context();
		context.setScreenRegion(screenRegion);
		
		canvas = new DesktopCanvas();		
		canvas.addLabel(screenRegion, "here")
		.withHorizontalAlignmentCenter().withVerticalAlignmentMiddle();;
		canvas.addBox(screenRegion);
		canvas.show();

	}

	@After
	public void tearDown(){
		detector.stop();
		canvas.hide();
	}

	@Test
	public void testLeftClickAction() throws IOException, ActionExecutionException{
		Action action = new LeftClickAction();
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
	public void testRightClickAction() throws IOException, ActionExecutionException{
		Action action = new RightClickAction();
		action.execute(context);

		assertNotNull("last mouse event", detector.getLastMouseEvent());
		assertEquals("mouse button", MouseEvent.BUTTON2, detector.getLastMouseEvent().getButton());
		assertEquals("click count", 1, detector.getLastMouseEvent().getClickCount());
	}

	@Test
	public void testDoubleClickAction() throws IOException, ActionExecutionException{
		Action action = new DoubleClickAction();
		action.execute(context);
	
		assertNotNull("last mouse event", detector.getLastMouseEvent());
		assertEquals("mouse button", MouseEvent.BUTTON1, detector.getLastMouseEvent().getButton());
		assertEquals("click count", 2, detector.getLastMouseEvent().getClickCount());
	}
	
}
