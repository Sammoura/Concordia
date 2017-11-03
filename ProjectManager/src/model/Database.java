package model;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Database{
	
//	private File dbFile;			// Database file
	private String aDriver;			// Driver of SQLite with JDBC
	private String aURL;				// URL of SQLite JDBC
//	private String userName;
//	private String password;
	private Connection con;			// Database connection
	private Statement dbStatement;	// Database statement
	int statementTimeout;
	
	protected Database() throws Exception {
		
		aDriver 	= "org.sqlite.JDBC";
		aURL 	= "jdbc:sqlite:database.db";
		File dbFile 	= new File("database.db");
		boolean fileExists = dbFile.exists();
//		userName = "";
//		password = "password";
		connect();
		setStatement();
		dbStatement.executeUpdate("PRAGMA foreign_keys = ON;");
		statementTimeout = 20;

		if (!fileExists) {

			String[] queriesList = 
				{
					" CREATE TABLE 	Members (UserID INTEGER PRIMARY KEY AUTOINCREMENT, Username TEXT UNIQUE, Password TEXT, Manager INTEGER); ",		
					
					" CREATE TABLE 	Projects (PMid INTEGER NOT NULL, ID INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT, Description TEXT, Budget DOUBLE, startDate TEXT, finishDate TEXT, Status INTEGER, FOREIGN KEY(PMid) REFERENCES Members(UserID) ON DELETE CASCADE); ",
					
					" CREATE TABLE 	Activities (ID INTEGER PRIMARY KEY AUTOINCREMENT, ParentProjectID INTEGER NOT NULL, Name TEXT, Description TEXT, Budget DOUBLE, startDate TEXT, finishDate TEXT, Status INTEGER); ",
										
					" CREATE TABLE 	PreReqActivities (activityID INTEGER NOT NULL, preReqID INTEGER NOT NULL, FOREIGN KEY(activityID) REFERENCES Activities(ID) ON DELETE CASCADE, FOREIGN KEY(preReqID) REFERENCES Activities(ID) ON DELETE CASCADE); ",
					
//					" CREATE TABLE 	MemberActivities (MemberID INTEGER NOT NULL, activityID INTEGER NOT NULL, FOREIGN KEY(MemberID) REFERENCES Members(UserID) ON DELETE CASCADE, FOREIGN KEY(activityID) REFERENCES Activities(ID) ON DELETE CASCADE); ",
					
					" CREATE TABLE 	ActivityMembers (activityID INTEGER NOT NULL, MemberID INTEGER NOT NULL, FOREIGN KEY(activityID) REFERENCES Activities(ID) ON DELETE CASCADE , FOREIGN KEY(MemberID) REFERENCES Members(UserID) ON DELETE CASCADE); ",
					
					" CREATE TRIGGER DeleteProject BEFORE DELETE ON Projects BEGIN DELETE FROM Activities WHERE ID IN (SELECT ID FROM Activities WHERE ParentProjectID = OLD.ID); END;" 
				};
		
			processQueries(queriesList);

		}
	}

	// connects to database and registers the driver
	protected void connect() throws ClassNotFoundException, SQLException{
		if (con != null){
			return;
		}
		Class.forName(aDriver);
		con = DriverManager.getConnection(aURL);
	}
	
	// sets the database statement
	private void setStatement() throws Exception {
		if (con == null){
			connect();
		}
		dbStatement = con.createStatement();
		dbStatement.setQueryTimeout(statementTimeout);
		
	}
	
	// disconnects the database
	protected void disconnect() throws SQLException{
		if(con != null){
			con.close();
		}
		if(dbStatement != null){
			dbStatement.close();
		}
	}
	
	// executes a list of queries
	private void processQueries(String[] queriesList) throws SQLException {
		for (int i = 0 ; i < queriesList.length ; i++){
			dbStatement.executeUpdate(queriesList[i]);
		}

	}

	// inserts a new project in the database
	protected void createProject(Project newProject) throws SQLException {
		
		String aQuery = " INSERT INTO Projects (PMid, Name, Description, Budget, startDate, finishDate, Status)" + " VALUES('"
							+ newProject.getPmID() 				+ "', '"
							+ newProject.getName() 				+ "', '"
							+ newProject.getDescription() 		+ "', "
							+ newProject.getBudget()			+ ", '"
							+ newProject.getStartDate()			+ "', '"
							+ newProject.getFinishDate()		+ "', " 
							+ newProject.getStatus().ordinal() 	+ "); ";
		
		dbStatement.executeUpdate(aQuery);
	}
	
/*	protected void associateProject(int pmID) throws Exception{
		
		Project lastAddedProject = this.getLastProject();
		
		String aQuery = " INSERT INTO Owns (ManagerID, ProjectID)" + " VALUES(" + pmID + ", " + lastAddedProject.getID() + "); ";

		dbStatement.executeUpdate(aQuery);
	}*/
	
	// inserts a new Activity in the database
	protected void createActivity(Activity newActivity) throws SQLException {
		
		String aQuery = " INSERT INTO Activities (ParentProjectID, Name, Description, Budget, startDate, finishDate, Status) VALUES ( "
							+ newActivity.getParentProjectID()	+ ", '"
							+ newActivity.getName()				+ "', '"
							+ newActivity.getDescription()		+ "', "
							+ newActivity.getBudget()			+ ", '"
							+ newActivity.getStartDate()		+ "', '"
							+ newActivity.getFinishDate()		+ "', " 
							+ newActivity.getStatus().ordinal()	+ "); ";
		
		dbStatement.executeUpdate(aQuery);
	}
		
	// adds an an activity as a prerequisite for an activity
	protected void addPreReqActivity(int preReqID, int activityID) throws SQLException {
		
		String aQuery = "INSERT INTO PreReqActivities (activityID, preReqID) VALUES(" + activityID + ", " + preReqID + ");";
		dbStatement.executeUpdate(aQuery);
	}
	
	// updates an existing project
	protected void updateProject(Project project) throws SQLException {
		
		String aQuery = " UPDATE Projects SET PMid = '" + project.getPmID()
						+ "', Name = '" 				+ project.getName()
						+ "', Description = '" 			+ project.getDescription()
						+ "', Budget = " 				+ project.getBudget() 
						+ ", startDate = '"				+ project.getStartDate() 
						+ "', finishDate = '"			+ project.getFinishDate() 
						+ "', Status = "				+ project.getStatus().ordinal()
						+ " WHERE ID = "				+ project.getID() + "; ";
		dbStatement.executeUpdate(aQuery);
	}
	
	// update an existing activity
	protected void updateActivity(Activity activity) throws SQLException {
		String aQuery = " UPDATE Activities SET Name = '"	+ activity.getName() 
						+ "', ParentProjectID = "			+ activity.getParentProjectID()
						+ ", Description = '"				+ activity.getDescription() 
						+ "', Budget = "					+ activity.getBudget() 
						+ ", startDate = '"					+ activity.getStartDate() 
						+ "', finishDate = '"				+ activity.getFinishDate() 
						+ "', Status = "					+ activity.getStatus().ordinal() 
						+ " WHERE ID = "					+ activity.getID() + "; ";
		dbStatement.executeUpdate(aQuery);
	}
	
	// deletes an existing project
	protected void deleteProject(int projectID) throws SQLException {
		
		String aQuery = "DELETE FROM Projects WHERE ID = " + projectID + "; ";
		dbStatement.executeUpdate(aQuery);
	}
	
	// deletes an existing activity
	protected void deleteActivity(int activityID) throws SQLException {
		
		String aQuery = " DELETE FROM Activities WHERE ID = " + activityID + "; ";
		dbStatement.executeUpdate(aQuery);
	}
		
	// adds a prerequisite activity to an activity
	protected void removePreReqActivity(int preReqID , int activityID) throws SQLException {
		
		String aQuery = " DELETE FROM PreReqActivities WHERE activityID = " + activityID + " AND preReqID = " + preReqID + "; ";
		dbStatement.executeUpdate(aQuery);
	}
	
	// returns and ArrayList of all existing projects
	protected ArrayList<Project> getAllProjects() throws Exception {
		
		String aQuery = " SELECT * FROM Projects ; ";
		ResultSet resultSet = null;
		resultSet = dbStatement.executeQuery(aQuery);
		
		ArrayList<Project> projectsList = new ArrayList<Project>();

		while (resultSet.next()) {
			int pmID = resultSet.getInt("PMid");
			int id = resultSet.getInt("ID");
			String name = resultSet.getString("Name");
			String description = resultSet.getString("Description");
			double budget = resultSet.getDouble("Budget");
			String startDate = resultSet.getString("startDate");
			String finishDate = resultSet.getString("finishDate");
			Status status = Status.values()[resultSet.getInt("Status")];
			Project project = new Project(pmID, name, description, budget, startDate, finishDate, status);
			project.setID(id);
			projectsList.add(project);
		}
		return projectsList;
	}
	
	// returns all Projects of a particular Project Manager
	protected ArrayList<Project> getManagerProjects(int pmID) throws Exception {
		
		String aQuery = " SELECT * FROM Projects WHERE PMid = "  + pmID + ";";
		ResultSet resultSet = null;
		resultSet = dbStatement.executeQuery(aQuery);
		
		ArrayList<Project> projectsList = new ArrayList<Project>();

		while (resultSet.next()) {
//			int pmID = resultSet.getInt("PMid");
			int id = resultSet.getInt("ID");
			String name = resultSet.getString("Name");
			String description = resultSet.getString("Description");
			double budget = resultSet.getDouble("Budget");
			String startDate = resultSet.getString("startDate");
			String finishDate = resultSet.getString("finishDate");
			Status status = Status.values()[resultSet.getInt("Status")];
			Project project = new Project(pmID, name, description, budget, startDate, finishDate, status);
			project.setID(id);
			projectsList.add(project);
		}
		return projectsList;
	}
	
	// returns and ArrayList of all existing Activities
	protected ArrayList<Activity> getAllActivities() throws Exception {
		
		String aQuery = " SELECT * FROM Activities ;";
		return getActivityList(aQuery);
	}
	
	
	// returns an ArrayList of activities associated with a particular project
	protected ArrayList<Activity> getProjectActivities(int parentID) throws Exception {
		
		String aQuery = " SELECT * FROM Activities WHERE ParentProjectID = " + parentID + ";";
		return getActivityList(aQuery);
	}
	
	// returns an ArrayList of prerequisite activities of an activity 
	protected ArrayList<Activity> getActivityPreReq(int actID) throws Exception {
		
		//String aQuery = "SELECT * FROM Activities WHERE ID IN ( SELECT preReqID FROM PreReqActivities WHERE activityID = " + actID + " ;";
		String aQuery = "SELECT * FROM Activities, PreReqActivities WHERE activityID = " + actID + " AND ID = preReqID;";
		return getActivityList(aQuery);
	}
	
	// returns an ArrayList of successor activities of an activity 
	protected ArrayList<Activity> getActivitySuccessors(int actID) throws Exception {
		
		// String aQuery = "SELECT * FROM Activities WHERE ID IN ( SELECT activityID FROM PreReqActivities WHERE preReqID = " + actID + " ;";
		String aQuery = "SELECT * FROM Activities, PreReqActivities WHERE preReqID = " + actID + " AND ID = activityID;";
		return getActivityList(aQuery);
	}
	
	// an auxiliary method to remove redundancy in the last 3 methods
	private ArrayList<Activity> getActivityList(String aQuery) throws Exception{
		
		ResultSet resultSet = null;

		resultSet = dbStatement.executeQuery(aQuery);

		ArrayList<Activity> activityList = new ArrayList<Activity>();

		while (resultSet.next()) {
			int parentProjectID = resultSet.getInt("ParentProjectID");
			int id = resultSet.getInt("ID");
			String name = resultSet.getString("Name");
			String description = resultSet.getString("Description");
			double budget = resultSet.getDouble("Budget");
			String startDate = resultSet.getString("startDate");
			String finishDate = resultSet.getString("finishDate");
			Status status = Status.values()[resultSet.getInt("Status")];
			Activity activity = new Activity(parentProjectID, name, description, budget, startDate, finishDate, status);
			activity.setID(id);
			activityList.add(activity);	
		}
		return activityList;
	}
	
	// get Last Project
	protected Project getLastProject() throws Exception {
		
		String aQuery = "SELECT * FROM Projects WHERE ID = (SELECT MAX(ID) FROM Projects);";
		
		ResultSet resultSet = null;
		resultSet = dbStatement.executeQuery(aQuery);
		Project project = null ;
		
		while(resultSet.next()){
			int pmID = resultSet.getInt("PMid");
			int id = resultSet.getInt("ID");
			String name = resultSet.getString("Name");
			String description = resultSet.getString("Description");
			double budget = resultSet.getDouble("Budget");
			String startDate = resultSet.getString("startDate");
			String finishDate = resultSet.getString("finishDate");
			Status status = Status.values()[resultSet.getInt("Status")];
			project = new Project(pmID, name, description, budget, startDate, finishDate, status);
			project.setID(id);
		}
			return project;
	}

	// get Last Activity
	protected Activity getLastActivity() throws Exception {
		
		String aQuery = " SELECT * FROM Activities WHERE ID = (SELECT MAX(ID) FROM Activities);";
		
		ResultSet resultSet = null;
		resultSet = dbStatement.executeQuery(aQuery);
		Activity activity = null;
		while (resultSet.next()) {
			int parentProjectID = resultSet.getInt("ParentProjectID");
			int id = resultSet.getInt("ID");
			String name = resultSet.getString("Name");
			String description = resultSet.getString("Description");
			double budget = resultSet.getDouble("Budget");
			String startDate = resultSet.getString("startDate");
			String finishDate = resultSet.getString("finishDate");
			Status status = Status.values()[resultSet.getInt("Status")];
			activity = new Activity(parentProjectID, name, description, budget, startDate, finishDate, status);
			activity.setID(id);
		} 
		return activity;
	}
	
	 // Verifies and logs into the application if the member exists in the database.
	
	 protected Member getMember(String username, String password) throws SQLException {

		String aQuery = " SELECT * FROM Members WHERE Username = '" + username + "' AND Password = '" + password + "' ;" ; 
		ResultSet resultSet = null;
		resultSet = dbStatement.executeQuery(aQuery);
		Member member = null;
		
		while (resultSet.next()) {
			member = new Member(username, password);
			int id = resultSet.getInt("UserID");
			int isManager = resultSet.getInt("Manager");
			member.setUserID(id);
			if(isManager == 1){
				member.setManager(true);
			}
			else {
				member.setManager(false);
			}
		}
			return member;
	 }
	

/*	 protected int[] verifyMember(String username, String password) throws SQLException {

		String aQuery = " SELECT * FROM Members WHERE Username = '" + username + "' AND Password = '" + password + "' ;" ; 
		ResultSet resultSet = null;
		resultSet = dbStatement.executeQuery(aQuery);
		int isPM = -1;
		int userID = -1;
		int[] couple = {-1, -1};
		
		while (resultSet.next()) {
			isPM = resultSet.getInt("Manager");
			userID = resultSet.getInt("UserID");
		}
			couple[0] = isPM;
			couple[1] = userID;
			
			return couple;
	 }*/
	 
	 protected boolean addMember(String username, String password, int role) throws SQLException {

		String query = "INSERT INTO Members (Username, Password, Manager) VALUES (?,?,?);";

		PreparedStatement prepStatement = con.prepareStatement(query);
		prepStatement.setString(1, username);
		prepStatement.setString(2, password);
		prepStatement.setInt(3, role);
		
		boolean result = prepStatement.execute();

		return !result;
		}
	 
	 protected ArrayList<Member> getAllTeamMembers() throws SQLException {
		 
		String aQuery = " SELECT * FROM Members WHERE Manager = 0 ; ";
		 
		ResultSet resultSet = null;
		resultSet = dbStatement.executeQuery(aQuery);
		
		ArrayList<Member> membersList = new ArrayList<Member>();
		
		while (resultSet.next()) {
			int id = resultSet.getInt("UserID");
			String name = resultSet.getString("Username");
			String pass = resultSet.getString("Password");
			
			Member member = new Member(name, pass);
			member.setUserID(id);
			member.setManager(false);
			membersList.add(member);
		}
		return membersList;
	 }
	 
	 protected ArrayList<Activity> getMemberActivities(int mID) throws Exception{
		String aQuery = " SELECT * FROM Activities, ActivityMembers WHERE MemberID = " + mID + " AND ID = activityID;";
		 
		return getActivityList(aQuery); 
	 }
	 
	 protected ArrayList<Project> getMemberProjects(int mID) throws Exception {
		String aQuery = " SELECT * FROM Projects, Activities, ActivityMembers WHERE MemberID = " + mID + " AND Projects.ID = ParentProjectID AND Activities.ID = activityID GROUP BY Projects.ID ;"; 
		
		ResultSet resultSet = null;
		resultSet = dbStatement.executeQuery(aQuery);
		
		ArrayList<Project> projectsList = new ArrayList<Project>();

		while (resultSet.next()) {
			int pmID = resultSet.getInt("PMid");
			int id = resultSet.getInt(2);
			String name = resultSet.getString(3);
			String description = resultSet.getString(4);
			double budget = resultSet.getDouble(5);
			String startDate = resultSet.getString(6);
			String finishDate = resultSet.getString(7);
			Status status = Status.values()[resultSet.getInt(8)];
			Project project = new Project(pmID, name, description, budget, startDate, finishDate, status);
			project.setID(id);
			projectsList.add(project);
		}
		return projectsList;
	 }
	 
	 protected ArrayList<Member> getActivityMembers(int aID) throws SQLException {
		 String aQuery = " SELECT * FROM Members, ActivityMembers WHERE activityID = " + aID + " AND MemberID = UserID;" ;
		 
			ResultSet resultSet = null;
			resultSet = dbStatement.executeQuery(aQuery);
			
			ArrayList<Member> membersList = new ArrayList<Member>();
			while (resultSet.next()) {
				int id = resultSet.getInt("UserID");
				String name = resultSet.getString("Username");
				String pass = resultSet.getString("Password");
				int isManager = resultSet.getInt("Manager");
				
				Member member = new Member(name, pass);
				member.setUserID(id);
				if(isManager == 1){
					member.setManager(true);
				}
				else {
					member.setManager(false);
				}
				membersList.add(member);
			}
			return membersList;
	 }
	 
	 protected void addMemberToActivity(int mID, int aID) throws SQLException{
			String aQuery = "INSERT INTO ActivityMembers (activityID, MemberID) VALUES(" + aID + ", " + mID + ");";
			dbStatement.executeUpdate(aQuery);
	 }
	 
	 protected void removeMemberFromActivity(int mID, int aID) throws SQLException{
			String aQuery = " DELETE FROM ActivityMembers WHERE activityID = " + aID + " AND MemberID = " + mID + "; ";
			dbStatement.executeUpdate(aQuery);
	 }
	 	 
	 protected Member getLastMember() throws SQLException {

		String aQuery =  "SELECT * FROM Members WHERE UserID = (SELECT MAX(UserID) FROM Members);"; 
		ResultSet resultSet = null;
		resultSet = dbStatement.executeQuery(aQuery);
		Member member = null;
		
		while (resultSet.next()) {
			String name = resultSet.getString("Username");
			String pass = resultSet.getString("Password");
			member = new Member(name, pass);
			int id = resultSet.getInt("UserID");
			int isManager = resultSet.getInt("Manager");
			member.setUserID(id);
			if(isManager == 1){
				member.setManager(true);
			}
			else {
				member.setManager(false);
			}
		}
			return member;
	 }
	 

}