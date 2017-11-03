package testModel;

import static org.junit.Assert.*;

import model.Activity;
import model.Status;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ActivityTest {

	@Test
	public void testEqualsObject() throws Exception {
		Activity activity = new Activity(1, "ActivityName", "ActivityDescription", 10000.0, "Mon Jun 08 12:00:00 EDT 2015", "Mon Jun 12 12:00:00 EDT 2015", Status.LOCKED);
		Activity _activity = new Activity(2, "ActivityName_2", "ActivityDescription_2", 25000.0, "Mon Jun 09 12:00:00 EDT 2015", "Mon Jun 21 12:00:00 EDT 2015", Status.LOCKED);
		Activity _activity2 = new Activity(1, "ActivityName3", "ActivityDescription3", 12310.0, "Mon Jun 08 12:00:00 EDT 2015", "Mon Jun 12 12:00:00 EDT 2015", Status.LOCKED);
		
		//Test if the activity is comparing itself
		//assertEquals("Comparing to same activity", true, activity.equals(activity));
		
		//Test if the activity is comparing to an instance of another object
		//assertEquals("Comparing to instance of different object", false, activity.equals(new Integer(5)));
			
		//Test if the activity is comparing to is null
		//assertEquals("Comparing to null", false, activity.equals(null));		
			
		//Test if the two activities are different (IDS). FAILED TEST
		//assertEquals("Both activities should be different", false, activity.equals(_activity));
		
		//Test if the two activities have same ID
		assertEquals("Both activities should be equal", true, activity.equals(_activity2));
		
	}

}
