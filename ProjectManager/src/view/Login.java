package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Login extends JFrame {

	private SignupPage signupFrame;
	private JTextField usernameTextField;
	private JTextField PasswordTextField;
	private JButton loginButton, registerBtn;
	private JPanel loginPanel;

	public Login(){
		// MainFrame setup (title, layout, dimensions)
		super("My Project Manager");							// calls super constructor in JFrame and assigning application title
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);			// the application doesn't exit on closing by default
		setSize(500, 380);										// sets default size for card1
		setVisible(true);										// JFrame is invisible by default
		setResizable(false);

		loginPanel = new JPanel();
		loginPanel.setBackground(Color.getHSBColor(2.43f, 0.8f, 0.7f));
		loginPanel.setLayout(null);
		
		
		//Login Panel Attributes
		JLabel usernameLabel = new JLabel("User Name");
		usernameLabel.setFont(new Font("Courier New Bold Italic", Font.BOLD, 18));
		usernameLabel.setBounds(30, 30, 200, 30);
		loginPanel.add(usernameLabel);

		usernameTextField = new JTextField(15);
		usernameTextField.setFont(new Font("Serif Bold", Font.BOLD, 18));
		usernameTextField.setBounds(250, 30, 200, 30);
		usernameTextField.setForeground(Color.getHSBColor(4.86f, 0.9f, 0.9f));
		usernameTextField.setText("a");				// for testing only .... remove
		loginPanel.add(usernameTextField);

		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setFont(new Font("Courier New Bold Italic", Font.BOLD, 18));
		passwordLabel.setBounds(30, 90, 200, 30);
		loginPanel.add(passwordLabel);

		PasswordTextField = new JPasswordField(15);
		PasswordTextField.setFont(new Font("Serif Bold", Font.BOLD, 18));
		PasswordTextField.setBounds(250, 90, 200, 30);
		PasswordTextField.setForeground(Color.getHSBColor(4.86f, 0.9f, 0.9f));
		PasswordTextField.setText("a");			// for testing only .... remove
		loginPanel.add(PasswordTextField);

		loginButton = new JButton("LOG IN");
		loginButton.setFont(new Font("Courier New Bold Italic", Font.BOLD, 18));
		loginButton.setBounds(30, 170, 200, 40);
//		loginButton.setBorder(BorderFactory.createRaisedBevelBorder());
//		loginButton.setBackground(Color.getHSBColor(2.1f, 0.9f, 0.9f));
		loginPanel.add(loginButton);

		signupFrame = new SignupPage();
		registerBtn = new JButton("REGISTER");
		registerBtn.setFont(new Font("Courier New Bold Italic", Font.BOLD, 18));
		registerBtn.setBounds(250, 170, 200, 40);
		loginPanel.add(registerBtn);
		
		getContentPane().add(loginPanel);
	}
	
	public JButton getLoginBtn() {
		return loginButton;
	}

	public String getUsername(){
		return usernameTextField.getText();
	}

	public String getPassword(){
		return PasswordTextField.getText();
	}

	public SignupPage getSignupFrame(){
		return signupFrame;
	}
	
	public void clearLoginForm(){
		usernameTextField.setText("");
		PasswordTextField.setText("");
	}
	

	// adds an action listener to the login button
	public void addLoginListener(ActionListener aListener){

		loginButton.addActionListener(aListener); 
	}
	
	public void addRegisterBtnListener(ActionListener aListener){
		registerBtn.addActionListener(aListener);
	}

}
