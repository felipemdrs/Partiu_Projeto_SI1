package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

	public static Result index() {
        return ok(views.html.index.render("Your new application is ready."));
    }
	
	public static Result travelList() {
		return ok(views.html.travel.list.index.render(false));
	}
	
	public static Result travelListIn() {
		return ok(views.html.travel.list.index.render(true));
	}
	
	public static Result travelListAdmin() {
		return ok(views.html.travel.admin.index.render());
	}

	public static Result travelNotifications() {
		return ok(views.html.travel.board.index.render());
	}
	
}
