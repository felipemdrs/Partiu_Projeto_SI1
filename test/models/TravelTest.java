package models;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;


public class TravelTest {

	private static User admin;
	//default constraints
	private static String DEFAULT_NAME = "Name",
						DEFAULT_DESCRIPTION = "Description", 
						DEFAULT_PLACE_DESCRIPTION = "Place description";
					
	private static double DEFAULT_COORD_X = 9.99, 
						DEFAULT_COORD_Y = 6.66;
	private static Date DATE = Calendar.getInstance().getTime();
	
	private Travel t;
	
	@Before
	public void setUp() throws Exception {
		admin = new User("user2", "user2@mail.com", "admin");
		t = admin.createTravel(DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_COORD_X, DEFAULT_COORD_Y, DEFAULT_PLACE_DESCRIPTION, DATE);
	}
	
	@Test
	public void mustCreateTravel() throws Exception {
		Travel v = new Travel(admin, DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_COORD_X, DEFAULT_COORD_Y,
				DEFAULT_PLACE_DESCRIPTION,  DATE); 
		//verify gets, sets and equals
		assertEquals(admin, v.getAdmin());
		assertEquals(DEFAULT_NAME, v.getName());
		assertEquals(DEFAULT_DESCRIPTION, v.getDescription());
		Place p = new Place(DEFAULT_COORD_X, DEFAULT_COORD_Y, DEFAULT_PLACE_DESCRIPTION);
		assertEquals(p, v.getPlace());	
	}

	@Test
	public void mustJoinPublicTravel() throws Exception {
		User newUser = new User("user3", "user3@mail.com", "password123");
		assertFalse(newUser.isParticipating(t));
		assertFalse(newUser.isAdminister(t));
		//password here is irrelevant
		assertTrue(t.join(newUser, "123")); //it can be any password
		assertFalse(t.join(newUser, "123")); //already is joined...
		assertTrue(newUser.isParticipating(t));
		assertFalse(newUser.isAdminister(t));
		
		assertTrue(admin.isAdminister(t));
		assertFalse(admin.isParticipating(t));
		assertFalse(t.join(admin, "123")); //admin is the manager, so it cant join (again)
		assertFalse(admin.isParticipating(t));
	}
	
	@Test
	public void mustJoinPrivateTravel() throws Exception {
		t.close("pass123");
		User newUser = new User("user3", "user3@mail.com", "password123");
		//first lets assure user is not joined
		assertFalse(newUser.isParticipating(t));
		assertFalse(newUser.isAdminister(t));
		//dont let him participate using a wrong password
		assertFalse(t.join(newUser, "wrongpassword"));
		assertFalse(newUser.isParticipating(t));
		//now let him participate using the right password
		assertTrue(t.join(newUser, "pass123"));
		assertTrue(newUser.isParticipating(t));
		//dont let admin participate even with right password
		assertFalse(t.join(admin, "pass123"));
		assertFalse(admin.isParticipating(t));
	}
	
	@Test
	public void mustReturnIllegalState() throws Exception {
		//open state (default)
		try {
			t.open();
			fail();
		} catch(Exception e) { }
		try {
			t.changePassword("...");
			fail();
		} catch(Exception e) { }
		try {
			t.close("123");
		} catch(Exception e) {
			e.printStackTrace();
			fail();
		}
		//closed state
		try {
			t.close("123");
			fail();
		} catch(Exception e) { }
		try {
			t.changePassword("567");
		} catch(Exception e) {
			fail();
		}
		try {
			t.open();
		} catch(Exception e) { }
	}
	
	@Test
	public void mustThrowExceptions() throws Exception {
		//null admin
		try {
			new Travel(null, DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_COORD_X, DEFAULT_COORD_Y, DEFAULT_PLACE_DESCRIPTION, DATE);
			fail();
		} catch(Exception e) { }
		//blank name
		try {
			new Travel(admin, "", DEFAULT_DESCRIPTION, DEFAULT_COORD_X, DEFAULT_COORD_Y, DEFAULT_PLACE_DESCRIPTION, DATE);
			fail();
		} catch(Exception e) { }
		//admin can be set only once (at constructor)
		try {
			t.setAdmin(admin);
			fail();
		} catch(Exception e) { }
		//place description cannot be empty
		try {
			new Travel(admin, DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_COORD_X, DEFAULT_COORD_Y, "", DATE);
			fail();
		} catch(Exception e) { }
	}
	
}
