package controllers;

import static play.data.Form.form;
import models.User;
import models.dao.GenericDAOImpl;
import play.data.DynamicForm;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public class AccountController extends Controller {
	
	@Transactional
	public static Result login() {
		String email = form().bindFromRequest().get("email");
		String password = form().bindFromRequest().get("password");

		User user = User.getUserByEmail(email);

		try {
			if (user.passwordIsValid(password)) {
				session().clear();
				session("user_email", user.getEmail());
				return redirect(routes.TravelController.list());
			}
		} catch (Exception e) {
			return badRequest(views.html.index.render(e.getMessage()));
		}
			
		return badRequest(views.html.index.render("Usuário ou senha inválidos."));
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
		
		if (User.getUserByEmail(email) != null) {
			return badRequest(views.html.index.render("Usuário já cadastrado."));
		}
		
		User newUser;
		try {
			newUser = new User(name, email, password);
		} catch(Exception e) {
			return badRequest(views.html.index.render(e.getMessage()));
		}
		
		try {
			User.persist(newUser);
		} catch(Throwable e) {
			return badRequest(views.html.index.render("Ocorreu um erro. Tente novamente."));
		}

		session().clear();
		session("user_email", newUser.getEmail());
		return redirect(routes.TravelController.list());
	}
	
	@Transactional
	public static Result edit() {
		DynamicForm form = form().bindFromRequest();
		String name = form.get("name"); 
		String oldPassword = form.get("old-password");
		String newPassword = form.get("password");
		String repeatPassword = form.get("repeat-password");
		
		final User currentUser = getCurrentUser();

		if (!newPassword.equals(repeatPassword)) {
			return badRequest(views.html.user.edit.index.render(currentUser, true, "Senhas não coincidem"));
		}
		
		currentUser.setName(name);

		try {
			currentUser.setPassword(oldPassword, newPassword);
		} catch(Exception e) {
			return badRequest(views.html.user.edit.index.render(currentUser, true, e.getMessage()));
		}
		
		/*
		MultipartFormData body = request().body().asMultipartFormData();
		FilePart picture = body.getFile("photo");
		if (picture != null) {
			String fileName = picture.getFilename();
			//String contentType = picture.getContentType(); 
			File file = picture.getFile();
			//file.renameTo(new File(fileName));
			currentUser.setPhotoUrl(file.getAbsolutePath());
		}
		*/
		
		try {
			User.merge(currentUser);
		} catch(Throwable e) {
			return badRequest(views.html.user.edit.index.render(currentUser, true, "Ocorreu um erro. Tente novamente."));
		} 
		
		return ok(views.html.user.edit.index.render(currentUser, false, "Dados alterados com sucesso."));
	}

	@Transactional
	public static Result profile() {
		return ok(views.html.user.profile.index.render(getCurrentUser()));
	}
	
	@Transactional
	public static Result searchProfile(Long id) {
		User user = GenericDAOImpl.getInstance().findByEntityId(User.class, id);
		return ok(views.html.user.profile.index.render(user));
	}

	@Transactional
	public static Result config() {
		return ok(views.html.user.edit.index.render(getCurrentUser(), false, ""));
	}
	
	@Transactional
	public static User getCurrentUser() {
		return User.getUserByEmail(session("user_email"));
	}
	
}
