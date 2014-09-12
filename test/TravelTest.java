import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import models.User;
import models.travel.Place;
import models.travel.Travel;

import org.junit.Before;
import org.junit.Test;


public class TravelTest {

	private static User admin;
	//default constraints
	private static String DEFAULT_NAME = "Name",
						DEFAULT_DESCRIPTION = "Description", 
						DEFAULT_PLACE_DESCRIPTION = "Place description", 
						DEFAULT_PHOTO_URL = "http://i.stack.imgur.com/WxVXe.jpg";
	private static double DEFAULT_COORD_X = 9.99, 
						DEFAULT_COORD_Y = 6.66;
	
	private Travel t;
	
	@Before
	public void setUp() throws Exception {
		admin = new User("user2", "user2@mail.com", "admin");
		t = new Travel(admin, DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_COORD_X, DEFAULT_COORD_Y, DEFAULT_PLACE_DESCRIPTION, DEFAULT_PHOTO_URL);
	}
	
	@Test
	public void mustCreateTravel() throws Exception {
		Travel v = new Travel(admin, DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_COORD_X, DEFAULT_COORD_Y,
				DEFAULT_PLACE_DESCRIPTION, DEFAULT_PHOTO_URL); 
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
		assertFalse(t.isParticipating(newUser));
		assertFalse(t.isAdminister(newUser));
		//password here is irrelevant
		assertTrue(t.join(newUser, "123")); //it can be any password
		assertFalse(t.join(newUser, "123")); //already is joined...
		assertTrue(t.isParticipating(newUser));
		assertFalse(t.isAdminister(newUser));
		
		assertTrue(t.isAdminister(admin));
		assertFalse(t.isParticipating(admin));
		assertFalse(t.join(admin, "123")); //admin is the manager, so it cant join (again)
		assertFalse(t.isParticipating(admin));
	}
	
	@Test
	public void mustJoinPrivateTravel() throws Exception {
		t.close("pass123");
		User newUser = new User("user3", "user3@mail.com", "password123");
		//first lets assure user is not joined
		assertFalse(t.isParticipating(newUser));
		assertFalse(t.isAdminister(newUser));
		//dont let him participate using a wrong password
		assertFalse(t.join(newUser, "wrongpassword"));
		assertFalse(t.isParticipating(newUser));
		//now let him participate using the right password
		assertTrue(t.join(newUser, "pass123"));
		assertTrue(t.isParticipating(newUser));
		//dont let admin participate even with right password
		assertFalse(t.join(admin, "pass123"));
		assertFalse(t.isParticipating(admin));
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
			new Travel(null, DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_COORD_X, DEFAULT_COORD_Y, DEFAULT_PLACE_DESCRIPTION, DEFAULT_PHOTO_URL);
			fail();
		} catch(Exception e) { }
		//blank name
		try {
			new Travel(admin, "", DEFAULT_DESCRIPTION, DEFAULT_COORD_X, DEFAULT_COORD_Y, DEFAULT_PLACE_DESCRIPTION, DEFAULT_PHOTO_URL);
			fail();
		} catch(Exception e) { }
		//admin can be set only once (at constructor)
		try {
			t.setAdmin(admin);
			fail();
		} catch(Exception e) { }
		//photo url can be blank
		try {
			new Travel(admin, DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_COORD_X, DEFAULT_COORD_Y, DEFAULT_PLACE_DESCRIPTION, "");
		} catch(Exception e) {
			System.out.println(e.getMessage());
			fail();
		}
	}
	
}
