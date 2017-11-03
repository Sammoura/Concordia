package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class Job {
	
	private String name, description;
	private double budget;
	private String startDate, finishDate;		
	private Status status;									// ENUM class
	private int ID;											// project ID
	// date formatter
	protected SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.CANADA);
	
	public Job(String name, String description, double budget, String startDate, String finishDate, Status status) throws Exception {

		setName(name);
		setDescription(description);
		setBudget(budget);
		setStartDate(startDate);
		setFinishDate(finishDate);
		setStatus(status);
	}

	public Job(Job originalJob) throws Exception{
		this(originalJob.getName(), originalJob.getDescription(), originalJob.getBudget(), originalJob.getStartDate(), originalJob.getFinishDate(), originalJob.getStatus());
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) throws Exception {
		if (name == null){
			throw new Exception("Invalid name");
		}
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) throws Exception {
		if (description == null){
			throw new Exception("Invalid description");
		}
		this.description = description;
	}
	
	public int getID() {
		return ID;
	}

	public void setID(int iD) throws Exception {
		if (iD < 0){
			throw new Exception("Invalid ID");
		}
		ID = iD;
	}
	
	public double getBudget() {
		return budget;
	}

	public void setBudget(double budget) throws Exception {
		if (budget < 0){
			throw new Exception("Invalid budget");	
		}
		this.budget = budget;
	}

	public String getStartDate() {

		return startDate;
	}

	public void setStartDate(String startDate) throws Exception {
		
		this.startDate = startDate;
	}

	public String getFinishDate() {
		
		return finishDate;
	}

	public void setFinishDate(String finishDate) throws Exception {
		
		this.finishDate = finishDate;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status){
		this.status = status;
	}
	
	
	public String toString(){
		return this.getName();
	}

	///////////////////////////////// added on iteration 3
	
	public boolean startInPast(){
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date); 
		cal.add(Calendar.DATE, -1);
		date = cal.getTime();
		try {
			return( sdf.parse(this.getStartDate()).before(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("here");
		return false;
	}
	
/*	
	public int getDuration(){												// return duration in hours
		return (int) ((finishDate.getTime() - startDate.getTime())/60000);
	}
*/
	
/*	// changes a date as a String to Date class to compare dates
	public Date dateFormatter(String dateAsString){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			 
			Date date = formatter.parse(dateAsString);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}*/
	
}
