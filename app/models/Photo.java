package models;
import java.awt.Image;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Photo {

	public static final String BASE_URL = "http://localhost:9000";
	public static final String DEFAULT_PHOTO = BASE_URL + "/assets/img/perfil.jpg";
	
	@Id @GeneratedValue
	private Long id;
	
	@Column
	private URL url;
	
	public Photo(String url) throws MalformedURLException {
		this.setUrl(url);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}
	
	public void setUrl(String url) throws MalformedURLException {
		if (url == null || url.trim().equals("")) {
			url = DEFAULT_PHOTO;
		}
		this.url = new URL(url);
	}

	public Image getPhoto() throws IOException {
		return ImageIO.read(url);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Photo other = (Photo) obj;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

}
