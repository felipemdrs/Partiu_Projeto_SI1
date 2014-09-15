import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import models.Travel;
import models.User;
import models.dao.GenericDAOImpl;
import play.Application;
import play.GlobalSettings;
import play.db.jpa.JPA;
import play.libs.F;
import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Http.Request;
import play.mvc.Result;
import controllers.routes;

public class Global extends GlobalSettings {

	private final List<String> ROUTES_WHITE_LIST = Arrays.asList("/", "/login",
			"/register");
	private final int AMOUNT_USER = 40;
	private final int AMOUNT_TRAVEL = 30;

	private final Double DEFAULT_COORD_X = -7.2038321;
	private final Double DEFAULT_COORD_Y = -35.8993208;

	private final String DEFAULT_PASSWORD = "password#123";

	private final int MAX_PARTICIPATING = 15;

	@Override
	public void onStart(Application arg0) {

		Calendar c = Calendar.getInstance();
		c.add(Calendar.YEAR, 1);
		final Date date = c.getTime();

		JPA.withTransaction(new F.Callback0() {
			@Override
			public void invoke() throws Throwable {

				createFakeUsers(date);
				
				createFakeTravels(date);

				createFakeParticipating();
				
			}

			private void createFakeParticipating() {
				int currentParticipating = 0;

				while (currentParticipating < MAX_PARTICIPATING) {
					Travel travel = getRandomTravel();
					User user = getRandomUser();

					if (!(travel.getAdmin().equals(user))) {
						boolean result = travel.join(user, DEFAULT_PASSWORD);
						if (result) {
							currentParticipating++;
						}
					}
				}
			}

			private void createFakeTravels(final Date date) throws Throwable {
				for (int i = 2; i <= AMOUNT_TRAVEL; i++) {
					User user = getRandomUser();

					if (randInt(0, 1) == 1) {
						createTravel(user, "Travel " + i, "Go to travel " + i,
								DEFAULT_COORD_X, DEFAULT_COORD_Y, "Location "
										+ i, date, DEFAULT_PASSWORD);
					} else {
						createTravel(user, "Travel " + i, "Go to travel " + i,
								DEFAULT_COORD_X, DEFAULT_COORD_Y, "Location "
										+ i, date, null);
					}
				}
			}

			private void createFakeUsers(final Date date) throws Throwable {
				User user1 = createUser("User1", "user1@mail.com",
						DEFAULT_PASSWORD);

				for (int i = 2; i <= AMOUNT_USER; i++) {
					createUser("User" + i, "user" + i + "@mail.com",
							DEFAULT_PASSWORD);
				}
				
				createTravel(user1, "Travel 1", "Go to travel 1",
						DEFAULT_COORD_X, DEFAULT_COORD_Y, "Location 1", date,
						null);
			}

			private int randInt(int min, int max) {
				Random rand = new Random();
				int randomNum = rand.nextInt((max - min) + 1) + min;
				return randomNum;
			}

			private User getRandomUser() {
				return (User) GenericDAOImpl
						.getInstance()
						.findByAttributeName("User", "id",
								String.valueOf(randInt(1, AMOUNT_USER))).get(0);
			}

			private Travel getRandomTravel() {
				return (Travel) GenericDAOImpl
						.getInstance()
						.findByAttributeName("Travel", "id",
								String.valueOf(randInt(1, AMOUNT_TRAVEL)))
						.get(0);
			}

			public User createUser(String name, String email, String password)
					throws Throwable {
				if (GenericDAOImpl.getInstance()
						.findByAttributeName("User", "email", email).isEmpty()) {
					User user = new User(name, email, password);
					User.persist(user);
					return user;
				}
				return null;
			}

			public Travel createTravel(User admin, String name,
					String description, double coordX, double coordY,
					String placeDescription, Date date, String password)
					throws Throwable {
				if (admin != null) {
					if (password != null) {
						admin.createTravel(name, description, coordX, coordY,
								placeDescription, date);
					} else {
						admin.createTravel(name, description, coordX, coordY,
								placeDescription, date, password);
					}
				}
				return null;
			}
		});
	}

	@Override
	public Action<Void> onRequest(Request request, Method arg1) {
		String requestURI = request.uri();
		if (!ROUTES_WHITE_LIST.contains(requestURI)
				&& request.cookie("PLAY_SESSION") == null) {
			return new Action.Simple() {
				@Override
				public Promise<Result> call(Context arg0) throws Throwable {
					return F.Promise.pure(redirect(routes.Application.index()));
				}
			};
		}
		return super.onRequest(request, arg1);
	}

}
