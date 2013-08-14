package org.sikuli.slides.api.parsers;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;

import org.junit.Test;
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
		
		System.out.println(slide);	
		assertEquals("size", 4, slide.getElements().size());
		
	}

	@Test 
	// Google Presentation Format
	public void testParseSlide2(){
		File xml = new File(getClass().getResource("slide2.xml").getFile());
		File rel = new File(getClass().getResource("slide2.xml.rels").getFile());
		//Files.re
		SlideParser parser = new SlideParser();
		Slide slide = parser.parse(xml, rel);
		
		System.out.println(slide);	
		assertEquals("size", 3, slide.getElements().size());
	}

}
