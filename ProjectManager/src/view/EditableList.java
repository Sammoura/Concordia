package view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class EditableList<T> extends JFrame {

    private static final long serialVersionUID = 1L;
	String title;
	Container container;
	JList<T> availableList;
	JList<T> assignedList;
	JButton addButton;
	JButton removeButton;
	DefaultListModel<T> availableListModel ;
	DefaultListModel<T> assignedListModel;
	
	  public EditableList(String title, ArrayList<T> availableType, ArrayList<T> assignedType) {
	    super(title);
	    
	    // List of Members to be assigned to an activity or List of Prereqs to be assigned to an activity
	    availableListModel = new DefaultListModel<T>();
	    for(T elt: availableType){
	    	availableListModel.addElement(elt);
	    }
	    
	    availableList = new JList<T>(availableListModel);	    
	    availableList.setBackground(Color.getHSBColor(0.22f, 0.7f, 0.88f));
	    availableList.setBorder(BorderFactory.createEtchedBorder());
	    JScrollPane scrollListAll = new JScrollPane(availableList);
	    Box availableListBox = new Box(BoxLayout.Y_AXIS);
//	    availableListBox.setSize(200, 200);
//	    availableListBox.setMinimumSize(new Dimension(150, 100));
	    availableListBox.add(scrollListAll);
	    availableListBox.add(new JLabel("Available Members"));

	    // List of Team Members assigned to an activity
	    assignedListModel = new DefaultListModel<T>();
	    for(T elt: assignedType){
	    	assignedListModel.addElement(elt);
	    }
	    
	    assignedList = new JList<T>(assignedListModel);
	    assignedList.setBackground(Color.getHSBColor(0.58f, 0.7f, 0.88f));
	    assignedList.setBorder(BorderFactory.createEtchedBorder());
	    JScrollPane scrollListTeam = new JScrollPane(assignedList);
	    Box assignedListBox = new Box(BoxLayout.Y_AXIS);
	    assignedListBox.setSize(200, 200);
	    assignedListBox.setMinimumSize(new Dimension(150, 100));
	    assignedListBox.add(scrollListTeam);
	    assignedListBox.add(new JLabel("Assigned Members"));
	    
	    // Lists Separator and Buttons
	    Box separatorBox = new Box(BoxLayout.Y_AXIS);
//	    separatorBox.setSize(200, 200);
//	    separatorBox.setMinimumSize(new Dimension(200, 100));
	    addButton = new JButton("====>");
	    addButton.setAlignmentX(CENTER_ALIGNMENT);
	    addButton.setBackground(Color.RED);
	    addButton.setForeground(Color.BLACK);
	    addButton.setEnabled(false);
	    
	    removeButton = new JButton("<====");
	    removeButton.setAlignmentX(CENTER_ALIGNMENT);
	    removeButton.setBackground(Color.LIGHT_GRAY);
	    removeButton.setBackground(Color.PINK);
	    removeButton.setEnabled(false);
	    separatorBox.add(addButton);
	    separatorBox.add(removeButton);
	    
	    // Container for the 2 lists and Separator 
	    container = getContentPane();
	    container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
	    container.add(availableListBox);
	    container.add(separatorBox);
	    container.add(assignedListBox);

	    setSize(600, 300);
	    setMinimumSize(new Dimension(450, 150));
	    setVisible(true);
	        
	    availableList.addListSelectionListener(new ListSelectionListener(){
			@Override
			public void valueChanged(ListSelectionEvent ev) {

					addButton.setEnabled(true);
					removeButton.setEnabled(false);
					assignedList.clearSelection();
			}	
	    });
	    
	    assignedList.addListSelectionListener(new ListSelectionListener(){
			@Override
			public void valueChanged(ListSelectionEvent arg0) {

				addButton.setEnabled(false);
				removeButton.setEnabled(true);
				availableList.clearSelection();
			}
	    });
	    
	  }

	public DefaultListModel<T> getAvailableListModel() {
		return availableListModel;
	}

	public void setAvailableListModel(DefaultListModel<T> availableListModel) {
		this.availableListModel = availableListModel;
	}

	public DefaultListModel<T> getAssignedListModel() {
		return assignedListModel;
	}

	public void setAssignedListModel(DefaultListModel<T> assignedListModel) {
		this.assignedListModel = assignedListModel;
	}

	public JList<T> getAvailableList() {
		return availableList;
	}

	public JList<T> getAssignedList() {
		return assignedList;
	}
		
	public void addButtonListener(ActionListener aListener) {
		// TODO Auto-generated method stub
		addButton.addActionListener(aListener);	
	}
	
	public void removeButtonListener(ActionListener aListener) {
		// TODO Auto-generated method stub
		removeButton.addActionListener(aListener);
	}

	public JButton getAddButton() {
		return addButton;
	}

	public JButton getRemoveButton() {
		return removeButton;
	}
	
	
}
