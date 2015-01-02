package org.sikuli.slides.api.interpreters;


import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.sikuli.slides.api.models.SlideEditor.on;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.sikuli.api.Target;
import org.sikuli.slides.api.TestResources;
import org.sikuli.slides.api.actions.Action;
import org.sikuli.slides.api.actions.Actions;
import org.sikuli.slides.api.actions.BookmarkAction;
import org.sikuli.slides.api.actions.BrowserAction;
import org.sikuli.slides.api.actions.DoubleClickAction;
import org.sikuli.slides.api.actions.AssertExistAction;
import org.sikuli.slides.api.actions.EmptyAction;
import org.sikuli.slides.api.actions.LabelAction;
import org.sikuli.slides.api.actions.LeftClickAction;
import org.sikuli.slides.api.actions.AssertNotExistAction;
import org.sikuli.slides.api.actions.PauseAction;
import org.sikuli.slides.api.actions.RelativeAction;
import org.sikuli.slides.api.actions.RightClickAction;
import org.sikuli.slides.api.actions.SleepAction;
import org.sikuli.slides.api.actions.TargetAction;
import org.sikuli.slides.api.actions.TypeAction;
import org.sikuli.slides.api.actions.WaitAction;
import org.sikuli.slides.api.interpreters.DefaultInterpreter;
import org.sikuli.slides.api.interpreters.DefaultInterpreter.ScreenLocationInterpreter;
import org.sikuli.slides.api.interpreters.DefaultInterpreter.SleepActionInterpreter;
import org.sikuli.slides.api.interpreters.Interpreter;
import org.sikuli.slides.api.interpreters.Keyword;
import org.sikuli.slides.api.interpreters.KeywordDictionary;
import org.sikuli.slides.api.models.ImageElement;
import org.sikuli.slides.api.models.Slide;
import org.sikuli.slides.api.models.SlideElement;
import org.sikuli.slides.api.sikuli.ContextImageTarget;

public class InterpreterTest {
	private Interpreter interpreter;
	
	@Test
	public void browse_http(){
		Slide slide = new Slide();
		on(slide).insert().element().text("browse http://slides.sikuli.org");
		
		Interpreter interpreter = new DefaultInterpreter.BrowseActionInterpreter(null);
		Action action = interpreter.interpret(slide);
		
		assertThat(action, instanceOf(BrowserAction.class));
		assertThat(((BrowserAction) action).getUrl().toString(), equalToIgnoringCase("http://slides.sikuli.org"));		
	}
	
	@Test
	public void open_http(){
		Slide slide = new Slide();
		on(slide).insert().element().text("open  http://slides.sikuli.org");
		
		Interpreter interpreter = new DefaultInterpreter.BrowseActionInterpreter(null);
		Action action = interpreter.interpret(slide);
		
		assertThat(action, instanceOf(BrowserAction.class));
		assertThat(((BrowserAction) action).getUrl().toString(), equalToIgnoringCase("http://slides.sikuli.org"));		
	}
	
	@Test
	public void browse(){
		Slide slide = new Slide();
		on(slide).insert().element().text("browse");
		
		Interpreter interpreter = new DefaultInterpreter.BrowseActionInterpreter(null);
		Action action = interpreter.interpret(slide);
		
		assertThat(action, nullValue());		
	}
	
	@Test
	public void browse_garbage(){
		Slide slide = new Slide();
		on(slide).insert().element().text("browse  garbage");
		
		Interpreter interpreter = new DefaultInterpreter.BrowseActionInterpreter(null);
		Action action = interpreter.interpret(slide);
		
		assertThat(action, nullValue());		
	}
	
