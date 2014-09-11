package models;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity(name="travel")
public class Travel {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String name;
	
	@Column
	private String description;
	
	@ManyToOne
	private Place place;
	
	@ManyToOne
	private User admin;
	
	@ManyToOne
	private Photo photo;
	
	@ManyToOne
	private TravelState state;
	
	@OneToMany
	private Set<User> participating = new HashSet<User>();
	
	public Travel(User admin, String name, String description, double coordX, 
			double coordY, String placeDescription, String photoUrl) 
			throws MalformedURLException, TravelException {
		setAdmin(admin);
		setName(name);
		setDescription(description);
		setPlace(new Place(coordX, coordY, placeDescription));
		setPhoto(new Photo(photoUrl));
		setState(new OpenState(this));
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
			throw new TravelException("Nome da viagem nao pode ser vazio");
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
			throw new TravelException("Local da viagem nao pode ser nulo");
		}
		this.place = place;
	}

	public User getAdmin() {
		return admin;
	}

	public void setAdmin(User admin) throws TravelException {
		if (admin == null) {
			throw new TravelException("Administrador da viagem nao pode ser nulo");
		} else if (this.admin != null) {
			throw new TravelException("Esta viagem ja possui um administrador");
		}
		this.admin = admin;
	}

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto(Photo photo) throws TravelException {
		if (photo == null) {
			throw new TravelException("Foto da viagem nao pode ser nula");
		}
		this.photo = photo;
	}

	public Set<User> getParticipating() {
		return Collections.unmodifiableSet(participating);
	}

	public void setParticipating(Set<User> participating) {
		this.participating = participating;
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

}
