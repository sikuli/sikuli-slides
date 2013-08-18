package org.sikuli.slides.api.actions;

import java.util.List;

import org.sikuli.slides.api.Context;
import org.sikuli.slides.api.ExecutionEvent;
import org.sikuli.slides.api.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

abstract public class AbstractAction implements Action{
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	private Action parent;
	private List<Action> actions = Lists.newArrayList();
	
	public void addChild(Action action){
		actions.add(action);
		((AbstractAction) action).setParent(this);
	}
	
	public void removeAllChildren(){
		for (Action action : actions){
			((AbstractAction) action).setParent(null);
		}
		actions.clear();
	}
	
	public Action getChild(int index){
		if (index >= 0 && index < actions.size()){
			return actions.get(index);
		}else{
			return null;
		}
	}
	
	public Action hasChild(int index){
		return actions.get(index);
	}

	
	public List<Action> getChildren(){
		return Lists.newArrayList(actions);
	}
	
	@Override
	public void execute(Context context) throws ActionExecutionException {
		ExecutionListener actionListener = context.getExecutionListener();
		if (actionListener != null){
			actionListener.beforeExecution(new ExecutionEvent(this, context));
		}
		
		logger.debug("executing {}", this);
		try {
			doExecute(context);
		} catch (ActionExecutionException e) {
			throw e;
		}finally{
			if (actionListener != null){
				actionListener.afterExecution(new ExecutionEvent(this, context));
			}			
		}
		
	}
	
	
	abstract protected void doExecute(Context context) throws ActionExecutionException;

	public Action getParent() {
		return parent;
	}

	public void setParent(Action parent) {
		this.parent = parent;
	}
	
}
