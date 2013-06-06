package org.sikuli.slides;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SlideDeck  extends ArrayList<Slide> implements List<Slide>{
	
	static public SlideDeck createFromPowerPoint(File file) throws IOException {
		return new SlideDeck();
	}

	static public SlideDeck createFromPowerPoint(URL url) throws IOException {
		return new SlideDeck();
	}

}
