package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class AccountController extends Controller {

	public static Result signin() {
		return ok(views.html.index.render());
    }
	
	public static Result signout() {
		return ok(views.html.index.render());
	}
	
	public static Result register() {
		return ok(views.html.index.render());
    }
	
}
