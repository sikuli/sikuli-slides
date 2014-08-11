package org.sikuli.slides.api.actions;

import org.junit.Before;
import org.junit.Test;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.slides.api.Context;
import org.sikuli.slides.api.ExecutionEvent;
import org.sikuli.slides.api.ExecutionListener;
import org.sikuli.slides.api.listeners.ExecutionVisualizer;
import static org.mockito.Mockito.*;

public class ExecutionListenerTest {
	
	private Context context;
	private ExecutionListener executionListener;

	@Before
	public void setUp(){
		context = new Context();
		executionListener = mock(ExecutionListener.class);
		context.setExecutionListener(executionListener);
	}
	
	@Test
	public void testExecutionListenerCanShowLeftClickLocation() throws ActionExecutionException{
		Action action = new LeftClickAction();
		context.setScreenRegion(new DesktopScreenRegion(500,500,100,100));		
		context.setExecutionListener(new ExecutionVisualizer());
		action.execute(context);
	}

	
	@Test
	public void testLeftClickActionCanInvokeExecutionListeners() throws ActionExecutionException{
		Action action = new LeftClickAction();
		action.execute(context);
		verify(executionListener).beforeExecution(any(ExecutionEvent.class));
		verify(executionListener).afterExecution(any(ExecutionEvent.class));
	}
	
	@Test
	public void testRightClickActionCanInvokeExecutionListeners() throws ActionExecutionException{
		Action action = new RightClickAction();
		action.execute(context);
		verify(executionListener).beforeExecution(any(ExecutionEvent.class));
		verify(executionListener).afterExecution(any(ExecutionEvent.class));
	}
	
	@Test
	public void testDoubleClickActionCanInvokeExecutionListeners() throws ActionExecutionException{
		Action action = new DoubleClickAction();
		action.execute(context);
		verify(executionListener).beforeExecution(any(ExecutionEvent.class));
		verify(executionListener).afterExecution(any(ExecutionEvent.class));
	}
		
}
