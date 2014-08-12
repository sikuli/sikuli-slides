package org.sikuli.slides.examples.driver;

import java.net.MalformedURLException;
import java.net.URL;

import org.sikuli.api.API;
import org.sikuli.slides.driver.SlideUIFactory;
import org.sikuli.slides.driver.UIElement;
import org.sikuli.slides.driver.annotations.Source;

public class GooglePlayMenuExample {
	
	@Source(url = "http://slides.sikuli.org/examples/driver/googleplay/menu.pptx")
	public static class Menu {
		public UIElement apps; 	
		public UIElement movies;    
		public UIElement music;    
		public UIElement books;
	}	

	public static void main(String[] arg) throws MalformedURLException  {			
		API.browse(new URL("http://slides.sikuli.org/examples/driver/googleplay/"));
		API.pause(1000);

		Menu menu = SlideUIFactory.create(Menu.class);
		menu.apps.click();
		menu.movies.click();
		menu.music.click();
		menu.books.click();
	}
}
