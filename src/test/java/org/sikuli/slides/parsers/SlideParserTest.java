package org.sikuli.slides.parsers;

import static org.junit.Assert.*;

import java.net.URL;

import org.junit.Test;
import org.sikuli.slides.models.Slide;

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
		assertEquals("size", slide.getElements().size(),4);		
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
		assertEquals("size", slide.getElements().size(),3);
	}

}
