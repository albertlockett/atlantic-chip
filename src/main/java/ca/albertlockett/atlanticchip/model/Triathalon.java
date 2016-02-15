package ca.albertlockett.atlanticchip.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("2")
public class Triathalon extends Race {

	private static final long serialVersionUID = -1629297231486206180L;

}
