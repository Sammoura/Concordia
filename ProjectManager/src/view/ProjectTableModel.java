package view;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import model.Project;

@SuppressWarnings("serial")
public class ProjectTableModel extends AbstractTableModel {

	private ArrayList<Project> tableProjects;
	private String[] colNames = {"Name" , "Description" , "Budget" , "Start Date" , "Finish Date" , "Status"};
	
	public ProjectTableModel(){
		tableProjects = new ArrayList<Project>();
	}
	
	public void setData(ArrayList<Project> allProjects){
		this.tableProjects = allProjects;
	}
	
	public String getColumnName(int column) {
		
		return colNames[column];
	}

	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 6;
	}

	public int getRowCount() {
		
		if(!tableProjects.isEmpty()){
			return tableProjects.size();
		}
		return 0;
	}

	public Object getValueAt(int row, int col) {
		
		Project project = tableProjects.get(row);
		
		switch(col){
		case 0:	
			return project.getName();
		case 1:
			return project.getDescription();
		case 2:
			return project.getBudget();
		case 3:
			return project.getStartDate();
		case 4:
			return project.getFinishDate();
		case 5:
			return project.getStatus();
		}
		return null;
	}
	
	public void setValueAt(Project proj) {
		System.out.println(this.getRowCount());
		this.setValueAt(proj.getName(), this.getRowCount(), 1);
	}

	public ArrayList<Project> getTableProjects() {
		return tableProjects;
	}

	public void setTableProjects(ArrayList<Project> tableProjects) {
		this.tableProjects = tableProjects;
	}
	
	
}
