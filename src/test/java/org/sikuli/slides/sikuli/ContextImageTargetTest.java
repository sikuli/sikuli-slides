package org.sikuli.slides.sikuli;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;

import org.jnativehook.NativeHookException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.StaticImageScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.slides.api.sikuli.ContextImageTarget;

public class ContextImageTargetTest {
	
	private URL keyboard;
	private URL google;
	private URL wiki;
	private URL login;

	@Before
	public void setUp() throws NativeHookException, IOException{
		keyboard = getClass().getResource("keyboard.png");
		google = getClass().getResource("google.png");
		wiki = getClass().getResource("wiki.png");
		login = getClass().getResource("login.png");
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
	public void testCanDistinguishAmbiguousCheckboxes() throws IOException{
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
	
	@Test
	public void testCanDistinguishAmbiguousInputFields() throws IOException{
		ScreenRegion screenRegion = new StaticImageScreenRegion(ImageIO.read(login));
		
		// the target is an input field, the whole image has two input fields
		// the label above is distinctive
		Target target = new ContextImageTarget(login, 59, 145, 80, 50);
		List<ScreenRegion> list = target.doFindAll(screenRegion);	
		
		// should return only one, instead of two
		assertThat(list.size(), equalTo(1));
		
		ScreenRegion r = list.get(0);
		assertThat(r.getBounds().x, equalTo(59));
		assertThat(r.getBounds().y, equalTo(145));
	}
	
}
