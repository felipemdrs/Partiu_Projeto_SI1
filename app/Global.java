import models.User;
import models.dao.GenericDAOImpl;
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
			}
			
		});
	}
}