	@Test
	public void click_on_target(){
		Slide slide = new Slide();
		on(slide).insert().element().text("Click");
		on(slide).insert().image().source(TestResources.get("sikuli_context.png")).bounds(100,100,1000,1000);
		on(slide).insert().element().bounds(348,223,200,200);

		Interpreter interpreter = new DefaultInterpreter.TargetActionInterpreter();
		Action action = interpreter.interpret(slide);
		
		assertThat(action, instanceOf(TargetAction.class));
		assertThat(((TargetAction) action).getChild(), instanceOf(LeftClickAction.class));		
	}
	
	@Test
	public void wait_3_for_target(){
		Slide slide = new Slide();
		on(slide).insert().element().text("Wait    3");
		on(slide).insert().image().source(TestResources.get("sikuli_context.png")).bounds(100,100,1000,1000);
		on(slide).insert().element().bounds(348,223,200,200);

		Interpreter interpreter = new DefaultInterpreter.WaitActionInterpreter();
		Action action = interpreter.interpret(slide);
		
		assertThat(action, instanceOf(WaitAction.class));
		assertThat(((WaitAction)action).getDuration(), equalTo(3000L));		
	}
	
	@Test
	public void wait_forever_for_target(){
		Slide slide = new Slide();
		on(slide).insert().element().text("Wait");
		on(slide).insert().image().source(TestResources.get("sikuli_context.png")).bounds(100,100,1000,1000);
		on(slide).insert().element().bounds(348,223,200,200);

		Interpreter interpreter = new DefaultInterpreter.WaitActionInterpreter();
		Action action = interpreter.interpret(slide);
		
		assertThat(action, instanceOf(WaitAction.class));
		assertThat(((WaitAction)action).getDuration(), equalTo(Long.MAX_VALUE));		
	}	
	
	@Test
	public void ContextImageTarget(){
		Slide slide = new Slide();
		on(slide).insert().image().source(TestResources.get("sikuli_context.png")).bounds(100,100,1000,1000);
		on(slide).insert().element().bounds(348,223,200,200);

		TargetInterpreter interpreter = new DefaultInterpreter.ContextImageTargetInterpreter();
		Target target = interpreter.interpret(slide);
		
		assertThat(target, instanceOf(ContextImageTarget.class));		
	}
	
	@Test
	public void click(){
		Slide slide = new Slide();
		on(slide).insert().element().text("Click");		
		interpreter = new DefaultInterpreter.LeftClickActionInterpreter();
		Action action = interpreter.interpret(slide);
		assertThat(action, instanceOf(LeftClickAction.class));
		assertThat(action, notNullValue());		
	}
	
	@Test
	public void doubleclick(){
		Slide slide = new Slide();
		on(slide).insert().element().text("DoubleClick");		
		interpreter = new DefaultInterpreter.DoubleClickActionInterpreter();
		Action action = interpreter.interpret(slide);	
		assertThat(action, instanceOf(DoubleClickAction.class));
		assertThat(action, notNullValue());		
	}
	
	@Test
	public void rightclick(){
		Slide slide = new Slide();
		on(slide).insert().element().text("RightClick");		
		interpreter = new DefaultInterpreter.RightClickActionInterpreter();
		Action action = interpreter.interpret(slide);	
		assertThat(action, instanceOf(RightClickAction.class));
		assertThat(action, notNullValue());		
	}


	@Test
	public void sleep_2(){
		Slide slide = new Slide();
		on(slide).insert().element().text("Sleep 2");
		
		
		interpreter = new DefaultInterpreter.SleepActionInterpreter();  
		
		SleepAction action = (SleepAction) interpreter.interpret(slide);		
		assertThat(action, notNullValue());
		assertThat(action.getDuration(), equalTo(2000L));
	}
	
	@Test
	public void sleep_20(){
		Slide slide = new Slide();
		on(slide).insert().element().text("Sleep 20");
		
		interpreter = new DefaultInterpreter.SleepActionInterpreter();  
		
		SleepAction action = (SleepAction) interpreter.interpret(slide);		
		assertThat(action, notNullValue());
		assertThat(action.getDuration(), equalTo(20000L));
	}
	
