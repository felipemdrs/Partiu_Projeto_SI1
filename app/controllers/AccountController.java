package controllers;

import static play.data.Form.form;
import models.User;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public class AccountController extends Controller {

	@Transactional
	public static Result login() {

		String email = form().bindFromRequest().get("email");
		String password = form().bindFromRequest().get("password");

		User user = User.findUserByEmail(email);
		if (user != null) {
			try {
				if (user.passwordIsValid(password)) {
					session().clear();
					session("user", user.getEmail());
					return redirect(routes.TravelController.list());
				}
			} catch (Exception e) {
				return badRequest(e.getMessage());
			}
		}
		return badRequest(views.html.index.render());
	}

	public static Result logout() {
		session().clear();
		return redirect(routes.Application.index());
	}

	public static Result register(String name, String email, String password) {
		return ok(views.html.index.render());
	}
	
	public static Result profile() {
		return ok(views.html.user.profile.index.render());
	}
	
	public static Result searchProfile(Long id) {
		return ok(views.html.user.profile.index.render());
	}

	public static String getCurrentUser() {
		return session("user");
	}

}
