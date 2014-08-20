package org.sikuli.slides.api.generators;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sikuli.api.Target;
import org.sikuli.slides.api.actions.Action;
import org.sikuli.slides.api.actions.BrowserAction;
import org.sikuli.slides.api.actions.AssertExistAction;
import org.sikuli.slides.api.actions.LeftClickAction;
import org.sikuli.slides.api.actions.AssertNotExistAction;
import org.sikuli.slides.api.actions.RightClickAction;
import org.sikuli.slides.api.actions.TargetAction;
import org.sikuli.slides.api.actions.TypeAction;
import org.sikuli.slides.api.sikuli.ContextImageTarget;

import com.google.common.collect.Lists;

public class JavaCodeGeneratorTest {
	
	private CodeGenerator generator;

	@Before
	public void setUp(){
		generator = new JavaAPICodeGenerator("MyProgram", new File("imagesxxx"));
	}
	
	@Test
	public void testSimple() throws MalformedURLException{
		

		List<Action> actions = Lists.newArrayList();
		
		Action click = new LeftClickAction();
		URL url = getClass().getResource("sikuli_context.png");
		Target target = new ContextImageTarget(url, 100,100,50,50);
		Action action = new TargetAction(target, click);
		actions.add(action);
		
		Action rightClick = new RightClickAction();
		action = new TargetAction(target, rightClick);
		actions.add(action);
		
		TypeAction type = new TypeAction();
		type.setText("something to type");
		action = new TargetAction(target, type);
		actions.add(action);
		
		BrowserAction browser = new BrowserAction();
		//type.setText("something to type");
		actions.add(browser);		


		AssertExistAction exist = new AssertExistAction(target);
		actions.add(exist);

		AssertNotExistAction notexist = new AssertNotExistAction(target);
		actions.add(notexist);
		
		type = new TypeAction();
		type.setText("something to type");
		action = new TargetAction(target, type);
		actions.add(action);

		
		generator.generate(actions, System.out);
		
		
		
		
	}

}
