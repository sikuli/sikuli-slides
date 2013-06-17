/**
Khalid
*/
package org.sikuli.slides.guis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.sikuli.slides.utils.Constants;

public class TutorialConrollerUI extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = -7849896402606865992L;
	
	private JButton previousButton;
	private JButton nextButton;
	public TutorialConrollerUI(){
		super("sikuli-slides");
		initUI();
	}
	
	private void initUI() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.black);
		
		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(Color.black);
		
		ImageIcon previousIcon=new ImageIcon(MainUI.class.getResource(Constants.RESOURCES_ICON_DIR+"previous-icon.png"));
		previousButton=new JButton("previous",previousIcon);
		previousButton.setMargin(new Insets(2, 5, 5, 5));
		previousButton.addActionListener(this);
		
		leftPanel.add(previousButton,BorderLayout.WEST);
		panel.add(leftPanel,BorderLayout.WEST);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setBackground(Color.black);
		
		ImageIcon nextIcon=new ImageIcon(MainUI.class.getResource(Constants.RESOURCES_ICON_DIR+"next-icon.png"));
		nextButton=new JButton("next    ",nextIcon);
		nextButton.setMargin(new Insets(2, 5, 5, 5));
		nextButton.addActionListener(this);

		rightPanel.add(nextButton,BorderLayout.EAST);
		panel.add(rightPanel,BorderLayout.EAST);
		
		add(panel);
		
        setTitle("Sikuli-Slides - Tutorial Controller");
        Dimension fullScreenDimension=Toolkit.getDefaultToolkit().getScreenSize();
        Dimension preferedDimension=getPreferredSize();
        setSize(fullScreenDimension.width, preferedDimension.height+20);
        
        setResizable(false);
        setAlwaysOnTop(true);
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==previousButton){
			Constants.IsPreviousStep=true;
		}
		else if(e.getSource()==nextButton){
			Constants.IsNextStep=true;
		}
	}
	
	public static void runUI(){
		// take the menu bar off the jframe
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        // set the name of the application menu item
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Sikuli-Slides - Tutorial Controller");
        // set the look and feel
        try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
                e.printStackTrace();
        } catch (InstantiationException e) {
                e.printStackTrace();
        } catch (IllegalAccessException e) {
                e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
        }
        
        SwingUtilities.invokeLater(new Runnable() {
                public void run(){
                        TutorialConrollerUI tutorialConrollerUI=new TutorialConrollerUI();
                        tutorialConrollerUI.setVisible(true);
                }
        });
	}

}
