package pvytykac.net.scrape.server.db.model.res;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import pvytykac.net.scrape.server.db.repository.Dbo;

@MappedSuperclass
public class ResEnum implements Dbo<String> {

	@Id
	@Column(name = "code")
	private String id;

	private String text;

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		ResEnum resEnum = (ResEnum) o;

		return id.equals(resEnum.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
