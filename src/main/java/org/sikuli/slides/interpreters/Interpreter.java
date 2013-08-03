package org.sikuli.slides.interpreters;

import org.sikuli.slides.actions.Action;
import org.sikuli.slides.models.Slide;

public interface Interpreter {
	Action interpret(Slide slide);
}
