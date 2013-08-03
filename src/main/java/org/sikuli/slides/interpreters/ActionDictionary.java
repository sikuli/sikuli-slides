package org.sikuli.slides.interpreters;

import static org.sikuli.slides.interpreters.ActionWord.*;

class ActionDictionary{
	final static String CLICK = "Click";
	final static String RIGHT_CLICK = "Right Click";
	final static String DOUBLE_CLICK = "Double Click";
	final static String TYPE = "Type";
	final static String DRAG = "Drag";
	final static String DROP = "Drop";
	final static String BROWSER = "Browser";
	final static String NOT_EXIST = "Not Exist";
	final static String EXIST = "Exist";
	final static String WAIT = "Wait";		
		
	
	final static ActionWord[] WORDS = {
		word(ActionDictionary.CLICK).build(),
		word(ActionDictionary.RIGHT_CLICK).build(),	
		word(ActionDictionary.DOUBLE_CLICK).build(),
		word(ActionDictionary.TYPE).build(),
		word(ActionDictionary.DRAG).build(),
		word(ActionDictionary.DROP).build(),
		word(ActionDictionary.BROWSER).build(),
		word(ActionDictionary.NOT_EXIST).build(),
		word(ActionDictionary.EXIST).build(),
		word(ActionDictionary.WAIT).build(),
	};
}