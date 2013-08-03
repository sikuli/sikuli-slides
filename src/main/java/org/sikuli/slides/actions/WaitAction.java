package org.sikuli.slides.actions;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaitAction implements Action {

	Logger logger = LoggerFactory.getLogger(WaitAction.class);

	private long duration;

	@Override
	public void perform() {
		logger.info("Performing wait operation...");
		try {
			//long timeout=Long.parseLong(waitTimeString);
			// display a label
			//			Dimension dimension=MyScreen.getScreenDimensions();
			//ScreenRegion canvasRegion=new DesktopScreenRegion(Constants.ScreenId, 0, dimension.height-200,50,200);
			//Canvas canvas=new ScreenRegionCanvas(new DesktopScreenRegion(Constants.ScreenId));
			//			String readyTime = getReadyDate(timeUnit, timeout);
			String waitMessage="Please wait for "+ duration + " milliseconds";
			logger.info(waitMessage);

			//			canvas.addLabel(canvasRegion, waitMessage).withFontSize(prefsEditor.getInstructionHintFontSize());
			//			canvas.show();
			//		
			TimeUnit.MILLISECONDS.sleep(duration);

			//			canvas.hide();
			logger.info("Waking up...");
		} 
		catch (InterruptedException e) {
			logger.error("Error in wait operation:" + e.getMessage());
		}
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}
}
