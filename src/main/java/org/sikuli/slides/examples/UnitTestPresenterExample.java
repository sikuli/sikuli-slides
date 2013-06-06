package org.sikuli.slides.examples;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.sikuli.slides.Presenter;
import org.sikuli.slides.PresenterException;
import org.sikuli.slides.SlideDeck;
import org.sikuli.slides.UnitTestPresenter;
import org.sikuli.slides.UnitTestPresenterException;

public class UnitTestPresenterExample {

	public static void main(String[] args) throws IOException {
		
		URL url = UnitTestPresenterExample.class.getResource("helloworld.pptx");
		
		SlideDeck slideDeck = SlideDeck.createFromPowerPoint(url);
		Presenter presenter = new UnitTestPresenter();
		
		try {
			
			presenter.play(slideDeck);

		} catch (PresenterException e) {
			UnitTestPresenterException ue = (UnitTestPresenterException) e;			
			
			System.out.println("Error occurred on slide: " + ue.getSlide());
			ImageIO.write(ue.getScreenShot(), "png", new File("screen.png"));
		}		
		
	}
}
