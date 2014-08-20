package org.sikuli.slides.api.parsers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;
import org.sikuli.slides.api.models.ImageElement;
import org.sikuli.slides.api.models.Slide;
import org.sikuli.slides.api.parsers.SlideParser;

public class SlideParserTest {
		
	@Test 
	// PowerPoint Format
	public void testParseSlide1(){
		File xml = new File(getClass().getResource("slide1.xml").getFile());
		File rel = new File(getClass().getResource("slide1.xml.rels").getFile());
		//Files.re
		SlideParser parser = new SlideParser();
		Slide slide = parser.parse(xml, rel);
			
		assertEquals("size", 4, slide.getElements().size());		
		assertThat(slide.select().isImage().exist(), equalTo(true));
		ImageElement image = (ImageElement) slide.select().isImage().first();
		assertThat(image.getFileName(), containsString("image"));
	}

	@Test 
	// Google Presentation Format
	public void testParseSlide2(){
		File xml = new File(getClass().getResource("slide2.xml").getFile());
		File rel = new File(getClass().getResource("slide2.xml.rels").getFile());
		//Files.re
		SlideParser parser = new SlideParser();
		Slide slide = parser.parse(xml, rel);
		
		
		assertEquals("size", 3, slide.getElements().size());
		assertThat(slide.select().isImage().exist(), equalTo(true));
		ImageElement image = (ImageElement) slide.select().isImage().first();
		assertThat(image.getFileName(), containsString("image"));
	}
	

}
