package org.sikuli.slides.api.actions;


import java.util.List;

import org.sikuli.slides.api.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class ActionNode {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	private Action action;
	
	private ActionNode parent;
	private List<ActionNode> actions = Lists.newArrayList();
	
	public void addChild(ActionNode child){
		actions.add(child);
		setParent(this);
	}
	
	public void removeAllChildren(){
		for (ActionNode action : actions){
			((ActionNode) action).setParent(null);
		}
		actions.clear();
	}
	
	public ActionNode getChild(int index){
		if (index >= 0 && index < actions.size()){
			return actions.get(index);
		}else{
			return null;
		}
	}
	
	public ActionNode hasChild(int index){
		return actions.get(index);
	}

	
	public List<ActionNode> getChildren(){
		return Lists.newArrayList(actions);
	}

	public ActionNode getParent() {
		return parent;
	}

	public void setParent(ActionNode parent) {
		this.parent = parent;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}
	
	
	/**
	 * Execute and wait for execution to complete
	 */
	public void execute(Context context) throws ActionExecutionException{};
	public void stop(){};

}

