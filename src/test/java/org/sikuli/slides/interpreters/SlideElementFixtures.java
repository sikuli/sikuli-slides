package org.sikuli.slides.interpreters;

import org.sikuli.slides.models.ImageElement;
import org.sikuli.slides.models.SlideElement;

class SlideElementFixtures {

	public SlideElement clickElement;
	public ImageElement imageElement;
	public SlideElement targetElement;
	public SlideElement belowTargetElement;
	public SlideElement aboveTargetElement;
	public SlideElement urlArgumentElement;
	public SlideElement textElement;

	SlideElementFixtures(){
		clickElement = new SlideElement();
		clickElement.setText("click");

		imageElement = new ImageElement();
		imageElement.setSource(getClass().getResource("sikuli_context.png"));
		imageElement.setOffx(100);
		imageElement.setOffy(100);
		imageElement.setCx(1000);
		imageElement.setCy(1000);

		targetElement = new SlideElement(); 
		targetElement.setOffx(348);
		targetElement.setOffy(223);
		targetElement.setCx(200);
		targetElement.setCy(200);		
		targetElement.setTextSize(3600);
		targetElement.setText("target text");

		belowTargetElement = new SlideElement();
		belowTargetElement.setOffx(800);
		belowTargetElement.setOffy(800);
		belowTargetElement.setCx(350);
		belowTargetElement.setCy(350);

		aboveTargetElement = new SlideElement();
		aboveTargetElement.setOffx(50);
		aboveTargetElement.setOffy(50);
		aboveTargetElement.setCx(350);
		aboveTargetElement.setCy(350);
		
		urlArgumentElement = new SlideElement();
		urlArgumentElement.setText("http://path.to");
		
		textElement = new SlideElement();
		textElement.setText("some text");

	}
}