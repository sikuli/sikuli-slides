package org.sikuli.slides.apps;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.sikuli.slides.api.Slides;
import org.sikuli.slides.api.actions.Action;
import org.sikuli.slides.api.generators.CodeGenerator;
import org.sikuli.slides.api.generators.JavaAPICodeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sampullara.cli.Args;
import com.sampullara.cli.Argument;

public class GenerateMain {

	private Logger logger = LoggerFactory.getLogger(GenerateMain.class);

	static final String EXE = "java -jar sikuli-slides-1.4.0.jar generate";
	static final String SYNTAX =  "input [options]";

	@Argument(value = "help", description = "Print help message", required = false)
	private boolean help = false;

	@Argument(value = "target", description = "The target language/library to generate code for (default: sikuli-java-api)", required = false)
	private String target = "sikuli-java-api";

	@Argument(value = "name", description = "The name of the program the code will be saved as", required = false)
	private String name = "GeneratedSikuliAPIProgram";

	@Argument(value = "image_dir", description = "The directory target images will be saved to (default: images/)", required = false)
	private String imageDir = "images";

	private String inputFilename;

	CodeGenerator generator;
	
	void parseArgs(String... args) throws IllegalArgumentException {
		List<String> rest = null;
		rest = Args.parse(this, args);
		if (rest == null || rest.size() != 1) {
			//exit("Invalid syntax");
			throw new IllegalArgumentException("missing input");
		}
		
		if (help){
			Args.usage(GenerateMain.class, EXE + "" + SYNTAX);
			return;
		}

		if (target.compareToIgnoreCase("sikuli-java-api")==0){
			generator = new JavaAPICodeGenerator(name, new File(imageDir));
		}else{
			throw new IllegalArgumentException("Unrecognized target: "  + target);
		}

		inputFilename = rest.get(0);		
	}

	public void execute(String... args){
		try{
			parseArgs(args);
		}catch(IllegalArgumentException e){
			System.err.println("Error parsing arguments: " + e.getMessage());
			Args.usage(GenerateMain.class, EXE + "" + SYNTAX);
			return;
		}

		List<Action> actions;
		try {
			actions = Slides.interpret(new File(inputFilename));
			logger.info("{} actions found.", actions.size());
		} catch (IOException e) {
			logger.error("Failed to read from the input file because " + e.getMessage());
			return;
		}

		String outputFilename = String.format("%s.java", name);
		File outputFile = new File(outputFilename);
		try {
			logger.info("Genearting code...");
			FileOutputStream fout = new FileOutputStream(outputFile);
			generator.generate(actions,  fout);
			logger.info("Code generation is complete.");
			logger.info("Generated source file is saved as {}. ", outputFile);
			logger.info("Images are saved in {}", imageDir);
		} catch (FileNotFoundException e) {
			logger.error("Filaed to write the generated source file because " + e.getMessage());
		}
		
	}

	public static void main(String... args) {
		GenerateMain main = new GenerateMain();		
		main.execute(args);
	}

}
