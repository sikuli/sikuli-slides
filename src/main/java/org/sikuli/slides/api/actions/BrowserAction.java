package org.sikuli.slides.api.actions;

import static org.sikuli.api.API.browse;

import java.net.URL;

import org.sikuli.slides.api.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;

public class BrowserAction extends DefaultAction {
	
	Logger logger = LoggerFactory.getLogger(BrowserAction.class);
	
	private URL url;

	@Override
	public void execute(Context context) {
		logger.info("launching the default browser...");
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
