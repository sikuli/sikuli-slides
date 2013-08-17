package org.sikuli.slides.api;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;

import org.junit.Test;
import org.sikuli.slides.api.actions.LabelAction;

public class ContextTest {
	
	@Test
	public void testRenderingParameterizedText(){
		Context context = new Context();
		LabelAction labelAction = new LabelAction();
		labelAction.setText("Hello <name>!");
		context.addParameter("name", "John");
		String renderedText = context.render(labelAction.getText());
		assertThat(renderedText, equalToIgnoringCase("Hello John!"));
	}
		
}
