package org.sikuli.slides.api;

import java.io.File;
import java.util.List;

import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ScreenRegion;

import com.sampullara.cli.Args;
import com.sampullara.cli.Argument;

public class ExecuteMain {
	
	static final String EXE = "java -jar sikuli-slides-1.4.0.jar execute";
	static final String SYNTAX =  "path-to-slides-file [options]";
	
	@Argument(value = "help", description = "Print help message", required = false)
	static private boolean help = false;
	
	@Argument(value = "screen_id", description = "The id of the connected screen/monitor (default is 0).", required = false)
	static private int screenId = 0;
	
	@Argument(value = "min_score", description = "The minimum similarity score for a target to be considered as a match. It's on a 0 to 1 scale where 0 is the least precise search and 1.0 is the most precise search (default is 0.7).", required = false)
	static private Float minScore = 0.7f;
	
	@Argument(value = "wait", description = "The maximum time to wait (ms) for a target to appear on the screen to perform an action on it (default is 5000).", required = false)
	static private long wait = 5000;


	@Argument(value = "parameters", description = "Parameters as name=value pairs joined by ;", required = false, delimiter = ";")
	static private String[] params = new String[]{};		 

	static void exit(String errMessage){
		System.err.println(errMessage);
		Args.usage(ExecuteMain.class, EXE + " " + SYNTAX);
		System.exit(1);
	}

	public static void main(String[] args) {	

		List<String> rest = null;
		try {
			rest = Args.parse(ExecuteMain.class, args);
		} catch (IllegalArgumentException e) {
		}
		
		if (rest == null || rest.size() != 1) {
			exit("Invalid syntax");
			return;			
		}
		
		if (help){
			exit("");
		}
		
		Context context = new Context();

		// set parameter values
		for (String param : params){
			String[] toks = param.split("=");
			if (toks.length == 2){
				String name = toks[0];
				String value = toks[1];
				context.addParameter(name,  value);				
			}
		}
		
		// set min score
		if (minScore < 0 || minScore > 1){
			exit("" + minScore + " is not a valid value for min_score. Please specify a score between 0 and 1.");
		}		
		context.setMinScore(minScore);
		
		// set wait time
		context.setWaitTime(wait);
		
		// set screen region
		ScreenRegion screenRegion = new DesktopScreenRegion(screenId);
		context.setScreenRegion(screenRegion);
		
		System.out.println(context);
		
		String input = rest.get(0);					
		try {
			Slides.execute(new File(input), context);
		} catch (SlideExecutionException e) {
			System.err.println("Execution failed because " + e.getMessage());			
			if (e.getSlide() != null){
				System.err.print("On slide no. " + e.getSlide().getNumber());
				System.err.println(" Failed to execute " + e.getAction());
			}
			System.exit(1);			
		}

	}
}
