/**
Khalid
*/
package org.sikuli.slides.uis;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.GnuParser;
import org.sikuli.api.robot.desktop.DesktopScreen;
import org.sikuli.recorder.RecorderMain;
import org.sikuli.slides.core.RunOptions;
import org.sikuli.slides.utils.Constants;
import org.sikuli.slides.utils.MyFileFilter;
import org.sikuli.slides.utils.UserPreferencesEditor;

/**
 * sikuli-slides Command Line tool using GNU like options.
 * 
 * @author Khalid
 *
 */
public class MainCommandLine {
	private static UserPreferencesEditor prefsEditor = new UserPreferencesEditor();
	private static final String applicationName = "sikuli-slides";
	private static final String versionNumber = MainCommandLine.class.getPackage().getImplementationVersion();
	private static final String NEW_LINE = System.getProperty("line.separator");
	private static final String commandLineSyntax = "java -jar "+
			applicationName + "-" + versionNumber+".jar {execute {Path_to_presentation_file.pptx | URL_to_presentation_file.pptx} [EXECUTE_OPTION]" + 
			" | record [RECORD_OPTION] | gui}" + NEW_LINE + "EXECUTE_OPTION:" + NEW_LINE
			+ "RECORD_OPTION" + NEW_LINE;
	
	
	/**
	* Parse the command-line arguments as GNU-style long option (one word long option).
	* @param args Command-line arguments.
	* @return RunOptions object that includes the necessary running arguments such as .pptx file path, 
	* start slide number, and end slide number.
	*/
	private static RunOptions useGNUParser(final String[] args) throws Exception 
	{
		RunOptions runOptions = new RunOptions();
		
		final CommandLineParser parser = new GnuParser();  
	    final Options posixOptions = getGNUCommandLineOptions();  
	    CommandLine cmd;  
	    	cmd = parser.parse(posixOptions, args);
	        if (cmd.hasOption("help")){
	        	printHelp(getGNUCommandLineOptions());
        		return null;
	        }
	        else if (cmd.hasOption("version")){
	        	String versionMsg = "sikuli-slides -- version "+ 
    					versionNumber + NEW_LINE;
	        	System.out.write(versionMsg.getBytes());
        		System.exit(0);
	        }
	    	if (cmd.hasOption("wait")){
	    		int wait=Integer.parseInt(cmd.getOptionValue("wait"));
	        	prefsEditor.putMaxWaitTime(wait);
	    	}
	    	if (cmd.hasOption("screen")){
	    		int screenId=Integer.parseInt(cmd.getOptionValue("screen"));
	        	if(screenId > DesktopScreen.getNumberScreens()){
	    			String errorMessage="Invalid screen id or screen is not connected." +NEW_LINE+
	    					"Please enter the id of the connected display or monitor." +NEW_LINE+
	    					"Example: 0 means main screen, 1 means the secondary screen, etc.";
	    			System.out.write(errorMessage.getBytes());
	    			throw new Exception();
	        	}
	        	else{
	        		prefsEditor.putDisplayId(screenId);
	        	}
	    	}
	    	if (cmd.hasOption("oldsyntax")){
	        	Constants.UseOldSyntax=true;
	    	}
	    	if (cmd.hasOption("mode")){
	    		String mode=cmd.getOptionValue("mode");
	    		if(mode.equalsIgnoreCase("automation")){
	    			Constants.AUTOMATION_MODE = true;
	    		}
	    		else if(mode.equalsIgnoreCase("help")){
	    			Constants.HELP_MODE = true;
	    		}
	    		else if(mode.equalsIgnoreCase("tutorial")){
	    			Constants.TUTORIAL_MODE = true;
	    		}
	    		else{
	    			String errorMessage="Invalid running mode value." + NEW_LINE +
	    					"Please enter one of the following running modes:" +
	    					NEW_LINE + "action" + NEW_LINE + "help" + NEW_LINE + "tutorial" + NEW_LINE;
	    			System.out.write(errorMessage.getBytes());
	    			throw new Exception();
	    		}
	    	}
	    	if(cmd.hasOption("minscore")){
	    		int precision=Integer.parseInt(cmd.getOptionValue("minscore"));
	    		if(precision> 0 && precision <= 10){
	    			prefsEditor.putPreciseSearchScore(precision);
	    		}
	    		else{
	    			String errorMessage="Invalid minscore value." + NEW_LINE;
	    			System.out.write(errorMessage.getBytes());
	    			throw new Exception();
	    		}
	    	}
	    	if(cmd.hasOption("range")){
	    		String[] toks = cmd.getOptionValue("range").split("-");
	    		if (toks.length == 1){
	    			int i = Integer.parseInt(toks[0]);
	    			runOptions.setStart(i);
	    			runOptions.setEnd(i);
	    		}else if (toks.length == 2){
	    			runOptions.setStart(Integer.parseInt(toks[0]));
	    			runOptions.setEnd(Integer.parseInt(toks[1]));	    			
	    		}
	    	}
	        
	        // check arguments
	        final String[] remainingArguments = cmd.getArgs();
	        if(remainingArguments==null || remainingArguments.length == 0){
	        	printUsage(applicationName, getGNUCommandLineOptions(), System.out);
	        	return null;
	        }
	        else{
	        	String argName_command=remainingArguments[0];
	        	// 1) Execute command
	        	if(argName_command == "execute" && remainingArguments.length >1){
	        		String argName_pptx = remainingArguments[1];
		        	// check if the file is remotely stored in the cloud
		        	if(argName_pptx.startsWith("http")){
		        		runOptions.setSourceName(argName_pptx);
		        		return runOptions;
		        	}
		        	else{
		        		// the file is locally stored
		        		MyFileFilter myFileFilter=new MyFileFilter();
		        		File source_file=new File(argName_pptx);
		        		if(myFileFilter.accept(source_file)){
		        			if(source_file.exists()){
		        				showTextHeader(System.out);
		        				displayBlankLine();
		        				runOptions.setSourceName(source_file.getAbsolutePath());
		        				return runOptions;
		        			}
		        			else{
		        				String fileNotFoundError = "No such file." + NEW_LINE;
		        				System.out.write(fileNotFoundError.getBytes()); 
		        				displayBlankLine();
		        				printUsage(applicationName, getGNUCommandLineOptions(), System.out);
		        			}
		        		}
		        	}
	        	}
	        	// 2) Record
	        	else if(argName_command == "record"){
	        		String [] otherArgs = Arrays.copyOfRange(remainingArguments,1,remainingArguments.length);
	        		RecorderMain.main(otherArgs);
	        	}
	        	// 3) Run GUI tool
	        	else if(argName_command == "gui"){
	        		MainUI.runGuiTool();
	        	}
	        	else{
	        		printUsage(applicationName, getGNUCommandLineOptions(), System.out);
	        	}

	        }
		return null;
	}
	
