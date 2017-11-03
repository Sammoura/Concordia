package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.border.Border;

import model.Activity;

@SuppressWarnings("serial")
public class ActivityPanel extends JobPanel{

	private JButton prereqBtn;
	private JButton memberBtn;
	
	protected ActivityPanel() {

		// Form Border
		Border innerBorder = BorderFactory.createTitledBorder("Activity Form");
		Border outerBorder = BorderFactory.createEmptyBorder(15, 15, 15, 15);
		setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder)); 
		
		//laying out prerequisites components
		////// Row 7 //////
		setRow(1, 6);
		prereqBtn = new JButton("ADD / REMOVE Prerequisite ");
		prereqBtn.setPreferredSize(new Dimension(220, 40));
		prereqBtn.setForeground(Color.BLUE);
		prereqBtn.setFont(new Font("Times New Roman", Font.BOLD, 14));
		add(prereqBtn, getGc());

		//laying out MemberTeam components
		////// Row 8 //////
		setRow(1, 7);
		memberBtn = new JButton("ADD / REMOVE Member");
		memberBtn.setPreferredSize(new Dimension(220, 40));
		memberBtn.setForeground(Color.getHSBColor(1.85f, 0.9f, 0.9f));
		memberBtn.setFont(new Font("Times New Roman", Font.BOLD, 16));
		add(memberBtn, getGc());

	}

	////////////////////////////////////////////////////////////////	Listeners	\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	
	public void addSaveListener(ActionListener listener) {
		getSaveBtn().addActionListener(listener);
	}

	public void addDeleteListener(ActionListener listener) {
		getDeleteBtn().addActionListener(listener);
	}
	
	@Override
	public void addNewActivityBtnListener(ActionListener aListener) {
		// TODO Auto-generated method stub	
	}

	public void addPrereqButtonListener(ActionListener aListener) {
		prereqBtn.addActionListener(aListener);
	}

	public void addMemberButtonListener(ActionListener aListener) {
		memberBtn.addActionListener(aListener);
	}
	
	public void addProjectActivitiesComboListener(ItemListener aListener) {
		// TODO Auto-generated method stub
	}
	////////////////////////////////////////////////////////////////	Getters and Setters	\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

	public JComboBox<Activity> getProjectActivitiesCombo() {
		return null;
	}

	
	////////////////////////////////////////////////////////////////	Helper Methods	\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	
	public JButton getPrereqBtn() {
		return prereqBtn;
	}

	public JButton getMemberBtn() {
		return memberBtn;
	}

	public void enableFieldsEdit(boolean bol){
		super.enableFieldsEdit(bol);
		prereqBtn.setEnabled(!bol);
		memberBtn.setEnabled(!bol);
	}

	@Override
	public JButton getNewActivityBtn() {
		// TODO Auto-generated method stub
		return null;
	}

}
