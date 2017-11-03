package view;

import java.awt.Color;
import java.awt.Component;

import java.awt.Dimension;


import java.awt.Font;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTree;

import javax.swing.event.TreeModelListener;


import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

import model.Activity;
import model.Job;
import model.Project;

@SuppressWarnings("serial")
public class TreePanel extends JPanel {
	
	private JTree viewTree;
	
	private DefaultTreeModel modelTree;
	private DefaultMutableTreeNode treeRoot;


	private ArrayList<Project> treeProjects;
	
	public TreePanel() {
		
		treeProjects = new ArrayList<Project>();
		buildTree();
	}
	
	public void buildTree() {

		treeRoot = new DefaultMutableTreeNode("Manager Projects");
		populateTree();
		
		modelTree = new DefaultTreeModel(treeRoot);
		viewTree = new JTree(modelTree);
		viewTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		// Tree Border
		viewTree.setBorder(BorderFactory.createLoweredBevelBorder());
		viewTree.setExpandsSelectedPaths(true);
		
		// Tree BackGround Colour
		viewTree.setBackground(Color.getHSBColor(0.1f, 0.3f, 0.9f));

		// Fonts
		viewTree.setFont(new Font("Arial Black", Font.BOLD, 18));
		
		// Foreground
		viewTree.setForeground(Color.getHSBColor(3.14f, 0.8f, 0.8f));
		
	    TreeCellRenderer renderer = new MyCellRenderer();
	    viewTree.setCellRenderer(renderer);
/*		
 		expand all tree nodes
 		for (int i = 0; i < viewTree.getRowCount(); i++) {
			viewTree.expandRow(i);
		}
		*/
		add(viewTree);
	}
	
	private void populateTree() {
	
		DefaultMutableTreeNode projectNode = null;
		DefaultMutableTreeNode activityNode = null;

		try {
			for(Project proj: treeProjects){

				projectNode = new DefaultMutableTreeNode(proj);

				ArrayList<Activity> projectActivities = proj.getProjectActivities();
				if(!projectActivities.isEmpty()){
					
					for(Activity act: projectActivities){
						activityNode = new DefaultMutableTreeNode(act);
						projectNode.add(activityNode);
					}
				}
				treeRoot.add(projectNode);
			}
			
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
	
	public void refreshTree() {

		treeRoot.removeAllChildren();
		populateTree();
		reloadTree();
	}
	
	public void reloadTree(){
		modelTree.reload(treeRoot);
	}
	
	public JTree getTree() {
		return viewTree;
	}


	public void addTreeSelectionListener(TreeSelectionListener tsl) {
		viewTree.addTreeSelectionListener(tsl);
	}
	

	public void addTreeSelectionListener(TreeModelListener tml){
		modelTree.addTreeModelListener(tml);
	}


/*	public void addTreeSelectionListener(TreeModelListener tml){
		modelTree.addTreeModelListener(tml);
	}
*/

	public DefaultMutableTreeNode getTreeRoot() {
		return treeRoot;
	}
	
	public DefaultMutableTreeNode searchNode(Job jobNode) 
	{ 
	   if(jobNode == null){
		   return null;
	   }
		DefaultMutableTreeNode node = null; 

	    @SuppressWarnings("unchecked")
		Enumeration<DefaultMutableTreeNode> enumeration= treeRoot.breadthFirstEnumeration(); 
	    while(enumeration.hasMoreElements()) {

	        node = (DefaultMutableTreeNode)enumeration.nextElement(); 
	        if(jobNode.equals(node.getUserObject())) {

	            return node;                          
	        } 
	    } 

	    //tree node with string node found return null 
	    return null; 
	}
	

	public DefaultTreeModel getModelTree() {
		return modelTree;
	}

	public ArrayList<Project> getTreeProjects() {
		return treeProjects;
	}

	public void setTreeProjects(ArrayList<Project> treeProjects) {
		this.treeProjects = treeProjects;
	}
	


	public class MyCellRenderer extends DefaultTreeCellRenderer {

	    @Override
	    public Color getBackgroundNonSelectionColor() {
	        return (null);
	    }

	    @Override
	    public Color getBackgroundSelectionColor() {
	        return Color.GREEN;
	    }

	    @Override
	    public Color getBackground() {

	        return (Color.getHSBColor(0.1f, 0.3f, 0.9f));

	    }

	    @Override
	    public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean sel, final boolean expanded, final boolean leaf, final int row, final boolean hasFocus) {
	        final Component ret = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

/*	        final DefaultMutableTreeNode node = ((DefaultMutableTreeNode) (value));
	        this.setText(value.toString());*/
	        return ret;
	    }
	}


	

}