	/**
	 * Return all valid GNU command-line options
	 * @return valid GNU command-line options
	 */
	@SuppressWarnings("static-access")
	private static Options getGNUCommandLineOptions() {
		final Options gnuOptions=new Options();
		
		Option waitOption=OptionBuilder.withArgName("max_wait_time_ms")
                .hasArg()
                .withDescription("The maximum time to wait in milliseconds to find a target " +
                		"on the screen (current default value is "+prefsEditor.getMaxWaitTime()+" ms)." )
                .create("wait");
		
		Option displayOption=OptionBuilder.withArgName("screen_id")
                .hasArg()
                .withDescription("The id of the connected screen/monitor " +
                		"(current default value is "+prefsEditor.getDisplayId()+")." )
                .create("screen");
		
		Option precisionOption=OptionBuilder.withArgName("minscore_value")
                .hasArg()
                .withDescription("The minimum similar score for a target to be considered " +
                		"a match in the image recognition search. It's a 10-point scale where 1 is the least precise search" +
                		" and 10 is the most precise search. (default is 7). The new value is stored in user preferences." )
                .create("minscore");
		
		Option oldSyntaxOption=OptionBuilder.withArgName("oldsyntax")
                .withDescription("Forces the system to use the old syntax that uses special shapes" +
                		" to represent actions. The syntax is based on the following annotations: Rectangle shape: left click. " +
                		"Rounded rectangle: drag and drop. Frame: double click. Oval: right click. " +
                		"Text Box: Keyboard typing. Cloud: open URL in default browser.")
                .create("oldsyntax");
		
		Option modeOption=OptionBuilder.withArgName("running_mode")
                .hasArg()
                .withDescription("The mode in which sikuli-slides is running. It can be one of the following:" +
                		" automation, tutorial, and help (default is automation)." )
                .create("mode");
		
		Option rangeOption=OptionBuilder.withArgName("range")
				.hasArg()
				.withDescription("The range of the slides to execute. For example, 1-4 means executing slide 1 to slide 4. " +
						"2 means executing slide 2 only. If this option is not specified, all the slides are executed from" +
						" the beginning to the end.")
				.create("range");						
		
		Option helpOption=new Option("help", "print help info.");
		Option versionOption=new Option("version", "print the version number.");
		
		gnuOptions.addOption(helpOption);
		gnuOptions.addOption(versionOption);
		gnuOptions.addOption(waitOption);
		gnuOptions.addOption(displayOption);
		gnuOptions.addOption(precisionOption);
		gnuOptions.addOption(oldSyntaxOption);
		gnuOptions.addOption(modeOption);
		gnuOptions.addOption(rangeOption);
		
		return gnuOptions;
	}
	
	private static void showTextHeader(final OutputStream out){
		String textHeader="sikuli-slides -- accessible visual automation";
		try{
			out.write(textHeader.getBytes());
		}
		catch (IOException ioEx){
			System.out.println(textHeader);
		}
	}
	
	/**
	 * print usage information to provided OutputStream
	 * @param applicationName Name of application to list in usage
	 * @param options Command-line options to be part of usage
	 * @param out OutputStream to which to write the usage information
	 */
	public static void printUsage(final String applicationName, final Options options,final OutputStream out)
	{
		final PrintWriter writer = new PrintWriter(out);  
	    final HelpFormatter usageFormatter = new HelpFormatter();  
	    usageFormatter.printUsage(writer, 120, commandLineSyntax, options);
	    writer.flush();
	}
	
	/**
	 * Write command-line tool help
	 * @param options the possible options for the command-line
	 */	
	private static void printHelp(final Options options){
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(commandLineSyntax, options);
	}
	
	private static void displayBlankLine(){
		try {
			System.out.write(NEW_LINE.getBytes());
		} catch (IOException e) {
			System.out.println();
		}
	}
	
	public static RunOptions runCommandLineTool(final String[] args){
		if (args.length < 1){
			printUsage(applicationName, getGNUCommandLineOptions(), System.out);
		}
		else{
			try{
				return useGNUParser(args);
			}
	    	catch(Exception exception){
				printUsage(applicationName, getGNUCommandLineOptions(), System.out);
				displayBlankLine();
	    	}
		}
		return null;
	}
	
}
