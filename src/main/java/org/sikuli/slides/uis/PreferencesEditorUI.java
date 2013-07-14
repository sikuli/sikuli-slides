/**
Khalid
*/
package org.sikuli.slides.uis;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.NumberFormatter;
import org.sikuli.api.robot.desktop.DesktopScreen;
import org.sikuli.slides.utils.Constants;
import org.sikuli.slides.utils.UserPreferencesEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PreferencesEditorUI extends JFrame implements ActionListener{
	private static final long serialVersionUID = 5720589766452453287L;
	private UserPreferencesEditor prefs = new UserPreferencesEditor();
	private static final Logger logger = (Logger) LoggerFactory.getLogger(PreferencesEditorUI.class);
	
	private JSpinner maxWaitTimeSpinner, maxLabelDisplayTimeSpinner, instructionHintFontSizeSpinner, 
	canvasWidthSizeSpinner;
	private JButton okButton, cancelButton, restoreButton;
    private JSlider preciseControlSlider;
    private JComboBox displaysComboBox;
	
	public PreferencesEditorUI(){
		super("sikuli-slides -- Preferences");
	}
	private void createAndShowUI() {
		JPanel panel = new JPanel(new BorderLayout());
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.add("General",makeGeneralPanel());
		tabbedPane.add("Tutorial mode settings",makeSettingsPanel());

		panel.add(tabbedPane, BorderLayout.PAGE_START);
		
		JPanel bottomPanel = new JPanel(new FlowLayout());
		okButton = new JButton("OK");
		okButton.addActionListener(this);
		bottomPanel.add(okButton);
		
		restoreButton = new JButton("Restore Defaults");
		restoreButton.addActionListener(this);
		bottomPanel.add(restoreButton);
		
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		bottomPanel.add(cancelButton);
		
		panel.add(bottomPanel, BorderLayout.PAGE_END);
		add(panel);
		
		pack();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setVisible(true);
	}
	private JPanel makeGeneralPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
		JPanel panel = new JPanel(false);
		panel.setLayout(gridBagLayout);
		JLabel maxWaitTimeLabel = new JLabel("Maximum wait time to find target on the screen:");
		
		int currentSpinnerMaxWaitTimeSec = prefs.getMaxWaitTime() / 1000;
		int maxSpinnerMaxWaitTimeSec = 259200; // 72 hours 
		
		SpinnerNumberModel maxWaitTimeSpinnerModel = new SpinnerNumberModel(currentSpinnerMaxWaitTimeSec, 0, maxSpinnerMaxWaitTimeSec, 1);
		maxWaitTimeSpinner = new JSpinner(maxWaitTimeSpinnerModel);
		JFormattedTextField txt = ((JSpinner.NumberEditor) maxWaitTimeSpinner.getEditor()).getTextField();
		((NumberFormatter) txt.getFormatter()).setAllowsInvalid(false);
		
		maxWaitTimeLabel.setLabelFor(maxWaitTimeSpinner);
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		gridBagLayout.setConstraints(maxWaitTimeLabel, constraints);
		
		panel.add(maxWaitTimeLabel);
		
		constraints.gridx = 1;
		gridBagLayout.setConstraints(maxWaitTimeSpinner, constraints);
		panel.add(maxWaitTimeSpinner);
		
		JLabel unitLabel = new JLabel(" seconds ");
		
		constraints.gridx = 2;
		gridBagLayout.setConstraints(unitLabel, constraints);
		panel.add(unitLabel);
		
		constraints.gridx = 0;
		constraints.gridy = 1;
		
		int currentPreciseScoreValue = (int) (prefs.getPreciseSearchScore() * 10);
		final int maxPreciseScoreValue = 10;
		
		JLabel perciseControlLabel=new JLabel("Precision value score that controls how accurate the image search is:");
		
		preciseControlSlider = new JSlider(JSlider.HORIZONTAL,1, maxPreciseScoreValue, currentPreciseScoreValue);
		preciseControlSlider.setPaintLabels(true);
		preciseControlSlider.setPaintTicks(true);
		preciseControlSlider.setMajorTickSpacing(1);
		preciseControlSlider.setMinorTickSpacing(1);
		preciseControlSlider.setMaximumSize(preciseControlSlider.getPreferredSize());
		
		perciseControlLabel.setLabelFor(preciseControlSlider);
		
		gridBagLayout.setConstraints(perciseControlLabel, constraints);
		panel.add(perciseControlLabel);
		
		constraints.gridx = 1;
		gridBagLayout.setConstraints(preciseControlSlider, constraints);
		panel.add(preciseControlSlider);
		
		JLabel displaysLabel=new JLabel("Main Display (select from connected displays):");
		constraints.gridy = 2;
		constraints.gridx = 0;
		
		int numberofDisplays = DesktopScreen.getNumberScreens();
		String [] availableDisplays = new String[numberofDisplays];
		for(int i=0; i<numberofDisplays; i++){
			availableDisplays[i] = Integer.toString(i);
		}
		displaysComboBox = new JComboBox(availableDisplays);
		displaysComboBox.setSelectedIndex(Constants.ScreenId);
		displaysComboBox.addActionListener(this);
		displaysComboBox.setMaximumSize(displaysComboBox.getPreferredSize());
		displaysComboBox.addActionListener(this);
		
		displaysLabel.setLabelFor(displaysComboBox);
		
		gridBagLayout.setConstraints(displaysLabel, constraints);
		panel.add(displaysLabel);
		
		constraints.gridx = 1;
		gridBagLayout.setConstraints(displaysComboBox, constraints);
		panel.add(displaysComboBox);
		
		return panel;
		
	}
	
	private JPanel makeSettingsPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        
		JPanel panel = new JPanel(false);
		panel.setLayout(gridBagLayout);
		
		JLabel canvasDisplayTimeLabel = new JLabel("<html>Time to display a label on the screen:</html>");
		
		int currentSpinnerLabelDisplayTimeSec = prefs.getLabelDisplayTime();
		int maxSpinnerLabelDisplayTimeSec = 21600; // 6 hours 
		
		SpinnerNumberModel maxLabelDisplayTimeModel = new SpinnerNumberModel(currentSpinnerLabelDisplayTimeSec, 0, maxSpinnerLabelDisplayTimeSec, 1);
		maxLabelDisplayTimeSpinner = new JSpinner(maxLabelDisplayTimeModel);
		JFormattedTextField txt = ((JSpinner.NumberEditor) maxLabelDisplayTimeSpinner.getEditor()).getTextField();
		((NumberFormatter) txt.getFormatter()).setAllowsInvalid(false);
		
		canvasDisplayTimeLabel.setLabelFor(maxLabelDisplayTimeSpinner);
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		gridBagLayout.setConstraints(canvasDisplayTimeLabel, constraints);
		
		panel.add(canvasDisplayTimeLabel);
		
		constraints.gridx = 1;
		gridBagLayout.setConstraints(maxLabelDisplayTimeSpinner, constraints);
		panel.add(maxLabelDisplayTimeSpinner);
		
		JLabel unitLabel = new JLabel(" seconds");
		constraints.gridx = 2;
		gridBagLayout.setConstraints(unitLabel, constraints);
		panel.add(unitLabel);
		
		// Instruction hint font size
		JLabel instructionHintFontSizeLabel = new JLabel("Instruction hint (tooltip) font size:");
		
		int currentSpinnerinstructionHintFontSize = prefs.getLabelDisplayTime();
		int maxSpinnerinstructionHintFontSize = 21600; // 6 hours 
		
		SpinnerNumberModel instructionHintFontSizeModel = new SpinnerNumberModel(currentSpinnerinstructionHintFontSize, 0, maxSpinnerinstructionHintFontSize, 1);
		instructionHintFontSizeSpinner = new JSpinner(instructionHintFontSizeModel);
		JFormattedTextField instructionHintFontSizeFormatted = ((JSpinner.NumberEditor) instructionHintFontSizeSpinner.getEditor()).getTextField();
		((NumberFormatter) instructionHintFontSizeFormatted.getFormatter()).setAllowsInvalid(false);
		
		instructionHintFontSizeLabel.setLabelFor(instructionHintFontSizeSpinner);
		
		constraints.gridx = 0;
		constraints.gridy = 1;
		gridBagLayout.setConstraints(instructionHintFontSizeLabel, constraints);
		
		panel.add(instructionHintFontSizeLabel);
		
		constraints.gridx = 1;
		gridBagLayout.setConstraints(instructionHintFontSizeSpinner, constraints);
		
		panel.add(instructionHintFontSizeSpinner);
		
		JLabel unitLabel2 = new JLabel(" points");
		constraints.gridx = 2;
		gridBagLayout.setConstraints(unitLabel2, constraints);
		panel.add(unitLabel2);
		
		// Canvas width size
		// Instruction hint font size
		JLabel CanvasWidthSizeLabel = new JLabel("Canvas width size:");
		
		int currentSpinnerCanvasWidthSize = prefs.getCanvasWidthSize();
		int maxSpinnerCanvasWidthSize = 30; // 30 points 
		
		SpinnerNumberModel CanvasWidthSizeModel = new SpinnerNumberModel(currentSpinnerCanvasWidthSize, 0, maxSpinnerCanvasWidthSize, 1);
		canvasWidthSizeSpinner = new JSpinner(CanvasWidthSizeModel);
		JFormattedTextField CanvasWidthSizeFormatted = ((JSpinner.NumberEditor) canvasWidthSizeSpinner.getEditor()).getTextField();
		((NumberFormatter) CanvasWidthSizeFormatted.getFormatter()).setAllowsInvalid(false);
		
		CanvasWidthSizeLabel.setLabelFor(canvasWidthSizeSpinner);
		
		constraints.gridx = 0;
		constraints.gridy = 2;
		gridBagLayout.setConstraints(CanvasWidthSizeLabel, constraints);
		
		panel.add(CanvasWidthSizeLabel);
		
		constraints.gridx = 1;
		gridBagLayout.setConstraints(canvasWidthSizeSpinner, constraints);
		
		panel.add(canvasWidthSizeSpinner);
		
		JLabel unitLabel3 = new JLabel(" points");
		constraints.gridx = 2;
		gridBagLayout.setConstraints(unitLabel3, constraints);
		panel.add(unitLabel3);
		return panel;
	}
	@Override
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == okButton){
			storeUserPreferences();
		}
		else if(e.getSource() == restoreButton){
			restoreDefaultUserPreferences();
		}
		else if(e.getSource() == cancelButton){
			this.dispose();
		}
		else if(e.getSource() == displaysComboBox){
			Constants.ScreenId = displaysComboBox.getSelectedIndex();
		}
	}
	
	private void restoreDefaultUserPreferences() {
		prefs.putMaxWaitTime(Constants.MAX_WAIT_TIME_MS_DEFAULT);
		maxWaitTimeSpinner.setValue(Constants.MAX_WAIT_TIME_MS_DEFAULT / 1000);
		
		prefs.putLabelDisplayTime(Constants.LABEL_DISPLAY_TIME_SEC_DEFAULT);
		maxLabelDisplayTimeSpinner.setValue(Constants.LABEL_DISPLAY_TIME_SEC_DEFAULT);
		
		prefs.putInstructionHintFontSize(Constants.INSTRUCTION_HINT_FONT_SIZE_DEFAULT);
		instructionHintFontSizeSpinner.setValue(Constants.INSTRUCTION_HINT_FONT_SIZE_DEFAULT);
		
		prefs.putCanvasWidthSize(Constants.CANVAS_WIDTH_SIZE_DEFAULT);
		canvasWidthSizeSpinner.setValue(Constants.CANVAS_WIDTH_SIZE_DEFAULT);
		
		prefs.putPreciseSearchScore( (int) Constants.PRECISE_SEARCH_SCORE_DEFAULT * 10);
		preciseControlSlider.setValue((int) Constants.PRECISE_SEARCH_SCORE_DEFAULT * 10);
		
		
	}
	private void storeUserPreferences() {
		try{
			prefs.putMaxWaitTime((Integer) maxWaitTimeSpinner.getValue() * 1000);
			prefs.putLabelDisplayTime((Integer)maxLabelDisplayTimeSpinner.getValue());
			prefs.putInstructionHintFontSize((Integer) instructionHintFontSizeSpinner.getValue());
			prefs.putCanvasWidthSize((Integer) canvasWidthSizeSpinner.getValue());
			prefs.putPreciseSearchScore(preciseControlSlider.getValue());
			this.dispose();
		}
		catch (Exception e){
			logger.error("Error: Failed to save preferences.");
		}
	}
	public static void showPreferencesEditorUI(){
		PreferencesEditorUI preferencesEditorUI = new PreferencesEditorUI();
		preferencesEditorUI.createAndShowUI();
	}	
}
