package org.sikuli.slides.api;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.junit.Test;
import org.sikuli.slides.api.models.Slide;

public class SlidesReaderTest {
	
	@Test
	public void testPPTXSlidesReader() throws IOException{		
		SlidesReader reader = new PPTXSlidesReader();
		URL url = getClass().getResource("click.pptx");
		List<Slide> slides = reader.read(url);
		assertEquals("# of slides", 1, slides.size());
		
		slides = reader.read(getClass().getResource("fivesteps.pptx"));
		assertEquals("# of slides", 5, slides.size());

	}
	
	@Test(expected = IOException.class)
	public void testPPTXSlidesReaderNoSuchFile() throws IOException{		
		SlidesReader reader = new PPTXSlidesReader();
		URL url = new URL("file:///none.pptx");
		//URL url = getClass().getResource("click.pptx");
		List<Slide> slides = reader.read(url);
		assertEquals("# of slides", 1, slides.size());
	}
	
	@Test(expected = IOException.class)
	public void testPPTXSlidesReaderBadFile() throws IOException{		
		SlidesReader reader = new PPTXSlidesReader();
		URL url = getClass().getResource("sikuli.png");
		//URL url = getClass().getResource("click.pptx");
		List<Slide> slides = reader.read(url);
		assertEquals("# of slides", 1, slides.size());
	}

}
