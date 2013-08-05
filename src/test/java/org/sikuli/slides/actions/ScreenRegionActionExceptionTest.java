package org.sikuli.slides.actions;


import org.jnativehook.NativeHookException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sikuli.api.robot.desktop.DesktopScreen;
import org.sikuli.slides.api.Context;
import org.sikuli.slides.sikuli.NullScreenRegion;

public class ScreenRegionActionExceptionTest {

	private NullScreenRegion nullScreenRegion;
	private Context context;	

	@Before
	public void setUp() throws NativeHookException{
		nullScreenRegion = new NullScreenRegion(new DesktopScreen(0));
		context = new Context();
	}

	@After
	public void tearDown(){
	}

	@Test(expected = ActionExecutionException.class)  
	public void testLeftClickActionOnNullScreenRegion() throws ActionExecutionException {
		Action action = new LeftClickAction(nullScreenRegion);
		action.execute(context);
	}

	@Test(expected = ActionExecutionException.class)  
	public void testRightClickActionOnNullScreenRegion() throws ActionExecutionException {
		Action action = new RightClickAction(nullScreenRegion);
		action.execute(context);
	}
	
	@Test(expected = ActionExecutionException.class)  
	public void testDoubleClickActionOnNullScreenRegion() throws ActionExecutionException {
		Action action = new DoubleClickAction(nullScreenRegion);
		action.execute(context);
	}
	
	@Test
	public void testLabelActionOnNullScreenRegion(){
		LabelAction labelAction = new LabelAction(nullScreenRegion);
		labelAction.setText("This is a test label");
		labelAction.setFontSize(15);
		labelAction.setDuration(1000);
		labelAction.execute(context);
	}
	
	
	
}
