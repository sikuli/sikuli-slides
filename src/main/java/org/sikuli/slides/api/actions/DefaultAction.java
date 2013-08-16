package org.sikuli.slides.api.actions;

import java.util.List;

import org.sikuli.slides.api.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class DefaultAction implements Action{
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	private List<Action> actions = Lists.newArrayList();
	
	public void addChild(Action action){
		actions.add(action);
	}
	
	public void removeAllChildren(){
		actions.clear();
	}
	
	public Action getChild(int index){
		return actions.get(index);
	}
	
	public List<Action> getChildren(){
		return Lists.newArrayList(actions);
	}

	@Override
	public void execute(Context context) throws ActionExecutionException {
	}

}
