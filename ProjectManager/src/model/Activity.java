package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Activity extends Job{

	private int parentProjectID;
	private ArrayList <Activity> preReq;			// list of prerequisite activities
	private ArrayList <Activity> successors;		// list of Activities that depend on this Activity
	private ArrayList <Member> activityTeam;		// members who are assigned to this activity        			Iteration 2
	// date formatter
	SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.CANADA);
	
	public Activity (int parentProjectID, String name, String description, double budget, String startDate, String finishDate, Status status) throws Exception {
		super(name, description, budget, startDate, finishDate, status);
		setParentProjectID(parentProjectID);
		preReq = new ArrayList <Activity>();
		successors = new ArrayList <Activity>();
		activityTeam = new ArrayList <Member>();
	}

	
	public ArrayList<Member> getActivityTeam() {
		return activityTeam;
	}

	public void setActivityTeam(ArrayList<Member> activityTeam) {
		this.activityTeam = activityTeam;
	}

	public void addMemberToTeam(Member member){
		this.activityTeam.add(member);
	}

	public void removeMemberFromTeam(Member member){
		this.activityTeam.remove(member);
	}
	
	public ArrayList<Activity> getPreReq() {
		return preReq;
	}

	public void setPreReq(ArrayList<Activity> preReq) {
		this.preReq = preReq;
	}
	
	public void addpreReq(Activity newActivity){
		preReq.add(newActivity);
	}
	
	public void removepreReq(Activity deletedActivity){
		preReq.remove(deletedActivity);
	}
	
	public ArrayList<Activity> getSuccessors() {
		return successors;
	}

	public void setSuccessors(ArrayList<Activity> successors) {
		this.successors = successors;
	}

	public void addSuccessor(Activity newActivity){
		successors.add(newActivity);
	}
	
	public void removeSuccessor(Activity deletedActivity){
		successors.remove(deletedActivity);
	}
	
	public int getParentProjectID() {
		return parentProjectID;
	}

	public void setParentProjectID(int parentProjectID) {
		this.parentProjectID = parentProjectID;
	}

	public boolean activityDatesSuitsPrereqs(){
		if(!preReq.isEmpty()){
			for(Activity prereqAct: preReq){
				try {
					if ( sdf.parse(this.getStartDate()).before(sdf.parse(prereqAct.getFinishDate()))) {
						return false;
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}
	
	public boolean activityDatesSuitsSuccessors(){
		if(!preReq.isEmpty()){
			for(Activity sucAct: successors){
				try {
					if ( sdf.parse(this.getStartDate()).before(sdf.parse(sucAct.getFinishDate()))) {
						return false;
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}
	
	@Override
	// to compare activities by ID
	public boolean equals(Object other){
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof Activity))return false;
	    Activity otherActivity = (Activity)other;
	    if (this.getID() == otherActivity.getID()) return true;
		return false;
	}
		
	/*	
	public int getDuration(){												// return duration in hours
		return (int) ((finishDate.getTime() - startDate.getTime())/60000);
	}
*/
	
/////////////////////////////// added on Iteration 3
	
	public Project getParentProject(){
		try {
			Project proj = MainModel.getInstance().getProjectByID(this.parentProjectID) ;
			return proj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// checks dates boundaries before adding an Activity to a parent project
	public int activityDatesSuitsParent() {	// checks dates boundaries before adding an Activity to a parent project
			
			// Check dates boundaries
			// "Adjust: the Activity can't precede its parent project";
			
			try {
				if (sdf.parse(this.getStartDate()).before(sdf.parse(this.getParentProject().getStartDate()))) {
					return 1;
					}
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			// 	"Adjust: the Activity Finish date can't follow its parent project Finish date";
			try {
				if (sdf.parse(this.getFinishDate()).after(sdf.parse(this.getParentProject().getFinishDate()))) {
					return 2;
					}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// otherwise it is okay
			return 0;
		}
	
	// checks budget boundaries before adding an Activity to a parent project
	public boolean activityBudgetSuitsParent(){
		
		// "Adjust: The Sum of a Project Activities budgets after adding activity.budget() can't exceed their parent project "
		Project parentProject = getParentProject();
		if (parentProject.getBudget() < parentProject.getActivitiesTotalBudget() + this.getBudget()){
				return false;
			}
		return true;
	}
	
}
