package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.jfree.ui.RefineryUtilities;

import model.Activity;
import model.Job;
import model.MainModel;
import model.Member;
import model.Project;
import model.Status;
import view.ActivityPanel;
import view.EditableList;
import view.GanttView;
import view.LoginView;
import view.MainView;
import view.MemberListView;
import view.ProjectPanel;

// Handles the interaction between the View and the Model
public class MainController {
	
	private MainModel mainModel;
	private MainView mainView;
	private Member currentMember;
	
	private ArrayList<Project> allMemberProjects; 
	private ArrayList<Activity> allMemberActivities;
	private List<ArrayList<Activity>> allProjActs;
	
	private EditableList<Activity> prereqEditable;
	private EditableList<Member> memberEditable;
	
	// the Project selected in the ExistingProjectsCombo box if any
	private Project selectedProject; 
	// the Activity selected in the ProjectActivitiesCombo box if any
	private Activity selectedActivity;
	// reference to the ExistingProjects combo box in the main tool bar 
	private JComboBox<Project> projCombo ;
	// reference to the combo box displaying activities of each project in projectActivities combo box in the Project panel
	private JComboBox<Activity> actCombo ;
	private ProjectPanel projPanel;
	private ActivityPanel actPanel;

	// Default Constructor for Main Controller
	public MainController(MainModel aModel, MainView aView, Member currentMember){

		mainModel = aModel;
		mainView = aView;
		this.currentMember = currentMember;
		
		allMemberProjects = new ArrayList<Project>();
		allMemberActivities = new ArrayList<Activity>();
		allProjActs = new ArrayList< ArrayList<Activity> >();
		
		// to eliminate long expressions redundancy
		projPanel = (ProjectPanel) mainView.getProjectPanel();
		actPanel = (ActivityPanel) mainView.getActivityPanel();
		projCombo = mainView.getExistingProjectsCombo();
		actCombo = projPanel.getProjectActivitiesCombo();
		
		linkAll();
		refreshAll();
		setInitialPosition();
		if( ! currentMember.isManager()){
				disableManagerFeatures();
		}
		// registers all listeners to the view
		registerListeners();
	}
	
	///////////////////////////////////////////////////////////////////	Listeners	\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	//////////////////////// Private Classes for Listeners
	
	
	
