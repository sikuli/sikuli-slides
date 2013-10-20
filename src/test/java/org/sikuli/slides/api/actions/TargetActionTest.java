package org.sikuli.slides.api.actions;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.event.MouseEvent;

import org.jnativehook.NativeHookException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.api.visual.DesktopCanvas;
import org.sikuli.slides.api.Context;
import org.sikuli.slides.api.mocks.AlwaysFoundTarget;
import org.sikuli.slides.api.mocks.MockTargetFactory;

import static org.mockito.Mockito.*;

public class TargetActionTest {

	private Context context;

	@Before
	public void setUp() throws NativeHookException{
		context = new Context();
	}

	@Test
	public void testTargetIsFoundImmediately() throws ActionExecutionException {
				
		Action childAction = mock(Action.class);
		Target target = MockTargetFactory.canBeFound();
		Action action = new TargetAction(target, childAction);
		action.execute(context);
				
		verify(childAction).execute(any(Context.class));
	}
	
	@Test(expected = ActionExecutionException.class)
	public void testTargetIsNotFoundImmediately() throws ActionExecutionException {
				
		Action childAction = mock(Action.class);
		Target target = MockTargetFactory.canNotBeFound();
		Action action = new TargetAction(target, childAction);
		action.execute(context);
				
		verify(childAction, never()).execute(any(Context.class));
	}
	
	

}
