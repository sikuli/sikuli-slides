package org.sikuli.slides.driver;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.sikuli.slides.api.models.SlideEditor.on;

import org.junit.Test;
import org.sikuli.slides.api.TestResources;
import org.sikuli.slides.api.models.Slide;
import org.sikuli.slides.driver.DefaultUISpecInterpreter;

public class InterpreterTest {

	@Test
	public void three_targets(){
		Slide slide = new Slide();
		
		on(slide).insert().image().source(TestResources.get("sikuli_context.png")).bounds(100,100,1000,1000);
		
		on(slide).insert().element().text("button1").bounds(200,190,60,20);
		on(slide).insert().element().bounds(200,200,50,50);
		
		on(slide).insert().element().text("button2").bounds(300,190,60,20);
		on(slide).insert().element().bounds(300,200,50,50);

		on(slide).insert().element().text("button3").bounds(400,190,60,20);
		on(slide).insert().element().bounds(400,200,50,50);

		SlideSpecInterpreter interpreter = new DefaultUISpecInterpreter();
		SlideSpec page = interpreter.interpret(slide);
				
		assertThat(page.getElementCount(), equalTo(3));
		
		// every element has a target properly set
		assertThat(page.getElement(0).getTarget(), notNullValue());
		assertThat(page.getElement(0).getLabel(), equalTo("button1"));
		
		assertThat(page.getElement(1).getTarget(), notNullValue());
		assertThat(page.getElement(1).getLabel(), equalTo("button2"));
		
		assertThat(page.getElement(2).getTarget(), notNullValue());
		assertThat(page.getElement(2).getLabel(), equalTo("button3"));
		
		
//		assertThat(((TargetAction) action).getChild(), instanceOf(LeftClickAction.class));		
	}
}
