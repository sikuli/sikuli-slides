package org.sikuli.slides.api.actions;

import static org.sikuli.api.API.browse;

import java.net.URL;

import org.sikuli.slides.api.Context;

import com.google.common.base.Objects;

public class BrowserAction extends RobotAction {
	
	private URL url;

	@Override
	protected void doExecute(Context context) {
		if (url != null)
			browse(url);
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}
	
	public String toString(){
		return Objects.toStringHelper(this).add("url",url).toString();
	}
}
