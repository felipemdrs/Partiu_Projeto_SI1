package controllers;

import static play.data.Form.form;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import models.dao.GenericDAOImpl;
import models.travel.Travel;
import play.data.DynamicForm;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public class TravelController extends Controller {

	@Transactional
	public static Result list() {
		if (AccountController.getCurrentUser() == null) {
			return redirect(routes.Application.index());
		}
		return ok(views.html.travel.list.index.render(0));
	}
	
	@Transactional
	public static Result listIn() {
		if (AccountController.getCurrentUser() == null) {
			return redirect(routes.Application.index());
		}
		return ok(views.html.travel.list.index.render(1));
	}
	
	@Transactional
	public static Result listAdmin() {
		if (AccountController.getCurrentUser() == null) {
			return redirect(routes.Application.index());
		}
		return ok(views.html.travel.list.index_admin.render());
	}

	@Transactional
	public static Result board(Long id) {
		if (AccountController.getCurrentUser() == null) {
			return redirect(routes.Application.index());
		}
		return ok(views.html.travel.board.index.render());
	}
	
	@Transactional
	public static Result info(Long id) {
		if (AccountController.getCurrentUser() == null) {
			return redirect(routes.Application.index());
		}
		return ok(views.html.travel.info.index.render());
	}
	
	@Transactional
	public static Result edit(Long id) {
		if (AccountController.getCurrentUser() == null) {
			return redirect(routes.Application.index());
		}
		return ok(views.html.travel.edit.index.render());
	}
	
	@Transactional
	public static Result create() {
		DynamicForm form = form().bindFromRequest();
		String name = form.get("name");
		String description = form.get("description");
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");  
		Date date = null;
		String datePattern = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";
		if (form.get("date").matches(datePattern)) {
			try {
				date = new Date(format.parse(form.get("date")).getTime());
			} catch (ParseException e1) {
				return badRequest("Data inválida.");
			}  
		} else {
			return badRequest("Data inválida.");
		}
		double coordX, coordY;
		try {
			coordX = Double.valueOf(form.get("coords-x"));
			coordY = Double.valueOf(form.get("coords-y"));
		} catch(Exception e) {
			return badRequest("Um local deve ser informado.");
		}
		String placeDescription = form.get("place-description");
		boolean locked = form.field("locked").value() != null;
		String password = form.get("password");
		String repeatPassword = form.get("repeat-password");
		if (locked && !password.equals(repeatPassword)) {
			return badRequest("Senhas não coincidem.");
		}
		
		Travel newTravel;
		try {
			newTravel = new Travel(AccountController.getCurrentUser(), name, description, coordX, coordY, placeDescription, date);
		} catch(Exception e) {
			return badRequest(e.getMessage());
		}
		
		try {
			GenericDAOImpl.getInstance().persist(newTravel);
			GenericDAOImpl.getInstance().flush();
		} catch(Throwable e) {
			return badRequest("Ocorreu um erro. Tente novamente.");
		}
		
		return redirect(routes.Application.index());
	}
	
}
