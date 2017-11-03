package model;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

	// handles Database communication
public class MainModel {
	
	// Singleton pattern
	private static MainModel modelInstance = null;
	
	// database reference
	private Database database;
	
	// date formatter
	SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.CANADA);
	
	///////////////////////////////////////////////////////////////////	Initialisers	\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	
	// Default Constructor
	private MainModel() throws Exception {
		
		database = new Database();
		
	}
	
	// ensures only one instance (singleton) is created 
	public static MainModel getInstance() throws Exception {
		
		if (modelInstance == null){
			modelInstance = new MainModel();
		}
		return modelInstance;
	}
	
	// validates user login
	public Member validateLogin(String username, String password) throws SQLException{
				
		return database.getMember(username, password );
	}
	
	public void addMemberToDatabase(String username, String password, int role) throws Exception {
		database.addMember(username, password, role);
		//currentUser = username;	
	}
	
	///////////////////////////////////////////////////////////////////	creation Methods	\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

	// adds a Project to the database passed as an Object
	public void addProjectToDb(Project project) throws Exception{
		
		addProjectToDb(project.getPmID(), project.getName() , project.getDescription() , project.getBudget() , project.getStartDate(), project.getFinishDate(), project.getStatus());
	}
	
	// adds a Project to the database passed as data
	public void addProjectToDb(int pmID, String name, String description, double budget, String startDate, String finishDate, Status status) throws Exception {
				
		Project newProject = new Project(pmID, name, description, budget, startDate, finishDate, status);
		// create it in the database
		database.createProject(newProject);
		
		// set this project ID which is handled by the database (auto increment)
		newProject.setID(database.getLastProject().getID());
	}
		
	// adds an Activity to the database passed as an Object
	public void addActivityToDb(Activity activity) throws Exception{
		
		addActivityToDb(activity.getParentProjectID(), activity.getName() , activity.getDescription() , activity.getBudget() , activity.getStartDate(), activity.getFinishDate(), activity.getStatus());
	}
	
	// adds an Activity to the database passed as data
	public void addActivityToDb(int parentID, String name, String description, double budget, String startDate, String finishDate, Status status) throws Exception {
				
		// get the parent project to check if it exists
		Project parentProject = getProjectByID(parentID);
		if (parentProject == null)
			throw new Exception("Parent Project doesn't exist");
		
		// create an activity object
		Activity newActivity = new Activity(parentID, name, description, budget, startDate, finishDate, status);
		
/*		// check if Activity is suitable to be associated to a parent Project
		if(!activityBudgetSuitsParent(newActivity, parentProject)){
			throw new Exception("Activities Total Budget will exceed parent Project");
		}*/
		
/*		if( activityDatesSuitsParent(newActivity, parentProject) != 0 ){
			throw new Exception("Activity Dates violates the boundaries of parent project");
		}*/	
		
		// add the new Activity to the database
		database.createActivity(newActivity);
		
		// update ProjectActivities Array
		parentProject.setProjectActivities(getProjectActivities(parentID));
	}

	// creates an updated project with updated data (don't create in DB), checks if the updated fields suits the old activities(won't be changed if Activities doesn't suit), and then original
	// notice that updatedProject is not yet in the DB no ID has been associated yet
	public void updateProject(Project originalProject, Project updatedProject) throws Exception {
		
		// retrieve original Project activities
		ArrayList<Activity> projectActivities = originalProject.getProjectActivities();
		
		// check if original Activities Dates is suitable to be associated to the parent Project with updated data
		for(Activity act: projectActivities){
			if( act.activityDatesSuitsParent() != 0){
				throw new Exception("Updated project dates doesn't suit original Activites dates");
			}
		}
		// Update project
		originalProject.setPmID(updatedProject.getPmID());
		originalProject.setName(updatedProject.getName());
		originalProject.setDescription(updatedProject.getDescription());
		originalProject.setBudget(updatedProject.getBudget());
		originalProject.setStartDate(updatedProject.getStartDate());
		originalProject.setFinishDate(updatedProject.getFinishDate());
		originalProject.setStatus(updatedProject.getStatus());

		database.updateProject(originalProject);
	}
	
	// creates an updated activity with updated data (don't create in DB), checks if the updated fields suits the parent project and other activities(won't be updated if any doesn't suit)
	// notice that updatedActivity is not yet in the DB no ID has been associated yet
	public void updateActivity(Activity originalActivity, Activity updatedActivity) throws Exception {
				
		int parentProjectID = originalActivity.getParentProjectID();
		Project parentProject = getProjectByID(parentProjectID);
		
		// check dates
		// start date cannot follow its finish date
		
		if (sdf.parse(updatedActivity.getStartDate()).after(sdf.parse(updatedActivity.getStartDate()))){
			throw new Exception("Updated activity startDate can't follow its finishDate");
		}

		//////// check if suits parent project
		// check budget
/*		if(! activityBudgetSuitsParent(updatedActivity, parentProject)){
			throw new Exception("Updated activity budget doesn't suit parent project");
		}*/
		
/*		// check dates
		if( activityDatesSuitsParent(updatedActivity, parentProject) != 0){
			throw new Exception("Updated activity dates doesn't suit parent project");
		}*/
		
		//////// check prerequisites
		// "Start date can't precede its prerequisites Finish date"
		if(! activityDatesSuitsAllPrereqs(originalActivity, updatedActivity)){ 
			throw new Exception("Updated Activity startDate doesn't suit its prereqs");
		}

		//////// check successors
		// "Finish date can't follow its successors Start date"
		if(! activityDatesSuitsAllSuccessors(originalActivity, updatedActivity)){ 
			throw new Exception("Updated Activity finishDate doesn't suit its sucessors");
		}

		// otherwise it is okay .... Update Activity
		// get Activity attributes
		originalActivity.setName(updatedActivity.getName());
		originalActivity.setDescription(updatedActivity.getDescription());
		originalActivity.setBudget(updatedActivity.getBudget());
		originalActivity.setStartDate(updatedActivity.getStartDate());
		originalActivity.setFinishDate(updatedActivity.getFinishDate());
		originalActivity.setStatus(updatedActivity.getStatus());
		
		// sets parent ID
		updatedActivity.setParentProjectID(originalActivity.getParentProjectID());
		
		// updates in DB
		database.updateActivity(originalActivity);	
	}

