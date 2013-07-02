/**
Khalid
*/
package org.sikuli.slides.utils.logging;

import javax.swing.JTextArea;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;


public class TextAreaAppender extends AppenderSkeleton {
	private JTextArea logArea;
	private PatternLayout patternLayout;
	

	
	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean requiresLayout() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void append(LoggingEvent loggingEvent) {
		 myTextArea.append(myPatternLayout.format(loggingEvent));
	}
	
}
