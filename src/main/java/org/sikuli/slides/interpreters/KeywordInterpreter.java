package org.sikuli.slides.interpreters;

import java.util.List;

import org.sikuli.slides.models.Slide;
import org.sikuli.slides.models.SlideElement;

import com.google.common.collect.Lists;

class KeywordInterpreter {
	Slide slide;
	private List<KeywordInterpreter.Result> matches;		
	
	public KeywordInterpreter(Slide slide){
		this.slide = slide;
	}
	
	boolean interpret(){				
		matches = Lists.newArrayList();
		for (SlideElement element : slide.getElements()){
			for (Keyword w : KeywordDictionary.WORDS){
				String text = element.getText();
				String matchedText;				
				if ((matchedText = w.match(text)) != null){
					KeywordInterpreter.Result m = new Result();
					m.setKeyword(w);
					m.setSlideElement(element);
					m.setMatchedText(matchedText);												
					String rt = text.substring(matchedText.length(), text.length());						
					m.setRemainingText(rt);
					matches.add(m);
				}
			}
		}
		return true;
	}
	
	List<Result> getResults(){
		return matches;
	}
	
	static class Result {			
		private Keyword keyword;
		private String matchedText;
		private String remainingText;
		private SlideElement slideElement;
		public Keyword getKeyword() {
			return keyword;
		}
		public void setKeyword(Keyword keyword) {
			this.keyword = keyword;
		}
		public String getMatchedText() {
			return matchedText;
		}
		public void setMatchedText(String matchedText) {
			this.matchedText = matchedText;
		}
		public SlideElement getSlideElement() {
			return slideElement;
		}
		public void setSlideElement(SlideElement slideElement) {
			this.slideElement = slideElement;
		}
		public String getRemainingText() {
			return remainingText;
		}
		public void setRemainingText(String remainingText) {
			this.remainingText = remainingText;
		}
	}
}