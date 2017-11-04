package application;

import javax.swing.SwingUtilities;

import controller.LoginController;
import model.MainModel;
import view.LoginView;

public class Application {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new LoginController(new LoginView());
			}
		});
	}
}
