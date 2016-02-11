package ca.albertlockett.atlanticchip.model;

import java.io.Serializable;

public class Racer implements Serializable {

	private static final long serialVersionUID = 1081891241159349293L;
	
	private String place;
	private String bibNo;
	private String name;
	private String city;
	private String Prov;
	private String divPlace;
	private String div;
	private String gunTime;
	private String kmpace;
	private String country;
	private String gender;
	
	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getBibNo() {
		return bibNo;
	}

	public void setBibNo(String bibNo) {
		this.bibNo = bibNo;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getProv() {
		return Prov;
	}
	
	public void setProv(String prov) {
		Prov = prov;
	}
	
	public String getDivPlace() {
		return divPlace;
	}
	
	public void setDivPlace(String divPlace) {
		this.divPlace = divPlace;
	}
	
	public String getDiv() {
		return div;
	}
	
	public void setDiv(String div) {
		this.div = div;
	}
	
	public String getGunTime() {
		return gunTime;
	}
	
	public void setGunTime(String gunTime) {
		this.gunTime = gunTime;
	}
	
	public String getKmpace() {
		return kmpace;
	}
	
	public void setKmpace(String kmpace) {
		this.kmpace = kmpace;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	
	
	
}
