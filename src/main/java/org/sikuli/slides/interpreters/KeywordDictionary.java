package org.sikuli.slides.interpreters;

import static org.sikuli.slides.interpreters.Keyword.*;

class KeywordDictionary{
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
		
	
	final static Keyword[] WORDS = {
		word(KeywordDictionary.CLICK).addAlias("leftclick").addAlias("left-click").build(),
		word(KeywordDictionary.RIGHT_CLICK).addAlias("right-click").addAlias("rightclick").build(),	
		word(KeywordDictionary.DOUBLE_CLICK).addAlias("double-click").addAlias("doubleclick").build(),
		word(KeywordDictionary.TYPE).build(),
		word(KeywordDictionary.DRAG).build(),
		word(KeywordDictionary.DROP).build(),
		word(KeywordDictionary.BROWSER).addAlias("browse").build(),
		word(KeywordDictionary.NOT_EXIST).build(),
		word(KeywordDictionary.EXIST).build(),
		word(KeywordDictionary.WAIT).addAlias("delay").build(),
	};
}