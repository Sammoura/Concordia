package application;

import javax.swing.SwingUtilities;

import model.MainModel;
import view.MainView;
import controller.MainController;

public class Application {

	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable(){
			
			public void run() {
			
				MainModel mainModel = null;
				try {
					mainModel = MainModel.getInstance();

				} catch (Exception e) {
					e.printStackTrace();
				}
				
				MainView mainView = new MainView();
				
				MainController mainController = new MainController(mainModel, mainView);
			}	
		});
	}
}
