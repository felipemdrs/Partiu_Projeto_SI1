package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

	public static Result index() {
        return ok(views.html.index.render("Your new application is ready."));
    }
	
	public static Result travels_all() {
        return ok(views.html.home.travel_list.index.render());
    }
	
	public static Result travels_board() {
        return ok(views.html.home.travel_list.index.render());
    }
	
	public static Result travels_admin() {
        return ok(views.html.home.travel_list.index.render());
    }
}
