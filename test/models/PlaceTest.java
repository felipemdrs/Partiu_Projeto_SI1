package models;
import static org.junit.Assert.fail;
import models.Place;

import org.junit.Test;


public class PlaceTest {

	@Test
	public void mustThrowException() {
		//empty description
		try {
			new Place(0, 0, "");
			fail();
		} catch(Exception e) { }
		//invalid lat
		try {
			new Place(0, 86, "");
			fail();
		} catch(Exception e) { }
		//invalid lng
		try {
			new Place(181, 0, "");
			fail();
		} catch(Exception e) { }
	}
	
}
