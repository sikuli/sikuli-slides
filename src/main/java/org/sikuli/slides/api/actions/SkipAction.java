package org.sikuli.slides.api.actions;

import org.sikuli.slides.api.Context;

import com.google.common.base.Objects;

public class SkipAction extends DefaultAction {
	
	@Override
	public void execute(Context context) {
		logger.info("executing " + this);
	}
	
	public String toString(){
		return Objects.toStringHelper(this).add("children",getChildren()).toString();
	}
}
