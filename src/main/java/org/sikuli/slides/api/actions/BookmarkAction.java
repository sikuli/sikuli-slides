package org.sikuli.slides.api.actions;

import org.sikuli.slides.api.Context;
import com.google.common.base.Objects;

public class BookmarkAction extends ChainedAction {
	
	private String name;

	@Override
	public void execute(Context context) throws ActionExecutionException {
		Action action = getChild();
		if (action != null){			
			action.execute(context);
		}
	}
	
	public String toString(){
		return Objects.toStringHelper(this).add("name",getName()).toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
