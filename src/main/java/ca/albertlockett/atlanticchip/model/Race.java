package ca.albertlockett.atlanticchip.model;

import java.io.Serializable;
import java.util.List;

public class Race implements Serializable {

	private static final long serialVersionUID = -867021343791960229L;
	
	private String name;
	private String location;
	
	private List<Racer> racers;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	public List<Racer> getRacers() {
		return racers;
	}
	public void setRacers(List<Racer> racers) {
		this.racers = racers;
	}
	
	
	
	
}
