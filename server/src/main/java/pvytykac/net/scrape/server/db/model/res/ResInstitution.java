package pvytykac.net.scrape.server.db.model.res;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.joda.time.DateTime;

@Entity
@Table(name = "res_institution")
public class ResInstitution {

	@Id
	private Integer id;

	private String ico;
	private String name;
	private DateTime created;
	private DateTime ceased;
	private String address;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "form_id", referencedColumnName = "code")
	private ResForm form;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "region_id", referencedColumnName = "code")
	private ResRegion region;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "unit_id", referencedColumnName = "code")
	private ResUnit unit;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIco() {
		return ico;
	}

	public void setIco(String ico) {
		this.ico = ico;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DateTime getCreated() {
		return created;
	}

	public void setCreated(DateTime created) {
		this.created = created;
	}

	public DateTime getCeased() {
		return ceased;
	}

	public void setCeased(DateTime ceased) {
		this.ceased = ceased;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public ResForm getForm() {
		return form;
	}

	public void setForm(ResForm form) {
		this.form = form;
	}

	public ResRegion getRegion() {
		return region;
	}

	public void setRegion(ResRegion region) {
		this.region = region;
	}

	public ResUnit getUnit() {
		return unit;
	}

	public void setUnit(ResUnit unit) {
		this.unit = unit;
	}
}
