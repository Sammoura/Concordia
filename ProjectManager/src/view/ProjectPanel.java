package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.border.Border;

import model.Activity;

@SuppressWarnings("serial")
public class ProjectPanel extends JobPanel{

	private JComboBox<Activity> projectActivitiesCombo;
	private JButton newActivityBtn ;
	
	// Default Constructor
	protected ProjectPanel() {
		
		// Form Border
		Border innerBorder = BorderFactory.createTitledBorder("Project Form");
		Border outerBorder = BorderFactory.createEmptyBorder(15, 15, 15, 15);
		setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder)); 
		
		// creates new activity associated to the picked project
		newActivityBtn = new JButton("New Activity");
		newActivityBtn.setFont(new Font("Times New Roman", Font.BOLD, 18));
		newActivityBtn.setForeground(Color.BLUE);
		
		// displays the picked project activities 
		projectActivitiesCombo = new JComboBox<Activity>();
		projectActivitiesCombo.setFont(new Font("Serif Bold", Font.BOLD, 18));
		projectActivitiesCombo.insertItemAt(null, 0);
		projectActivitiesCombo.setPreferredSize(new Dimension(200, 40));
		projectActivitiesCombo.setEditable(false);
		projectActivitiesCombo.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		
		// Laying out projectActivitiesCombo
		////// Row 7 //////
		setRow(0, 6);
		add(new JLabel("Activities"), getGc());
		
		setRow(1, 6);
		add(projectActivitiesCombo, getGc());
		
		////// Row 8 //////		
		setRow(0, 7);
		add(newActivityBtn, getGc());
			
	}
	
	////////////////////////////////////////////////////////////////	Listeners	\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	
	// adding an action listener to the save button
	public void addSaveListener(ActionListener listener) {
		getSaveBtn().addActionListener(listener);
	}
	
	// adding an action listener to the Save/Edit button
	public void addDeleteListener(ActionListener listener) {
		getDeleteBtn().addActionListener(listener);
	}

	public void addProjectActivitiesComboListener(ItemListener aListener){
		projectActivitiesCombo.addItemListener(aListener);
	}
	
	public void addNewActivityBtnListener(ActionListener aListener){
		newActivityBtn.addActionListener(aListener);
	}
	
	public void addPrereqButtonListener(ActionListener aListener) {
		// TODO Auto-generated method stub
	}

	public void addMemberButtonListener(ActionListener aListener) {
		// TODO Auto-generated method stub
	}
	
	////////////////////////////////////////////////////////////////	Getters and Setters	\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
		
	public JComboBox<Activity> getProjectActivitiesCombo() {
		return projectActivitiesCombo;
	}
	
	public JButton getNewActivityBtn() {
		return newActivityBtn;
	}

	public void setNewActivityBtn(JButton newActivityBtn) {
		this.newActivityBtn = newActivityBtn;
	}
	
	////////////////////////////////////////////////////////////////	Helper Methods	\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	
	public void enableFieldsEdit(boolean bol){
		super.enableFieldsEdit(bol);
		projectActivitiesCombo.setEnabled(!bol);
		newActivityBtn.setEnabled(!bol);
	}

}
