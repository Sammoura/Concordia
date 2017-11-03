package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;

import model.Project;

@SuppressWarnings("serial")
public class TablePanel extends JPanel {

	private JTable table;
	private ProjectTableModel tableModel;
	ArrayList<Project> allProjects;
	
	public TablePanel(){
		
		tableModel = new ProjectTableModel();
		table = new JTable(tableModel);
		
		setLayout(new BorderLayout());

//		table.setColumnSelectionAllowed(true);
	    JTableHeader header = table.getTableHeader();
	    header.setBackground(Color.black);
	    header.setForeground(Color.yellow);
	    header.setFont(new Font("Serif Bold", Font.BOLD, 20));
	    
		table.setRowSelectionAllowed(true);
	    table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    
        table.setBackground(Color.blue);
        table.setForeground(Color.orange);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.BOLD, 16));
		add(new JScrollPane(table), BorderLayout.CENTER);
	}
	
	public void setData(ArrayList<Project> allProjects){
		tableModel.setData(allProjects);
	}
	
	public void refreshTable(){
		tableModel.fireTableDataChanged();
	}

	public ProjectTableModel getTableModel() {
		return tableModel;
	}	
	
	public JTable getTable() {
		return table;
	}

	public void addTableListener(TableModelListener aListener){
		
		tableModel.addTableModelListener(aListener);
	}
	
	public void addListSelectionListener(ListSelectionListener aListener){
		
		table.getSelectionModel().addListSelectionListener(aListener);
	}
}
