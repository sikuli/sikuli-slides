package org.sikuli.slides.api.interpreters;

import org.sikuli.api.ScreenRegion;
import org.sikuli.slides.api.Context;

public interface SpatialRelationship {
	ScreenRegion apply(Context input);
}