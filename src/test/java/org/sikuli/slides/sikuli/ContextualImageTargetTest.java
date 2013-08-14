package org.sikuli.slides.sikuli;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;

import org.jnativehook.NativeHookException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sikuli.api.DefaultRegion;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.StaticImageScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.DesktopCanvas;
import org.sikuli.slides.api.sikuli.ContextImageTarget;
import org.sikuli.slides.api.sikuli.CrossSearchStrategy;

public class ContextualImageTargetTest {
	
	private URL keyboard;
	private URL google;
	private URL wiki;

	@Before
	public void setUp() throws NativeHookException, IOException{
		keyboard = getClass().getResource("keyboard.png");
		google = getClass().getResource("google.png");
		wiki = getClass().getResource("wiki.png");
	}

	@After
	public void tearDown(){		
//		canvas.hide();
	}

	@Test
	public void testBasicFind() throws IOException{
		ScreenRegion screenRegion = new StaticImageScreenRegion(ImageIO.read(keyboard));
		Target target = new ContextImageTarget(keyboard, 405, 552, 100, 30);
		List<ScreenRegion> list = target.doFindAll(screenRegion);		
		assertThat(list.size(), equalTo(1));
		
		ScreenRegion r = list.get(0);
		assertThat(r.getBounds().x, equalTo(405));
		assertThat(r.getBounds().y, equalTo(552));
	}
	
	@Test
	public void testWhenTargetCanNotBeFound() throws IOException{
		ScreenRegion screenRegion = new StaticImageScreenRegion(ImageIO.read(wiki));
		Target target = new ContextImageTarget(keyboard, 405, 552, 100, 30);
		List<ScreenRegion> list = target.doFindAll(screenRegion);		
		assertThat(list.size(), equalTo(0));
	}
	
	@Test
	public void testCanResolveAmbiguity() throws IOException{
		ScreenRegion screenRegion = new StaticImageScreenRegion(ImageIO.read(keyboard));
		
		// the target is a checkbox, the whole image has two checkboxes
		Target target = new ContextImageTarget(keyboard, 138, 402, 20, 19);
		List<ScreenRegion> list = target.doFindAll(screenRegion);	
		
		// should return only one, instead of two
		assertThat(list.size(), equalTo(1));
		
		ScreenRegion r = list.get(0);
		assertThat(r.getBounds().x, equalTo(138));
		assertThat(r.getBounds().y, equalTo(402));
	}
	
}
