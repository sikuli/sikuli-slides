package org.sikuli.slides.api.sikuli;

import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;

public interface Hypothesis {
	Target getTarget();
	ScreenRegion interpretResult(ScreenRegion rawResult);
}