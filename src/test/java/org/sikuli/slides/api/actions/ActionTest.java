package org.sikuli.slides.api.actions;

import org.junit.Test;
import org.sikuli.slides.api.Context;
import org.sikuli.slides.api.actions.WaitAction;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ActionTest {
	
	@Test
	public void testWaitAction(){
		WaitAction waitAction = new WaitAction();
		waitAction.setDuration(1000);
		
		Context context = new Context();
				
		long startTime = System.currentTimeMillis();
		waitAction.execute(context);
		long elapsedTime = System.currentTimeMillis() - startTime;
		
		assertThat(elapsedTime,  greaterThanOrEqualTo(1000L));
		assertThat(elapsedTime,  lessThan(1100L));
		
		waitAction.setDuration(500);
		
		startTime = System.currentTimeMillis();
		waitAction.execute(context);
		elapsedTime = System.currentTimeMillis() - startTime;
		
		assertThat(elapsedTime,  greaterThanOrEqualTo(500L));
		assertThat(elapsedTime,  lessThan(600L));
	}
	

}
