package org.sikuli.slides.apps;


import java.util.Arrays;

public class Main {
	
	public static void exit(){
		String helpMessage =
		"Sikuli Slides commands:\n\n"+ 
		"     execute         execute slides\n" +
		"     record          record actionsas slides\n" +
		"     gui             launch GUI";
		System.err.println(helpMessage);
		System.exit(1);
	}

	public static void main(String[] args) {	
		
		if (args.length >= 1){
			String command = args[0];	
			String[] otherArgs;
			if (args.length >= 2){
				otherArgs = Arrays.copyOfRange(args,1,args.length);
			}else{
				otherArgs = new String[]{};
			}
			if (command.compareToIgnoreCase("execute") == 0){
				ExecuteMain.main(otherArgs);
			}else if (command.compareToIgnoreCase("gui") == 0){
				//MainUI.runGuiTool();			
			}else if (command.compareToIgnoreCase("record") == 0){
				RecorderMain.main(otherArgs);				
			}else{
				System.err.println("[" + command + "] is not a valid command");
				exit();
			}		
		}else{
			exit();
		}
	}
}
