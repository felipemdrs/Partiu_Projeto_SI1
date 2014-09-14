package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;

@Entity
public class Place {

	private static final int MAX_DESCRIPTION_LENGTH = 150;
	private static final double MAX_LAT_VALUE = 85.1;
	private static final double MAX_LNG_VALUE = 180;
	
	@Id @GeneratedValue
	private Long id;
	
	@Required
	@Column
	private double coordX;
	
	@Required
	@Column
	private double coordY;
	
	@Column
	@Required
	@NotNull
	@MaxLength(value = MAX_DESCRIPTION_LENGTH)
	private String description;
	
	@SuppressWarnings("unused")
	private Place() { }
	
	public Place(double coordX, double coordY, String description) 
		throws TravelException {
		this.setCoordX(coordX);
		this.setCoordY(coordY);
		this.setDescription(description);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getCoordX() {
		return coordX;
	}

	public void setCoordX(double coordX) throws PlaceException {
		if (Math.abs(coordX) >= MAX_LNG_VALUE) {
			throw new PlaceException("Longitude inválida.");
		}
		this.coordX = coordX;
	}

	public double getCoordY() {
		return coordY;
	}

	public void setCoordY(double coordY) throws PlaceException {
		if (Math.abs(coordY) >= MAX_LAT_VALUE) {
			throw new PlaceException("Latitude inválida.");
		}
		this.coordY = coordY;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) throws PlaceException {
		if (description == null || description.trim().equals("")) {
			throw new PlaceException("Descrição do lugar não pode ser vazia.");
		}
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(coordX);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(coordY);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
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
		Place other = (Place) obj;
		if (Double.doubleToLongBits(coordX) != Double
				.doubleToLongBits(other.coordX))
			return false;
		if (Double.doubleToLongBits(coordY) != Double
				.doubleToLongBits(other.coordY))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		return true;
	}

}
