package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import models.Utils.PasswordService;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;

	@Column
	private String nome;

	@Column
	private String email;

	@Column
	private String password;

	public User() {
	}

	public User(String nome, String email, String password) throws Exception {
		setEmail(email);
		setNome(nome);
		setPassword(password);
	}

	public void setPassword(String password) throws Exception {
		if (this.password != null) {
			throw new Exception("Password already exists");
		}
		this.password = PasswordService.getInstance().encrypt(password);
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getPassword() {
		return password;
	}

	public void setEmail(String email) throws Exception {
		if (this.email != null) {
			throw new Exception("Email already exists");
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