	@Test
	public void sleep(){
		Slide slide = new Slide();
		on(slide).insert().element().text("Sleep");
		
		interpreter = new DefaultInterpreter.SleepActionInterpreter();  
		
		SleepAction action = (SleepAction) interpreter.interpret(slide);		
		assertThat(action, nullValue());
	}
	
	@Test
	public void exist(){
		Slide slide = new Slide();
		on(slide).insert().element().text("Exist");
		on(slide).insert().image().source(TestResources.get("sikuli_context.png")).bounds(100,100,1000,1000);
		on(slide).insert().element().bounds(348,223,200,200);

		
		interpreter = new DefaultInterpreter.ExistActionInterpreter();  
		
		Action action = interpreter.interpret(slide);	
		assertThat(action, instanceOf(AssertExistAction.class));
	}
	
	@Test
	public void not_exist(){
		Slide slide = new Slide();
		on(slide).insert().element().text("not exist");
		on(slide).insert().image().source(TestResources.get("sikuli_context.png")).bounds(100,100,1000,1000);
		on(slide).insert().element().bounds(348,223,200,200);

		
		interpreter = new DefaultInterpreter.NotExistActionInterpreter();  
		
		Action action = interpreter.interpret(slide);	
		assertThat(action, instanceOf(AssertNotExistAction.class));
	}
	
	@Test
	public void skip(){
		Slide slide = new Slide();
		on(slide).insert().element().text("skip");
		
		interpreter = new DefaultInterpreter.SkipActionInterpreter();  
		
		Action action = interpreter.interpret(slide);	
		assertThat(action, instanceOf(EmptyAction.class));
	}
	
	@Test
	public void pause(){
		Slide slide = new Slide();
		on(slide).insert().element().text("pause");
		
		interpreter = new DefaultInterpreter.PauseActionInterpreter();  
		
		Action action = interpreter.interpret(slide);	
		assertThat(action, instanceOf(PauseAction.class));
	}
	
	@Test
	public void bookmark_001(){
		Slide slide = new Slide();
		on(slide).insert().element().text("bookmark  001");
		
		interpreter = new DefaultInterpreter.BookmarkActionInterpreter();  
		
		Action action = interpreter.interpret(slide);	
		assertThat(action, instanceOf(BookmarkAction.class));
		assertThat(((BookmarkAction) action).getName(), equalTo("001"));
	}
	
	@Test
	public void bookmark_abc(){
		Slide slide = new Slide();
		on(slide).insert().element().text("bookmark abc");

		interpreter = new DefaultInterpreter.BookmarkActionInterpreter();  
		
		Action action = interpreter.interpret(slide);
		assertThat(((BookmarkAction) action).getName(), equalTo("abc"));	
	}
	
	@Test
	public void bookmark(){
		Slide slide = new Slide();
		on(slide).insert().element().text("bookmark");

		interpreter = new DefaultInterpreter.BookmarkActionInterpreter();  
		
		Action action = interpreter.interpret(slide);
		assertThat(action, nullValue());	
	}

	
	@Test
	public void type_something(){
		Slide slide = new Slide();
		on(slide).insert().element().text("type something");
		
		interpreter = new DefaultInterpreter.TypeActionInterpreter();  
		
		Action action = interpreter.interpret(slide);	
		assertThat(action, instanceOf(TypeAction.class));
		assertThat(((TypeAction) action).getText(), equalTo("something"));		
	}
	
	@Test
	public void type_two_words(){
		Slide slide = new Slide();
		on(slide).insert().element().text("type two words");
		
		interpreter = new DefaultInterpreter.TypeActionInterpreter();  
		
		Action action = interpreter.interpret(slide);	
		assertThat(action, instanceOf(TypeAction.class));
		assertThat(((TypeAction) action).getText(), equalTo("two words"));		
	}
	
