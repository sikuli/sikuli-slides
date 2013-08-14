package org.sikuli.slides.api.actions;

import org.sikuli.slides.api.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;

public class SkipAction extends DefaultAction {
	
	Logger logger = LoggerFactory.getLogger(SkipAction.class);
	
	@Override
	public void execute(Context context) {
		logger.info("skipping " + getChildren());
	}
	
	public String toString(){
		return Objects.toStringHelper(this).add("children",getChildren()).toString();
	}
}
