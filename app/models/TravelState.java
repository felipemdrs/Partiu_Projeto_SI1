package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "travel_state")
public abstract class TravelState {

	@Id @GeneratedValue
	private Long id;

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
	
}
