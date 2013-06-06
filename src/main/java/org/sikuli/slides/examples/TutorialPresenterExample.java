package org.sikuli.slides.examples;

import java.io.IOException;
import java.net.URL;

import org.sikuli.slides.Presenter;
import org.sikuli.slides.PresenterException;
import org.sikuli.slides.SlideDeck;
import org.sikuli.slides.TutorialPresenter;

public class TutorialPresenterExample {

	public static void main(String[] args) throws IOException, PresenterException {
		
		URL url = TutorialPresenterExample.class.getResource("helloworld.pptx");
		
		SlideDeck slideDeck = SlideDeck.createFromPowerPoint(url);
		Presenter presenter = new TutorialPresenter();
		
		// play the entire slide deck
		presenter.play(slideDeck);
		
		// play the entire slide deck again, in AUTO mode		
		((TutorialPresenter) presenter).setMode(TutorialPresenter.Mode.AUTO);		
		presenter.play(slideDeck);
		
	}
}
