package org.sikuli.slides.api.slideshow;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
	private JComboBox slideList;

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
					slideshow.next();
				}				
			});

			playButton = new JButton("P");
			playButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					if (slideshow.isPaused()){
						slideshow.play();
					}else{
						slideshow.pause();
					}
					
					
				}				
			});


			List<Slide> slides = slideshow.getContent();
			List<String> names = Lists.newArrayList();
			for (Slide slide : slides){
				names.add("Slide " + slide.getNumber());
			}			
			slideList = new JComboBox(names.toArray());
			slideList.setSelectedIndex(0);
			slideList.addItemListener(new ItemListener(){
				@Override
			    public void itemStateChanged(ItemEvent event) {
			       if (event.getStateChange() == ItemEvent.SELECTED) {
			    	   slideshow.jumpTo(slideList.getSelectedIndex());
			       }
			    }  
			});

			add(previousButton);
			add(playButton);
			add(nextButton);
			add(slideList);
		}		
	}

	private final PCanvas canvas;
	final private SlideShowController slideshow;
	public SlideShowViewer(final SlideShowController slideshow) {		
		this.slideshow = checkNotNull(slideshow);
		slideshow.addListener(this);

		canvas = new PCanvas();
		getContentPane().add(canvas);
		JComponent controlBar = new ControlBar();
		getContentPane().add(controlBar, BorderLayout.PAGE_END);
		getContentPane().setPreferredSize(new Dimension(400,300));
		pack();
		setAlwaysOnTop(true);

		addWindowListener(new WindowAdapter(){  
			public void windowClosing(WindowEvent e) {  
				slideshow.quit();
			}
		});
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

		slideList.setSelectedIndex(slide.getNumber()-1);
	}

	@Override
	public void slideExecuted(Slide slide) {
		//setSlide(slide);			
	}

	@Override
	public void slideFinished(Slide slide) {
		playButton.setText("S");
	}

	@Override
	public void slideSelected(Slide slide) {
		setSlide(slide);		
	}

}