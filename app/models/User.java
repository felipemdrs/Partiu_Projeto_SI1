package models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import models.Utils.PasswordService;

@Entity
public class User {
	
	private static final String DEFAULT_PHOTO = "/assets/images/default-user.gif";
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name, email,  password, photoUrl;
	private Date dateRegister;
	
	public User() { 
		setDateRegister(Calendar.getInstance().getTime());
	}

	public User(String name, String email, String password) throws Exception {
		this();
		setEmail(email);
		setName(name);
		setPassword(password);
		setPhotoUrl("");
	}

	public void setPassword(String password) throws Exception {
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
			throw new Exception("E-mail já existe.");
		}
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public Date getDateRegister() {
		return dateRegister;
	}

	public void setDateRegister(Date dateRegister) {
		this.dateRegister = dateRegister;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		if (photoUrl == null || photoUrl.trim().equals("")) {
			photoUrl = DEFAULT_PHOTO;
		}
		this.photoUrl = photoUrl;
	}

	public String getFormattedDate() { 
		return new SimpleDateFormat("dd/MM/yyyy").format(dateRegister);
	}
	
	public String getFirstName() {
		return name.split(" ")[0];
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
