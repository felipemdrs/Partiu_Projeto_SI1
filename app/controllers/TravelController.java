package controllers;

import static play.data.Form.form;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.Post;
import models.Travel;
import models.User;
import models.dao.GenericDAOImpl;
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
			if (!currentUser.isAdminister(travel) && !currentUser.isParticipating(travel)) {
				travels.add(travel);
			}
		}
	
		return ok(views.html.travel.list.index.render(0, currentUser, travels));
	}
	
	@Transactional
	public static Result listIn() {
		User currentUser = AccountController.getCurrentUser();

		return ok(views.html.travel.list.index.render(1, currentUser, currentUser.getTravelsParticipating()));
	}
	
	@Transactional
	public static Result listAdmin() {
		User current = AccountController.getCurrentUser();
		return ok(views.html.travel.list.index_admin.render(current, current.getTravelsAdmin()));
	}

	@Transactional
	public static Result board(Long id) {
		Travel found = Travel.getTravelById(id);
		User current = AccountController.getCurrentUser();
		if (found == null) {
			return badRequest(views.html.travel.board.index.render(current, found, "Viagem não encontrada."));
		} else if (!current.isParticipating(found) && !current.isAdminister(found)) {
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
			return ok(views.html.travel.edit.index.render(found, "Viagem não encontrada.", "", true));
		}
		User current = AccountController.getCurrentUser();
		if (!current.isAdminister(found)) {
			return ok(views.html.travel.edit.index.render(found, "Você não tem permissões para editar esta viagem.", "", true));
		}
		return ok(views.html.travel.edit.index.render(found, "", "", true));
	}
	
	@Transactional
	public static Result saveEdit(Long id) {
		Travel found = Travel.getTravelById(id);
		User current = AccountController.getCurrentUser();
		
		DynamicForm form = form().bindFromRequest();
		String accountPassword = form.get("account-password");
		try {
			if (!current.passwordIsValid(accountPassword)) {
				return badRequest(views.html.travel.edit.index.render(found, "", "Senha da conta inválida.", true));
			}
		} catch (Exception e2) {
			return badRequest(views.html.travel.edit.index.render(found, "", "Senha da conta inválida.", true));
		}
		
		String name = form.get("travel-name");
		String description = form.get("description");
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");  
		Date date = null;
		try {
			date = new Date(format.parse(form.get("date")).getTime());
		} catch (ParseException e1) {
			return badRequest(views.html.travel.edit.index.render(found, "", "Data inválida.", true));
		}  

		double coordX, coordY;
		try {
			coordX = Double.valueOf(form.get("coords-x"));
			coordY = Double.valueOf(form.get("coords-y"));
		} catch(Exception e) {
			return badRequest(views.html.travel.edit.index.render(found, "", "Um local deve ser informado.", true));
		}
		String placeDescription = form.get("place-description");
		boolean locked = form.field("locked").value() != null;
		String password = form.get("password");
		String repeatPassword = form.get("repeat-password");
		if (locked && !password.equals(repeatPassword)) {
			return badRequest(views.html.travel.edit.index.render(found, "", "Senhas não coincidem.", true));
		}
		
		try {
			found.setName(name);
			found.setDescription(description);
			found.setDate(date);
			found.getPlace().setDescription(placeDescription);
			found.getPlace().setCoordX(coordX);
			found.getPlace().setCoordY(coordY);
			if (locked && !password.trim().equals("")) {
				if (found.isLocked()) {
					found.changePassword(password);
				} else {
					found.close(password);
				}
			} else if (!locked) {
				if (found.isLocked()) {
					found.open();
				}
			}
		} catch(Exception e) {
			return badRequest(views.html.travel.edit.index.render(found, "", e.getMessage(), true));
		}
		
		try {
			Travel.merge(found);
		} catch(Exception e) {
			return badRequest(views.html.travel.edit.index.render(found, "", "Erro ao atualizar dados. Tente novamente.", true));
		}
		
		return ok(views.html.travel.edit.index.render(found, "", "Dados atualizados com sucesso.", false));
	}
	
	@Transactional
	public static Result insertPost(Long travelId) {
		DynamicForm form = form().bindFromRequest();
		String message = form.get("message");
		
		Travel found = Travel.getTravelById(travelId);
		if (found == null) {
			return badRequest("Viagem não encontrada.");
		}
		User current = AccountController.getCurrentUser();
		
		found.addPost(current, message);
		try {
			Travel.merge(found);
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
		boolean canEdit = current.isAdminister(travel) || post.getUser().equals(current);
		return ok("[" + canEdit + "]");
	}
	
	@Transactional
	public static Result deletePost(Long travelId, Long postId) {
		Travel travel = Travel.getTravelById(travelId);
		Post post = Post.getPostById(postId);
		User current = AccountController.getCurrentUser();
		boolean canEdit = current.isAdminister(travel) || post.getUser().equals(current);
		if (canEdit) {
			travel.removePost(post);
		} else {
			return badRequest("Operação inválida.");
		}
		try {
			Travel.merge(travel);
		} catch(Exception e) {
			return badRequest("Erro no banco de dados.");
		}
		return ok();
	}
	
	@Transactional
	public static Result create() {
		DynamicForm form = form().bindFromRequest();
		String name = form.get("travel-name");
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
		if (!locked) {
			password = null;
		}

		try {
			AccountController.getCurrentUser().createTravel(name, description, coordX, coordY, placeDescription, date, password);
		} catch(Exception e) {
			return badRequest("Ocorreu um erro. Tente novamente.");
		}

		return redirect(routes.Application.index());
	}

	@Transactional
	public static Result leave(Long id) {
		Travel travel = Travel.getTravelById(id);
		if (travel == null) {
			return badRequest("Viagem não encontrada.");
		}
		User current = AccountController.getCurrentUser();
		if (!travel.leave(current)) {
			return badRequest("Usuário não está participando da viagem."); 
		}

		return ok();
	}

	@Transactional
	public static Result join(Long id, String password) {
		Travel travel = Travel.getTravelById(id);
		if (travel == null) {
			return badRequest("Viagem não encontrada.");
		}

		User current = AccountController.getCurrentUser();
		if (!travel.join(current, password)) {	
			return badRequest("Usuário não está participando da viagem."); 
		}

		return ok();
	}
	
	@Transactional
	public static Result joinOpenTravel(Long id) {
		return join(id, null);
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
