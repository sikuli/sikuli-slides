package org.sikuli.slides.api.interpreters;

import static org.sikuli.slides.api.interpreters.Keyword.*;

public class KeywordDictionary{
	
	final static Keyword CLICK = word("click").addAlias("leftclick").addAlias("left-click").build();
	final static Keyword RIGHT_CLICK = word("Right Click").addAlias("right-click").addAlias("rightclick").build();
	final static Keyword DOUBLE_CLICK = word("Double Click").addAlias("double-click").addAlias("doubleclick").build();
	final static Keyword TYPE = word("Type").build();
	final static Keyword DRAG = word("Drag").build();
	final static Keyword DROP = word("Drop").build();
	final static Keyword BROWSER = word("Browser").addAlias("browse").build();
	final static Keyword NOT_EXIST = word("Not Exist").build();
	final static Keyword EXIST = word("Exist").build();
	final static Keyword DELAY = word("Delay").addAlias("sleep").addAlias("pause").build();
	final static Keyword WAIT = word("Wait").build();
		
	
	final static Keyword[] WORDS = {
		CLICK, RIGHT_CLICK, DOUBLE_CLICK, TYPE, DRAG, DROP, BROWSER, NOT_EXIST, EXIST, DELAY, WAIT};
	
	final static public Keyword lookup(String name){
		for (Keyword word : WORDS){
			if (word.isMatched(name))
				return word;
		}
		return null;
	}
}