package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class ReportPanel extends JPanel {
		
	protected GridBagConstraints gc;
	
	private JTextField userNameFld;
	private JTextArea reportArea;
	private JLabel userLbl, reportLbl ;
	private JScrollPane reportScrollPane;
	
	public ReportPanel(){
		
		// Form Border
		Border innerBorder = BorderFactory.createTitledBorder("Member Tasks");
		Border outerBorder = BorderFactory.createEmptyBorder(15, 15, 15, 15);
		setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder)); 
		
		setLayout(new GridBagLayout());
		gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.NONE;
		
		// creating Text fields objects
		userNameFld = new JTextField(15);
		userNameFld.setFont(new Font("Serif Bold", Font.BOLD, 16));
		userNameFld.setMargin(new Insets(10,10,10,10));
		userNameFld.setBackground(Color.getHSBColor(0.25f, 0.5f, 0.9f));
		
		userLbl = new JLabel("USER NAME");
		userLbl.setFont(new Font("Serif Bold", Font.BOLD, 16));
		
		reportArea = new JTextArea(15,15);
		reportArea.setFont(new Font("Serif Bold", Font.BOLD, 16));
		reportArea.setMargin(new Insets(10,10,10,10));
		reportArea.setBackground(Color.getHSBColor(0.25f, 0.5f, 0.9f));
		reportScrollPane = new JScrollPane(reportArea);
		reportArea.setEditable(false);
		reportLbl = new JLabel("USER REPORT");
		reportLbl.setFont(new Font("Serif Bold", Font.BOLD, 16));
		
		////// Row 1 //////
		setRow(0, 0);
		add(userLbl, gc);
		
		setRow(1, 0);
		add(userNameFld, gc);
		
		////// Row 2 //////
		setRow(0, 1);
		add(reportLbl, gc);
		
		setRow(1, 1);
		add(reportScrollPane, gc);
				
	}
		
		protected void setRow(int col, int row){
			gc.gridx = col;
			gc.gridy = row;
			gc.weightx = 0.5;
			gc.weighty = 0.05;
			gc.insets = new Insets(0, 0, 0, 5);
			gc.anchor = GridBagConstraints.CENTER;
	}

		public String getUserName() {
			return userNameFld.getText();
		}

		public void setUserName(String userName) {
			userNameFld.setText(userName);
		}

		public JTextArea getReport() {
			return reportArea;
		}

		public void setReport(String report) {
			reportArea.setText(report);
		}

		public JTextField getUserNameFld() {
			return userNameFld;
		}

		public void setUserNameFld(JTextField userNameFld) {
			this.userNameFld = userNameFld;
		}

		public JTextArea getReportArea() {
			return reportArea;
		}

		public void setReportArea(JTextArea reportArea) {
			this.reportArea = reportArea;
		}

		
}
