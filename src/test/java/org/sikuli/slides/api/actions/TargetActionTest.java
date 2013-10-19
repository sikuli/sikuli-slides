package org.sikuli.slides.api.actions;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.event.MouseEvent;

import org.jnativehook.NativeHookException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.visual.DesktopCanvas;
import org.sikuli.slides.api.Context;
import org.sikuli.slides.api.mocks.AlwaysFoundTarget;


public class TargetActionTest {

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
	public void testCanLeftClickOnTarget() throws ActionExecutionException {
		Action action = new TargetAction(new AlwaysFoundTarget(), new LeftClickAction());
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
	public void testCanTypeOnTarget() throws ActionExecutionException{
		TypeAction typeAction = new TypeAction();
		typeAction.setText("abcde");		
		Action action = new TargetAction(new AlwaysFoundTarget(), typeAction);
		action.execute(context);

		assertNotNull("last mouse event", detector.getLastMouseEvent());
		assertEquals("mouse button", MouseEvent.BUTTON1, detector.getLastMouseEvent().getButton());
		assertEquals("click count", 1, detector.getLastMouseEvent().getClickCount());
		
		assertEquals("last key typed", 'e', detector.getLastKeyEvent().getKeyChar());
		assertEquals("num keys typed", 5, detector.getNumKeyEvents());
	}
	
}
