package models.travel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;

import models.User;
import models.dao.GenericDAOImpl;
import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;

@Entity
public class Travel {

	private static final String DEFAULT_PHOTO = "/assets/img/perfil.jpg";
	private static final int MAX_NAME_LENGTH = 40;
	private static final int MAX_DESCRIPTION_LENGTH = 350;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	@Required
	@NotNull
	@MaxLength(value = MAX_NAME_LENGTH)
	private String name;
	
	@Column
	@MaxLength(value = MAX_DESCRIPTION_LENGTH)
	private String description;
	
	@Column
	private String photoUrl;

	@ManyToOne(cascade=CascadeType.ALL)
	@Required
	@NotNull
	private Place place;

	@ManyToOne(cascade=CascadeType.ALL)
	@Required
	@NotNull
	@JsonBackReference
	private User admin;

	@Temporal(TemporalType.DATE)
	@Required
	@NotNull
	private Date date;
	
	@ManyToOne(cascade=CascadeType.ALL)
	private TravelState state = new OpenState(this);

	@OneToMany(cascade=CascadeType.ALL)
	private Set<User> participating = new HashSet<User>();
	
	@OneToMany(cascade=CascadeType.ALL)
	@JsonBackReference
	private List<Post> posts = new ArrayList<Post>();
	
	@SuppressWarnings("unused")
	private Travel() { }
	
	public Travel(User admin, String name, String description, double coordX, 
			double coordY, String placeDescription, Date date) 
			throws TravelException {
		setAdmin(admin);
		setName(name);
		setDescription(description);
		setPlace(new Place(coordX, coordY, placeDescription));
		setDate(date);
		setPhotoUrl("");
	}
	
	public Travel(User admin, String name, String description, double coordX,
			double coordY, String placeDescription, String photoUrl, Date date)
			throws TravelException {
		this(admin, name, description, coordX, coordY, placeDescription, date);
		setPhotoUrl(photoUrl);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) throws TravelException {
		if (name == null || name.trim().equals("")) {
			throw new TravelException("Nome da viagem não pode ser vazio.");
		}
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Place getPlace() {
		return place;
	}

	public void setPlace(Place place) throws TravelException {
		if (place == null) {
			throw new TravelException("Local da viagem não pode ser nulo");
		}
		this.place = place;
	}

	public User getAdmin() {
		return admin;
	}

	public void setAdmin(User admin) throws TravelException {
		if (admin == null) {
			throw new TravelException("Administrador da viagem não pode ser nulo.");
		} else if (this.admin != null) {
			throw new TravelException("Esta viagem já possui um administrador.");
		}
		this.admin = admin;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		if (photoUrl == null || photoUrl.equals("")) {
			photoUrl = DEFAULT_PHOTO;
		}
		this.photoUrl = photoUrl;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Set<User> getParticipating() {
		return Collections.unmodifiableSet(participating);
	}

	public List<Post> getPosts() {
		return Collections.unmodifiableList(posts);
	}

	protected void setState(TravelState state) {
		this.state = state;
	}

	public boolean leave(User usr) {
		return participating.remove(usr);
	}
	
	//protected because only the travelstate
	//can add a user this way (without the need of password)
	protected boolean join(User usr) {
		return participating.add(usr);
	}
	
	//this is the method which is public (requires password)
	public boolean join(User usr, String password) {
		return state.join(usr, password);
	}
	
	public void open() {
		state.open();
	}
	
	public void close(String password) { 
		state.close(password);
	}
	
	public void changePassword(String password) {
		state.changePassword(password);
	}
	
	public boolean isParticipating(User usr) {
		return participating.contains(usr);
	}
	
	public boolean isAdminister(User usr) {
		return admin.equals(usr);
	}

	public void addPost(User user, String message) {
		posts.add(0, new Post(user, message));
	}

	public void removePost(Post post) {
		posts.remove(post);
	}

	public static Travel getTravelById(Long id) {
		Travel found = GenericDAOImpl.getInstance().findByEntityId(Travel.class, id);
		return found;
	}

}
