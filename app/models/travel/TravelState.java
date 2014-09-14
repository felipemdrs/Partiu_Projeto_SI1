package models.travel;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import models.User;

@Entity
public abstract class TravelState {

	@Id @GeneratedValue
	private Long id;

	@ManyToOne
	protected Travel context;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public abstract boolean join(User usr, String password);
	public abstract void open();
	public abstract void close(String password);
	public abstract void changePassword(String password);
	public abstract boolean hasPassword();
	
}
