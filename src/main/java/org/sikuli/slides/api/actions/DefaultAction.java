package org.sikuli.slides.api.actions;

import java.util.List;

import org.sikuli.slides.api.Context;

import com.google.common.collect.Lists;

public class DefaultAction implements Action{
	
	private List<Action> actions = Lists.newArrayList();
	
	public void addChild(Action action){
		actions.add(action);
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
