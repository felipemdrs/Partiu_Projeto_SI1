package models.travel;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import models.User;
import models.Utils.PasswordService;

@Entity
@DiscriminatorValue("CloseState")
@Table(name = "CLOSESTATE")
public class ClosedState extends TravelState {

	@Column
	private String password;
	
	public ClosedState() { }

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
		throw new InvalidStateException("A viagem já é privada.");
	}

	@Override
	public void changePassword(String password) {
		setPassword(password);
	}
	
	@Override
	public boolean hasPassword() {
		return true;
	}

}
