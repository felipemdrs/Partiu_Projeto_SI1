package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import models.Utils.PasswordService;
import models.dao.GenericDAO;
import models.dao.GenericDAOImpl;

@Entity(name="user")
public class User {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String name;

	@Column
	private String email;

	@Column
	private String password;

	public User() { }

	public User(String name, String email, String password) throws Exception {
		this();
		setEmail(email);
		setName(name);
		setPassword(password);
	}

	public void setPassword(String password) throws Exception {
		if (this.password != null) {
			throw new Exception("Senha ja existe");
		}
		this.password = PasswordService.getInstance().encrypt(password);
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String nome) {
		this.name = nome;
	}

	public String getPassword() {
		return password;
	}

	public void setEmail(String email) throws Exception {
		if (this.email != null) {
			throw new Exception("E-mail ja existe");
		}
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public boolean passwordIsValid(String password) throws Exception {
		String criptoPass = PasswordService.getInstance().encrypt(password);
		return criptoPass.equals(this.password);
	}
	
	public static User findUserByEmail(String email) {
		List<User> users = GenericDAOImpl.getInstance().findByAttributeName("user", "email", email);
		if (users.size() > 0) {
			return users.get(0);
		}
		return null;
	}
	
	public static boolean registerUser(User usr) throws UserException {
		User found = findUserByEmail(usr.getEmail());
		if (found != null) {
			throw new UserException("Usuário já cadastrado.");
		}
		GenericDAO dao = GenericDAOImpl.getInstance();
		boolean success = dao.persist(usr);
		dao.flush();
		return success;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof User) {
			User other = (User) obj;
			return this.getEmail().equals(other.getEmail());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.email.hashCode();
	}

}
