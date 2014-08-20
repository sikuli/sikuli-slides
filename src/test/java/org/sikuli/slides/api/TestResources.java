package org.sikuli.slides.api;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.sikuli.slides.api.io.PPTXSlidesReader;
import org.sikuli.slides.api.models.Slide;

public class TestResources {	
	static public URL get(String name){
		return TestResources.class.getResource(name);	
	}
	
	static public List<Slide> readSlides(String resourceName) throws IOException {
		return (new PPTXSlidesReader()).read(TestResources.class.getResource(resourceName));
	}

	static public Slide readSlide(String resourceName, int index) throws IOException {
		return readSlides(resourceName).get(index);
	}

}
