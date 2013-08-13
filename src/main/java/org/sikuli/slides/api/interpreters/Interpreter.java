package org.sikuli.slides.api.interpreters;

import org.sikuli.slides.api.actions.Action;
import org.sikuli.slides.api.models.Slide;

public interface Interpreter {
	Action interpret(Slide slide);
}
