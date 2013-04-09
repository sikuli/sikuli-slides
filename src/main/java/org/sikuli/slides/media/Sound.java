/**
Khalid
*/
package org.sikuli.slides.media;

public class Sound {
	private String name;
	private String fileName;
	private String relationshipId;
	
	public Sound(){
		
	}
	public Sound (String name, String fileName, String relationshipId){
		this.name=name;
		this.fileName=fileName;
		this.relationshipId=relationshipId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getRelationshipId() {
		return relationshipId;
	}
	public void setRelationshipId(String relationshipId) {
		this.relationshipId = relationshipId;
	}
	
	public String toString(){
		return "Sound info:\n" +"name:"+name+"\n file name:"+fileName+
				"\n relationshipID="+relationshipId;
	}
	
}
