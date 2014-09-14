package models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import models.Utils.PasswordService;
import models.dao.GenericDAOImpl;
import models.travel.Travel;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class User {
	
	private static final String DEFAULT_PHOTO = "/assets/images/default-user.gif";
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	@Required
	@NotNull
	private String name;
	
	@Column(unique = true)
	@Email
	@Required
	@NotNull
	private String email;
	
	@Column
	@Required
	@NotNull
	private String password;
	
	@Column
	private String photoUrl;

	@Temporal(TemporalType.DATE)
	private Date dateRegister;
	
	@OneToMany(cascade=CascadeType.ALL)
	private Set<Travel> travelsParticipating = new HashSet<Travel>();

	@OneToMany(mappedBy="admin", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JsonManagedReference
	private Set<Travel> travelsAdmin = new HashSet<Travel>();
	
	public User() {
	}

	public User(String name, String email, String password) throws Exception {
		setEmail(email);
		setName(name);
		setPassword(password);
		setPhotoUrl(""); 
		setDateRegister(Calendar.getInstance().getTime());
	}


	public void setPassword(String password) throws Exception {
		if (stringIsValid(this.password)) {
			throw new Exception("Senha já existe.");
		}
		if (!stringIsValid(password)) {
			throw new Exception("Senha inválida.");
		}
		this.password = PasswordService.getInstance().encrypt(password);
	}

	public void setPassword(String oldPassword, String newPassword) throws Exception {
		if (stringIsValid(newPassword) && stringIsValid(oldPassword)){
			if (passwordIsValid(oldPassword)) {
				this.password = oldPassword;
			} else {
				throw new Exception("Senha inválida.");
			}
		}
	}
	
	private boolean stringIsValid(String str) {
		return str != null && str.trim().length() > 0;
	}

	public Long getId() {
		return id;
	}
	
	public Set<Travel> getTravelsParticipating() {
		return this.travelsParticipating;
	}
	
	public Set<Travel> getTravelsAdmin() {
		return this.travelsAdmin;
	}

	public void setId(Long id) {
		this.id = id;
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
	
	public static User getUserByEmail(String email) {
		List<User> users = GenericDAOImpl.getInstance().findByAttributeName("User", "email", email);
		if (!users.isEmpty()) {
			return users.get(0);
		}
		return null;
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
