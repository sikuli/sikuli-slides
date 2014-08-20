package org.sikuli.slides.api.actions;

import org.jnativehook.NativeHookException;
import org.junit.Before;
import org.junit.Test;
import org.sikuli.api.Target;
import org.sikuli.slides.api.Context;
import org.sikuli.slides.api.mocks.MockTargetFactory;

public class AssertActionTest {
	private Context context;
	@Before
	public void setUp() throws NativeHookException{
		context = new Context();
	}

	@Test
	public void testExistAction() throws ActionExecutionException {	
		Target target = MockTargetFactory.canBeFound();
		Action action = new AssertExistAction(target);
		action.execute(context);
	}
	
	@Test(expected = ActionExecutionException.class)
	public void testExistActionFailed() throws ActionExecutionException {	
		Target target = MockTargetFactory.canNotBeFound();
		Action action = new AssertExistAction(target);
		action.execute(context);
	}
	
	@Test(expected = ActionExecutionException.class)
	public void testNotExistActionFailed() throws ActionExecutionException {
		Target target = MockTargetFactory.canBeFound();
		Action action = new AssertNotExistAction(target);
		action.execute(context);
	}
	
	@Test
	public void testNotExistAction() throws ActionExecutionException {			
		Target target = MockTargetFactory.canNotBeFound();
		Action action = new AssertNotExistAction(target);
		action.execute(context);
	}

}