	@Test
	public void label_single(){
		Slide slide = new Slide();
		on(slide).insert().element().text("this is a label");
		
		interpreter = new DefaultInterpreter.LabelInterpreter();  
		
		Action action = interpreter.interpret(slide);
		assertThat(action, instanceOf(LabelAction.class));
		assertThat(((LabelAction) action).getText(), equalTo("this is a label"));
		
	}
	
	@Test
	public void label_bounds(){
		Slide slide = new Slide();
		on(slide).insert().element().text("this is a label").bounds(0,0,4572000,3429000);
		
		interpreter = new DefaultInterpreter.LabelInterpreter();  
		
		Action action = interpreter.interpret(slide);
		assertThat(action, instanceOf(LabelAction.class));	
		assertThat(((LabelAction) action).getSpatialRelationship(), notNullValue());
	}
	
	@Test
	public void label_two(){
		Slide slide = new Slide();
		on(slide).insert().element().text("this is the first label");
		on(slide).insert().element().text("this is the second label");
		
		interpreter = new DefaultInterpreter.LabelInterpreter();  
		
		Action action1 = interpreter.interpret(slide);
		Action action2 = interpreter.interpret(slide);
		Action action3 = interpreter.interpret(slide);
		
		assertThat(action1, instanceOf(LabelAction.class));
		assertThat(action2, instanceOf(LabelAction.class));
		assertThat(action3, nullValue());
		
		assertThat(((LabelAction) action1).getText(), equalTo("this is the first label"));
		assertThat(((LabelAction) action2).getText(), equalTo("this is the second label"));		
	}
	
	
//	@Test
//	public void InterpretDelayAction() {
//		Slide slide = createDelaySlide("2 seconds");				
//		Action action = interpreter.interpret(slide);		
//		assertThat(Actions.select(action).isInstanceOf(SleepAction.class).all().size(), equalTo(1));
//		SleepAction delayAction = (SleepAction) Actions.select(action).isInstanceOf(SleepAction.class).first();
//		assertEquals(2000, delayAction.getDuration());
//
//
//		slide = createDelaySlide("2");
//		action = interpreter.interpret(slide);		
//		assertThat(Actions.select(action).isInstanceOf(SleepAction.class).all().size(), equalTo(1));
//		delayAction = (SleepAction) Actions.select(action).isInstanceOf(SleepAction.class).first();
//		assertEquals(2000, delayAction.getDuration());
//		
//		slide = createDelaySlide("1 minute");		
//		action = interpreter.interpret(slide);		
//		assertThat(Actions.select(action).isInstanceOf(SleepAction.class).all().size(), equalTo(1));
//		delayAction = (SleepAction) Actions.select(action).isInstanceOf(SleepAction.class).first();
//		assertEquals(1000 * 60, delayAction.getDuration());
//
//		slide = createDelaySlide("0.5 second");		
//		action = interpreter.interpret(slide);		
//		assertThat(Actions.select(action).isInstanceOf(SleepAction.class).all().size(), equalTo(1));
//		delayAction = (SleepAction) Actions.select(action).isInstanceOf(SleepAction.class).first();
//		assertEquals(500, delayAction.getDuration());
//
//		slide = createDelaySlide("0.5");		
//		action = interpreter.interpret(slide);		
//		assertThat(Actions.select(action).isInstanceOf(SleepAction.class).all().size(), equalTo(1));
//		delayAction = (SleepAction) Actions.select(action).isInstanceOf(SleepAction.class).first();
//		assertEquals(500, delayAction.getDuration());
//
//		slide = createDelaySlide("0");		
//		action = interpreter.interpret(slide);		
//		assertThat(Actions.select(action).isInstanceOf(SleepAction.class).all().size(), equalTo(1));
//		delayAction = (SleepAction) Actions.select(action).isInstanceOf(SleepAction.class).first();
//		assertEquals(0, delayAction.getDuration());
//
//		
//	}
	
}
