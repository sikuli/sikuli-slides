package org.sikuli.slides.apps;

import java.io.File;
import java.util.concurrent.CountDownLatch;

import javax.swing.JFrame;

import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.DesktopCanvas;
import org.sikuli.slides.api.slideshow.SlideShowViewer;

import com.install4j.api.launcher.StartupNotification;
import com.install4j.api.launcher.StartupNotification.Listener;


public class FileOpenMain {


	static final CountDownLatch doneSignal = new CountDownLatch(1);
	private static File inputFile;


	public static void main(String[] args) {
		final Canvas canvas = new DesktopCanvas();
		
		final SlideShowViewer viewer = new SlideShowViewer();
		viewer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		viewer.setVisible(true);
		
		if (args.length == 1){
			
			inputFile = new File(args[0]);
			
		}else{

			StartupNotification.registerStartupListener(new Listener(){

				@Override
				synchronized public void startupPerformed(String filename) {
					if (inputFile == null){
						inputFile = new File(filename);
						viewer.invokeOpen(inputFile);
					}
				}			

			});
		}
		
		
		
		
//		try {
//			Slides.execute(inputFile);
//		} catch (SlideExecutionException e) {
//			System.err.println("Execution failed because " + e.getMessage());
//			if (e.getSlide() != null){				
//				ScreenRegion s = new DesktopScreenRegion();
//				canvas.addLabel(s, "Execution failed on slide #" + e.getSlide().getNumber())
//				.withHorizontalAlignmentCenter().withVerticalAlignmentMiddle()
//				.withFontSize(20).withBackgroundColor(Color.red).withColor(Color.white);
//								
//				TargetAction targetAction = (TargetAction) Actions.select(e.getAction()).isInstanceOf(TargetAction.class).first();
//				if (targetAction != null){
//					Target target = targetAction.getTarget();
//					if (target instanceof ContextImageTarget){
//						ContextImageTarget t = (ContextImageTarget) target;
//						canvas.addLabel(Relative.to(s).center().below(50).getScreenLocation(), "Unable to find target")
//						.withFontSize(20).withBackgroundColor(Color.red).withColor(Color.white)
//						.withHorizontalAlignmentCenter();
//						canvas.addImage(Relative.to(s).center().below(100).getScreenLocation(), t.getTargetImage()).withHorizontalAlignmentCenter();
//					}
//					canvas.display(5);
//				}
//			}			
//		}	

	}

}
