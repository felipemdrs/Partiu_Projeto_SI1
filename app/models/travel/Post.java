package models.travel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import models.User;
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
	@Temporal(TemporalType.DATE)
	private Date date = Calendar.getInstance().getTime();
	
	@Column
	private String timeOfDay = String.format("%02d", Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) + ":" + 
						String.format("%02d", Calendar.getInstance().get(Calendar.MINUTE));
	
	@Column
	private String formattedDate = getSimplifiedDate(date) + " às " + timeOfDay + "h";
	
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTimeOfDay() {
		return timeOfDay;
	}

	public void setTimeOfDay(String timeOfDay) {
		this.timeOfDay = timeOfDay;
	}
	
	public String getFormattedDate() { 
		return formattedDate;
	}

	public void setFormattedDate(String formattedDate) {
		this.formattedDate = formattedDate;
	}
	
	private static String getSimplifiedDate(Date date) { 
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");  
		return format.format(date);
	}

	public static Post getPostById(Long id) {
		return GenericDAOImpl.getInstance().findByEntityId(Post.class, id);
	}
	
}
