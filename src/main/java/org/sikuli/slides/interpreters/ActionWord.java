package org.sikuli.slides.interpreters;

class ActionWord {
	
	ActionWord(String name){
		this.setName(name);

	}		
	private String name;

	boolean isMatched(String otherName){
		if (otherName == null)
			return false;
		else
			// TODO: this can be modified to allow fuzzy or alias matching
			return getName().compareToIgnoreCase(otherName) == 0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	static class Builder {
		private ActionWord word;
		Builder(String name){
			word = new ActionWord(name);
		}
		ActionWord build(){
			return word;
		}
	}

	static Builder word(String name){
		return new Builder(name);
	}
}