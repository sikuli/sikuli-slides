package org.sikuli.slides.examples.driver;

import java.net.MalformedURLException;
import java.net.URL;

import org.sikuli.api.API;
import org.sikuli.slides.driver.WidgetContainerFactory;
import org.sikuli.slides.driver.Widget;
import org.sikuli.slides.driver.annotations.Source;

public class GooglePlayMenuExample1 {
	
	@Source(url = "http://slides.sikuli.org/examples/driver/googleplay/menu.pptx")
	public static class Menu {
		public Widget apps; 	
		public Widget movies;    
		public Widget music;    
		public Widget books;
	}	

	public static void main(String[] arg) throws MalformedURLException  {			
		API.browse(new URL("http://slides.sikuli.org/examples/driver/googleplay/"));
		API.pause(1000);

		Menu menu = WidgetContainerFactory.create(Menu.class);
		menu.apps.click();
		menu.movies.click();
		menu.music.click();
		menu.books.click();
	}
}
