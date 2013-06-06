package org.sikuli.slides;

public class TutorialPresenter implements Presenter {

	private Mode mode;

	public TutorialPresenter(){
		mode = Mode.MANUAL;
	}

	@Override
	public void play(SlideDeck slideDeck) {
		// TODO Auto-generated method stub

	}

	@Override
	public void play(Slide slide) {
		// TODO Auto-generated method stub

	}


	public static enum Mode {
		MANUAL,
		AUTO
	};

	public void setMode(Mode mode){
		this.mode = mode;
	}

}