/////////////////////////////////////////////////////////////////// Checking Methods	\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	
	public boolean projectExists(int pID) throws Exception {
		
		return (getProjectByID(pID) != null);
	}

	public boolean activityExists(int aID) throws Exception {
		
		return (getActivityByID(aID) != null);
	}
	
	// checks budget boundaries of an updated project against original project Activities total budget
	public boolean actsTotalBudgetSuitsParent(Project updatedProject, Project originalProject){
		
		// "Adjust: The Sum of a original Project Activities' budgets can't exceed the updated project Budget "
		if (updatedProject.getBudget() < originalProject.getActivitiesTotalBudget()){
				return false;
			}
		
		return true;
	}
////////////////delegated to activity class
/*	// checks budget boundaries before adding an Activity to a parent project
	public boolean activityBudgetSuitsParent(Activity newActivity, Project parentProject){
		
		// "Adjust: The Sum of a Project Activities budgets after adding activity.budget() can't exceed their parent project "
			try {
				parentProject.setProjectActivities(getProjectActivities(parentProject.getID()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		if (parentProject.getBudget() < parentProject.getActivitiesTotalBudget() + newActivity.getBudget()){
				return false;
			}
		return true;
	}*/
	
/////////////// No longer needed, delegated to the activity class
	
/*	// checks dates boundaries before adding an Activity to a parent project
	public int activityDatesSuitsParent(Activity activity, Project parentProject) throws Exception {	// checks dates boundaries before adding an Activity to a parent project
			
			// Check dates boundaries
			// "Adjust: the Activity can't precede its parent project";
			
			if (sdf.parse(activity.getStartDate()).before(sdf.parse(parentProject.getStartDate()))) {
				return 1;
				}
			
			// 	"Adjust: the Activity Finish date can't follow its parent project Finish date";
			if (sdf.parse(activity.getFinishDate()).after(sdf.parse(parentProject.getFinishDate()))) {
				return 2;
				}
			
			// otherwise it is okay
			return 0;
		}*/
		
	// checks if an activity suits a prerequisite regarding the dates
	public boolean activityDatesSuitsPrereq(Activity activity , Activity prereqActivity){
		
		// "Start date can't precede its prerequisite Finish date"

		try {
			if ( sdf.parse(activity.getStartDate()).before(sdf.parse(prereqActivity.getFinishDate())) ){
				return false;
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	// used in update Activity
	// checks if an activity suits all its prerequisites regarding the dates
	public boolean activityDatesSuitsAllPrereqs(Activity activity, Activity updatedActivity){
		
		ArrayList<Activity> allPrereq;
		try {
			allPrereq = getActivityPreReq(activity.getID());
			if(!allPrereq.isEmpty()){
				for(Activity prereqAct: allPrereq){
					if(!activityDatesSuitsPrereq(updatedActivity, prereqAct)){
						return false;
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	// checks if an activity suits a successor regarding the dates
	public boolean activityDatesSuitsSuccessor(Activity activity , Activity sucActivity){
		
		// "Finish date can't follow its successor Start date"
		try {
			if(sdf.parse(activity.getFinishDate()).after(sdf.parse(sucActivity.getStartDate()))){
				return false;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	// checks if an activity suits all its successors regarding the dates
	public boolean activityDatesSuitsAllSuccessors(Activity originalActivity, Activity updatedActivity){

		ArrayList<Activity> allSucs;
		try {
			allSucs = getActivitySuccessors(originalActivity.getID());
			if(!allSucs.isEmpty()){
				for(Activity sucAct: allSucs){
					if(!activityDatesSuitsSuccessor(updatedActivity, sucAct)){
						return false;
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	// checks if prerequisites are done
	public boolean isPrereqDone(Activity activity){

		ArrayList<Activity> prereqActivities = activity.getPreReq();
		
		if(!prereqActivities.isEmpty()){
			for(Activity act: prereqActivities){
				if(act.getStatus() != Status.FINISHED){
					return false;
				}
			}
		}
		return true;
	}
	
	///////////////////////////////////////////////////////////////////	Delete Methods	\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	
	public void deleteProjectfromDb(int pID) throws Exception {
		
		if (getProjectByID(pID) == null)
			throw new Exception("Project not found");

		// Removes associated activities     // auto done by foriegn key   ????
		ArrayList<Activity> assocActs = database.getProjectActivities(pID);
		if(!assocActs.isEmpty()){
			for (Activity act : assocActs){
				int id = act.getID();
				database.deleteActivity(id);
			}
		}
		database.deleteProject(pID);
	}
	
	public void deleteActivityfromDb(int aID) throws Exception {
		
		Activity activity = getActivityByID(aID);
		
		if (activity == null)
			throw new Exception("Activity not found");

		// prevents from deletion whenever successors exist 
		if (activity.getSuccessors().size() > 0){
			throw new Exception("The Activity has successors, remove all successors first");
		}		
		database.deleteActivity(aID);
	}
	
	// adds a prerequisite Activity of a passed Activity  .... should check if it is already exists, otherwise DB throws SQL Exception 
	public void addActivityPrereq(int prereqID, int activityID) throws Exception {
		
		if (! activityExists(prereqID))
			throw new Exception("Prerequisite Activity doesn't exist");


		if (! activityExists(activityID))
			throw new Exception("Activity doesn't exist");
		
		database.addPreReqActivity(prereqID, activityID);
	}
	
	// removes a prerequisite Activity of a passed Activity    .... should check if it is there, otherwise DB throws SQL Exception 
	public void removeActivityPrereq(int prereqID, int activityID) throws Exception {
		
		if (! activityExists(prereqID))
			throw new Exception("Prerequisite Activity doesn't exist");


		if (! activityExists(activityID))
			throw new Exception("Activity doesn't exist");

		database.removePreReqActivity(prereqID, activityID);
	}
	
	///////////////////////////////////////////////////////////////////	Getters	\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	
	// gets all projects    // not really needed
	public ArrayList<Project> getAllProjects() throws Exception {
		return database.getAllProjects();
	}
	
	// gets all projects of a PM
	public ArrayList<Project> getManagerProjects(int pmID) throws Exception {
		return database.getManagerProjects(pmID);
	}
	
	// get all Activities
	public ArrayList<Activity> getAllActivities() throws Exception {
		return database.getAllActivities();
	}

	// returns last added Project
	public Project getLastProject() throws Exception {
		return database.getLastProject();
	}
	
	// returns last added Activity
	public Activity getLastActivity() throws Exception{
		
		return database.getLastActivity();
		
	}
		
	public Project getProjectByID(int pID) throws Exception {
		
		ArrayList<Project> allProjects = getAllProjects();
		if(!allProjects.isEmpty()){
			for(Project item: allProjects){
				if(item.getID() == pID){
					linkToProject(item);
					return item;
				}
			}
		}
		throw new Exception("Project with ID " + pID + " doesn't exist");
	}

	public Activity getActivityByID(int aID) throws Exception {
		
		ArrayList<Activity> allActivities = getAllActivities();
		if(!allActivities.isEmpty()){
			for(Activity item: allActivities){
				if(item.getID() == aID){
					linkToActivity(item);
					return item;
				}
			}
		}

		throw new Exception("Activity with ID " + aID + " doesn't exist");
	}
	
	public ArrayList<Activity> getProjectActivities(int pID) throws Exception {
		
		if (!projectExists(pID)){
			throw new Exception("Project not found");
		}
		return database.getProjectActivities(pID);
	}
	
	public ArrayList<Activity> getActivityPreReq(int aID) throws Exception {
		
		if (!activityExists(aID)) {
			throw new Exception("Activity doesn't exist");
		}
		return database.getActivityPreReq(aID);
	}
	
	public ArrayList<Activity> getActivitySuccessors(int aID) throws Exception {

		Activity activity = getActivityByID(aID);
		if (activity == null) {
			throw new Exception("Activity doesn't exist");
		}
		
		return database.getActivitySuccessors(aID);	

	}	
	
	public ArrayList<String> getProjectActivitiesNames(int pID) throws Exception {
		
		ArrayList<Activity> pActivities = getProjectActivities( pID);
		ArrayList<String> aNames = new ArrayList<String>();
		
		for(Activity act: pActivities){
			aNames.add(act.getName());
		}
		return aNames;
	}	
	
	public ArrayList<Activity> getMemberActivities(int mID) throws Exception {
		
		return database.getMemberActivities(mID);
	}
	
	public ArrayList<Project> getMemberProjects(int mID) throws Exception {
		
		return database.getMemberProjects(mID);
	}
		
	public ArrayList<Member> getActivityMembers(int aID) throws Exception {
		return database.getActivityMembers(aID);
	}
	
	public void addMemberToActivity(int mID, int aID) throws Exception {
		database.addMemberToActivity(mID, aID);
	}
	
	public void removeMemberFromActivity(int mID, int aID) throws Exception {
		database.removeMemberFromActivity(mID, aID);
	}

	public ArrayList<Member> getAllTeamMembers() throws Exception{

		return database.getAllTeamMembers();
	}
		
	public Member getLastMember() throws SQLException {
		Member member = database.getLastMember();
		return member;
	}
	
	public void linkToProject(Project proj) throws Exception{
		proj.setProjectActivities(database.getProjectActivities(proj.getID()));
	}
	
	public void linkToActivity(Activity act) throws Exception {
		int aID = act.getID();
		act.setActivityTeam(database.getActivityMembers(aID));
		act.setPreReq(database.getActivityPreReq(aID));
		act.setSuccessors(database.getActivitySuccessors(aID));
	}
	
	public void linkToMember(Member member) throws Exception{
		int mID = member.getUserID();
		if(member.isManager()){
			member.setMemberProjects(database.getManagerProjects(mID));
		} else {
			member.setMemberProjects(database.getMemberProjects(mID));
		}
		////member.se
	}
	
	public void linkAllToMember(Member member) throws Exception {
		int mID = member.getUserID();
		ArrayList<Project> allProjs;
		ArrayList<Activity> allMemberActs = new ArrayList<Activity>();
		if(member.isManager()){
			allProjs = this.getManagerProjects(mID);
			for(Project proj: allProjs){
				ArrayList<Activity> allActs = this.getProjectActivities(proj.getID());
				allMemberActs.addAll(allActs);	
				for(Activity act: allActs){
					allMemberActs.add(act);
					int aID = act.getID();
					act.setPreReq(this.getActivityPreReq(aID));
					act.setSuccessors(this.getActivitySuccessors(aID));
					act.setActivityTeam(this.getActivityMembers(aID));
				}
				proj.setProjectActivities(allActs);
			}
		} else {
			allProjs = this.getMemberProjects(mID);
			allMemberActs = this.getMemberActivities(mID);
			for(Project proj: allProjs){
				ArrayList<Activity> projActs = this.getProjectActivities(proj.getID());
				ArrayList<Activity> memberActsOfProject = new ArrayList<Activity>();
				for(Activity act: projActs){
					if(allMemberActs.contains(act)){
						memberActsOfProject.add(act);
						int aID = act.getID();
						act.setPreReq(this.getActivityPreReq(aID));
						act.setSuccessors(this.getActivitySuccessors(aID));
						act.setActivityTeam(this.getActivityMembers(aID));
					}
				}
				proj.setProjectActivities(memberActsOfProject);
			}
		}
		member.setMemberProjects(allProjs);
		member.setMemberActivities(allMemberActs);
	}
}
