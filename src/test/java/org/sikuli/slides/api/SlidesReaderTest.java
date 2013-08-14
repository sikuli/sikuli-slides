package org.sikuli.slides.api;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.junit.Test;
import org.sikuli.slides.api.io.PPTXSlidesReader;
import org.sikuli.slides.api.io.SlidesReader;
import org.sikuli.slides.api.models.Slide;

public class SlidesReaderTest {
	
	@Test
	public void testCanReadSlidesNormally() throws IOException{		
		SlidesReader reader = new PPTXSlidesReader();
		URL url = getClass().getResource("click.pptx");
		List<Slide> slides = reader.read(url);
		assertEquals("# of slides", 1, slides.size());
		
		slides = reader.read(getClass().getResource("fivesteps.pptx"));
		assertEquals("# of slides", 5, slides.size());

		slides = reader.read(getClass().getResource("3blankslides.pptx"));
		assertEquals("# of slides", 3, slides.size());
	}
	
	@Test
	public void testCanReadSlidesRemotely() throws IOException{		
		SlidesReader reader = new PPTXSlidesReader();
		URL url = new URL("http://slides-dev.sikuli.org/helloworld.pptx");
		List<Slide> slides = reader.read(url);
		assertEquals("# of slides", 1, slides.size());
	}
	
	@Test
	public void testCanReadGoogleSlidesRemotely() throws IOException{		
		SlidesReader reader = new PPTXSlidesReader();
		URL url = new URL("https://docs.google.com/presentation/d/1w48gExh5oLIT0J8xYXR1RxpqTrZTXJC8OR4UXxShTQ8/edit?usp=sharing");
		List<Slide> slides = reader.read(url);
		assertEquals("# of slides", 2, slides.size());		
	}
	
	@Test(expected = IOException.class)
	public void testCanHandleNoSuchFile() throws IOException{		
		SlidesReader reader = new PPTXSlidesReader();
		URL url = new URL("file:///none.pptx");
		//URL url = getClass().getResource("click.pptx");
		List<Slide> slides = reader.read(url);
		assertEquals("# of slides", 1, slides.size());
	}
	
	@Test(expected = IOException.class)
	public void testCanHnadleBadFileExtension() throws IOException{		
		SlidesReader reader = new PPTXSlidesReader();
		URL url = getClass().getResource("sikuli.png");
		//URL url = getClass().getResource("click.pptx");
		List<Slide> slides = reader.read(url);
		assertEquals("# of slides", 1, slides.size());
	}

}
