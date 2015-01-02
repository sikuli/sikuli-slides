package org.sikuli.slides.api;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ScreenRegion;
import org.sikuli.slides.api.actions.Action;
import org.sikuli.slides.api.interpreters.DefaultInterpreter;
import org.sikuli.slides.api.interpreters.Interpreter;
import org.sikuli.slides.api.io.PPTXSlidesReader;
import org.sikuli.slides.api.io.SlidesReader;
import org.sikuli.slides.api.models.Slide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * Provides static methods for executing slides. The entry point of Sikuli Slides API.
 * 
 * @author Sikuli Lab
 */
public class Slides {
	
	static Logger logger = LoggerFactory.getLogger(Slides.class);

	private Slides(){}		
	
	/**
	 * Execute a presentation file at a given url
	 * 
	 * @param url	url to download the file
	 * @throws SlideExecutionException
	 */
	static public void execute(URL url) throws SlideExecutionException {
		checkNotNull(url);
		ScreenRegion screenRegion = new DesktopScreenRegion();
		Context context = new Context(screenRegion);
		execute(url, context);		
	}

	/**
	 * Execute a presentation file at an URL using a specific {@link Context}
	 * @param url	url to download the file
	 * @param context	context
	 * @throws SlideExecutionException
	 */
	static public void execute(URL url, Context context) throws SlideExecutionException {
		checkNotNull(url);
		checkNotNull(context);
		logger.debug("execute slides with context {}", context);
		SlidesReader reader = new PPTXSlidesReader();		
		List<Slide> slides;
		try {
			slides = reader.read(url);
		} catch (IOException e) {
			throw new SlideExecutionException(e);		
		}

		SlidesExecutor executor = new AutomationExecutor(context);
		executor.execute(slides);
	}

	/**
	 * Execute a presentation file
	 * @param file	the presentation file
	 * @throws SlideExecutionException
	 */
	static public void execute(File file) throws SlideExecutionException {
		checkNotNull(file);
		try {
			execute(file.toURI().toURL());
		} catch (MalformedURLException e) {
			throw new SlideExecutionException(e);
		}
	}

	/**
	 * Execute a presentation file using a specific {@link Context}
	 * @param file	the presentation file
	 * @param context	the context
	 * @throws SlideExecutionException
	 */
	static public void execute(File file, Context context) throws SlideExecutionException {
		checkNotNull(file);
		checkNotNull(context);
		try {
			execute(file.toURI().toURL(), context);
		} catch (MalformedURLException e) {
			throw new SlideExecutionException(e);
		}
	}

	/**
	 * Interpret a presentation file as a list of executable actions
	 * @param file	the presentation file
	 * @return	a list of {@link Action}
	 * @throws IOException
	 */
	public static List<Action> interpret(File file) throws IOException {
		checkNotNull(file);
		Interpreter interpreter = new DefaultInterpreter(null);
		SlidesReader reader = new PPTXSlidesReader();		
		List<Slide> slides;
		slides = reader.read(file);
		List<Action> actions = Lists.newArrayList();
		for (Slide slide : slides){
			Action action = interpreter.interpret(slide);
			actions.add(action);
			logger.info("Action interpreted: {}", action);
		}
		return actions;
	}

}
