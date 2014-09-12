package models.travel;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

import models.User;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name="TRAVEL_TYPE")
@Table(name="TRAVEL_STATE")
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
