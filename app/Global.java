import models.User;
import models.dao.GenericDAO;
import models.dao.GenericDAOImpl;
import models.travel.Travel;
import play.Application;
import play.GlobalSettings;
import play.db.jpa.JPA;
import play.libs.F;

public class Global extends GlobalSettings {
	
	@Override
	public void onStart(Application arg0) {
		JPA.withTransaction(new F.Callback0() {
			@Override
			public void invoke() throws Throwable {
				User user = new User("User1", "user1@mail.com", "password#123");
				GenericDAOImpl.getInstance().persist(user);
				GenericDAOImpl.getInstance().flush();
				
				Travel travel = new Travel(user, "Viagem 1", "Vamos viajar LOL", -7.2038321, -35.8993208, "Campina Grande");
				GenericDAOImpl.getInstance().persist(travel);
				GenericDAOImpl.getInstance().flush();
			}
			
		});
	}
	
}
