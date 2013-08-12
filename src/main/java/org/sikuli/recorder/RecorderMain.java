package org.sikuli.recorder;

import java.io.File;
import java.util.List;

import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.recorder.pptx.PPTXGenerator;

import com.sampullara.cli.Args;
import com.sampullara.cli.Argument;

public class RecorderMain {

	public static void main(String[] args) {	
		
		final List<String> parse;
	    //Args.usage(c);
		try {
			parse = Args.parse(Command.class, args);
		} catch (IllegalArgumentException e) {
			Args.usage(Command.class, "java -jar sikuli-slides-1.3.0.jar record [options]");
			System.exit(1);
			return;
		}
		
		if (Command.help){
			Args.usage(Command.class, "java -jar sikuli-slides-1.3.0.jar record [options]");
			System.exit(1);
			return;			
		}
	       
	    System.out.println("Press [Ctrl-Shift-2] to start recording");
	    System.out.println("Press [Ctrl-Shift-ESC] to stop recording");

		Recorder rec = new Recorder();
		
		if (Command.bounds != null){
			int x = Command.bounds[0];
			int y = Command.bounds[1];
			int w = Command.bounds[2];
			int h = Command.bounds[3];			
			rec.setRegionOfInterest(new DesktopScreenRegion(x,y,w,h));
		}
		
		rec.start();
		
		File eventDir = rec.getEventDir();		
		
		File output;
		if (Command.output == null)
			output = new File(eventDir.getName() + ".pptx");
		else
			output = new File(Command.output);	
						
		PPTXGenerator.generate(eventDir, output);
		System.out.println("Slides are saved as " + output);
	}

	
	static class Command {
		 @Argument(value = "output", description = "This is the output file (e.g., output.pptx)", required = false)
	     static private String output = null;

		 @Argument(value = "help", description = "Print help message", required = false)
	     static private boolean help = false;		 		 				 
		 
		
		 @Argument(value = "region", description = "Screen region (x, y, width, height) to record (e.g., 100,100,400,400)", required = false, delimiter = ",")
		 static private Integer[] bounds = null;		 
	}

       
}
