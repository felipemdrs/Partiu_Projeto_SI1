package models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import models.dao.GenericDAOImpl;
import play.data.validation.Constraints.Required;

@Entity
public class Post {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@Required
	@NotNull
	private User user;
	
	@Column
	@Required
	@NotNull
	private String message;
	
	@Column
	private String formattedDate = getSimplifiedDate() + " às " + getTimeOfDay() + "h";
	
	@SuppressWarnings("unused")
	private Post() { }
	
	public Post(User user, String message) {
		this.setUser(user);
		this.setMessage(message);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFormattedDate() { 
		return formattedDate;
	}

	public void setFormattedDate(String formattedDate) {
		this.formattedDate = formattedDate;
	}
	
	private static String getSimplifiedDate() { 
		Date date = Calendar.getInstance().getTime();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");  
		return format.format(date);
	}

	private static String getTimeOfDay() { 
		return String.format("%02d", Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) + ":" + 
				String.format("%02d", Calendar.getInstance().get(Calendar.MINUTE));

	}
	
	public static Post getPostById(Long id) {
		return GenericDAOImpl.getInstance().findByEntityId(Post.class, id);
	}
	
}
