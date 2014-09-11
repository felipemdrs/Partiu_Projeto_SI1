package models;

import models.Utils.PasswordService;

public class ClosedState extends TravelState {

	private Travel context;
	private String password;
	
	public ClosedState(Travel context, String password) {
		setContext(context);
		setPassword(password);
	}

	public Travel getContext() {
		return context;
	}

	public void setContext(Travel context) {
		this.context = context;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		try {
			this.password = PasswordService.getInstance().encrypt(password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean passwordIsValid(String password) throws Exception {
		String criptoPass = PasswordService.getInstance().encrypt(password);
		return criptoPass.equals(this.password);
	}
	
	@Override
	public boolean join(User usr, String password) {
		try {
			if (passwordIsValid(password) && !context.isAdminister(usr)) {
				return context.join(usr);
			}
		} catch(Exception e) {
			return false;
		}
		return false;
	}

	@Override
	public void open() {
		context.setState(new OpenState(context));
	}

	@Override
	public void close(String password) {
		throw new InvalidStateException("A viagem ja e privada");
	}

	@Override
	public void changePassword(String password) {
		setPassword(password);
	}

}
