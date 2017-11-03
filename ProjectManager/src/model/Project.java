package model;

import java.text.ParseException;
import java.util.ArrayList;

public class Project extends Job{
	
	private int pmID;										// ID of project manager owns this project to be implemented by getting the ID of the logged in Project Manager from login page
	private ArrayList <Activity> projectActivities;			// Project Associated Activities
	
	public Project(int pmID, String name, String description, double budget, String startDate, String finishDate, Status status) throws Exception {
		super( name, description, budget, startDate, finishDate, status);
		setPmID(pmID);
		projectActivities = new ArrayList<Activity>();
	}
	

	public Project(Project originalProject) throws Exception{
		super(originalProject);
		this.pmID = originalProject.getPmID();
		this.setProjectActivities(originalProject.getProjectActivities());
	}
	
	/**
	 * @return the projectActivities
	 */
	public ArrayList<Activity> getProjectActivities() {
		return projectActivities;
	}

	/**
	 * @param projectActivities the projectActivities to set
	 */
	public void setProjectActivities(ArrayList <Activity> projectActivities) {

		this.projectActivities = projectActivities;
	}

	/**
	 * @return the pmID
	 */
	
	// associates an Activity with a project
	public void addActivity(Activity newActivity){
		projectActivities.add(newActivity);
	}
	
	public void removeActivity(Activity deletedActivity){			// throws an exception if deletedActivity is not enlisted in projectActivities 
		projectActivities.remove(deletedActivity);
	}
	
	/**
	 * @return the sum of the project's activities budgets
	 */
	public double getActivitiesTotalBudget() {
		
		double total = 0;
	
		if(!this.projectActivities.isEmpty()) {
			for (Activity activity : getProjectActivities())
				total += activity.getBudget();
		}
		return total;
	}

	/**
	 * @return the pmID
	 */
	public int getPmID() {
		return pmID;
	}

	/**
	 * @param pmID the pmID to set
	 */
	public void setPmID(int pmID) {
		this.pmID = pmID;
	}	
	
	@Override
	// to compare projects by ID
	public boolean equals(Object other){
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof Project))return false;
	    Project otherProject = (Project)other;
	    if (this.getID() == otherProject.getID()) return true;
		return false;
	}
	
	/////////////////////////////////////////////////// added on Iteration 3     
	
	public boolean ProjectSuitsActivites(){
		// check if original Activities Dates is suitable to be associated to the parent Project with updated data
		for(Activity act: projectActivities){
			
			if( activityDatesSuitsParent(act) != 0){
				//Updated project dates doesn't suit original Activites dates
				return false;
			}
		}
		return true;
	}
	
	
	// checks dates boundaries before adding an Activity to a parent project
	public int activityDatesSuitsParent(Activity activity) {	// checks dates boundaries before adding an Activity to a parent project
			
			// Check dates boundaries
			// "Adjust: the Activity can't precede its parent project";
			
			try {
				if (sdf.parse(activity.getStartDate()).before(sdf.parse(this.getStartDate()))) {
					return 1;
					}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// 	"Adjust: the Activity Finish date can't follow its parent project Finish date";
			try {
				if (sdf.parse(activity.getFinishDate()).after(sdf.parse(this.getFinishDate()))) {
					return 2;
					}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// otherwise it is okay
			return 0;
		}
	
}
