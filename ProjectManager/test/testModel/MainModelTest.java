/*package testModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import model.Activity;
import model.MainModel;
import model.Member;
import model.Project;
import model.Status;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MainModelTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetInstance() {
		fail("Not yet implemented");
	}

	@Test
	public void testValidateLogin() {
		// case 1:	user is enlisted in the database and is a manager
		// case 2:	user is enlisted in the database and is a teamMember
		// case 3:	user is not enlisted in the database
	}

	@Test
	public void testAddMemberToDatabase() {
		try {
			MainModel mainModel = MainModel.getInstance();
			// adding a manager
			String name = "Johnnn";
			String pass = "Fares";
			
			mainModel.addMemberToDatabase(name, pass, 1);
			
			Member retrievedMember = mainModel.getLastMember();
			
			assertEquals(retrievedMember.getUsername(), name);
			assertEquals(retrievedMember.getUserPassword(), pass);
			assertEquals(retrievedMember.isManager(), true);
			
		} catch (Exception e){
			if(e.getMessage().contains("UNIQUE")) {
				System.out.println("This user is already a member!");
			}
		}
	}


	@Test
	public void testAddProjectToDbIntStringStringDoubleStringStringStatus() {
		fail("Not yet implemented");
	}
	@Test
	public void testGetMemberActivities() {
		fail("Not yet implemented");
	}
	@Test
	public void testGetMemberProjects() {
		fail("Not yet implemented");
	}
	@Test
	public void testGetActivityMembers() {
		fail("Not yet implemented");
	}
	@Test
	public void testAddMemberToActivity() {
		fail("Not yet implemented");
	}
	@Test
	public void testRemoveMemberFromActivity() {
		fail("Not yet implemented");
	}
	@Test
	public void testGetAllTeamMembers() {
		fail("Not yet implemented");
	}
	@Test
	public void testObject() {
		fail("Not yet implemented");
	}
	@Test
	public void testGetClass() {
		fail("Not yet implemented");
	}
	@Test
	public void testHashCode() {
		fail("Not yet implemented");
	}
	@Test
	public void testEquals() {
		fail("Not yet implemented");
	}
	@Test
	public void testClone() {
		fail("Not yet implemented");
	}
	@Test
	public void testToString() {
		fail("Not yet implemented");
	}
	@Test
	public void testNotify() {
		fail("Not yet implemented");
	}
	@Test
	public void testNotifyAll() {
		fail("Not yet implemented");
	}
	@Test
	public void testWaitLong() {
		fail("Not yet implemented");
	}
	@Test
	public void testWaitLongInt() {
		fail("Not yet implemented");
	}
	@Test
	public void testWait() {
		fail("Not yet implemented");
	}
	@Test
	public void testFinalize() {
		fail("Not yet implemented");
	}

	
	@Test
	
	//Testing both AddProjectToDb
	public void testAddProjectToDbProject() {
		try {
			//First test
			MainModel mainModel = MainModel.getInstance();
			
			// project data as if entered in text fields
			int pmID = 1;
			String name = "Test_1";
			String description = "";
			double budget = 10;
			String today = "Wed Aug 01 00:00:00 EDT 2018";
			String end = "Wed Aug 29 00:00:00 EDT 2018";
			String startDate = today;
			String finishDate = end;
			Status status = Status.LOCKED;
			
			//creating a project and adding it to database

			mainModel.addProjectToDb(pmID, name, description, budget, startDate, finishDate, status);
			
			// retrieve it
			
			Project retrievedProject = mainModel.getLastProject();
			
			assertEquals(name,(retrievedProject.getName()));
			assertEquals(description, (retrievedProject.getDescription()));
			assertEquals(true,budget == (retrievedProject.getBudget()));
			assertEquals(startDate ,(retrievedProject.getStartDate()));
			assertEquals(finishDate ,(retrievedProject.getFinishDate()));
			assertEquals(0,status.compareTo(retrievedProject.getStatus()));
			
			mainModel.addProjectToDb(retrievedProject);
			
			
			//Second test
			Project retrievedProject2 = mainModel.getLastProject();
			
			assertEquals(name,(retrievedProject2.getName()));
			assertEquals(description, (retrievedProject2.getDescription()));
			assertEquals(true,budget == (retrievedProject2.getBudget()));
			assertEquals(startDate ,(retrievedProject2.getStartDate()));
			assertEquals(finishDate ,(retrievedProject2.getFinishDate()));
			assertEquals(0,status.compareTo(retrievedProject2.getStatus()));
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

	@Test
	//Testing both adActivityToDb Methods, Also testing UpdateActivity in here
	public void testAddActivityToDbActivity() {
		try {//method 1
			MainModel mainModel = MainModel.getInstance();
			ArrayList<Project> ExistingProjects = mainModel.getAllProjects();			// get all existing projects
			int projectCount = ExistingProjects.size();
//			System.out.println(ExistingProjects.size());								// print count
			if(projectCount > 0){
				Project lastProject = mainModel.getLastProject();						// get last project
				int lastProjectID = lastProject.getID();
//				System.out.println(ExistingProjects.size());
				Activity newAct = new Activity(lastProjectID, "act1", "testing activity" , 2 , "Wed Aug 01 00:00:00 EDT 2018", "Wed Aug 08 00:00:00 EDT 2018", Status.UNLOCKED);
				mainModel.addActivityToDb(newAct);
				
				// retrieve it
				Activity retrievedActivity = mainModel.getLastActivity();
				
				assertEquals(newAct.getName(),(retrievedActivity.getName()));;
				assertEquals(newAct.getDescription(),(retrievedActivity.getDescription()));
				assertEquals(true , newAct.getBudget() == retrievedActivity.getBudget());
				assertEquals(newAct.getStartDate(),(retrievedActivity.getStartDate()));
				assertEquals(newAct.getFinishDate(),(retrievedActivity.getFinishDate()));
				assertEquals(true, newAct.getStatus() == retrievedActivity.getStatus() ) ;
				
				mainModel.addActivityToDb(retrievedActivity);
				
				
				//method 2
				Activity retrievedActivity2 = mainModel.getLastActivity();
				
				assertEquals(newAct.getName(),(retrievedActivity2.getName()));;
				assertEquals(newAct.getDescription(),(retrievedActivity2.getDescription()));
				assertEquals(true , newAct.getBudget() == retrievedActivity2.getBudget());
				assertEquals(newAct.getStartDate(),(retrievedActivity2.getStartDate()));
				assertEquals(newAct.getFinishDate(),(retrievedActivity2.getFinishDate()));
				assertEquals(true, newAct.getStatus() == retrievedActivity2.getStatus());
				
				//Testing Update activity
				
				Activity testUpdate = new Activity(lastProjectID, "act2", "testing activity update" , 3 , "Wed Aug 01 00:00:00 EDT 2018", "Fri Aug 03 00:00:00 EDT 2018", Status.LOCKED);
				
				mainModel.updateActivity(mainModel.getLastActivity(), testUpdate);
				
				Activity retrievedActivity3 = mainModel.getLastActivity();
				
				assertEquals("act2",(retrievedActivity3.getName()));;
				assertEquals("testing activity update",(retrievedActivity3.getDescription()));
				assertEquals(true , 3 == retrievedActivity3.getBudget());
				assertEquals("Wed Aug 01 00:00:00 EDT 2018",(retrievedActivity3.getStartDate()));
				assertEquals("Fri Aug 03 00:00:00 EDT 2018",(retrievedActivity3.getFinishDate()));
				assertEquals(true, Status.LOCKED == retrievedActivity3.getStatus());
			}
			
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testUpdateProject() {

		try {
			MainModel mainModel = MainModel.getInstance();
			int pmID = 1;
			String name = "Test_1";
			String description = "";
			double budget = 10;
			String today = "Sun Jan 01 00:00:00 EST 2017";
			String end = "Tue Jan 31 00:00:00 EST 2017";
			String startDate = today;
			String finishDate = end;
			Status status = Status.UNLOCKED;
				
			//creating a project and adding it to database
	
			mainModel.addProjectToDb(pmID, name, description, budget, startDate, finishDate, status);
			
			
			Project updatedProject = new Project(1,"Simon","hello", 5, "Mon Jan 02 00:00:00 EST 2017", "Mon Jan 30 00:00:00 EST 2017", Status.LOCKED);
			
			mainModel.updateProject(mainModel.getLastProject(), updatedProject);
			
			Project didItWork = mainModel.getLastProject();
			
			
			assertEquals("Simon",(didItWork.getName()));
			assertEquals("hello", (didItWork.getDescription()));
			assertEquals(true,5 == (didItWork.getBudget()));
			assertEquals("Mon Jan 02 00:00:00 EST 2017" ,(didItWork.getStartDate()));
			assertEquals("Mon Jan 30 00:00:00 EST 2017" ,(didItWork.getFinishDate()));
			assertEquals(0,Status.LOCKED.compareTo(didItWork.getStatus()));
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	

	@Test
	public void testProjectExists() {

		try {
			MainModel mainModel = MainModel.getInstance();
			assertEquals(true,mainModel.projectExists(1));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testActivityExists() {

		try {
			MainModel mainModel = MainModel.getInstance();
			assertEquals(true,mainModel.activityExists(1));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Test
	public void testActsTotalBudgetSuitsParent() {

		try {
			MainModel mainModel = MainModel.getInstance();
			Project testBudget = new Project(1,"Samer","hello", 1000, "Wed Jun 01 00:00:00 EDT 2016", "Wed Jun 29 00:00:00 EDT 2016", Status.LOCKED);
			mainModel.addProjectToDb(testBudget);
			testBudget = mainModel.getLastProject();
			int pID = testBudget.getID();
			Activity newAct1 = new Activity(pID, "act1", "testing activity" , 500 , "Wed Jun 01 00:00:00 EDT 2016", "Wed Jun 08 00:00:00 EDT 2016", Status.LOCKED);
			Activity newAct2 = new Activity(pID, "act2", "testing activity" , 600 , "Fri Jun 10 00:00:00 EDT 2016", "Fri Jun 17 00:00:00 EDT 2016", Status.LOCKED);
			mainModel.addActivityToDb(newAct1);
			assertEquals(false,mainModel.activityBudgetSuitsParent(newAct2,testBudget));
			
			assertEquals(true,mainModel.activityExists(1));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//This method doesn't really need testing as actsTotalBudgetSuitsParent work. If the method was to fail then we should look for this case.
	@Test
	public void testActivityBudgetSuitsParent() {
		fail("Not yet implemented");
	}



	@Test
	public void testActivityDatesSuitsParent() {

		try {
			MainModel mainModel = MainModel.getInstance();
			Project testDate = new Project(1,"Simon","hello", 1000, "Wed Jun 01 00:00:00 EDT 2016", "Fri Jul 01 00:00:00 EDT 2016", Status.UNLOCKED);
			Activity newAct1 = new Activity(2, "act1", "testing activity" , 500 , "Wed Jun 01 00:00:00 EDT 2016", "Wed Jun 08 00:00:00 EDT 2016", Status.UNLOCKED);
			Activity newAct2 = new Activity(2, "act2", "testing activity" , 500 , "Mon May 30 00:00:00 EDT 2016", "Fri Jul 15 00:00:00 EDT 2016", Status.UNLOCKED);
			Activity newAct3 = new Activity(2, "act3", "testing activity" , 500 , "Thu Jun 16 00:00:00 EDT 2016", "Sat Jul 02 00:00:00 EDT 2016", Status.UNLOCKED);
		
			assertEquals(0,mainModel.activityDatesSuitsParent(newAct1, testDate));
			assertEquals(1,mainModel.activityDatesSuitsParent(newAct2, testDate));
			assertEquals(2,mainModel.activityDatesSuitsParent(newAct3, testDate));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testActivityDatesSuitsPrereq() {
		
		try {
			MainModel mainModel = MainModel.getInstance();
			Activity newAct1 = new Activity(2, "act1", "testing activity" , 500 , "Wed Jun 01 00:00:00 EDT 2016", "Wed Jun 08 00:00:00 EDT 2016", Status.UNLOCKED);
			Activity newAct2 = new Activity(2, "act1", "testing activity" , 500 , "Thu Jun 09 00:00:00 EDT 2016", "Fri Jul 15 00:00:00 EDT 2016", Status.UNLOCKED);
			
			assertEquals(true,mainModel.activityDatesSuitsPrereq(newAct2, newAct1));
			assertEquals(false,mainModel.activityDatesSuitsPrereq(newAct1, newAct2));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	// This method is calling testActivityDatesSuitsPrereq for all activities since testActivityDatesSuitsPrereq works this one should also.
	@Test
	public void testActivityDatesSuitsAllPrereqs() {
		fail("Not yet implemented");
	}




	@Test
	public void testActivityDatesSuitsSuccessor() {
		
		try {
			MainModel mainModel = MainModel.getInstance();
			Activity newAct = new Activity(2, "act1", "testing activity" , 500 , "Wed Jun 01 00:00:00 EDT 2016", "Wed Jun 08 00:00:00 EDT 2016", Status.UNLOCKED);
			Activity newAct2 = new Activity(2, "act1", "testing activity" , 500 , "Thu Jun 09 00:00:00 EDT 2016", "Fri Jul 15 00:00:00 EDT 2016", Status.UNLOCKED);
			
			assertEquals(true,mainModel.activityDatesSuitsPrereq(newAct2, newAct));
			assertEquals(false,mainModel.activityDatesSuitsPrereq(newAct, newAct2));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	// This method is calling testActivityDatesSuitsSuccessor for all activities since testActivityDatesSuitsSuccessor works this one should also
  	@Test
	public void testActivityDatesSuitsAllSuccessors() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsPrereqDone() {
		
		try {
			MainModel mainModel = MainModel.getInstance();
			Activity newAct = new Activity(2, "act1", "testing activity" , 500 , "Wed Jun 01 00:00:00 EDT 2016", "Wed Jun 08 00:00:00 EDT 2016", Status.FINISHED);
			Activity newAct2 = new Activity(2, "act2", "testing activity" , 500 , "Thu Jun 09 00:00:00 EDT 2016", "Fri Jul 15 00:00:00 EDT 2016", Status.UNLOCKED);
			
			newAct2.addpreReq(newAct);
			assertEquals(true,mainModel.isPrereqDone(newAct2));
			
			
			Activity newAct1 = new Activity(2, "act1", "testing activity" , 500 , "Wed Jun 01 00:00:00 EDT 2016", "Wed Jun 08 00:00:00 EDT 2016", Status.UNLOCKED);
			Activity newAct3 = new Activity(2, "act1", "testing activity" , 500 , "Thu Jun 09 00:00:00 EDT 2016", "Fri Jul 15 00:00:00 EDT 2016", Status.UNLOCKED);
			newAct3.addpreReq(newAct1);
			assertEquals(false,mainModel.isPrereqDone(newAct3));
			
			
		} catch (Exception e) {
			
			System.out.println("In testIsPrereqDone");
			e.printStackTrace();
		}
	}
	
	
	
	

	@Test //Altought the test passes, this test doesn't work it always goes in the catch... 
	public void testDeleteProjectfromDb() {
		
		try{
			MainModel mainModel = MainModel.getInstance();
			Project testdelete = new Project(1,"Simon","hello", 1000, "Fri Jun 10 00:00:00 EDT 2016", "Fri Jul 15 00:00:00 EDT 2016", Status.UNLOCKED);
			mainModel.addProjectToDb(testdelete);
			int pID = mainModel.getLastProject().getID();
			Activity newAct = new Activity(pID, "act1", "testing activity" , 500 , "Sat Jun 11 00:00:00 EDT 2016", "Wed Jun 15 00:00:00 EDT 2016", Status.UNLOCKED);
			//Activity newAct2 = new Activity(2, "act2", "testing activity" , 600 , "Fri Jun 17 00:00:00 EDT 2016", "Fri Jun 24 00:00:00 EDT 2016", Status.UNLOCKED);

			mainModel.addActivityToDb(newAct);
			mainModel.deleteProjectfromDb(pID);
			
			assertEquals(true,(mainModel.getProjectByID(pID) == null));
		} catch(Exception e){
			if(e.getMessage().contains("doesn't exist")){
			System.out.println("Project was successfully deleted");
			}
		}
	}
		


	@Test
	public void testDeleteActivityfromDb() {
		fail("Not yet implemented");
	}
	@Test
	public void testAddActivityPrereq() {
		fail("Not yet implemented");
	}
	@Test
	public void testRemoveActivityPrereq() {
		fail("Not yet implemented");
	}
	@Test
	public void testGetAllProjects() {
		fail("Not yet implemented");
	}
	@Test
	public void testGetManagerProjects() {
		fail("Not yet implemented");
	}
	@Test
	public void testGetAllActivities() {
		fail("Not yet implemented");
	}
	@Test
	public void testGetLastProject() {
		fail("Not yet implemented");
	}
	@Test
	public void testGetLastActivity() {
		fail("Not yet implemented");
	}
	@Test
	public void testGetProjectByID() {
		fail("Not yet implemented");
	}
	@Test
	public void testGetActivityByID() {
		fail("Not yet implemented");
	}
	@Test
	public void testGetProjectActivities() {
		fail("Not yet implemented");
	}
	@Test
	public void testGetActivityPreReq() {
		fail("Not yet implemented");
	}
	@Test
	public void testGetActivitySuccessors() {
		fail("Not yet implemented");
	}
	@Test
	public void testGetProjectActivitiesNames() {
		fail("Not yet implemented");
	}

	//This method also tests getMemberActivities(), getMemberProjects() and getActivityMembers()
		@Test
		public void testAddMemberToActivity() {
			try {
				MainModel mainModel = MainModel.getInstance();

				//Adding a member to the database
				mainModel.addMemberToDatabase("AAAA", "testMemberA", 0);
				mainModel.addMemberToDatabase("BBBB", "testMemberB", 0);

				//Adding a project to the database.
				int pmID = 1;
				String name = "TestProject1";
				String description = "";
				double budget = 1000;
				String startDate = "Mon Jul 20 00:00:00 EDT 2015";
				String finishDate = "Wed Jul 20 00:00:00 EDT 2016";
				Status status = Status.LOCKED;

				mainModel.addProjectToDb(pmID, name, description, budget, startDate, finishDate, status);

				//Associating a new activity with TestProject1
				int parentID = mainModel.getLastProject().getID();
				String activityName = "TestA";
				String activitydescription = "";
				double activitybudget = 100;
				String activityStartDate = "Tue Jul 21 00:00:00 EDT 2015";
				String activityFinishDate = "Wed Jul 20 00:00:00 EDT 2016";
				Status activityStatus = Status.LOCKED;

				mainModel.addActivityToDb(parentID, activityName, activitydescription, activitybudget,
						activityStartDate, activityFinishDate, activityStatus);

				//Assigning members to the activity
				int memberA_ID = mainModel.getLastMember().getUserID() - 1;
				int memberB_ID = mainModel.getLastMember().getUserID();
				int activityID = mainModel.getLastActivity().getID();

				mainModel.addMemberToActivity(memberA_ID, activityID);
				mainModel.addMemberToActivity(memberB_ID, activityID);

				ArrayList<Member> activityMembers = mainModel.getActivityMembers(activityID);

				assertEquals("1st Member should be AAAA", "AAAA",
						activityMembers.get(0).getUsername());
				assertEquals("2nd Member should be BBBB", "BBBB",
						activityMembers.get(1).getUsername());

				//Testing getMemberActivities() and getMemberProjects()
				assertEquals("MemberActivities should be equal 1", 1, mainModel.getMemberActivities(memberA_ID).size());
				assertEquals("MemberProjects should be equal 1", 1, mainModel.getMemberProjects(memberA_ID).size());

				mainModel.removeMemberFromActivity(memberB_ID, activityID);

				assertEquals("Remaing number of members assigned should be 1", 1,
						mainModel.getActivityMembers(activityID).size());

			} catch (Exception e) {
				if(e.getMessage().contains("UNIQUE")) {
					System.out.println("This user is already a member!");
				}
			}
		}

		@Test
		public void testRemoveMemberFromActivity() {
			try {
				MainModel mainModel = MainModel.getInstance();

				Activity activity = mainModel.getLastActivity();
				int activityID = activity.getID();

				//Adding a member to the database and retrieve user ID
				mainModel.addMemberToDatabase("testerA", "aa", 0);
				int testerA_ID = mainModel.getLastMember().getUserID();
				mainModel.addMemberToDatabase("testerB", "bb", 0);
				int testerB_ID = mainModel.getLastMember().getUserID();
				
				//Assigning members to the activity
				mainModel.addMemberToActivity(testerA_ID, activityID);
				mainModel.addMemberToActivity(testerB_ID, activityID);

				//Total number of members assigned to activity
				int numberOfMembers = mainModel.getActivityMembers(activityID).size();
				mainModel.removeMemberFromActivity(testerB_ID, activityID);

				//remaining number of members after removing testerB
				int remainingMembers = mainModel.getActivityMembers(activityID).size();
				assertEquals(numberOfMembers-1, remainingMembers);

			} catch (Exception e) {
				if(e.getMessage().contains("UNIQUE")) {
					System.out.println("This user is already a member!");
				}
			}

		}


}*/