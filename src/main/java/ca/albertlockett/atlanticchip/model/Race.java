package ca.albertlockett.atlanticchip.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;



@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="RACE_TYPE", 
		discriminatorType=DiscriminatorType.INTEGER)
@DiscriminatorValue("0")
@Table(name = "RACE")
public class Race implements Serializable {

	private static final long serialVersionUID = -867021343791960229L;
	
	private Integer raceId;
	private String name;
	private String location;

	private List<Racer> racers;
	
	@Id
	@Column(name="RACE_ID", unique=true, nullable=false, precision=9, scale=0)
	public Integer getRaceId() {
		return raceId;
	}
	public void setRaceId(Integer raceId) {
		this.raceId = raceId;
	}
	
	@Column(name="RACE_NAME", length=200)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="RACE_LOCATION", length=200)
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	

	@OneToMany(fetch=FetchType.EAGER, mappedBy="race",cascade={CascadeType.ALL})
	public List<Racer> getRacers() {
		return racers;
	}
	
	public void setRacers(List<Racer> racers) {
		this.racers = racers;
	}
}
