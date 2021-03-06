package models;

import javax.persistence.Entity;

@Entity
public class OpenState extends TravelState {

	@SuppressWarnings("unused")
	private OpenState() { }

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
		if (!usr.isAdminister(context)) {
			return usr.join(context);
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
