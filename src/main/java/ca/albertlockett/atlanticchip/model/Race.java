package ca.albertlockett.atlanticchip.model;

import java.io.Serializable;

public class Race implements Serializable {

	private static final long serialVersionUID = -867021343791960229L;
	
	private String name;
	private String location;
	
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
	
	
	
}
