package org.sikuli.slides.api.interpreters;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.robot.desktop.DesktopScreen;
import org.sikuli.slides.api.Context;
import org.sikuli.slides.api.actions.RobotAction;
import org.sikuli.slides.api.actions.Action;
import org.sikuli.slides.api.actions.ActionExecutionException;
import org.sikuli.slides.api.actions.SequentialAction;
import org.sikuli.slides.api.actions.ConfigAction;
import org.sikuli.slides.api.models.Slide;
import org.sikuli.slides.api.models.SlideElement;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ConfigInterpreter implements Interpreter {

	class SequentialConfigAction extends ConfigAction {
		List<Action> configs = Lists.newArrayList();

		public void addChild(Action action) {
			configs.add(action);	
		}
		
		@Override
		public void execute(Context context) throws ActionExecutionException{
			for (Action config : configs){
				config.execute(context);
			}
		}
		
	}
	
	@Override
	public Action interpret(Slide slide) {
		
		SlideElement keywordElement = slide.select().textMatches("(?i)config").first();
		if (keywordElement == null)
			return null;

		List<Interpreter> interpreters = Lists.newArrayList(
				configMinScoreInterpreter,
				configScreenRegionInterpreter,
				configParamsInterpreter
				);

		SequentialConfigAction seqAction = new SequentialConfigAction();
		for (Interpreter interpreter : interpreters){			
			Action action = interpreter.interpret(slide);
			if (action != null){
				seqAction.addChild(action);
			}			
		}
		return seqAction;
	}

	
	static Interpreter configRangeInterpreter = new Interpreter(){

		@Override
		public Action interpret(Slide slide) {
			SlideElement heading = slide.select().hasText().textContains("RANGE").first();
			if (heading == null)
				return null;
			
			SlideElement startLabel = slide.select().orderByY().print()
					.textContains("Start Slide").below(heading).orderByY().first();
			if (startLabel == null)
				return null;
			
			SlideElement endLabel = slide.select()
					.textContains("End Slide").below(heading).orderByY().first();
			if (endLabel == null)
				return null;
			
						
			SlideElement startValue = slide.select().toRightOf(startLabel).orderByX().hasText()
					.overlapVerticallyWith(startLabel, 0.9f).print().first();
			if (startValue == null)
				return null;
			
			SlideElement endValue = slide.select().toRightOf(endLabel).orderByX().hasText()
					.overlapVerticallyWith(endLabel, 0.9f).print().first();
			if (endValue == null)
				return null;
			
			
			Action action = new ConfigAction(){
				@Override
				public void execute(Context context)
						throws ActionExecutionException {
//					context.setScreenRegion(new DesktopScreenRegion(idToSet));			
				}			
			};
			return action;
		}

	};
	static Interpreter configScreenRegionInterpreter = new Interpreter(){

		@Override
		public Action interpret(Slide slide) {
			SlideElement heading = slide.select().hasText().textContains("SCREEN").first();
			if (heading == null)
				return null;
			
			SlideElement selection = slide.select()
					.hasNoText().nameStartsWith("Rectangle").below(heading).orderByY().first();
			if (selection == null)
				return null;
			
			SlideElement value = slide.select().intersects(selection).hasText().first();
			if (value == null)
				return null;
			
			Pattern pattern = Pattern.compile("(\\d)");
			Matcher matcher = pattern.matcher(value.getText());
			int id = 0;
			if (matcher.find()){
				try {
					id = Integer.parseInt(matcher.group(1));
				}catch (NumberFormatException e) {
					return null;
				}
			}
			final int idToSet = id;

			Action action = new ConfigAction(){
				@Override
				public void execute(Context context)
						throws ActionExecutionException {
					if (idToSet < DesktopScreen.getNumberScreens()){
						context.setScreenRegion(new DesktopScreenRegion(idToSet));
					}
				}			
			};
			return action;
		}

	};
	
	static Interpreter configParamsInterpreter = new Interpreter(){

		@Override
		public Action interpret(Slide slide) {
			SlideElement heading = slide.select().hasText().textContains("PARAMETERS").first();
			if (heading == null)
				return null;
			slide.remove(heading);
			
			List<SlideElement> strings = slide.select().hasText().below(heading).orderByY().all();

			final Map<String,String> map = Maps.newHashMap();
			
			
			for (int i = 0; i < strings.size(); i = i + 2){																
				String key = strings.get(i).getText();				
				SlideElement valElement = strings.get(i+1);
				if (valElement.getLineColor() != null){
					String val = strings.get(i+1).getText();
					map.put(key, val);
				}				
			}
			
			Action action = new ConfigAction(){
				@Override
				public void execute(Context context)
						throws ActionExecutionException {					
					for (Entry<String,String> entry : map.entrySet())
						context.addParameter(entry.getKey(),entry.getValue());
				}			
			};
			
			return action;
		}
	};

	static Interpreter configMinScoreInterpreter = new Interpreter(){
		@Override
		public Action interpret(Slide slide) {
			SlideElement heading = slide.select().hasText().textContains("MIN SCORE").first();
			if (heading == null)
				return null;
		
			SlideElement selection = slide.select()
					.hasNoText().nameStartsWith("Rectangle").below(heading).orderByY().first();
			if (selection == null)
				return null;
			
			SlideElement value = slide.select().intersects(selection).hasText().first();
			if (value == null)
				return null;


			float score = 0;
			try {
				score = Float.parseFloat(value.getText()) / 10f;
			}catch (NumberFormatException e) {
				return null;
			}

			final float scoreToSet = score;						
			Action action = new ConfigAction(){
				@Override
				public void execute(Context context)
						throws ActionExecutionException {
					context.setMinScore(scoreToSet);			
				}			
			};
			return action;
		}

	};

}
