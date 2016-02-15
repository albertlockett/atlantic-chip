package ca.albertlockett.atlanticchip.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("1")
public class RunningRace extends Race {
	
	private static final long serialVersionUID = 7167792039674096325L;
	
	private double distance;

	@Column(name="DISTANCE")
	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	
	
}
