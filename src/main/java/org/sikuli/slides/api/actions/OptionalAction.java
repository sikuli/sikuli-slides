package org.sikuli.slides.api.actions;

import org.sikuli.slides.api.Context;

public class OptionalAction extends ChainedAction {

	@Override
	public void execute(Context context) {
		Action action = getChild();
		if (action != null){			
			try {
				action.execute(context);
			} catch (ActionExecutionException e) {
				logger.info("An optinoal action failed: {}, action={}", e.getMessage(), action);
			}
		}
	}
}
