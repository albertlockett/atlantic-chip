package ca.albertlockett.atlanticchip.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "RACER")
public class Racer implements Serializable {

	private static final long serialVersionUID = 1081891241159349293L;
	
	private Integer raceId;
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
	
	private Race Race;
	
	@Id
	@Column(name="RACER_ID", unique=true, nullable=false, precision=9, scale=0)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, 
			generator="SEQ_RACER_ID")
	@SequenceGenerator(name="SEQ_RACER_ID", sequenceName="SEQ_RACER_ID",
			allocationSize=1)
	public Integer getRaceId() {
		return this.raceId;
	}
	
	public void setRaceId(Integer raceId) {
		this.raceId = raceId;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="RACE_ID", nullable=false)
	public Race getRace() {
		return Race;
	}

	public void setRace(Race race) {
		Race = race;
	}

	@Transient
	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	@Transient
	public String getBibNo() {
		return bibNo;
	}

	public void setBibNo(String bibNo) {
		this.bibNo = bibNo;
	}

	@Transient
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Transient
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	@Transient
	public String getProv() {
		return Prov;
	}
	
	public void setProv(String prov) {
		Prov = prov;
	}
	
	@Transient
	public String getDivPlace() {
		return divPlace;
	}
	
	public void setDivPlace(String divPlace) {
		this.divPlace = divPlace;
	}
	
	@Transient
	public String getDiv() {
		return div;
	}
	
	public void setDiv(String div) {
		this.div = div;
	}
	
	@Transient
	public String getGunTime() {
		return gunTime;
	}
	
	public void setGunTime(String gunTime) {
		this.gunTime = gunTime;
	}
	
	@Transient
	public String getKmpace() {
		return kmpace;
	}
	
	public void setKmpace(String kmpace) {
		this.kmpace = kmpace;
	}

	@Transient
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Transient
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	
	
	
}
