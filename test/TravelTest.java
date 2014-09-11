import static org.junit.Assert.*;
import models.Photo;
import models.Place;
import models.Travel;
import models.User;

import org.junit.Before;
import org.junit.Test;


public class TravelTest {

	private static User admin;
	private static String DEFAULT_NAME = "Name",
						DEFAULT_DESCRIPTION = "Description", 
						DEFAULT_PLACE_DESCRIPTION = "Place description", 
						DEFAULT_PHOTO_URL = "http://i.stack.imgur.com/WxVXe.jpg";
	private static double DEFAULT_COORD_X = 9.99, 
						DEFAULT_COORD_Y = 6.66;
	
	@Before
	public void setUp() throws Exception {
		admin = new User("user2", "user2@mail.com", "admin");
	}
	
	@Test
	public void mustCreateTravel() throws Exception {
		Travel v = new Travel(admin, DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_COORD_X, DEFAULT_COORD_Y,
				DEFAULT_PLACE_DESCRIPTION, DEFAULT_PHOTO_URL); 
		assertEquals(admin, v.getAdmin());
		assertEquals(DEFAULT_NAME, v.getName());
		assertEquals(DEFAULT_DESCRIPTION, v.getDescription());
		Place p = new Place(DEFAULT_COORD_X, DEFAULT_COORD_Y, DEFAULT_PLACE_DESCRIPTION);
		assertEquals(p, v.getPlace());	
		Photo photo = new Photo(DEFAULT_PHOTO_URL);
		assertEquals(photo, v.getPhoto());
	}
	
	@Test
	public void mustReturnDefaultPhoto() throws Exception {
		Photo photoNormal = new Photo(DEFAULT_PHOTO_URL);
		Photo photoDefault = new Photo(Photo.DEFAULT_PHOTO);
		Travel v1 = new Travel(admin, DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_COORD_X, DEFAULT_COORD_Y,
				DEFAULT_PLACE_DESCRIPTION, DEFAULT_PHOTO_URL);
		Travel v2 = new Travel(admin, DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_COORD_X, DEFAULT_COORD_Y,
				DEFAULT_PLACE_DESCRIPTION, "");
		assertEquals(photoNormal, v1.getPhoto());
		assertEquals(photoDefault, v2.getPhoto());
	}
	
	@Test
	public void mustThrowExceptions() throws Exception {
		
		try {
			new Travel(null, DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_COORD_X, DEFAULT_COORD_Y, DEFAULT_PLACE_DESCRIPTION, DEFAULT_PHOTO_URL);
			fail();
		} catch(Exception e) { }
		try {
			new Travel(admin, "", DEFAULT_DESCRIPTION, DEFAULT_COORD_X, DEFAULT_COORD_Y, DEFAULT_PLACE_DESCRIPTION, DEFAULT_PHOTO_URL);
			fail();
		} catch(Exception e) { }
		try {
			new Travel(admin, DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_COORD_X, DEFAULT_COORD_Y, DEFAULT_PLACE_DESCRIPTION, "");
		} catch(Exception e) {
			System.out.println(e.getMessage());
			fail();
		}
	}
	
}
