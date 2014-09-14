package models.travel;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import models.User;

@Entity
@DiscriminatorValue("OpenState")
@Table(name = "OPENSTATE")
public class OpenState extends TravelState {

	@ManyToOne(cascade = CascadeType.ALL)
	private Travel context;

	public OpenState() { }

	public OpenState(Travel context) {
		setContext(context);
	}

	public Travel getContext() {
		return context;
	}

	public void setContext(Travel context) {
		this.context = context;
	}

	@Override
	public boolean join(User usr, String password) {
		if (!context.isAdminister(usr)) {
			return context.join(usr);
		}
		return false;
	}

	@Override
	public void open() {
		throw new InvalidStateException("Esta viagem já é aberta.");
	}

	@Override
	public void close(String password) {
		context.setState(new ClosedState(context, password));
	}

	@Override
	public void changePassword(String password) {
		throw new InvalidStateException("Apenas viagens privadas possuem senha.");
	}
	
	@Override
	public boolean hasPassword() {
		return false;
	}

}
