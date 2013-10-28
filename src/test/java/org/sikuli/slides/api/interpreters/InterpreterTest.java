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
import org.sikuli.slides.api.actions.BrowserAction;
import org.sikuli.slides.api.actions.DoubleClickAction;
import org.sikuli.slides.api.actions.AssertExistAction;
import org.sikuli.slides.api.actions.LabelAction;
import org.sikuli.slides.api.actions.LeftClickAction;
import org.sikuli.slides.api.actions.AssertNotExistAction;
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

	private static final String TEST_TEXT = "some text";
	private Interpreter interpreter;
	private URL source;
	private Slide slide;

//	@Test
//	public void testInterpretBrowserAction() throws MalformedURLException{
//		URL url = new URL("http://slides.sikuli.org");
//		
//		Slide slide = new Slide();
//		slide.newKeywordElement().keyword(KeywordDictionary.BROWSER).add();
//		slide.newElement().text(url.toString()).add();
//		
//		Interpreter interpreter = new DefaultInterpreter();
//		Action action = interpreter.interpret(slide);
//		
//		action = interpreter.interpret(slide);		
//		assertThat(Actions.select(action).isInstanceOf(BrowserAction.class).all().size(), equalTo(1));
//		BrowserAction browserAction = (BrowserAction) Actions.select(action).isInstanceOf(BrowserAction.class).first();
//		assertNotNull(browserAction.getUrl());
//		assertEquals("browser url", url, browserAction.getUrl());
//	}
//	
//	
//	private void addTarget(Slide slide){
//		ImageElement screenshotElement = new ImageElement();
//		screenshotElement.setSource(getClass().getResource("sikuli_context.png"));
//		screenshotElement.setOffx(100);
//		screenshotElement.setOffy(100);
//		screenshotElement.setCx(1000);
//		screenshotElement.setCy(1000);
//		slide.add(screenshotElement);
//		
//		SlideElement targetElement = new SlideElement(); 
//		targetElement.setOffx(348);
//		targetElement.setOffy(223);
//		targetElement.setCx(200);
//		targetElement.setCy(200);		
//		targetElement.setTextSize(3600);
//		slide.add(targetElement);
//	}
//	
//	private Slide createKeywordWithTargetSlide(Keyword keyword){		
//		Slide slide = new Slide();
//		slide.newKeywordElement().keyword(keyword).add();		
//		addTarget(slide);
//		return slide;
//	}
//	
//	private Slide createDelaySlide(String text){		
//		Slide slide = new Slide();
//		slide.newKeywordElement().keyword(KeywordDictionary.DELAY).add();
//		slide.newElement().text(text).add();
//		return slide;
//	}
//	
//	@Before
//	public void setUp() throws IOException{
//		interpreter = new DefaultInterpreter();
//		slide = new Slide();
//		source = getClass().getResource("sikuli_context.png");
//	}
//	
//	@Test
//	public void testLeftClickAction(){
//		Slide slide = createKeywordWithTargetSlide(KeywordDictionary.CLICK);		
//		Action action = interpreter.interpret(slide);		
//		assertThat(action, notNullValue());
//		assertThat(Actions.select(action).isInstanceOf(LeftClickAction.class).all().size(), equalTo(1));
//	}
//	
//	@Test
//	public void testRightClickAction() {
//		Slide slide = createKeywordWithTargetSlide(KeywordDictionary.RIGHT_CLICK);		
//		Action action = interpreter.interpret(slide);		
//		assertThat(action, notNullValue());
//		assertThat(Actions.select(action).isInstanceOf(RightClickAction.class).all().size(), equalTo(1));
//	}
//	
//	@Test
//	public void testDoubleClickAction() {
//		Slide slide = createKeywordWithTargetSlide(KeywordDictionary.DOUBLE_CLICK);
//		Action action = interpreter.interpret(slide);		
//		assertThat(action, notNullValue());
//		assertThat(Actions.select(action).isInstanceOf(DoubleClickAction.class).all().size(), equalTo(1));
//	}
//	
//	@Test
//	public void testLabelActionFromOnlyText() {
//		Slide slide = new Slide();
//		slide.newElement().text("label").add();
//		
//		Action action = interpreter.interpret(slide);
//		assertThat(action, notNullValue());
//		assertThat(Actions.select(action).isInstanceOf(LabelAction.class).all().size(), equalTo(1));
//	}	
//	
//	@Test
//	public void testLabelActionFromOnlyTextTarget() {
//		Slide slide = new Slide();
//		slide.newElement().bounds(100,100,50,50).text("label").add();
//		slide.newImageElement().source(source).bounds(0,0,200,200).add();
//		
//		Action action = interpreter.interpret(slide);
//		assertThat(action, notNullValue());		
//		assertThat(Actions.select(action).isInstanceOf(LabelAction.class).all().size(), equalTo(1));
//	}	
//	
//	@Test
//	public void testExistAction() {
//		Slide slide = createKeywordWithTargetSlide(KeywordDictionary.EXIST);
//		Action action = interpreter.interpret(slide);		
//		assertThat(action, notNullValue());
//		assertThat(Actions.select(action).isInstanceOf(AssertExistAction.class).all().size(), equalTo(1));
//	}	
//
//	@Test
//	public void testInterpretNotExistAction() {
//		Slide slide = createKeywordWithTargetSlide(KeywordDictionary.NOT_EXIST);
//		Action action = interpreter.interpret(slide);		
//		assertThat(action, notNullValue());
//		assertThat(Actions.select(action).isInstanceOf(AssertNotExistAction.class).all().size(), equalTo(1));
//	}	
//	
//	
//	@Test
//	public void testTypeActionOnTarget() {
//		slide = new Slide();
//		slide.newKeywordElement().keyword(KeywordDictionary.TYPE).add();		
//		slide.newImageElement().source(source).bounds(100,100,50,50).add();
//		slide.newElement().text("some text").bounds(120,120,30,30).add();		
//		
//		Action action = interpreter.interpret(slide);		
//		assertThat(action, notNullValue());
//		assertThat(Actions.select(action).isInstanceOf(TypeAction.class).all().size(), equalTo(1));
//		
//		TypeAction typeAction = (TypeAction) Actions.select(action).isInstanceOf(TypeAction.class).first();
//		assertThat(typeAction.getText(), equalToIgnoringCase("some text"));
//	}	
//	
//	@Test
//	public void testTypeActionWithTextInKeywordElement() {
//		slide = new Slide();
//		slide.newKeywordElement().keyword(KeywordDictionary.TYPE).text("some text").add();		
//		
//		Action action = interpreter.interpret(slide);		
//		assertThat(action, notNullValue());
//		
//		assertThat(Actions.select(action).isInstanceOf(TypeAction.class).all().size(), equalTo(1));
//		
//		TypeAction typeAction = (TypeAction) Actions.select(action).isInstanceOf(TypeAction.class).first();
//		assertThat(typeAction.getText(), equalToIgnoringCase("some text"));
//	}
//	
//	@Test
//	public void testTypeActionWithTextInAnotherElement() {
//		slide = new Slide();
//		slide.newKeywordElement().keyword(KeywordDictionary.TYPE).add();
//		slide.newElement().text("some text").add();
//		
//		Action action = interpreter.interpret(slide);		
//		assertThat(action, notNullValue());
//		
//		assertThat(Actions.select(action).isInstanceOf(TypeAction.class).all().size(), equalTo(1));
//		
//		TypeAction typeAction = (TypeAction) Actions.select(action).isInstanceOf(TypeAction.class).first();
//		assertThat(typeAction.getText(), equalToIgnoringCase("some text"));
//	}
//
//	@Test
//	public void testInterpretWaitAction() {
//		Slide slide = new Slide();
//		slide.newKeywordElement().keyword(KeywordDictionary.WAIT).add();
//		slide.newElement().text("2 seconds").add();
//		slide.newImageElement().source(source).bounds(100,100,50,50).add();
//		slide.newElement().bounds(120,120,30,30).add();		
//
//		Action action = interpreter.interpret(slide);
//		
//		assertThat(action, notNullValue());
//		assertThat(Actions.select(action).isInstanceOf(WaitAction.class).all().size(), equalTo(1));
//		WaitAction waitAction = (WaitAction) Actions.select(action).isInstanceOf(WaitAction.class).first();
//		assertThat(waitAction.getDuration(), equalTo(2000L));
//		assertThat(waitAction.getTarget(), notNullValue());
//	}
//	
	
	@Test
	public void testCanInterpretBrowseAction(){
		Slide slide = new Slide();
		on(slide).insert().element().text("Browse http://slides.sikuli.org");
		
		Interpreter interpreter = new DefaultInterpreter.BrowseActionInterpreter();
		Action action = interpreter.interpret(slide);
		
		assertThat(action, instanceOf(BrowserAction.class));
		assertThat(((BrowserAction) action).getUrl().toString(), equalToIgnoringCase("http://slides.sikuli.org"));		
	}
	
	@Test
	public void testCanInterpretTargetActionWithLeftClick(){
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
	public void testCanInterpretContextImageTarget(){
		Slide slide = new Slide();
		on(slide).insert().image().source(TestResources.get("sikuli_context.png")).bounds(100,100,1000,1000);
		on(slide).insert().element().bounds(348,223,200,200);

		TargetInterpreter interpreter = new DefaultInterpreter.ContextImageTargetInterpreter();
		Target target = interpreter.interpret(slide);
		
		assertThat(target, instanceOf(ContextImageTarget.class));		
	}
	
	@Test
	public void testCanInterpretLeftClickAction(){
		Slide slide = new Slide();
		on(slide).insert().element().text("Click");		
		interpreter = new DefaultInterpreter.LeftClickActionInterpreter();
		Action action = interpreter.interpret(slide);
		assertThat(action, instanceOf(LeftClickAction.class));
		assertThat(action, notNullValue());		
	}
	
	@Test
	public void testCanInterpretDoubleClickAction(){
		Slide slide = new Slide();
		on(slide).insert().element().text("DoubleClick");		
		interpreter = new DefaultInterpreter.DoubleClickActionInterpreter();
		Action action = interpreter.interpret(slide);	
		assertThat(action, instanceOf(DoubleClickAction.class));
		assertThat(action, notNullValue());		
	}
	
	@Test
	public void testCanInterpretRightClickAction(){
		Slide slide = new Slide();
		on(slide).insert().element().text("RightClick");		
		interpreter = new DefaultInterpreter.RightClickActionInterpreter();
		Action action = interpreter.interpret(slide);	
		assertThat(action, instanceOf(RightClickAction.class));
		assertThat(action, notNullValue());		
	}


	@Test
	public void testCanInterpretSleepAction_2(){
		Slide slide = new Slide();
		on(slide).insert().element().text("Sleep 2");
		
		
		interpreter = new DefaultInterpreter.SleepActionInterpreter();  
		
		SleepAction action = (SleepAction) interpreter.interpret(slide);		
		assertThat(action, notNullValue());
		assertThat(action.getDuration(), equalTo(2000L));
	}
	
	@Test
	public void testCanInterpretExistAction(){
		Slide slide = new Slide();
		on(slide).insert().element().text("Exist");
		on(slide).insert().image().source(TestResources.get("sikuli_context.png")).bounds(100,100,1000,1000);
		on(slide).insert().element().bounds(348,223,200,200);

		
		interpreter = new DefaultInterpreter.ExistActionInterpreter();  
		
		Action action = interpreter.interpret(slide);	
		assertThat(action, instanceOf(AssertExistAction.class));
	}
	
	@Test
	public void testCanInterpretNotExistAction(){
		Slide slide = new Slide();
		on(slide).insert().element().text("not exist");
		on(slide).insert().image().source(TestResources.get("sikuli_context.png")).bounds(100,100,1000,1000);
		on(slide).insert().element().bounds(348,223,200,200);

		
		interpreter = new DefaultInterpreter.NotExistActionInterpreter();  
		
		Action action = interpreter.interpret(slide);	
		assertThat(action, instanceOf(AssertNotExistAction.class));
	}
	
	@Test
	public void testCanInterpretTypeAction(){
		Slide slide = new Slide();
		on(slide).insert().element().text("type something to type");
		
		interpreter = new DefaultInterpreter.TypeActionInterpreter();  
		
		Action action = interpreter.interpret(slide);	
		assertThat(action, instanceOf(TypeAction.class));
		assertThat(((TypeAction) action).getText(), equalTo("something to type"));
		
	}
	
	@Test
	public void testCanInterpretLabelAction(){
		Slide slide = new Slide();
		on(slide).insert().element().text("this is a label");
		
		interpreter = new DefaultInterpreter.LabelInterpreter();  
		
		Action action = interpreter.interpret(slide);
		assertThat(action, instanceOf(LabelAction.class));
		assertThat(((LabelAction) action).getText(), equalTo("this is a label"));
		
	}
	
	@Test
	public void testCanInterpretLabelActionBounds(){
		Slide slide = new Slide();
		on(slide).insert().element().text("this is a label").bounds(0,0,4572000,3429000);
		
		interpreter = new DefaultInterpreter.LabelInterpreter();  
		
		Action action = interpreter.interpret(slide);
		assertThat(action, instanceOf(LabelAction.class));	
		assertThat(((LabelAction) action).getSpatialRelationship(), notNullValue());
	}
	
	@Test
	public void testCanInterpretMultipleLabels(){
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
//	public void testCanInterpretLabel_ScreenRelativeLocation(){
//		Slide slide = new Slide();
//		SlideElement element = on(slide).insert().element().bounds(0,0,4572000,3429000).get();		
//		
//		SpatialRelationshipInterpreter interpreter = new DefaultInterpreter.ScreenLocationInterpreter();
//		
//		Action action = interpreter.interpret(slide, element);
//		assertThat(action, instanceOf(RelativeAction.class));
//		
//		assertThat(((RelativeAction) action).getMinX(), closeTo(0, 0.01));
//		assertThat(((RelativeAction) action).getMaxX(), closeTo(0.5, 0.01));
//		
//		
//		
////		assertThat(action1, instanceOf(LabelAction.class));
////		assertThat(action2, instanceOf(LabelAction.class));
////		assertThat(action3, nullValue());
//		
////		assertThat(((LabelAction) action1).getText(), equalTo("this is the first label"));
////		assertThat(((LabelAction) action2).getText(), equalTo("this is the second label"));		
//	}
	
//	@Test
//	public void testInterpretDelayAction() {
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
