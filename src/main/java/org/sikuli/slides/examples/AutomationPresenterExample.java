package org.sikuli.slides.examples;

import java.io.IOException;
import java.net.URL;

import org.sikuli.slides.AutomationPresenter;
import org.sikuli.slides.Presenter;
import org.sikuli.slides.PresenterException;
import org.sikuli.slides.SlideDeck;

public class AutomationPresenterExample {

	public static void main(String[] args) throws IOException, PresenterException {
		
		URL url = AutomationPresenterExample.class.getResource("helloworld.pptx");
		
		SlideDeck slideDeck = SlideDeck.createFromPowerPoint(url);
		Presenter presenter = new AutomationPresenter();	
		
		// play the entire slide deck
		presenter.play(slideDeck);
				
		// play a single slide in a slide deck
		presenter.play(slideDeck.get(0));
		
		
	}
}
