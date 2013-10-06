package org.sikuli.slides.api;

import java.net.URL;

public class TestResources {	
	static public URL get(String name){
		return TestResources.class.getResource(name);	
	}
}
