package org.sikuli.slides.api.slideshow;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.sikuli.slides.api.actions.Action;
import org.sikuli.slides.api.actions.SlideAction;
import org.sikuli.slides.api.models.Slide;
import org.sikuli.slides.api.views.PSlide;

import com.google.common.collect.Lists;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;

@SuppressWarnings("serial")
public class SlideShowViewer extends JFrame implements SlideShowListener {
	
	private JButton previousButton;
	private JButton nextButton;
	private JButton playButton;
	private JComboBox petList;
	
	class ControlBar extends JPanel {
		
		public ControlBar(){
			setLayout(new FlowLayout());
			
			previousButton = new JButton("<");
			previousButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					slideshow.previous();
				}				
			});
			
			nextButton = new JButton(">");
			nextButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					slideshow.previous();
				}				
			});
			
			playButton = new JButton("P");
			playButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					slideshow.previous();
				}				
			});
			
			
			List<Slide> slides = slideshow.getContent();
			List<String> names = Lists.newArrayList();
			for (Slide slide : slides){
				names.add("Slide " + slide.getNumber());
			}			
			petList = new JComboBox(names.toArray());
			petList.setSelectedIndex(0);
			
//			petList.addActionListener(this);

		    add(previousButton);
		    add(playButton);
		    add(nextButton);
		    add(petList);
		}
		
	}

	 
	private final PCanvas canvas;
	private SlideShowController slideshow;
	public SlideShowViewer(SlideShowController slideshow) {		
		this.slideshow = checkNotNull(slideshow);
		slideshow.addListener(this);
		
		canvas = new PCanvas();
		getContentPane().add(canvas);
		JComponent controlBar = new ControlBar();
		getContentPane().add(controlBar, BorderLayout.PAGE_END);
//		getContentPane().setLayout(mgr)
		getContentPane().setPreferredSize(new Dimension(400,300));
		pack();
		setAlwaysOnTop(true);
	}

	public void setSlide(Slide slide){
		canvas.getLayer().removeAllChildren();

		PNode pSlide = new PSlide(slide);

		// scale to fit the display area
		double xScale = 1.0 * getContentPane().getWidth()/ pSlide.getWidth();
		double yScale = 1.0 * getContentPane().getHeight() / pSlide.getHeight();
		pSlide.scale(Math.min(xScale,yScale));
		pSlide.setTransparency(0.8f);

		canvas.getLayer().addChild(pSlide);
		canvas.repaint();
		
		petList.setSelectedIndex(slide.getNumber()-1);
	}

	@Override
	public void slideStarted(Slide slide) {
		setSlide(slide);			
	}

	@Override
	public void slideFinished(Slide slide) {
		playButton.setText("S");
	}

}