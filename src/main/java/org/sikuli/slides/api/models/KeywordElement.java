package org.sikuli.slides.api.models;

import org.sikuli.slides.api.interpreters.Keyword;

import com.google.common.base.Objects;

public class KeywordElement extends SlideElement {
	private Keyword keyword; 
		
	
	public String toString(){
		return Objects.toStringHelper(getClass())
		.add("keyword", keyword)
		.add("super", super.toString()).toString();		
	}


	public Keyword getKeyword() {
		return keyword;
	}


	public void setKeyword(Keyword keyword) {
		this.keyword = keyword;
	}
}
