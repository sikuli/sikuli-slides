package org.sikuli.slides.api.interpreters;

import static org.sikuli.slides.api.interpreters.Keyword.Type.*;
import static org.sikuli.slides.api.interpreters.Keyword.*;

public class KeywordDictionary{
	
	final static Keyword CLICK = word("click").type(ACTION)
			.addAlias("leftclick").addAlias("left-click").build();
	final static Keyword RIGHT_CLICK = word("Right Click").type(ACTION)
			.addAlias("right-click").addAlias("rightclick").build();
	final static Keyword DOUBLE_CLICK = word("Double Click").type(ACTION)
			.addAlias("double-click").addAlias("doubleclick").build();
	final static Keyword TYPE = word("Type").type(ACTION).build();
	final static Keyword DRAG = word("Drag").type(ACTION).build();
	final static Keyword DROP = word("Drop").type(ACTION).build();
	final static Keyword BROWSER = word("Browser").type(ACTION).addAlias("browse").build();
	final static Keyword NOT_EXIST = word("Not Exist").type(ACTION).build();
	final static Keyword EXIST = word("Exist").type(ACTION).build();
	final static Keyword DELAY = word("Delay").type(ACTION)
			.addAlias("sleep").build();
	final static Keyword WAIT = word("Wait").type(ACTION).build();
	
	
	
	final static Keyword SKIP = word("Skip").type(CONTROL).build();
	final static Keyword OPTIONAL = word("Optional").type(CONTROL).build();
	final static Keyword BOOKMARK = word("Bookmark").type(CONTROL).build();
	final static Keyword PAUSE = word("pause").type(ACTION).build();
	final static Keyword CONFIG = word("config").type(CONTROL).build();
	
	final static Keyword[] WORDS = {
		CLICK, RIGHT_CLICK, DOUBLE_CLICK, TYPE, DRAG, DROP, BROWSER, NOT_EXIST, EXIST, DELAY, WAIT,
		SKIP, OPTIONAL, BOOKMARK, PAUSE, CONFIG};
	
	final static public Keyword lookup(String name){
		for (Keyword word : WORDS){
			if (word.isMatched(name))
				return word;
		}
		return null;
	}
}