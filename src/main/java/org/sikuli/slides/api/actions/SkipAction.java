package org.sikuli.slides.api.actions;

import org.sikuli.slides.api.Context;

import com.google.common.base.Objects;

public class SkipAction extends AbstractAction {
	
	@Override
	protected void doExecute(Context context) {
	}
	
	public String toString(){
		return Objects.toStringHelper(this).add("children",getChildren()).toString();
	}
}
