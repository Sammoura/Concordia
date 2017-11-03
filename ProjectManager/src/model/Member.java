package model;

import java.util.ArrayList;

// Members exist in the database

public class Member {
	private String username, userPassword;
	private int userID;
	private boolean isManager;
	private ArrayList<Project> memberProjects;
	private ArrayList<Activity> memberActivities;
	
	public Member(String username, String userPassword) {
		this.username = username;
		this.userPassword = userPassword;
		setMemberProjects(new ArrayList<Project>());
		setMemberActivities(new ArrayList<Activity>());
	}

	public String getUsername() {
		return username;
	}

	protected void setUsername(String username) {
		this.username = username;
	}

	public String getUserPassword() {
		return userPassword;
	}

	protected void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public ArrayList<Activity> getMemberActivities() {
		return memberActivities;
	}

	public void setMemberActivities(ArrayList<Activity> memberActivities) {
		this.memberActivities = memberActivities;
	}

	public ArrayList<Project> getMemberProjects() {
		return memberProjects;
	}

	public void setMemberProjects(ArrayList<Project> memberProjects) {
		this.memberProjects = memberProjects;
	}
	
	public boolean isManager(){
		return isManager;
	}
	
	public void setManager(boolean val){
		isManager = val;
	}
	
	
	public String toString(){
		return this.getUsername();
	}
	
	@Override
	// to compare Members by UserID
	public boolean equals(Object other){
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof Member))return false;
	    Member otherMember = (Member)other;
	    if (this.getUserID() == otherMember.getUserID()) return true;
		return false;
	}
}
