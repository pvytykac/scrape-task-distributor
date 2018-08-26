package pvytykac.net.scrape.server.service.impl;

import com.google.common.collect.ImmutableSet;
import pvytykac.net.scrape.server.TaskTypeConfiguration;
import pvytykac.net.scrape.server.db.repository.impl.RepositoryFacade;
import pvytykac.net.scrape.server.service.TaskTypeService;
import pvytykac.net.scrape.server.task.TaskType;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class TaskTypeServiceImpl implements TaskTypeService {

	private final Map<String, TaskType> taskTypes;
	private final Set<String> taskTypeKeys;

	public TaskTypeServiceImpl(List<TaskTypeConfiguration> configurations, RepositoryFacade repositoryFacade) {
		this.taskTypes = configurations.stream()
				.collect(Collectors.toMap(
						TaskTypeConfiguration::getId,
						cfg -> instantiateTaskType(cfg, repositoryFacade))
				);

		this.taskTypeKeys = ImmutableSet.copyOf(taskTypes.keySet());
	}

	private static TaskType instantiateTaskType(TaskTypeConfiguration cfg, RepositoryFacade facade) {
		try {
			@SuppressWarnings("unchecked")
			Class<TaskType> clazz = (Class<TaskType>) Class.forName(cfg.getClassName());
			return clazz.getDeclaredConstructor(String.class, RepositoryFacade.class, String.class)
					.newInstance(cfg.getId(), facade, cfg.getOffsetIco());
		} catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException
				| IllegalAccessException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public Set<String> getTaskTypes() {
		return taskTypeKeys;
	}

	@Override
	public Set<String> getTaskTypes(Set<String> ignoredTypes) {
		return ImmutableSet.copyOf(
				getTaskTypes().stream()
						.filter(key -> !ignoredTypes.contains(key))
						.iterator());
	}

	@Override
	public Optional<TaskType> getTaskType(String taskType) {
		return Optional.ofNullable(taskTypes.get(taskType));
	}
}
