package pvytykac.net.scrape.server.db.model.res;

import java.util.Objects;

import pvytykac.net.scrape.server.db.model.DboBuilder;
import pvytykac.net.scrape.server.db.repository.Dbo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class ResEnum implements Dbo<String> {

	@Id
	@Column(name = "code")
	private String id;

	@Column
	private String text;

	protected ResEnum() {}

	protected <B extends Builder, T extends ResEnum> ResEnum(Builder<B, T> builder) {
		this.id = builder.getId();
		this.text = builder.getText();
	}

	@Override
	public String getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public static abstract class Builder<BUILDER extends Builder, T extends ResEnum> extends DboBuilder<BUILDER, String, T> {
		
		private String text;

		public String getText() {
			return text;
		}

		@SuppressWarnings("unchecked")
		public BUILDER withText(String text) {
			this.text = text;
			return (BUILDER) this;
		}
	}
}
