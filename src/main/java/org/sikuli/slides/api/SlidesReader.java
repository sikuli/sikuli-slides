package org.sikuli.slides.api;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.sikuli.slides.models.Slide;

public interface SlidesReader {	
	public List<Slide> read(URL url) throws IOException;
}
