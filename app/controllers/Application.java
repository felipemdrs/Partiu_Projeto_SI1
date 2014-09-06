package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

	private static String TRAVELS_ALL_PAGE_ID = "travels";
	private static String TRAVELS_ADMIN_PAGE_ID = "admin";
	private static String TRAVELS_BOARD_PAGE_ID = "board";

	public static Result index() {
        return ok(views.html.index.render("Your new application is ready."));
    }
	
	public static Result travels_all() {
        return ok(views.html.home.travel_list.index.render(TRAVELS_ALL_PAGE_ID));
    }
	
	public static Result travels_board() {
        return ok(views.html.home.travel_list.index.render(TRAVELS_BOARD_PAGE_ID));
    }
	
	public static Result travels_admin() {
        return ok(views.html.home.travel_list.index.render(TRAVELS_ADMIN_PAGE_ID));
    }
}
