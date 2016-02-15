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
	private String chipTime;
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

	@Column(name="PLACE", length=50)
	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	@Column(name="BIB_NO", length=50)
	public String getBibNo() {
		return bibNo;
	}

	public void setBibNo(String bibNo) {
		this.bibNo = bibNo;
	}

	@Column(name="RACER_NAME", length=50)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="CITY", length=50)
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	@Column(name="PROV", length=50)
	public String getProv() {
		return Prov;
	}
	
	public void setProv(String prov) {
		Prov = prov;
	}
	
	@Column(name="DIV_PLACE", length=50)
	public String getDivPlace() {
		return divPlace;
	}
	
	public void setDivPlace(String divPlace) {
		this.divPlace = divPlace;
	}
	
	@Column(name="DIV", length=50)
	public String getDiv() {
		return div;
	}
	
	public void setDiv(String div) {
		this.div = div;
	}
	
	@Column(name="GUN_TIME", length=50)
	public String getGunTime() {
		return gunTime;
	}
	
	public void setGunTime(String gunTime) {
		this.gunTime = gunTime;
	}
	
	@Column(name="CHIP_TIME", length=50)
	public String getChipTime() {
		return this.chipTime;
	}
	
	public void setchipTime(String chipTime) {
		this.chipTime = chipTime;
	}
	
	@Column(name="KM_PACE", length=50)
	public String getKmpace() {
		return kmpace;
	}
	
	public void setKmpace(String kmpace) {
		this.kmpace = kmpace;
	}

	@Column(name="COUNTRY", length=50)
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column(name="GENDER", length=50)
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	
	
	
}
