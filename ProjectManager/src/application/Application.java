package application;

import javax.swing.SwingUtilities;

import controller.LoginController;

public class Application {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new LoginController();
			}
		});
	}
}
