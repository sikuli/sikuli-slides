package org.sikuli.slides.actions;

import static org.sikuli.api.API.browse;

import java.net.URL;

import org.sikuli.slides.api.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrowserAction implements Action {
	
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
}
