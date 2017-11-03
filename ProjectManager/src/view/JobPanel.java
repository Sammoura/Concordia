package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import model.Activity;
import model.Status;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

public abstract class JobPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// Form basic components
	private JTextField nameFld, budgetFld;
	private JTextArea descriptionArea;
	private JScrollPane descriptionScrollPane;
	private JComboBox<String> statusCombo;
	private JButton saveBtn, deleteBtn;
	
	// for the dates fields
	private JDatePickerImpl startDatePicker ;
	private JDatePickerImpl finishDatePicker ;
	private JDatePanelImpl startDatePanel;
	private JDatePanelImpl finishDatePanel;
	private UtilDateModel startDateModel ;
	private UtilDateModel finishDateModel;
	
	// GridBag
	protected GridBagConstraints gc;
	
	// constructor
	protected JobPanel(){

		// Form dimensions
		Dimension dim = getPreferredSize();
		dim.width = 420;
		setPreferredSize(dim);

		// creating Name text field
		nameFld = new JTextField(15);
		nameFld.setFont(new Font("Serif Bold", Font.BOLD, 16));
		nameFld.setBorder(BorderFactory.createLoweredSoftBevelBorder());
		
		// creating Budget text field
		budgetFld = new JTextField(15);
		budgetFld.setFont(new Font("Serif Bold", Font.BOLD, 16));
		budgetFld.setText("0.0");
		budgetFld.setBorder(BorderFactory.createLoweredSoftBevelBorder());
		
		// setting description area
		descriptionArea = new JTextArea(5,15);
		descriptionArea.setFont(new Font("Serif Bold", Font.BOLD, 16));
		descriptionArea.setText("Write a description");
		descriptionArea.setBorder(BorderFactory.createLoweredBevelBorder());
		descriptionScrollPane = new JScrollPane(descriptionArea);
		
		// setting date panels
		startDateModel = new UtilDateModel();
		finishDateModel = new UtilDateModel();
		
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		
		startDatePanel = new JDatePanelImpl(startDateModel, p);
		finishDatePanel = new JDatePanelImpl(finishDateModel, p);
		
		startDatePicker = new JDatePickerImpl(startDatePanel, new DateLabelFormatter());
		finishDatePicker = new JDatePickerImpl(finishDatePanel, new DateLabelFormatter());
		
		startDatePicker.setBorder(BorderFactory.createRaisedBevelBorder());
		finishDatePicker.setBorder(BorderFactory.createRaisedBevelBorder());
		
		// combo box to show status
		statusCombo = new JComboBox<String>();
		statusCombo.setPreferredSize(new Dimension(200, 30));
		statusCombo.setFont(new Font("Serif Bold", Font.BOLD, 18));
		statusCombo.setEditable(false);
		statusCombo.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		// setting Status combo box
		DefaultComboBoxModel<String> statusModel = new DefaultComboBoxModel<String>();
		statusModel.addElement("LOCKED");
		statusModel.addElement("UNLOCKED");
		statusModel.addElement("IN_PROGRESS");
		statusModel.addElement("FINISHED");
		statusCombo.setModel(statusModel);
		
		// creating Buttons objects
		saveBtn = new JButton("Save");
		saveBtn.setFont(new Font("Times New Roman", Font.BOLD, 18));
		saveBtn.setForeground(Color.red);
		saveBtn.setEnabled(true);
		
		deleteBtn = new JButton("Delete");
		deleteBtn.setFont(new Font("Times New Roman", Font.BOLD, 18));
		deleteBtn.setForeground(Color.getHSBColor(100, 0.2f, 1.3f));
		deleteBtn.setEnabled(false);
				
		// setting form components
		layoutComponents();
	}
	
	// sets labels and text fields for each row (line) inside the JobForm
	public void layoutComponents(){
			setLayout(new GridBagLayout());
			gc = new GridBagConstraints();
			gc.fill = GridBagConstraints.NONE;
			
			////// Row 1 //////
			setRow(0, 0);
			add(new JLabel("NAME"), gc);
			
			setRow(1, 0);
			add(nameFld, gc);
			
			////// Row 2 //////
			setRow(0, 1);
			add(new JLabel("Description"), gc);
			
			setRow(1, 1);
			add(descriptionScrollPane, gc);
			
			////// Row 3 //////
			setRow(0, 2);
			add(new JLabel("Budget"), gc);
			
			setRow(1, 2);
			add(budgetFld, gc);
			
			////// Row 4 //////
			setRow(0, 3);
			add(new JLabel("Start Date"), gc);
			
			setRow(1, 3);
			add(startDatePicker, gc);
			
			////// Row 5 //////
			setRow(0, 4);
			add(new JLabel("Finish Date"), gc);
			
			setRow(1, 4);
			add(finishDatePicker, gc);
			
			////// Row 6 //////
			setRow(0, 5);
			add(new JLabel("Status"), gc);
			
			setRow(1, 5);
			add(statusCombo, gc);
			
			////// last row //////
			setRow(0, 9);
			add(saveBtn, gc);
			
			setRow(1, 9);
			add(deleteBtn, gc);
		}
		
	////////////////////////////////////////////////////////////////	Listeners	\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

	public abstract void addProjectActivitiesComboListener(ItemListener aListener);

	public abstract void addPrereqButtonListener(ActionListener aListener) ;
	
	public abstract void addMemberButtonListener(ActionListener aListener) ;

	public abstract void addNewActivityBtnListener(ActionListener aListener) ;	
	
	// adding an action listener to the save button
	public abstract void addSaveListener(ActionListener listener) ;
	
	// adding an action listener to the Save/Edit button
	public abstract void addDeleteListener(ActionListener listener) ;
	
	////////////////////////////////////////////////////////////////	Getters and Setters	\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\	

	protected GridBagConstraints getGc() {
		return gc;
	}

	protected void setStatusDefault(int def){
		statusCombo.setSelectedIndex(def);
	}

	public String getName() {
		return nameFld.getText();
	}

	public String getDescription() {
		return descriptionArea.getText();
	}
	
	public double getBudget(){
		return Double.parseDouble( budgetFld.getText() );
	}
	
	public String getStartDate(){

		return  startDateModel.getValue().toString();
	}
	
	public String getFinishDate(){

		return  finishDateModel.getValue().toString();
	}
	
	public Status getStatus(){

		return switchToStatus(statusCombo.getSelectedItem().toString());
	}
	
	public JButton getSaveBtn() {
		return saveBtn;
	}
	
	public JButton getDeleteBtn() {
		return deleteBtn;
	}
	
	public void setNameFld(String name) {
		nameFld.setText(name);
	}

	public void setBudgetFld(Double budget){
		budgetFld.setText(budget.toString());
	}
	
	public void setDescriptionArea(String description){
		descriptionArea.setText(description);
	}
			
	public void setStartDate(String startDate){
				
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.CANADA);
		try {
			cal.setTime(sdf.parse(startDate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// all done
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		startDatePicker.getModel().setDate(year, month, day);
		
	}
	
	public void setFinishDate(String finishDate){
			
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.CANADA);
		try {
			cal.setTime(sdf.parse(finishDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		finishDatePicker.getModel().setDate(year, month, day);
		
	}
	
	public void setStatus(Status status){
		statusCombo.getModel().setSelectedItem(status);
	}

	public abstract JComboBox<Activity> getProjectActivitiesCombo();

	public abstract JButton getNewActivityBtn() ;
	
	////////////////////////////////////////////////////////////////	Helper Methods	\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	
	// private method used to switch int to Enum Status
	private Status switchToStatus(String status){
		
		switch(status){
			case "LOCKED":
				return Status.LOCKED;
			case "UNLOCKED":
				return Status.UNLOCKED;
			case "IN_PROGRESS":
				return Status.IN_PROGRESS;
			case "FINISHED":
				return Status.FINISHED;
			default:
				return Status.UNLOCKED;
		}	
		
	}
	
	// clears Form
	public void clearForm(){
		nameFld.setText("");
		budgetFld.setText("0.0");
		descriptionArea.setText("");
		Calendar cal = Calendar.getInstance();
		startDateModel.setValue(cal.getTime());
		finishDateModel.setValue(cal.getTime());
		statusCombo.setSelectedIndex(0);
	}
	
	// Internal Checking the form before submitting, used in mainController
	public int isJobFormReady(){
		
		Calendar cal = Calendar.getInstance();
		
		// Check to Fill all Fields
		if(nameFld.getText().equals("") || budgetFld.getText().equals("") || descriptionArea.getText().equals("")){
			return 0;
		}
		
		if( startDateModel.getValue().after(finishDateModel.getValue())){
			return 1;
		}
		
		if(getStatus().equals(Status.IN_PROGRESS)  &&  startDateModel.getValue().after(cal.getTime()) ){
			return 2;
		}
		
		if(getStatus().equals(Status.FINISHED)  &&  finishDateModel.getValue().after(cal.getTime())){
			return 3;
		}
					
		return -1;	
	}
	
/// no longer needed, replaced by a method in Job class
/*	public boolean startInPast(){
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date); 
		cal.add(Calendar.DATE, -1);
		date = cal.getTime();
		if( startDateModel.getValue().before(cal.getTime()) ){
			return true;
		}
		return false;
	}*/
	
	public void enableFieldsEdit(boolean bol){
		nameFld.setEditable(bol);
		budgetFld.setEditable(bol);
		descriptionArea.setEditable(bol);
		startDatePicker.setTextEditable(bol);
		finishDatePicker.setTextEditable(bol);
		statusCombo.setEnabled(bol);
		if(!bol){
			setFieldsForeground(Color.red);
		}
		else {
			setFieldsForeground(Color.blue);
		}
	}
	
	public void setFieldsForeground(Color c){
		nameFld.setForeground(c);
		budgetFld.setForeground(c);
		descriptionArea.setForeground(c);
		startDatePicker.setForeground(c);
		finishDatePicker.setForeground(c);
		statusCombo.setForeground(c);
	}
	// positions labels and text fields for each row (line) inside the JobForm
	protected void setRow(int col, int row){
		gc.gridx = col;
		gc.gridy = row;
		gc.weightx = 0.5;
		gc.weighty = 0.1;
		gc.insets = new Insets(0, 0, 0, 5);
		gc.anchor = GridBagConstraints.CENTER;
	}
	
	// helper private class to implement Date Picker
	public class DateLabelFormatter extends AbstractFormatter {	

		private static final long serialVersionUID = 1L;
	    private SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.CANADA);

	    @Override
	    public Object stringToValue(String text) throws ParseException {
	        return dateFormatter.parseObject(text);
	    }
	    @Override
	    public String valueToString(Object value) throws ParseException {
	        if (value != null) {
	            Calendar cal = (Calendar) value;
	            return dateFormatter.format(cal.getTime());
	        }
	        return "";
	    }
	}

	public AbstractButton getPrereqBtn() {
		return null;
	}

	public AbstractButton getMemberBtn() {
		return null;
	}
	
}


