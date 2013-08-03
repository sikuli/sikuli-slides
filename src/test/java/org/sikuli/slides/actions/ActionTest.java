package org.sikuli.slides.actions;

import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ActionTest {
	
	@Test
	public void testWaitAction(){
		WaitAction waitAction = new WaitAction();
		waitAction.setDuration(1000);
				
		long startTime = System.currentTimeMillis();
		waitAction.perform();
		long elapsedTime = System.currentTimeMillis() - startTime;
		
		assertThat(elapsedTime,  greaterThanOrEqualTo(1000L));
		assertThat(elapsedTime,  lessThan(1100L));
		
		waitAction.setDuration(500);
		
		startTime = System.currentTimeMillis();
		waitAction.perform();
		elapsedTime = System.currentTimeMillis() - startTime;
		
		assertThat(elapsedTime,  greaterThanOrEqualTo(500L));
		assertThat(elapsedTime,  lessThan(600L));
	}

}
