package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class TravelController extends Controller {

	public static Result list() {
		return ok(views.html.travel.list.index.render(0));
	}
	
	public static Result listIn() {
		return ok(views.html.travel.list.index.render(1));
	}
	
	public static Result listAdmin() {
		return ok(views.html.travel.list.index_admin.render());
	}

	public static Result board(Long id) {
		return ok(views.html.travel.board.index.render());
	}
	
	public static Result info(Long id) {
		return ok(views.html.travel.info.index.render());
	}
	
}
