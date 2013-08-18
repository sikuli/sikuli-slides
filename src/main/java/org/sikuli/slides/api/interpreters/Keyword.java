package org.sikuli.slides.api.interpreters;

import java.util.List;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

public class Keyword {
	
	enum Type {
		ACTION,
		CONTROL
	};

	Keyword(String name){
		this.setName(name);

	}
	private String name;
	private Type type;
	private List<String> aliases = Lists.newArrayList();

	boolean isMatched(String textToMatch){
		return match(textToMatch) != null;
	}	

	public String match(String textToMatch){
				
		if (textToMatch == null)
			return null;

		if (textToMatch.toLowerCase().startsWith(getName().toLowerCase())){
			return textToMatch.substring(0, getName().length());
		}

		for (String alias : aliases){
			if (textToMatch.toLowerCase().startsWith(alias.toLowerCase())){
				return textToMatch.substring(0, alias.length());
			}
		}
		return null;
	}	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	static class Builder {
		private Keyword word;
		Builder(String name){
			word = new Keyword(name);
		}
		Builder addAlias(String alias){
			word.aliases.add(alias);
			return this;
		}
		Builder type(Type type){
			word.type = type;
			return this;
		}
		Keyword build(){
			return word;
		}
	}

	static Builder word(String name){
		return new Builder(name);
	}
	
	public String toString(){
		return Objects.toStringHelper("Keyword").add("name", name).add("aliases", aliases).toString();
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

}