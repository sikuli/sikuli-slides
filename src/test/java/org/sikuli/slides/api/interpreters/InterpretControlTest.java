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
import org.sikuli.slides.api.actions.BookmarkAction;
import org.sikuli.slides.api.actions.OptionalAction;
import org.sikuli.slides.api.actions.SkipAction;
import org.sikuli.slides.api.models.Slide;

public class InterpretControlTest {
	
	private DefaultInterpreter interpreter;
	private URL source;
	private Slide slide;

	
	@Before
	public void setUp() throws IOException{
		interpreter = new DefaultInterpreter();
		slide = new Slide();
		source = getClass().getResource("sikuli_context.png");
	}
	
	@Test
	public void testSkip(){
		slide.newKeywordElement().keyword(KeywordDictionary.SKIP).geom("hex").add();		
		Action action = interpreter.interpret(slide);
		assertThat(action, notNullValue());
		assertThat(Actions.select(action).isInstaceOf(SkipAction.class).all().size(), equalTo(1));
	}
	
	@Test
	public void testOptional(){
		slide.newKeywordElement().keyword(KeywordDictionary.OPTIONAL).geom("hex").add();		
		Action action = interpreter.interpret(slide);
		assertThat(action, notNullValue());
		assertThat(Actions.select(action).isInstaceOf(OptionalAction.class).all().size(), equalTo(1));
	}
	
	@Test
	public void testBookmark(){
		slide.newKeywordElement().keyword(KeywordDictionary.BOOKMARK).geom("hex").text("step5").add();		
		Action action = interpreter.interpret(slide);
		assertThat(action, notNullValue());
		assertThat(Actions.select(action).isInstaceOf(BookmarkAction.class).all().size(), equalTo(1));
		BookmarkAction bookmark = (BookmarkAction) Actions.select(action).isInstaceOf(BookmarkAction.class).first();
		assertThat(bookmark.getName(), equalTo("step5"));
	}

	
}
