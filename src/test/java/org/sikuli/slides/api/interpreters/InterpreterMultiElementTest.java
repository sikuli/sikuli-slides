package org.sikuli.slides.api.interpreters;


import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.sikuli.api.Target;
import org.sikuli.slides.api.RelativeScreenRegionTarget;
import org.sikuli.slides.api.actions.Action;
import org.sikuli.slides.api.actions.ActionExecutionException;
import org.sikuli.slides.api.actions.BrowserAction;
import org.sikuli.slides.api.actions.DoubleClickAction;
import org.sikuli.slides.api.actions.ExistAction;
import org.sikuli.slides.api.actions.LabelAction;
import org.sikuli.slides.api.actions.LeftClickAction;
import org.sikuli.slides.api.actions.NotExistAction;
import org.sikuli.slides.api.actions.ParallelAction;
import org.sikuli.slides.api.actions.RightClickAction;
import org.sikuli.slides.api.actions.TargetAction;
import org.sikuli.slides.api.actions.TypeAction;
import org.sikuli.slides.api.actions.DelayAction;
import org.sikuli.slides.api.actions.WaitAction;
import org.sikuli.slides.api.interpreters.DefaultInterpreter;
import org.sikuli.slides.api.interpreters.Interpreter;
import org.sikuli.slides.api.interpreters.Keyword;
import org.sikuli.slides.api.interpreters.KeywordDictionary;
import org.sikuli.slides.api.models.ImageElement;
import org.sikuli.slides.api.models.Slide;
import org.sikuli.slides.api.models.SlideElement;

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

		ParallelAction pa = (ParallelAction) interpreter.interpret(slide);
		assertThat(pa, notNullValue());		
		assertThat(pa.getActions().size(), equalTo(2));
	}
	
	@Test
	public void testClickAndTarget() {
		Slide slide = new Slide();
		slide.newElement().text("label1").add();

		slide.newKeywordElement().keyword(KeywordDictionary.CLICK).add();
		slide.newElement().bounds(100,100,50,50).text("label").add();
		slide.newImageElement().source(source).bounds(0,0,200,200).add();

		ParallelAction pa = (ParallelAction) interpreter.interpret(slide);
		assertThat(pa, notNullValue());
		assertThat(pa.getActions().size(), equalTo(2));
	}
	
}