	// inner class to implement Save/Edit/Modify Project
	private class SaveProjectListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ev) {
			
			boolean formReady = isFormReady(1);
			if(formReady){
				// get Project data from Project Panel Form
				JButton saveBtn = (JButton) ev.getSource();
				try {
					Project updatedProject = getProjectValues();
					// new project
					if(saveBtn.getText().equalsIgnoreCase("Save")){
						if(updatedProject.startInPast()){
							int reply = JOptionPane.showConfirmDialog(null, "Warning: Start in the past !!!!", "Choose one", JOptionPane.YES_NO_OPTION);
							if(reply == JOptionPane.NO_OPTION)	{	return; }
						}
						addProjectEvent(updatedProject);
					}
					
					else if(saveBtn.getText().equalsIgnoreCase("Edit")){
						projPanel.enableFieldsEdit(true);
						saveBtn.setText("Update");
						projPanel.getDeleteBtn().setText("Cancel");
						actPanel.setEnabled(false);
					}
					
					else if(saveBtn.getText().equalsIgnoreCase("Update")){
						if(updatedProject.startInPast()){
							int reply = JOptionPane.showConfirmDialog(null, "Warning: Start in the past !!!!", "Choose one", JOptionPane.YES_NO_OPTION);
							if(reply == JOptionPane.NO_OPTION)	{	return; }
						}
						if(!updatedProject.ProjectSuitsActivites()){
							JOptionPane.showMessageDialog(null, "Updated project dates doesn't suit original Activites dates \n Adjust Activities first");
						} else {
							mainModel.updateProject(selectedProject, updatedProject);
							JOptionPane.showMessageDialog(null, "Project updated sucessfully");
							saveBtn.setText("Edit");
							projPanel.getDeleteBtn().setText("Delete");
							actCombo.setEnabled(true);
							linkAll();
							Project sProject = selectedProject;
							refreshAll();
							projCombo.setSelectedItem(sProject);
						}
					}
				} catch (Exception e) {
						e.printStackTrace();
					}
			}
		}
	}
	
	// inner class to implement Save/Edit/Modify Activity
	private class SaveActivityListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ev) {
			
			boolean formReady = isFormReady(2);
			if(formReady){
				JButton saveBtn = (JButton) ev.getSource();
				Activity updatedActivity = getActivityValues();
				try {
					if(saveBtn.getText().equalsIgnoreCase("Save")){
						
						if( updatedActivity.startInPast()){
							int reply = JOptionPane.showConfirmDialog(null, "Warning: Start in the past !!!!", "Choose one", JOptionPane.YES_NO_OPTION);
							if(reply == JOptionPane.NO_OPTION)	{	return;}
						}
						if( ! updatedActivity.activityBudgetSuitsParent()){
							int reply = JOptionPane.showConfirmDialog(null, "Warning: The Sum of a Project Activities budgets will exceed their parent project ", "Choose one", JOptionPane.YES_NO_OPTION);
							if(reply == JOptionPane.NO_OPTION){	return; }
						}
						
						int suitsParent = updatedActivity.activityDatesSuitsParent();
						if(suitsParent == 1) {
							JOptionPane.showMessageDialog(null, "Adjust: the Activity start date can't precede its parent project start date");
						}
						else if(suitsParent == 2) {
							JOptionPane.showMessageDialog(null, "Adjust: the Activity Finish date can't follow its parent project Finish date");
						}
						else{
							if(selectedProject.getStatus().equals(Status.LOCKED) && !actPanel.getStatus().equals(Status.LOCKED) ){
								actPanel.setStatus(Status.LOCKED);
								updatedActivity.setStatus(Status.LOCKED);
								JOptionPane.showMessageDialog(null, "Activity Status was set to Locked like its parent");
							}
							addActivityEvent(updatedActivity);
						}
					}
					
					else if(saveBtn.getText().equalsIgnoreCase("Edit")){
						actPanel.enableFieldsEdit(true);
						actPanel.getSaveBtn().setText("Update");
						actPanel.getDeleteBtn().setText("Cancel");
					}
					
					else if(saveBtn.getText().equalsIgnoreCase("Update")){
						
/*						if( updatedActivity.startInPast()){
							int reply = JOptionPane.showConfirmDialog(null, "Warning: Start in the past !!!!", "Choose one", JOptionPane.YES_NO_OPTION);
							if(reply == JOptionPane.NO_OPTION)	{	return;}
						}*/
						if( ! updatedActivity.activityBudgetSuitsParent()){
							int reply = JOptionPane.showConfirmDialog(null, "Warning: The Sum of a Project Activities budgets will exceed their parent project ", "Choose one", JOptionPane.YES_NO_OPTION);
							if(reply == JOptionPane.NO_OPTION) {	return; }
						}
						
						int suitsParent = updatedActivity.activityDatesSuitsParent();
						if(suitsParent == 1) {
							JOptionPane.showMessageDialog(null, "Adjust: the Activity start date can't precede its parent project start date");
						}
						else if(suitsParent == 2) {
							JOptionPane.showMessageDialog(null, "Adjust: the Activity Finish date can't follow its parent project Finish date");
						}
						else if( ! mainModel.activityDatesSuitsAllPrereqs(selectedActivity, updatedActivity) ){
							JOptionPane.showMessageDialog(null, "Adjust: the Activity doesn't suit its prerequisites");
						}
						else if ( ! mainModel.activityDatesSuitsAllSuccessors(selectedActivity, updatedActivity) ) {
							JOptionPane.showMessageDialog(null, "Adjust: the Activity doesn't suit its successors");
						}
						else if( ! mainModel.activityDatesSuitsAllSuccessors(selectedActivity, updatedActivity) ){
							JOptionPane.showMessageDialog(null, "Adjust: the Activity doesn't suit its successors");
						}
						else{
							if(selectedProject.getStatus().equals(Status.LOCKED) ){
								if( actPanel.getStatus().equals(Status.UNLOCKED)){
									JOptionPane.showMessageDialog(null, "Activity Status was set to Locked like its parent");
									actPanel.setStatus(Status.LOCKED);
									updatedActivity.setStatus(Status.LOCKED);
								}
							}
							updatedActivity.setPreReq(selectedActivity.getPreReq());
							mainModel.updateActivity(selectedActivity, updatedActivity);

							JOptionPane.showMessageDialog(null, "Activity updated sucessfully");
							actPanel.getSaveBtn().setText("Edit");
							actPanel.getDeleteBtn().setText("Delete");
							actPanel.enableFieldsEdit(false);
							mainView.getJobsTabbedPane().setSelectedIndex(2);
							mainView.getJobsTabbedPane().setEnabledAt(1, true);
							mainView.getJobsTabbedPane().setEnabledAt(2, true);
							if(prereqEditable != null){
								prereqEditable.dispose();
							}
							linkAll();
							Project sProject = selectedProject;
							Activity sAct = selectedActivity;
							refreshActivitiesCombo(selectedProject.getID());
							refreshTree();
							projCombo.setSelectedItem(sProject);
							actCombo.setSelectedItem(sAct);
						}
					}		
				} catch (Exception e) {
						e.printStackTrace();
					}
			}
		}
	}
		
	// inner class to implement Delete Project
	private class DeleteProjectListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent ev) {
			try {
				JButton delBtn = (JButton) ev.getSource();
				if(delBtn.getText().equalsIgnoreCase("Delete")){
					Project deletedProject = (Project)projCombo.getSelectedItem();
					removeProjectEvent(deletedProject);
					setInitialPosition();
				}
				else if(delBtn.getText().equalsIgnoreCase("Cancel")) {
					if(projCombo.getSelectedIndex() < 0) {				// in case of cancel creating a new project
						setInitialPosition();
						return;
					}
					else {												// in case of cancel editing an existing project
						projPanel.getSaveBtn().setText("Edit");
						delBtn.setText("Delete");
						mainView.getNewProjectItem().setEnabled(true);
						projPanel.enableFieldsEdit(false);
						actPanel.setEnabled(true);
						projCombo.setEnabled(true);
						projCombo.setSelectedItem(selectedProject);
						refreshActivitiesCombo(selectedProject.getID());
						actCombo.setSelectedItem(selectedActivity);
						mainView.getTreePanel().getTree().setEnabled(true);
						mainView.getTablePanel().getTable().setEnabled(true);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	// inner class to implement Delete Activity
	private class DeleteActivityListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent ev) {
			
			JButton delBtn = (JButton) ev.getSource();
			try {
				if(delBtn.getText().equalsIgnoreCase("Delete")){
					Activity deletedActivity = selectedActivity;
					deleteActivityEvent(deletedActivity);
				}
				else if(delBtn.getText().equalsIgnoreCase("Cancel")) {
					
					if(actCombo.getSelectedIndex() < 0) {								// case of cancel creating new Activity
						actPanel.clearForm();
						mainView.getJobsTabbedPane().setSelectedIndex(1);
						mainView.getJobsTabbedPane().setEnabledAt(2,false);
						disableSelecion(true);
					}
					else {																// case of cancel editing an existing Activity
						mainView.getJobsTabbedPane().setEnabledAt(2,true);
						mainView.getJobsTabbedPane().setSelectedIndex(2);
					}
					delBtn.setText("Delete");											// common Behaviour
					actPanel.getSaveBtn().setText("Edit");
					actPanel.getDeleteBtn().setText("Delete");
					actPanel.enableFieldsEdit(false);
					projPanel.getSaveBtn().setText("Edit");
					projPanel.getSaveBtn().setEnabled(true);
					projPanel.getDeleteBtn().setText("Delete");
					projPanel.getDeleteBtn().setEnabled(true);
					projPanel.getNewActivityBtn().setEnabled(true);
					projPanel.setEnabled(true);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	// inner class to implement selecting a project from Existing Projects combo 
	private class ComboListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent ev) {
		    
		    selectedProject = (Project) ev.getItem();
		    if (ev.getStateChange() == ItemEvent.SELECTED) {
				mainView.getJobsTabbedPane().setSelectedIndex(1);
				mainView.getJobsTabbedPane().setEnabledAt(1, true);
				mainView.getJobsTabbedPane().setEnabledAt(2, false);
				fillProjectForm(selectedProject);
				
				int index = ((JComboBox<Project>) ev.getSource()).getSelectedIndex();
				mainView.getTablePanel().getTable().setRowSelectionInterval(index-1, index-1);
				DefaultMutableTreeNode pNode = mainView.getTreePanel().searchNode(selectedProject);
				if(pNode != null){
					TreePath path = new TreePath(pNode.getPath());
					mainView.getTreePanel().getTree().getSelectionModel().addSelectionPath(path);
					mainView.getTreePanel().getTree().expandPath(path);
				}
		    } else if (ev.getStateChange() == ItemEvent.DESELECTED) {
		    	mainView.getJobsTabbedPane().setEnabledAt(1, false);
		    	mainView.getJobsTabbedPane().setEnabledAt(2, false);
				mainView.getJobsTabbedPane().setSelectedIndex(0);
				if(projCombo.getSelectedIndex() == -1){
					selectedProject = null;
					mainView.getTreePanel().getTree().setSelectionRow(0);
//					mainView.getTreePanel().getTree().collapseRow(0);
				}
		    }
		}
	}	
	
	// inner class to implement selecting an Activity from Existing Projects combo 
	private class ActivitiesComboListener implements ItemListener{
		@Override
		public void itemStateChanged(ItemEvent ev) {
			
		    selectedActivity = (Activity) ev.getItem();	 
		    if (ev.getStateChange() == ItemEvent.SELECTED) {
		    	
		    	fillActivityForm(selectedActivity);
		    	mainView.getJobsTabbedPane().setEnabledAt(2, true);
		    	mainView.getJobsTabbedPane().setSelectedIndex(2);
		    	
				DefaultMutableTreeNode aNode = mainView.getTreePanel().searchNode(selectedActivity);
				if(aNode != null){
					TreePath path = new TreePath(aNode.getPath());
					mainView.getTreePanel().getTree().getSelectionModel().addSelectionPath(path);
				} 
		    } else if (ev.getStateChange() == ItemEvent.DESELECTED) {
		      // Item is no longer selected
		    	mainView.getJobsTabbedPane().setEnabledAt(2, false);
		    	mainView.getJobsTabbedPane().setSelectedIndex(1);
		    	
				if(actCombo.getSelectedIndex() == -1){
					selectedActivity = null;
					DefaultMutableTreeNode pNode = mainView.getTreePanel().searchNode(selectedProject);
					if(pNode != null){
						TreePath path = new TreePath(pNode.getPath());
						mainView.getTreePanel().getTree().getSelectionModel().addSelectionPath(path);
					}
				}
		    }
		}
	}
	
	// implements selecting a tree node (single selection only)
	private class SelectionListener implements TreeSelectionListener {
		@Override
		public void valueChanged(TreeSelectionEvent e) {
			DefaultMutableTreeNode node =  (DefaultMutableTreeNode) mainView.getTreePanel().getTree().getLastSelectedPathComponent();
		    if (node == null || node.isRoot()){
		    	mainView.getTablePanel().getTable().clearSelection();
		    	projCombo.setSelectedIndex(-1);
		    	actCombo.setSelectedIndex(-1);
		        return;
		    }
			Job treeSelectedJob = (Job) node.getUserObject();
			
			if(treeSelectedJob instanceof Project){
				projCombo.setSelectedItem((Project) treeSelectedJob);
		    	actCombo.setSelectedIndex(-1);
			}
			else if(treeSelectedJob instanceof Activity){
				Activity sAct = (Activity) treeSelectedJob;
				try {
					selectedProject = mainModel.getProjectByID(sAct.getParentProjectID());
					projCombo.setSelectedItem(selectedProject);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				actCombo.setSelectedItem(sAct);
			}
			else{
		    	mainView.getTablePanel().getTable().clearSelection();
		    	projCombo.setSelectedIndex(-1);
		    	actCombo.setSelectedIndex(-1);
			}
		}
	}
		
	// implements adding a prerequisite activity in Activity form 
	private class PrereqListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(currentMember.isManager()){
				if(prereqEditable != null){			// prevents opening same window several times
					prereqEditable.dispose();
				}
				Project sProject = findProject(selectedProject);
				try {
					ArrayList<Activity> allActivities = sProject.getProjectActivities();
					ArrayList<Activity> prereqList;
					ArrayList<Activity> availableList = new ArrayList<Activity>();
					
					if(selectedActivity != null) {
						Activity sActivity = findActivity(selectedActivity); 
						prereqList =  sActivity.getPreReq();
						
						for(Activity activity: allActivities){
							// I had to override equals in Activity class to make the ArrayList.contains ... work !!!!!
							if( activity.getID() != selectedActivity.getID() && !prereqList.contains(activity)){
									availableList.add(activity);
							}
						}
					} else {		// not used unless we can add a prerequisite activity while creating a new activity ... not recommended 
						prereqList = new ArrayList<Activity>();
						availableList = allActivities;
					}
					prereqEditable = new EditableList<Activity>("*** " + selectedActivity + " *** prerequisites", availableList, prereqList);
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				prereqEditable.addButtonListener(new editableAddPrereqListener());
				prereqEditable.removeButtonListener(new editableRemovePrereqListener());
			} 
		}	
	}
		
	// implements the add button in the Editable list of prerequisite in activity form
	private class editableAddPrereqListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(currentMember.isManager()){
				Activity elt = prereqEditable.getAvailableList().getSelectedValue();
				if(elt != null){
					if(!prereqEditable.getAssignedListModel().contains(elt)){
						
						if(activitySuitsPrereq(selectedActivity, elt)){
							adjustActivityStatus(selectedActivity, elt);
							prereqEditable.getAssignedListModel().addElement(elt);
							selectedActivity.addpreReq(elt);
							elt.addSuccessor(selectedActivity);
							try {
								mainModel.addActivityPrereq(elt.getID() , selectedActivity.getID() );
								mainModel.updateActivity(selectedActivity, selectedActivity);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							prereqEditable.getAvailableListModel().removeElement(elt);
						}
					}
				} 
			}
			prereqEditable.getAvailableList().clearSelection();
			prereqEditable.getAddButton().setEnabled(false);
			prereqEditable.getRemoveButton().setEnabled(false);
		}
	}
		// implements the remove button in the Editable list of prerequisite in activity form
	private class editableRemovePrereqListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(currentMember.isManager()){
				Activity elt = prereqEditable.getAssignedList().getSelectedValue();
				if(elt != null){
					prereqEditable.getAssignedListModel().removeElement(elt);
					selectedActivity.removepreReq(elt);
					try {
						mainModel.removeActivityPrereq(elt.getID() , selectedActivity.getID() );
						prereqEditable.getRemoveButton().setEnabled(false);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					if(!prereqEditable.getAvailableListModel().contains(elt)){
						prereqEditable.getAvailableListModel().addElement(elt);
					}
				}
			}
			prereqEditable.getAssignedList().clearSelection();
			prereqEditable.getAddButton().setEnabled(false);
			prereqEditable.getRemoveButton().setEnabled(false);
		}
	}

	// implements the add/remove member button in the activity form
	private class MemberListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(currentMember.isManager()){
				if(memberEditable != null){			// prevents opening same window several times
					memberEditable.dispose();
				}
				try {
					ArrayList<Member> allMembers = mainModel.getAllTeamMembers();
					ArrayList<Member> membersList;
					ArrayList<Member> availableList = new ArrayList<Member>();
					
					if(selectedActivity != null) {
						Activity sActivity = findActivity(selectedActivity);
						membersList =  sActivity.getActivityTeam();
						for(Member member: allMembers){
							// I had to override equals in Member class to make the ArrayList.contains ... work !!!!!
							if( ! membersList.contains(member)){
									availableList.add(member);
							}
						}
					} else {			// not actually used unless we can add a Member to an activity while creating a new activity ... not recommended 
						membersList = new ArrayList<Member>();
						availableList = allMembers;
					}
					memberEditable = new EditableList<Member>("*** " + selectedActivity + " *** Available & Assigned Team", availableList, membersList);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				memberEditable.addButtonListener(new editableAddMemberListener());
				memberEditable.removeButtonListener(new editableRemoveMemberListener());
			}
			else{				// if a member
				try {
					ArrayList<Member> allMembers = mainModel.getAllTeamMembers();
					ArrayList<Member> membersList;
					ArrayList<Member> availableList = new ArrayList<Member>();
					
					if(selectedActivity != null) {
						Activity sActivity = findActivity(selectedActivity);
						membersList =  sActivity.getActivityTeam();
						for(Member member: allMembers){
							// I had to override equals in Member class to make the ArrayList.contains ... work !!!!!
							if( ! membersList.contains(member)){
									availableList.add(member);
							}
						}
					} else {			// not actually used unless we can add a Member to an activity while creating a new activity ... not recommended 
						membersList = new ArrayList<Member>();
						availableList = allMembers;
					}
					new MemberListView<Member>("*** " + selectedActivity + " *** Assigned Team", membersList);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	// implements the add button in the Editable list of prerequisite in activity form
	private class editableAddMemberListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Member elt = memberEditable.getAvailableList().getSelectedValue();
			if(elt != null){
				if(!memberEditable.getAssignedListModel().contains(elt)){
					memberEditable.getAssignedListModel().addElement(elt);
					selectedActivity.addMemberToTeam(elt);
					try {
						mainModel.addMemberToActivity(elt.getUserID() , selectedActivity.getID() );
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					memberEditable.getAvailableListModel().removeElement(elt);
				}
				memberEditable.getAvailableList().clearSelection();
				memberEditable.getAddButton().setEnabled(false);
				memberEditable.getRemoveButton().setEnabled(false);
			}
		}
	}
	
	// implements the remove button in the Editable list of prerequisite in activity form
	private class editableRemoveMemberListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Member elt = memberEditable.getAssignedList().getSelectedValue();
			if(elt != null){	
				memberEditable.getAssignedListModel().removeElement(elt);
				selectedActivity.removeMemberFromTeam(elt);
				try {
					mainModel.removeMemberFromActivity(elt.getUserID() , selectedActivity.getID() );
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				if(!memberEditable.getAvailableListModel().contains(elt)){
					memberEditable.getAvailableListModel().addElement(elt);
				}
				memberEditable.getAssignedList().clearSelection();
				memberEditable.getAddButton().setEnabled(false);
				memberEditable.getRemoveButton().setEnabled(false);
			}
		}	
	}

	// implements creating a new activity in the project form
	private class NewActivityBtnListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ev) {
			disableSelecion(false);
			JButton newActBtn = (JButton) ev.getSource();
			newActBtn.setEnabled(false);
			refreshActivitiesCombo(selectedProject.getID());
			projPanel.getSaveBtn().setEnabled(false);
			projPanel.getDeleteBtn().setEnabled(false);
			actPanel.getSaveBtn().setText("Save");
			actPanel.getDeleteBtn().setText("Cancel");
			actPanel.enableFieldsEdit(true);
			actPanel.getSaveBtn().setEnabled(true);
			actPanel.getDeleteBtn().setEnabled(true);
			actPanel.getPrereqBtn().setEnabled(false);
			actPanel.getMemberBtn().setEnabled(false);
			actPanel.clearForm();
			mainView.getJobsTabbedPane().setEnabledAt(2, true);
			mainView.getJobsTabbedPane().setSelectedIndex(2);	
		}
	}
	
	// implements creating a new project in the new menu item in the menu bar
	private class NewProjectItemListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			refreshAll();
			mainView.getNewProjectItem().setEnabled(false);
			projCombo.setEnabled(false);
			mainView.getTreePanel().getTree().setEnabled(false);
			mainView.getTablePanel().getTable().setEnabled(false);
			mainView.getJobsTabbedPane().setEnabledAt(1, true);
			mainView.getJobsTabbedPane().setSelectedIndex(1);
			mainView.getJobsTabbedPane().setEnabledAt(2, false);
			projPanel.enableFieldsEdit(true);
			actPanel.enableFieldsEdit(false);
			projPanel.clearForm();
			projPanel.getDeleteBtn().setText("Cancel");
			projPanel.getDeleteBtn().setEnabled(true);
			projPanel.getSaveBtn().setText("Save");
			projPanel.getSaveBtn().setEnabled(true);
			projPanel.getNewActivityBtn().setEnabled(false);
		}
	}
	
	// inner class to implement logoutItemListener
	private class LogoutItemListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			mainView.dispose();
			new LoginController();
		}
	}
	
	// implements exit menu item in the menu bar
	private class ExitItemListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(null, "Confirm Exit");
			System.exit(0);
		}
	}
	
	// implements selecting a single row (No cell selection) from the table
	private class tableRowSelectionListener implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent ev) {
	        int[] selectedRow = mainView.getTablePanel().getTable().getSelectedRows();
        	if(selectedRow.length > 0){
        		projCombo.setSelectedIndex(selectedRow[0]+1);
        	} else {
        		projCombo.setSelectedIndex(-1);
        	}
		}	
	}
	
	// implements gantt chart
	private class GantItemListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				if (selectedProject == null)
				{
					JOptionPane.showMessageDialog(null, "Please select a Project ");
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Project sProject = findProject(selectedProject);
			final GanttView gantView = new GanttView( selectedProject.getName() +  "'s Gantt Chart", sProject);
			gantView.pack();
	        RefineryUtilities.centerFrameOnScreen(gantView);
	        gantView.setVisible(true);
	        gantView.createDataset();
		}
	}
	
	/////////////////////////////////////////////////////////// Helper Methods \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	
	// checks if the project / activity form fields are suitable for creating project / activity
	public boolean isFormReady(int classType){
		int isReady = -1;
		// classType = 1   for a Project form
		if(classType == 1){
			isReady = projPanel.isJobFormReady() ;
		}
		// classType = 1   for an Activity form
		if(classType == 2){
			isReady = actPanel.isJobFormReady() ;
		}
		switch(isReady) {
		case 0:
			JOptionPane.showMessageDialog(null, "Fill all fields");
			return false;
		case 1:
			JOptionPane.showMessageDialog(null, "Finish Date can't percede Start Date");
			return false;
		case 2:
			JOptionPane.showMessageDialog(null, "Status can't be in progress while StartDate is yet to come, Change the Start Date or Change Status");
			return false;
		case 3: 	
			JOptionPane.showMessageDialog(null, "Status can't be Finished while Finish Date is yet to come, Change the Finish Date or Change Status");
			return false;
		}
		return true;
	}
	
	// loads a project fields from the database and displays it in the project form
	public void fillProjectForm(Project sProject){
		projPanel.enableFieldsEdit(false);
		ProjectPanel form = (ProjectPanel) projPanel;
		mainView.ProjectPaneEnabled(true);
		mainView.getJobsTabbedPane().setSelectedIndex(1);
		projPanel.getDeleteBtn().setEnabled(true);
		projPanel.getSaveBtn().setText("Edit");
		form.setNameFld(sProject.getName());
		form.setDescriptionArea(sProject.getDescription());
		form.setBudgetFld(sProject.getBudget());
		form.setStartDate(sProject.getStartDate());
		form.setFinishDate(sProject.getFinishDate());
		form.setStatus(sProject.getStatus());
		refreshActivitiesCombo(sProject.getID());
	}
	
	// loads an activity fields from the database and displays it in the activity form	
	public void fillActivityForm(Activity sActivity){
		actPanel.enableFieldsEdit(false);
		ActivityPanel form = (ActivityPanel) actPanel;
		mainView.ActivityPaneEnabled(true);
		mainView.getJobsTabbedPane().setSelectedIndex(2);
		actPanel.getDeleteBtn().setEnabled(true);
		actPanel.getSaveBtn().setText("Edit");
		
		form.setNameFld(sActivity.getName());
		form.setDescriptionArea(sActivity.getDescription());
		form.setBudgetFld(sActivity.getBudget());
		form.setStartDate(sActivity.getStartDate());
		form.setFinishDate(sActivity.getFinishDate());
		form.setStatus(sActivity.getStatus());
	}

	// invokes the methods:	 setProjectsToMember, inkActivitiesToProjets , linkPrereqsToActivities , linkMemberstoActivites
	public void linkAll() {
		try {
			mainModel.linkAllToMember(currentMember);
			allMemberProjects = currentMember.getMemberProjects();
			allMemberActivities = currentMember.getMemberActivities();
			for(Project proj: allMemberProjects){
				allProjActs.add(proj.getProjectActivities());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// enables initial default features for a manager
	public void setInitialPosition(){
		mainView.getTreePanel().getTree().setEnabled(true);
		mainView.getTablePanel().getTable().setEnabled(true);
		projCombo.setEnabled(true);
		projCombo.setSelectedIndex(-1);
		actCombo.setEnabled(true);
		actCombo.setSelectedIndex(-1);
		projPanel.setEnabled(false);
		projPanel.clearForm();
		actPanel.setEnabled(false);
		actPanel.clearForm();
		mainView.getJobsTabbedPane().setSelectedIndex(0);
		mainView.getJobsTabbedPane().setEnabledAt(1, false);
		mainView.getJobsTabbedPane().setEnabledAt(2, false);
		mainView.getNewProjectItem().setEnabled(true);
		projPanel.getSaveBtn().setText("Save");
		projPanel.getDeleteBtn().setText("Delete");
		actPanel.getSaveBtn().setText("Save");
		actPanel.getDeleteBtn().setText("Delete");
	}
	
	// loads updated member/manager projects into the existingProjects combo box in the main tool bar
	public void refreshProjectsCombo(){
		projCombo.removeAllItems();
		projCombo.insertItemAt(null, 0);

		try {
			if(allMemberProjects != null){
				for(Project proj: allMemberProjects){
					projCombo.addItem(proj);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public void refreshTree(){
		mainView.getTreePanel().updateUI();
		mainView.getTreePanel().setTreeProjects(allMemberProjects);
		mainView.getTreePanel().refreshTree();
	}
	
	public void refreshTable(){
		try {
			mainView.getTablePanel().setData(allMemberProjects);
			mainView.getTablePanel().refreshTable();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void refreshAll(){
		refreshProjectsCombo();
		refreshTable();
		refreshTree();
		refreshReport();
	}
	
	public void refreshActivitiesCombo(int pID){
		actCombo.removeAllItems();
		actCombo.insertItemAt(null, 0);
		try {
			ArrayList<Activity> pActivities = new ArrayList<Activity>() ;
			for(Project proj: allMemberProjects){
				if(proj.equals(selectedProject)){
					pActivities = proj.getProjectActivities();
				}
			}
			if(!pActivities.isEmpty()){
				for(Activity act: pActivities) {
					actCombo.addItem(act);
				}
			}		
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	// give a report of the projects managed by a manager or activities assigned to a team member
	public void refreshReport(){
		 mainView.getReportPanel().setReport("");
		 mainView.getReportPanel().setUserName(currentMember.getUsername());
		 mainView.getReportPanel().getUserNameFld().setEditable(false);
		 String Report;
		 
		 if(currentMember.isManager()){
			 Report = "You Manage: \n___________________\n";
			 
			 if(!allMemberProjects.isEmpty()){
				 for(Project pro: allMemberProjects){
					Report += pro.getName() + "\n";
					mainView.getReportPanel().setReport(Report);
				 }
			 } else {
				 Report += "None";
			 }
		} else {
			Report = "Your Tasks: \n___________________\n";
			if(!allMemberProjects.isEmpty()){
				 for(Project pro: allMemberProjects){
					Report +=  pro.getName() + " :\n\n";
					for(Activity act: pro.getProjectActivities()){
						Report  += act + "	" + act.getStatus() + "\n";
					}
					Report += "___________________\n";
					mainView.getReportPanel().setReport(Report);
				 }
			 } else {
				 Report += "None";
			 }
		}
	}
		
	public Member getCurrentMember() {
		return currentMember;
	}
	
	// used in prereqListener to check if a prerequisite activity suits a specific activity before adding it as a prerequisite
	public boolean activitySuitsPrereq(Activity originalActivity, Activity prereqActivity){
		// "Start date can't precede its prerequisite Finish date"
		if( ! mainModel.activityDatesSuitsPrereq(originalActivity , prereqActivity)) {	
			JOptionPane.showMessageDialog(null, "Activity start date can't precede its prerequisite Finish date");
			return false;
		}
		return true;
	}
	
	// Adjust Activity status to suit its prerequisites
	private void adjustActivityStatus(Activity originalActivity, Activity prereqActivity){
		if ( ! prereqActivity.getStatus().equals(Status.FINISHED)){
			if(! originalActivity.getStatus().equals(Status.LOCKED)){
				JOptionPane.showMessageDialog(null, originalActivity + " is now Locked because " + prereqActivity + " is not Finished");
				actPanel.setStatus(Status.LOCKED);
				originalActivity.setStatus(Status.LOCKED);
			}
		} else {
			if(! originalActivity.getStatus().equals(Status.LOCKED) ){
				if(mainModel.isPrereqDone(originalActivity)){
					JOptionPane.showMessageDialog(null, originalActivity + " is now Locked because prerequisites are not Finished");
					actPanel.setStatus(Status.LOCKED);
					originalActivity.setStatus(Status.LOCKED);
				}
			} else {
				JOptionPane.showMessageDialog(null, "All prerequisites are finished, " + originalActivity + " is ready to start ");
				actPanel.setStatus(Status.UNLOCKED);
				originalActivity.setStatus(Status.UNLOCKED);
			}
		}
	}
	
	// registers all listeners
	public void registerListeners(){
		projPanel.addSaveListener(new SaveProjectListener());
		projPanel.addDeleteListener(new DeleteProjectListener());
		actPanel.addSaveListener(new SaveActivityListener());
		actPanel.addDeleteListener(new DeleteActivityListener());
		mainView.addComboListener(new ComboListener());
		projPanel.addProjectActivitiesComboListener(new ActivitiesComboListener());
		mainView.getTreePanel().addTreeSelectionListener(new SelectionListener());
		actPanel.addPrereqButtonListener(new PrereqListener());
		actPanel.addMemberButtonListener(new MemberListener());
		projPanel.addNewActivityBtnListener(new NewActivityBtnListener());
		mainView.addNewProjectItemListener(new NewProjectItemListener());
		mainView.addLogoutItemListener(new LogoutItemListener());
		mainView.addExitItemListener(new ExitItemListener());
		mainView.getTablePanel().addListSelectionListener(new tableRowSelectionListener());
		mainView.addGantItemListener(new GantItemListener());
	}
	
	// disable manager capabilities for a team member login
	public void disableManagerFeatures(){
		mainView.getNewProjectItem().setEnabled(false);
		mainView.getJobsTabbedPane().setEnabledAt(1, false);
		mainView.getJobsTabbedPane().setEnabledAt(2, false);
		projPanel.enableFieldsEdit(false);
		actPanel.enableFieldsEdit(false);
		projPanel.getSaveBtn().setVisible(false);
		actPanel.getSaveBtn().setVisible(false);
		projPanel.getDeleteBtn().setVisible(false);
		actPanel.getDeleteBtn().setVisible(false);
		projPanel.getNewActivityBtn().setVisible(false);
		actPanel.getMemberBtn().setText("Show Assigned Members");
		DefaultTreeModel model = mainView.getTreePanel().getModelTree();
		DefaultMutableTreeNode root = ((DefaultMutableTreeNode) model.getRoot());
		root.setUserObject("Member Tasks");
		model.nodeChanged(root);
		actPanel.getPrereqBtn().setVisible(false);
		mainView.getLogoutItem().setEnabled(true);
		mainView.getExitItem().setEnabled(true);
		mainView.getGantItem().setEnabled(false);
		mainView.getPertItem().setEnabled(false);
		mainView.getCriticalItem().setEnabled(false);
		mainView.getEarnedItem().setEnabled(false);
	}
	
	public void disableSelecion(boolean bol){
		projCombo.setEnabled(bol);
		mainView.getTreePanel().getTree().setEnabled(bol);
		mainView.getTablePanel().getTable().setEnabled(bol);
		actCombo.setEnabled(bol);
		mainView.getTheMenuBar().getMenu(0).setEnabled(bol);
	}
	
	public void clearSelections(){
		projCombo.setSelectedIndex(-1);
		actCombo.setSelectedIndex(-1);
		mainView.getTreePanel().getTree().clearSelection();
		mainView.getTablePanel().getTable().clearSelection();
	}

	private void addProjectEvent(Project newProj){
		// update database
		try {
			mainModel.addProjectToDb(newProj);
			selectedProject = mainModel.getLastProject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, "Project created sucessfully");
		// update member(manager) projects
		allMemberProjects.add(selectedProject);
		// update table
		mainView.getTablePanel().getTableModel().setData(allMemberProjects);
		mainView.getTablePanel().refreshTable();
		// update projects Combo
		projCombo.addItem(selectedProject);
		// update tree
		mainView.getTreePanel().getTreeRoot().add(new DefaultMutableTreeNode(selectedProject));
		mainView.getTreePanel().reloadTree();
		// update report
		refreshReport();
		// select the new Project in Project Combo
		projCombo.setSelectedItem(selectedProject);
		projPanel.getSaveBtn().setText("Edit");
		projPanel.getDeleteBtn().setText("Delete");
		projPanel.getDeleteBtn().setEnabled(true);
		projPanel.getNewActivityBtn().setEnabled(false);
		mainView.getNewProjectItem().setEnabled(true);
		projPanel.setEnabled(true);
		projPanel.enableFieldsEdit(false);
		projCombo.setEnabled(true);
		mainView.getTreePanel().getTree().setEnabled(true);
		mainView.getTablePanel().getTable().setEnabled(true);
	}
	
	public void removeProjectEvent(Project deletedProj){	
		int reply = JOptionPane.showConfirmDialog(null, "Confirm Delete", "Choose one", JOptionPane.YES_NO_OPTION);
		if(reply == JOptionPane.NO_OPTION) {return;}
		try {
			mainModel.deleteProjectfromDb(deletedProj.getID());
		} catch (Exception e) {
			e.printStackTrace();
		}	
		clearSelections();
		// to unlink activities with a deleted project
		allMemberProjects.remove(deletedProj);
		// update table
		mainView.getTablePanel().getTableModel().setData(allMemberProjects);
		mainView.getTablePanel().refreshTable();
		// update projects Combo
		projCombo.removeItem(deletedProj);
		// update tree
		DefaultMutableTreeNode deletedNode = mainView.getTreePanel().searchNode(deletedProj);
		mainView.getTreePanel().getModelTree().removeNodeFromParent(deletedNode);	
		//refresh report
		refreshReport();
	}

	public void addActivityEvent(Activity newAct) {
		try {
			mainModel.addActivityToDb(newAct);
			selectedActivity = mainModel.getLastActivity();
		} catch (Exception e) {
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, "Activity created sucessfully");
		linkAll();
		// update activities combo
		actCombo.addItem(selectedActivity);
		// update tree
		DefaultMutableTreeNode newActNode = new DefaultMutableTreeNode(selectedActivity);
		DefaultMutableTreeNode node =  (DefaultMutableTreeNode) mainView.getTreePanel().getTree().getLastSelectedPathComponent();
		node.add(newActNode);
		mainView.getTreePanel().reloadTree();
		actCombo.setSelectedItem(selectedActivity);
		actPanel.getSaveBtn().setText("Edit");
		actPanel.getDeleteBtn().setText("Delete");
		actPanel.getSaveBtn().setEnabled(true);
		actPanel.getDeleteBtn().setEnabled(true);
		actPanel.enableFieldsEdit(false);
		projPanel.getDeleteBtn().setText("Delete");
		projPanel.getSaveBtn().setEnabled(true);
		projPanel.getDeleteBtn().setEnabled(true);
		projPanel.enableFieldsEdit(false);
		mainView.getJobsTabbedPane().setEnabledAt(1, true);
		mainView.getJobsTabbedPane().setEnabledAt(2, true);
		mainView.getJobsTabbedPane().setSelectedIndex(2);
		disableSelecion(true);
	}

	public void deleteActivityEvent(Activity deletedAct) {
		int reply = JOptionPane.showConfirmDialog(null, "Confirm Delete", "Choose one", JOptionPane.YES_NO_OPTION);
		if(reply == JOptionPane.NO_OPTION) {return;}
		try {
			mainModel.deleteActivityfromDb(deletedAct.getID());
		} catch (Exception e) {
			e.printStackTrace();
		}
		actCombo.removeItem(deletedAct);
		linkAll();
		DefaultMutableTreeNode deletedNode = mainView.getTreePanel().searchNode(deletedAct);
		mainView.getTreePanel().getModelTree().removeNodeFromParent(deletedNode);	
	}
	
	private Project getProjectValues(){
		String name = projPanel.getName();
		String description = projPanel.getDescription();
		double budget = projPanel.getBudget();
		String startDate = projPanel.getStartDate();
		String finishDate = projPanel.getFinishDate();
		Status status = projPanel.getStatus();
		Project updatedProject = null;
		try {
			updatedProject = new Project(currentMember.getUserID(), name, description, budget, startDate, finishDate, status);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return updatedProject;
	}
	
	private Activity getActivityValues(){
		String name = actPanel.getName();
		String description = actPanel.getDescription();
		double budget = actPanel.getBudget();
		String startDate = actPanel.getStartDate();
		String finishDate = actPanel.getFinishDate();
		Status status = actPanel.getStatus();
		int pID = selectedProject.getID();
		Activity updatedActivity = null;
		try {
			updatedActivity = new Activity(pID, name, description, budget, startDate, finishDate, status);
		} catch (Exception e) {
			e.printStackTrace();
		}
		updatedActivity.setParentProjectID(selectedActivity.getParentProjectID());
		return updatedActivity;
	}
	
	public Project findProject(Project proj){
		for(Project original: allMemberProjects){
			if(original.getID() == proj.getID()){
				return original;
			}
		}
		return proj;
	}
	
	public Activity findActivity(Activity act){
		for(Activity original: allMemberActivities){
			if(original == act){
				return original;
			}
		}
		return act;
	}
	
}