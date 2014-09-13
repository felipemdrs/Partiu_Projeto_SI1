import java.util.Calendar;
import java.util.Date;

import models.User;
import models.dao.GenericDAOImpl;
import models.travel.Travel;
import play.Application;
import play.GlobalSettings;
import play.db.jpa.JPA;
import play.libs.F;

public class Global extends GlobalSettings {
	
	@Override
	public void onStart(Application arg0) {

		Calendar c = Calendar.getInstance();
		c.add(Calendar.YEAR, 1);
		final Date date = c.getTime();
	
		JPA.withTransaction(new F.Callback0() {
			@Override
			public void invoke() throws Throwable {
					User u = createUser("User1", "user1@mail.com", "password#123");
					createTravel(u, "Viagem 1", "Vamos viajar LOL", -7.2038321, -35.8993208, "Campina Grande", date);
			}
			
			public User createUser(String name, String email, String password) throws Throwable {
				if (GenericDAOImpl.getInstance().findByAttributeName("User", "email", email).isEmpty()) {
					User user = new User(name, email, password);
					GenericDAOImpl.getInstance().persist(user);
					GenericDAOImpl.getInstance().flush();
					return user;
				}
				return null;
			}
			
			public Travel createTravel(User admin, String name, String description, double coordX, 
					double coordY, String placeDescription, Date date) throws Throwable { 
				if (admin != null) {
					Travel travel = new Travel(admin, name, description, coordX, coordY, placeDescription, date);
					GenericDAOImpl.getInstance().persist(travel);
					GenericDAOImpl.getInstance().flush();
				}
				return null;
			}
			
		});
	}
	
}
