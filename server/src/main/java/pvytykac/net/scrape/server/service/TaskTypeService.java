package pvytykac.net.scrape.server.service;

import pvytykac.net.scrape.server.task.TaskType;

import java.util.Optional;
import java.util.Set;

public interface TaskTypeService {

	Set<String> getTaskTypes();

	Set<String> getTaskTypes(Set<String> ignoredTypes);

	Optional<TaskType> getTaskType(String taskType);

}
