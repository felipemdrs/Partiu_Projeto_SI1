package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

	public static Result index() {
        return redirect(routes.Application.travelList());
    }
	
	public static Result travelList() {
		return ok(views.html.travel.list.index.render(0));
	}
	
	public static Result travelListIn() {
		return ok(views.html.travel.list.index.render(1));
	}
	
	public static Result travelListAdmin() {
		return ok(views.html.travel.list.index_admin.render());
	}

	public static Result travelBoard() {
		return ok(views.html.travel.board.index.render());
	}
	
}
