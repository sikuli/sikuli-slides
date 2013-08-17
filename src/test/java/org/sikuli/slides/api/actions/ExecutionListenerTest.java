package org.sikuli.slides.api.actions;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.ScreenRegionCanvas;
import org.sikuli.slides.api.Context;
import org.sikuli.slides.api.ExecutionEvent;
import org.sikuli.slides.api.ExecutionListener;
import org.sikuli.slides.api.listeners.ExecutionVisualizer;

public class ExecutionListenerTest {
	
	private Context context;
	private ExecutionListener executionListener;
	private boolean isBeforeExecutionInvoked;
	private boolean isAfterExecutionInvoked;

	@Before
	public void setUp(){
		context = new Context();
		isBeforeExecutionInvoked = false;
		isAfterExecutionInvoked = false;
		executionListener = new ExecutionListener(){
			@Override
			public void beforeExecution(ExecutionEvent event) {
				isBeforeExecutionInvoked = true;
			}

			@Override
			public void afterExecution(ExecutionEvent event) {
				isAfterExecutionInvoked = true;
			}			
		};
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
		assertThat(isBeforeExecutionInvoked, equalTo(true));
		assertThat(isAfterExecutionInvoked, equalTo(true));
	}
	
	@Test
	public void testRightClickActionCanInvokeExecutionListeners() throws ActionExecutionException{
		Action action = new RightClickAction();
		action.execute(context);
		assertThat(isBeforeExecutionInvoked, equalTo(true));
		assertThat(isAfterExecutionInvoked, equalTo(true));
	}
	
	@Test
	public void testDoubleClickActionCanInvokeExecutionListeners() throws ActionExecutionException{
		Action action = new DoubleClickAction();
		action.execute(context);
		assertThat(isBeforeExecutionInvoked, equalTo(true));
		assertThat(isAfterExecutionInvoked, equalTo(true));
	}
		
}
