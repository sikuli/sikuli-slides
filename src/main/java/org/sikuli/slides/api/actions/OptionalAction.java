package org.sikuli.slides.api.actions;

import java.util.List;

import org.sikuli.slides.api.Context;

import com.google.common.base.Objects;

public class OptionalAction extends AbstractAction {
	
	@Override
	public void doExecute(Context context) {
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
