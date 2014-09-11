package models;

public class OpenState extends TravelState {

	private Travel context;
	
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
		throw new InvalidStateException("Esta viagem ja e publica");
	}

	@Override
	public void close(String password) {
		context.setState(new ClosedState(context, password));
	}

	@Override
	public void changePassword(String password) {
		throw new InvalidStateException("Apenas viagens privadas possuem senha");
	}

}
