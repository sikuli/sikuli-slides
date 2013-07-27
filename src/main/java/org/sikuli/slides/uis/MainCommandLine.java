/**
Khalid
*/
package org.sikuli.slides.uis;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.GnuParser;
import org.sikuli.api.robot.desktop.DesktopScreen;
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
	private static final String commandLineSyntax = "java -jar "+
			applicationName + "-" + versionNumber+".jar " +
			"Path_to_presentation_file.pptx | URL_to_presentation_file.pptx";
	private static final String NEW_LINE = System.getProperty("line.separator");
	
	/**
	* Parse the command-line arguments as GNU-style long option (one word long option).
	* @param args Command-line arguments
	* @return the location of the .pptx file
	*/
	private static String useGNUParser(final String[] args) throws Exception 
	{
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
	    		int wait=Integer.parseInt(cmd.getOptionValue("max_wait_time_ms"));
	        	prefsEditor.putMaxWaitTime(wait);
	    	}
	    	if (cmd.hasOption("screen")){
	    		int screenId=Integer.parseInt(cmd.getOptionValue("screen_id"));
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
	    		String mode=cmd.getOptionValue("running_mode");
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
	    		int precision=Integer.parseInt(cmd.getOptionValue("minscore_value"));
	    		if(precision> 0 && precision <= 10){
	    			prefsEditor.putPreciseSearchScore(precision);
	    		}
	    		else{
	    			String errorMessage="Invalid minscore value." + NEW_LINE;
	    			System.out.write(errorMessage.getBytes());
	    			throw new Exception();
	    		}
	    	}
	        
	        // check arguments
	        final String[] remainingArguments = cmd.getArgs();
	        if(remainingArguments==null||remainingArguments.length==0){
	        	printUsage(applicationName, getGNUCommandLineOptions(), System.out);
	        	return null;
	        }
	        else if(remainingArguments.length>0){
	        	String argName=remainingArguments[0];
	        	// check if the file is remotely stored in the cloud
	        	if(argName.startsWith("http")){
	        		return argName;
	        	}
	        	else{
	        		// the file is locally stored
	        		MyFileFilter myFileFilter=new MyFileFilter();
	        		File source_file=new File(argName);
	        		if(myFileFilter.accept(source_file)){
	        			if(source_file.exists()){
	        				showTextHeader(System.out);
	        				displayBlankLine();
	        				return source_file.getAbsolutePath();
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
	        else{
	        	printUsage(applicationName, getGNUCommandLineOptions(), System.out);
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
		
		Option helpOption=new Option("help", "print help info.");
		Option versionOption=new Option("version", "print the version number.");
		
		gnuOptions.addOption(helpOption);
		gnuOptions.addOption(versionOption);
		gnuOptions.addOption(waitOption);
		gnuOptions.addOption(displayOption);
		gnuOptions.addOption(precisionOption);
		gnuOptions.addOption(oldSyntaxOption);
		gnuOptions.addOption(modeOption);
		
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
	
	public static String runCommandLineTool(final String[] args){
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
