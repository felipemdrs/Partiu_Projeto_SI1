package models;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import models.dao.GenericDAOImpl;
import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class Travel {

	private static final String DEFAULT_PHOTO = "/assets/images/default-travel.jpg";
	private static final int MAX_NAME_LENGTH = 40;
	private static final int MAX_DESCRIPTION_LENGTH = 350;
	
	@Id
	@Column(name="TRAVEL_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

	@Temporal(TemporalType.DATE)
	@Required
	@NotNull
	private Date date;
	
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private TravelState state;

	@OneToMany(cascade=CascadeType.ALL)
	@JsonBackReference
	private List<Post> posts = new ArrayList<Post>();
	
	@ManyToOne
	@JoinColumn(name="entity_travelsadmin")
	@JsonManagedReference
	private User admin;
	
	@SuppressWarnings("unused")
	private Travel() { }
	
	protected Travel(User admin, String name, String description, double coordX, 
			double coordY, String placeDescription, Date date) 
			throws TravelException {
		setAdmin(admin);
		setName(name);
		setDescription(description);
		setPlace(new Place(coordX, coordY, placeDescription));
		setDate(date);
		setPhotoUrl("");
		this.state  = new OpenState(this);
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
	
	public boolean isLocked() {
		return state.hasPassword();
	}

	public void setPlace(Place place) throws TravelException {
		if (place == null) {
			throw new TravelException("Local da viagem não pode ser nulo");
		}
		this.place = place;
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

	public List<Post> getPosts() {
		return Collections.unmodifiableList(posts);
	}

	public User getAdmin() {
		return admin;
	}

	protected void setAdmin(User admin) throws TravelException {
		if (admin == null) {
			throw new TravelException("Administrador da viagem não pode ser nulo.");
		} else if (this.admin != null) {
			throw new TravelException("Esta viagem já possuí um administrador.");
		}
		this.admin = admin;
	}
	
	//-----------------------------------------
	// STATE METHODS
	//-----------------------------------------
	protected void setState(TravelState state) {
		this.state = state;
	}

	public boolean leave(User usr) {
		return usr.leave(this);
	}
	
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
	//-----------------------------------------
	// END OF STATE METHODS
	//-----------------------------------------
	
	public Set<User> getParticipating() {
		Set<User> participating = new HashSet<User>();
		List<User> all = GenericDAOImpl.getInstance().findAllByClassName("User");
		for(User u : all) {
			if (u.isParticipating(this)) {
				participating.add(u);
			}
		}
		return Collections.unmodifiableSet(participating);
	}

	public void addPost(User user, String message) {
		posts.add(0, new Post(user, message));
	}

	public void removePost(Post post) {
		posts.remove(post);
	}

	public String getFormattedDate() { 
		return new SimpleDateFormat("dd/MM/yyyy").format(date);
	}
	
	public static Travel getTravelById(Long id) {
		Travel found = GenericDAOImpl.getInstance().findByEntityId(Travel.class, id);
		return found;
	}

	public static void persist(Travel travel) {
		GenericDAOImpl.getInstance().persist(travel);
		GenericDAOImpl.getInstance().flush();
	}
	
	public static void merge(Travel travel) {
		GenericDAOImpl.getInstance().merge(travel);
		GenericDAOImpl.getInstance().flush();
	}
	
}
