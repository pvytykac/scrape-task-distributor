package pvytykac.net.scrape.server.db.model.res;

import static net.pvytykac.scrape.util.ModelBuilderUtil.asImmutableList;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.joda.time.DateTime;

import pvytykac.net.scrape.server.db.model.DboBuilder;
import pvytykac.net.scrape.server.db.repository.Dbo;

@Entity
@Table(name = "res_institution")
public class ResInstitution implements Dbo<Integer> {

	@Id
	private Integer id;

	@Column
	private String ico;

	@Column
	private String name;

	@Column
	private DateTime created;

	@Column
	private DateTime ceased;

	@Column
	private String address;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "form_id", referencedColumnName = "code")
	private ResForm form;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "region_id", referencedColumnName = "code")
	private ResRegion region;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "unit_id", referencedColumnName = "code")
	private ResUnit unit;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(
			name = "res_institution_attribute",
			joinColumns = @JoinColumn(name = "institution_id", referencedColumnName = "id"),
			inverseJoinColumns = {
					@JoinColumn(name = "attribute_value_id", referencedColumnName = "code"),
					@JoinColumn(name = "attribute_id", referencedColumnName = "attribute_id")
			}
	)
	private List<ResAttributeValue> attributes;

	private ResInstitution() {}

	private ResInstitution(Builder builder) {
		this.id = builder.getId();
		this.ico = builder.getIco();
		this.name = builder.getName();
		this.created = builder.getCreated();
		this.ceased = builder.getCeased();
		this.address = builder.getAddress();
		this.form = builder.getForm();
		this.region = builder.getRegion();
		this.unit = builder.getUnit();
		this.attributes = builder.getAttributes();
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getIco() {
		return ico;
	}

	public String getName() {
		return name;
	}

	public DateTime getCreated() {
		return created;
	}

	public DateTime getCeased() {
		return ceased;
	}

	public String getAddress() {
		return address;
	}

	public ResForm getForm() {
		return form;
	}

	public ResRegion getRegion() {
		return region;
	}

	public ResUnit getUnit() {
		return unit;
	}

	public List<ResAttributeValue> getAttributes() {
		return attributes;
	}

	public static class Builder extends DboBuilder<Builder, Integer, ResInstitution> {

		private String ico;
		private String name;
		private DateTime created;
		private DateTime ceased;
		private String address;
		private ResForm form;
		private ResRegion region;
		private ResUnit unit;
		private List<ResAttributeValue> attributes;

		public String getIco() {
			return ico;
		}

		public String getName() {
			return name;
		}

		public DateTime getCreated() {
			return created;
		}

		public DateTime getCeased() {
			return ceased;
		}

		public String getAddress() {
			return address;
		}

		public ResForm getForm() {
			return form;
		}

		public ResRegion getRegion() {
			return region;
		}

		public ResUnit getUnit() {
			return unit;
		}

		public List<ResAttributeValue> getAttributes() {
			return asImmutableList(attributes);
		}

		public Builder withIco(String ico) {
			this.ico = ico;
			return this;
		}

		public Builder withName(String name) {
			this.name = name;
			return this;
		}

		public Builder withCreated(DateTime created) {
			this.created = created;
			return this;
		}

		public Builder withCeased(DateTime ceased) {
			this.ceased = ceased;
			return this;
		}

		public Builder withAddress(String address) {
			this.address = address;
			return this;
		}

		public Builder withForm(ResForm form) {
			this.form = form;
			return this;
		}

		public Builder withRegion(ResRegion region) {
			this.region = region;
			return this;
		}

		public Builder withUnit(ResUnit unit) {
			this.unit = unit;
			return this;
		}

		public Builder withAttributes(List<ResAttributeValue> attributes) {
			this.attributes = attributes;
			return this;
		}

		@Override
		public ResInstitution build() {
			return new ResInstitution(this);
		}
	}

}
