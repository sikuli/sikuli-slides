package org.sikuli.slides.api.interpreters;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import java.io.IOException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.sikuli.slides.api.actions.Action;
import org.sikuli.slides.api.actions.Actions;
import org.sikuli.slides.api.actions.LabelAction;
import org.sikuli.slides.api.actions.LeftClickAction;
import org.sikuli.slides.api.actions.ParallelAction;
import org.sikuli.slides.api.models.Slide;

public class InterpreterMultiElementTest {

	private DefaultInterpreter interpreter;
	private Slide slide;
	private URL source;

	@Before
	public void setUp() throws IOException{
		interpreter = new DefaultInterpreter();
		slide = new Slide();
		source = getClass().getResource("sikuli_context.png");
	}

	@Test
	public void testMultiLabels() {
		Slide slide = new Slide();
		slide.newElement().text("label1").add();
		slide.newElement().text("label2").add();

		Action action = interpreter.interpret(slide);
		assertThat(action, notNullValue());		
		assertThat(Actions.select(action).isInstaceOf(LabelAction.class).all().size(), equalTo(2));
	}
	
	@Test
	public void testClickAndTarget() {
		Slide slide = new Slide();
		slide.newElement().text("label1").add();

		slide.newKeywordElement().keyword(KeywordDictionary.CLICK).add();
		slide.newElement().bounds(100,100,50,50).text("label").add();
		slide.newImageElement().source(source).bounds(0,0,200,200).add();

		Action action = interpreter.interpret(slide);
		assertThat(action, notNullValue());
		assertThat(Actions.select(action).isInstaceOf(LabelAction.class).all().size(), equalTo(2));
		assertThat(Actions.select(action).isInstaceOf(LeftClickAction.class).all().size(), equalTo(1));

	}
	
}
