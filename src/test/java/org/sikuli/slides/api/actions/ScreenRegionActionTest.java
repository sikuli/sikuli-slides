package org.sikuli.slides.api.actions;



import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.jnativehook.NativeHookException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.visual.DesktopCanvas;
import org.sikuli.slides.api.Context;
import org.sikuli.slides.api.actions.Action;
import org.sikuli.slides.api.actions.ActionExecutionException;
import org.sikuli.slides.api.actions.DoubleClickAction;
import org.sikuli.slides.api.actions.ExistAction;
import org.sikuli.slides.api.actions.LabelAction;
import org.sikuli.slides.api.actions.LeftClickAction;
import org.sikuli.slides.api.actions.NotExistAction;
import org.sikuli.slides.api.actions.RightClickAction;
import org.sikuli.slides.api.actions.TargetAction;
import org.sikuli.slides.api.actions.TypeAction;
import org.sikuli.slides.api.mocks.AppearLaterTarget;
import org.sikuli.slides.api.mocks.AlwaysFoundTarget;
import org.sikuli.slides.api.mocks.NeverFoundTarget;

import com.google.common.base.Stopwatch;


public class ScreenRegionActionTest {

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
	public void testFindDoLeftClick() throws ActionExecutionException {
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
	
	@Test
	public void testTypeOnTarget() throws ActionExecutionException{
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
	
	@Test
	public void testLabelAction(){
		LabelAction labelAction = new LabelAction();
		labelAction.setText("This is a test label");
		labelAction.setFontSize(15);
		labelAction.setDuration(1000);
		labelAction.execute(context);
	}
	
	
	@Test
	public void testWaitAction() throws ActionExecutionException {	
		Stopwatch stopwatch = new Stopwatch().start();
		Action action = new WaitAction(new AppearLaterTarget(5000));
		action.execute(context);
		stopwatch.stop();
		long actualWaitTime = stopwatch.elapsed(TimeUnit.MILLISECONDS);
		assertThat(actualWaitTime, greaterThan(4000L));
		assertThat(actualWaitTime, lessThan(6000L));
	}
	
	@Test
	public void testExistAction() throws ActionExecutionException {	
		Action action = new ExistAction(new AlwaysFoundTarget());
		action.execute(context);
	}
	
	@Test(expected = ActionExecutionException.class)
	public void testExistActionFailed() throws ActionExecutionException {		
		Action action = new ExistAction(new NeverFoundTarget());
		action.execute(context);
	}
	
	@Test(expected = ActionExecutionException.class)
	public void testNotExistActionFailed() throws ActionExecutionException {			
		Action action = new NotExistAction(new AlwaysFoundTarget());
		action.execute(context);
	}
	
	@Test	
	public void testNotExistAction() throws ActionExecutionException {				
		Action action = new NotExistAction(new NeverFoundTarget());
		action.execute(context);
	}
}
