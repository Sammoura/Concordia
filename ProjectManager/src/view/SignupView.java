package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class SignupView extends JFrame {

	private JTextField signupUN;
	private JPasswordField signupPW, signupCPW;
	private JButton cancelBtn, signupBtn;
	private JPanel signupPanel;
	private JComboBox<String> comboBox;
	
	public SignupView(){
		super("My Project Manager");							// calls super constructor in JFrame and assigning our Apps title
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);			// the application doesn't exit on closing by default
		setSize(500,380);										// sets default size for card1
		setVisible(false);										// JFrame is invisible by default
		setResizable(false);

		signupPanel = new JPanel();
		signupPanel.setBackground(Color.getHSBColor(2.5f, 0.8f, 0.7f));
		signupPanel.setLayout(null);


		JLabel signupUNLbl = new JLabel("User Name");
		signupUNLbl.setFont(new Font("Courier New Bold Italic", Font.BOLD, 18));
		signupUNLbl.setBounds(30, 30, 200, 30);
		signupPanel.add(signupUNLbl);

		signupUN = new JTextField(15);
		signupUN.setFont(new Font("Serif Bold", Font.BOLD, 18));
		signupUN.setBounds(250, 30, 200, 30);
		signupPanel.add(signupUN);

		JLabel lblRole = new JLabel("Role");
		lblRole.setFont(new Font("Courier New Bold Italic", Font.BOLD, 18));
		lblRole.setBounds(30, 90, 200, 30);
		signupPanel.add(lblRole);

		String [] roles = {"Project Manager","Team-Member"};
		comboBox = new JComboBox<String>(roles);
		comboBox.setFont(new Font("Serif Bold", Font.BOLD, 18));
		comboBox.setBounds(250, 90, 200, 30);
		signupPanel.add(comboBox);
		getContentPane().add(signupPanel);
		
		JLabel signupPWLbl = new JLabel("Password");
		signupPWLbl.setFont(new Font("Courier New Bold Italic", Font.BOLD, 18));
		signupPWLbl.setBounds(30, 150, 200, 30);
		signupPanel.add(signupPWLbl);

		signupPW = new JPasswordField(15);
		signupPW.setFont(new Font("Serif Bold", Font.BOLD, 18));
		signupPW.setBounds(250, 150, 200, 30);
		signupPanel.add(signupPW);

		JLabel signupCPWLbl = new JLabel("Confirm Password");
		signupCPWLbl.setFont(new Font("Courier New Bold Italic", Font.BOLD, 18));
		signupCPWLbl.setBounds(30, 210, 200, 30);
		signupPanel.add(signupCPWLbl);

		signupCPW = new JPasswordField(15);
		signupCPW.setFont(new Font("Serif Bold", Font.BOLD, 18));
		signupCPW.setBounds(250, 210, 200, 30);
		signupPanel.add(signupCPW);


		signupBtn = new JButton("SIGN UP");
		signupBtn.setFont(new Font("Courier New Bold Italic", Font.BOLD, 18));
		signupBtn.setBounds(30, 280, 200, 40);
		signupPanel.add(signupBtn);

		cancelBtn = new JButton("CANCEL");
		cancelBtn.setFont(new Font("Courier New Bold Italic", Font.BOLD, 18));
		cancelBtn.setBounds(250, 280, 200, 40);
		signupPanel.add(cancelBtn);

	}	
		
	public String getSignupPW() {
		return signupPW.getText();
	}

	public String getSignupCPW() {
		return signupCPW.getText();
	}
	
	public int getRole() {
		if (comboBox.getSelectedItem().toString().equals("Project Manager"))
			return 1;
		return 0;
	}

	public String getSignupUN() {
		return signupUN.getText();
	}

	public void clearSignupForm(){
		signupPW.setText("");
		signupCPW.setText("");
		signupUN.setText("");
	}
	
	public JPanel getSignupPanel() {
		return signupPanel;
	}

	// Adds an action listener to the Sign up Button.
	public void addSignupListener(ActionListener alistener) {
		signupBtn.addActionListener(alistener);
	}
	
	public void addCancelSignUpListener(ActionListener alistener){
		cancelBtn.addActionListener(alistener);
	}

}
