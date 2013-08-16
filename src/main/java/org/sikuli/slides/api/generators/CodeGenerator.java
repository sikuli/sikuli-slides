package org.sikuli.slides.api.generators;

import java.io.OutputStream;
import java.util.List;

import org.sikuli.slides.api.actions.Action;

public interface CodeGenerator {
		boolean generate(List<Action> actions, OutputStream output);
}
