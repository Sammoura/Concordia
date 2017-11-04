package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import model.MainModel;
import model.Member;
import view.LoginView;
import view.MainView;
import view.SignupView;

public class LoginController {
	private LoginView loginView;
	private SignupView signupView;
	private MainView mainView;
	private MainModel mainModel;
	private Member currentMember;

	/**
	 * @param loginView
	 */
	public LoginController() {
		loginView = new LoginView();
		signupView = new SignupView();
		try {
			mainModel = MainModel.getInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		registerListeners();
	}

	// inner class to implement loginListener
	private class LoginListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ev) {
			String user = loginView.getUsername();
			String pass = loginView.getPassword();
			try {
				currentMember = mainModel.validateLogin(user, pass);
				if (currentMember == null) { // not a member nor a manager
					JOptionPane.showMessageDialog(null, "Error! Wrong Username and/or Password.");
					loginView.clearLoginForm();
				} else {
					mainView = new MainView();
					mainView.setVisible(true);
					new MainController(mainModel, mainView, currentMember);
					loginView.dispose();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// inner class to implement registerBtnListener
	private class RegisterBtnListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			loginView.setVisible(false);
			signupView.setVisible(true);
		}
	}

	// inner class to implement sign up a new member or project manager
	private class SignupListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			String user = signupView.getSignupUN();
			int role = signupView.getRole();
			String pass = signupView.getSignupPW();
			String confirm = signupView.getSignupCPW();

			if (user.isEmpty() || pass.isEmpty() || confirm.isEmpty())
				JOptionPane.showMessageDialog(null, "Complete all fields");
			else if (!pass.equals(confirm))
				JOptionPane.showMessageDialog(null, "Passwords do not match");
			else {
				try {
					mainModel.addMemberToDatabase(user, pass, role);
					signupView.dispose();
					loginView.setVisible(true);
				} catch (SQLException e) {
					if (e.getMessage().contains("UNIQUE")) {
						JOptionPane.showMessageDialog(null, "This username is already taken!");
						signupView.clearSignupForm();
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error : Member Account was not created! try again.");
					signupView.clearSignupForm();
				}
			}
		}
	}

	// inner class to implement cancel sign up in register form
	private class CancelSignUpListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			signupView.dispose();
			loginView.setVisible(true);
		}
	}

	private void registerListeners() {
		loginView.addLoginListener(new LoginListener());
		loginView.addRegisterBtnListener(new RegisterBtnListener());
		signupView.addSignupListener(new SignupListener());
		signupView.addCancelSignUpListener(new CancelSignUpListener());
	}
}
