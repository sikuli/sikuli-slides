package org.sikuli.slides.api.generators;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.sikuli.slides.api.actions.Action;
import org.sikuli.slides.api.actions.Actions;
import org.sikuli.slides.api.actions.DoubleClickAction;
import org.sikuli.slides.api.actions.AssertExistAction;
import org.sikuli.slides.api.actions.LeftClickAction;
import org.sikuli.slides.api.actions.AssertNotExistAction;
import org.sikuli.slides.api.actions.RightClickAction;
import org.sikuli.slides.api.actions.TargetAction;
import org.sikuli.slides.api.actions.TypeAction;
import org.sikuli.slides.api.sikuli.ContextImageTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import com.google.common.collect.Lists;

public class JavaAPICodeGenerator implements CodeGenerator{

	private Logger logger = LoggerFactory.getLogger(getClass());
	private Images images;
	private String className;

	static class Step {
		public int number;
		public String filename;
		public String actionName;
		public String argument;
		public String type;
	}

	
	/* 
	 * 
	 * @param className the name of the Java class to generate
	 * @imageDir the directory target images will be saved to
	 */
	public JavaAPICodeGenerator(String className, File imageDir){
		this.className = className;
		images = new Images(imageDir);
	}

	static class Images {
		public Images(File dir){
			if (!dir.exists()){
				dir.mkdir();
			}
			this.dir = dir;
		}

		private File dir;
		public File addImage(BufferedImage image, int index){
			File outFile= new File(dir, String.format("image%d.png", index));
			try {
				ImageIO.write(image,  "png", outFile);
				return outFile;
			} catch (IOException e) {
			}
			return null;
		}
	}
	
	public Step generateStep(TargetAction targetAction, int number){
		
		logger.debug("targetAction {}", targetAction);			
		if (targetAction == null)
			return null;

		ContextImageTarget target = (ContextImageTarget) targetAction.getTarget();
		if (target == null)
			return null;;

		BufferedImage targetImage = target.getTargetImage();
		if (targetImage == null)
			return null;;

		File imageFile = images.addImage(targetImage, number);
			
		Action child = targetAction.getChild();
		if (child != null){

			String actionName = "";
			String argument = "";
			if (child instanceof LeftClickAction){
				actionName = "click"; 
			}else if (child instanceof RightClickAction){
				actionName = "rightClick"; 
			}else if (child instanceof DoubleClickAction){
				actionName = "doubleClick";			
			}else if (child instanceof TypeAction){
				actionName = "type";		
				argument = ((TypeAction) child).getText();
			}		

			Step step = new Step();			
			step.type = "targetAction";
			step.number = number;
			step.filename = imageFile.getName();
			step.actionName = actionName;
			step.argument = argument;		
			return step;
		
		}else{
			
			String type ="";
			if (targetAction instanceof AssertExistAction){
				type = "exist";
			}else if (targetAction instanceof AssertNotExistAction){
				type = "notExist";
			}
			
			Step step = new Step();			
			step.type = type;
			step.actionName = type;
			step.number = number;
			step.filename = imageFile.getName();
			return step;			
		
			
		}
		
		
		
	}
	
	@Override
	public boolean generate(List<Action> actions, OutputStream output) {

		List<Step> steps = Lists.newArrayList();
		int number = 1;
		for (Action action : actions){			
			TargetAction targetAction = (TargetAction) Actions.select(action).isInstanceOf(TargetAction.class).first();
			Step step = generateStep(targetAction, number);
			if (step != null){
				steps.add(step);
			}else{				
				step = new Step();
				step.type = "empty";
				step.actionName = action.getClass().getSimpleName();
				step.number = number;
				steps.add(step);
			}		
			number = number + 1;
		}
		

		STGroup g = new STGroupFile("org/sikuli/slides/api/generators/javaapi.stg","utf-8", '$', '$');
		ST st = g.getInstanceOf("main");
		st.add("classname", className);
		st.add("imgdir", images.dir.getPath());
		st.add("list", steps);
		//logger.debug(st.render());		
		try {
			output.write(st.render().getBytes());
		} catch (IOException e) {
			return false;
		}		
		return true;
	}

}
