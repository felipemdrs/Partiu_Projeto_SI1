package controllers;

import static play.data.Form.form;

import java.util.List;

import models.User;
import models.dao.GenericDAO;
import models.dao.GenericDAOImpl;
import play.data.DynamicForm;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.F.Function0;
import play.mvc.Controller;
import play.mvc.Result;

public class AccountController extends Controller {

	private static GenericDAO dao = new GenericDAOImpl();
	
	@Transactional
	public static Result login() {
		String email = form().bindFromRequest().get("email");
		String password = form().bindFromRequest().get("password");

		List<User> users = dao.findByAttributeName("User", "email", email);
		if (!users.isEmpty()) {
			User user = users.get(0);
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
		return badRequest(views.html.index.render(true, "Usuário ou senha inválidos."));
	}

	public static Result logout() {
		session().clear();
		return redirect(routes.Application.index());
	}

	@Transactional
	public static Result register() { 
		DynamicForm form = form().bindFromRequest();
		String name = form.get("name");
		String email = form.get("email");
		String password = form.get("password");
		
		User newUser;
		try {
			newUser = new User(name, email, password);
		} catch(Exception e) {
			return badRequest(views.html.index.render(true, e.getMessage()));
		}
		
		boolean success;
		try {
			success = dao.persist(newUser);
			dao.flush();
		} catch(Exception e) {
			return badRequest(views.html.index.render(true, e.getMessage()));
		}
		if (!success) {
			return badRequest(views.html.index.render(true, "Ocorreu um erro. Tente novamente."));
		}
		
		session().clear();
		session("user", newUser.getEmail());
		return redirect(routes.TravelController.list());
	}
	
	@Transactional
	public static Result edit() {
		if (AccountController.getCurrentUser() == null) {
			return redirect(routes.Application.index());
		}
		DynamicForm form = form().bindFromRequest();
		String name = form.get("name");
		String oldPassword = form.get("old-password");
		String password = form.get("password");
		String repeatPassword = form.get("repeat-password");
		
		final User current = getCurrentUser();
		try {
			if (!current.passwordIsValid(oldPassword)) {
				return badRequest(views.html.user.edit.index.render(current, true, "Senha incorreta."));
			}
		} catch(Exception e) {
			return badRequest(views.html.user.edit.index.render(current, true, "Ocorreu um erro. Tente novamente."));
		}
		if (!password.equals(repeatPassword)) {
			return badRequest(views.html.user.edit.index.render(current, true, "Senhas não coincidem"));
		}
		
		current.setName(name);
		if (!password.trim().equals("")) {
			try {
				current.setPassword(password);
			} catch(Exception e) {
				return badRequest(views.html.user.edit.index.render(current, true, "Ocorreu um erro. Tente novamente."));
			}
		}
		
		try {
			JPA.withTransaction(new Function0<Void>() {
			    @Override
			    public Void apply() throws Throwable {
			    	dao.merge(current);
					dao.flush();
					return null;
			    }
			});
		} catch(Throwable e) {
			return badRequest(views.html.user.edit.index.render(current, true, "Ocorreu um erro. Tente novamente."));
		} 
		
		return ok(views.html.user.edit.index.render(current, false, "Dados alterados com sucesso."));
	}

	@Transactional
	public static Result profile() {
		if (AccountController.getCurrentUser() == null) {
			return redirect(routes.Application.index());
		}
		return ok(views.html.user.profile.index.render(getCurrentUser()));
	}
	
	@Transactional
	public static Result searchProfile(Long id) {
		if (AccountController.getCurrentUser() == null) {
			return redirect(routes.Application.index());
		}
		User user = dao.findByEntityId(User.class, id);
		return ok(views.html.user.profile.index.render(user));
	}

	@Transactional
	public static Result config() {
		if (AccountController.getCurrentUser() == null) {
			return redirect(routes.Application.index());
		}
		return ok(views.html.user.edit.index.render(getCurrentUser(), false, ""));
	}
	
	@Transactional
	public static User getCurrentUser() {
		try {
			User u = JPA.withTransaction(new Function0<User>() {
			    @Override
			    public User apply() throws Throwable {
			    	List<User> users = dao.findByAttributeName("User", "email", session("user"));
					if (users.isEmpty()) {
						return null;
					}
					return users.get(0);
			    }
			});
			return u;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

}
