package org.sikuli.slides.api.actions;

import java.util.List;

import org.sikuli.slides.api.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;

public class OptionalAction extends DefaultAction {
	
	Logger logger = LoggerFactory.getLogger(OptionalAction.class);
	
	@Override
	public void execute(Context context) {
		logger.info("executing (optional) " + getChildren());
		List<Action> children = getChildren();
		if (children.size() == 1){
			Action firstChild = children.get(0);
			try {
				firstChild.execute(context);
			} catch (ActionExecutionException e) {
				logger.info("An optinoal action failed: {}, action={}", e.getMessage(), firstChild);
			}
		}
	}
	
	public String toString(){
		return Objects.toStringHelper(this).add("children",getChildren()).toString();
	}
}
