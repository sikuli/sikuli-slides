/**
Khalid
 */
package org.sikuli.slides.utils.logging;

import javax.swing.JTextArea;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class TextAreaAppender extends AppenderBase<ILoggingEvent> {
	private JTextArea jTextArea;
	private PatternLayout patternLayout;

	public JTextArea getjTextArea() {
		return jTextArea;
	}

	public void setjTextArea(JTextArea jTextArea) {
		this.jTextArea = jTextArea;
	}

	@Override
	public void start() {
		if (this.patternLayout == null) {
			addError("No pattern layout set for the appender named [" + name + "].");
			return;
		}
		if (this.jTextArea == null) {
			addError("No JTextArea set for the appender named [" + name + "].");
			return;
		}
		super.start();
	}

	@Override
	protected void append(ILoggingEvent event) {
		String msg = this.patternLayout.doLayout(event);
		this.jTextArea.append(msg);
	}

	public PatternLayout getPatternLayout() {
		return patternLayout;
	}

	public void setPatternLayout(PatternLayout patternLayout) {
		this.patternLayout = patternLayout;
	}

}
