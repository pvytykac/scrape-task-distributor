package pvytykac.net.scrape.server;

import io.dropwizard.validation.ValidationMethod;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import pvytykac.net.scrape.server.task.TaskType;

import javax.validation.constraints.NotNull;

/**
 * @author Paly
 * @since 2018-08-11
 */
public class TaskTypeConfiguration {

    @NotNull
    private String id;

    @NotBlank
    private String className;

    public String getId() {
        return id;
    }

	public String getClassName() {
		return className;
	}

	@ValidationMethod(message = "class does not exist or doesn't implement TaskType")
    public boolean isClassNameValid() {
		if (StringUtils.isNotBlank(className)) {
			try {
				Class<?> clazz = Class.forName(className);
				return TaskType.class.isAssignableFrom(clazz);
			} catch (ClassNotFoundException ex) {
				return false;
			}
		}

		return true;
	}
}
