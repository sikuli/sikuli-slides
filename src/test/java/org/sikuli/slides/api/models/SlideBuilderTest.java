package org.sikuli.slides.api.models;

import static org.sikuli.slides.api.models.SlideEditor.on;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.sikuli.slides.api.TestResources;

public class SlideBuilderTest {


	private Slide slide;

	@Before
	public void setUp(){
		slide = new Slide();
	}

	@Test
	public void testInsertElement() throws IOException{
		on(slide).insert().image().source(TestResources.get("sikuli_context.png")).bounds(50,20,200,150);
		on(slide).insert().element().text("Click").bounds(5,5,20,20).textSize(15).backgroundColor("FFFF00");
		on(slide).insert().element().bounds(80,50,50,30).lineColor("00FF00");
	}

}
