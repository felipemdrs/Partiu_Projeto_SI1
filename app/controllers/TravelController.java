package controllers;

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
	
}
