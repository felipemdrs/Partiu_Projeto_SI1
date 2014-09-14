package controllers;

import static play.data.Form.form;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.User;
import models.dao.GenericDAOImpl;
import models.travel.Post;
import models.travel.Travel;
import play.data.DynamicForm;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TravelController extends Controller {

	@Transactional
	public static Result list() {
		User currentUser = AccountController.getCurrentUser();
		
		List<Travel> allTravels = GenericDAOImpl.getInstance().findAllByClassName("Travel");

		Set<Travel> travels = new HashSet<>();

		for (Travel travel : allTravels) {
			if (!travel.isAdminister(currentUser) && !travel.isParticipating(currentUser)) {
				travels.add(travel);
			}
		}
		
		return ok(views.html.travel.list.index.render(0, travels));
	}
	
	@Transactional
	public static Result listIn() {
		return ok(views.html.travel.list.index.render(1, AccountController.getCurrentUser().getTravelsParticipating()));
	}
	
	@Transactional
	public static Result listAdmin() {
		return ok(views.html.travel.list.index_admin.render(AccountController.getCurrentUser().getTravelsAdmin()));
	}

	@Transactional
	public static Result board(Long id) {
		Travel found = Travel.getTravelById(id);
		User current = AccountController.getCurrentUser();
		if (found == null) {
			return badRequest(views.html.travel.board.index.render(current, found, "Viagem não encontrada."));
		} else if (!found.isParticipating(current) && !found.isAdminister(current)) {
			return badRequest(views.html.travel.board.index.render(current, found, "Você não tem permissões para ver esta viagem."));
		}
		return ok(views.html.travel.board.index.render(current, found, ""));
	}
	
	@Transactional
	public static Result info(Long id) {
		Travel found = Travel.getTravelById(id);
		User current = AccountController.getCurrentUser();
		if (found == null) {
			return ok(views.html.travel.info.index.render(current, found, "Viagem não encontrada."));
		}
		return ok(views.html.travel.info.index.render(current, found, ""));
	}
	
	@Transactional
	public static Result edit(Long id) {
		Travel found = Travel.getTravelById(id);
		if (found == null) {
			return ok(views.html.travel.edit.index.render(found, "Viagem não encontrada."));
		}
		User current = AccountController.getCurrentUser();
		if (!found.isAdminister(current)) {
			return ok(views.html.travel.edit.index.render(found, "Você não tem permissões para editar esta viagem."));
		}
		return ok(views.html.travel.edit.index.render(found, ""));
	}
	
	@Transactional
	public static Result post(Long travelId) {
		DynamicForm form = form().bindFromRequest();
		String message = form.get("message");
		
		Travel found = Travel.getTravelById(travelId);
		if (found == null) {
			return badRequest("Viagem não encontrada.");
		}
		User current = AccountController.getCurrentUser();
		
		found.addPost(current, message);
		try {
			GenericDAOImpl.getInstance().merge(found);
			GenericDAOImpl.getInstance().flush();
		} catch(Exception e) {
			return badRequest("Erro ao publicar o post. Tente novamente.");
		}
		
		return redirect(routes.Application.index());
	}
	
	@Transactional
	public static Result getPosts(Long id) {
		Travel found = Travel.getTravelById(id);
		if (found == null) {
			return badRequest("Viagem não encontrada.");
		}
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		
		try {
			json = mapper.writeValueAsString(found.getPosts());
		} catch (Exception e) {
			return badRequest();
		}
		
		return ok(json);
	}
	
	@Transactional
	public static Result canEditPost(Long travelId, Long postId) {
		Travel travel = Travel.getTravelById(travelId);
		Post post = Post.getPostById(postId);
		User current = AccountController.getCurrentUser();
		boolean canEdit = travel.isAdminister(current) || post.getUser().equals(current);
		return ok("[" + canEdit + "]");
	}
	
	@Transactional
	public static Result deletePost(Long travelId, Long postId) {
		Travel travel = Travel.getTravelById(travelId);
		Post post = Post.getPostById(postId);
		User current = AccountController.getCurrentUser();
		boolean canEdit = travel.isAdminister(current) || post.getUser().equals(current);
		if (canEdit) {
			travel.removePost(post);
		} else {
			return badRequest("Operação inválida.");
		}
		GenericDAOImpl.getInstance().merge(travel);
		GenericDAOImpl.getInstance().flush();
		return ok();
	}
	
	@Transactional
	public static Result create() {
		DynamicForm form = form().bindFromRequest();
		String name = form.get("name");
		String description = form.get("description");
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");  
		Date date = null;
			try {
				date = new Date(format.parse(form.get("date")).getTime());
			} catch (ParseException e1) {
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
	
	@Transactional
	public static Result travelsParticipating() {
		User user = AccountController.getCurrentUser();
		
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		
		try {
			json = mapper.writeValueAsString(user.getTravelsParticipating());
		} catch (Exception e) {
			return badRequest();
		}
		
		return ok(json);
	}
	
	@Transactional
	public static Result travelsAdmin() {
		User user = AccountController.getCurrentUser();
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		
		try {
			json = mapper.writeValueAsString(user.getTravelsAdmin());
		} catch (Exception e) {
			return badRequest(e.getMessage());
		}
		
		return ok(json);
	}
	
}
