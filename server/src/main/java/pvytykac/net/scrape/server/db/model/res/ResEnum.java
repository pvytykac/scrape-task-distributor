package pvytykac.net.scrape.server.db.model.res;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class ResEnum {

	@Id
	private String code;

	private String text;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
