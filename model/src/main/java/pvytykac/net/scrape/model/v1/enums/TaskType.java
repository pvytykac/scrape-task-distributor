package pvytykac.net.scrape.model.v1.enums;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

public enum TaskType {

	TMP(101),
	ANOTHER(105);

	private final Set<Integer> supportedForms;

	TaskType(Integer...supportedForms) {
		this.supportedForms = ImmutableSet.copyOf(supportedForms);
	}

	public Set<Integer> getSupportedForms() {
		return supportedForms;
	}
}
