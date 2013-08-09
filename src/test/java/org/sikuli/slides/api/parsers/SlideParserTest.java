package org.sikuli.slides.api.parsers;

import static org.junit.Assert.*;

import java.net.URL;

import org.junit.Test;
import org.sikuli.slides.api.models.Slide;
import org.sikuli.slides.api.parsers.SlideParser;

public class SlideParserTest {
		
	@Test 
	// PowerPoint Format
	public void testParseSlide1(){
		URL xml = getClass().getResource("slide1.xml");
		URL rel = getClass().getResource("slide1.xml.rels");
		//Files.re
		SlideParser parser = new SlideParser();
		Slide slide = parser.parse(xml, rel);
		
		System.out.println(slide);	
		assertEquals("size", 4, slide.getElements().size());
		
	}

	@Test 
	// Google Presentation Format
	public void testParseSlide2(){
		URL xml = getClass().getResource("slide2.xml");
		URL rel = getClass().getResource("slide2.xml.rels");
		//Files.re
		SlideParser parser = new SlideParser();
		Slide slide = parser.parse(xml, rel);
		
		System.out.println(slide);	
		assertEquals("size", 3, slide.getElements().size());
	}

}
