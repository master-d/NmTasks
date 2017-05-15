package nmtasks.repositories;

import org.springframework.data.repository.CrudRepository;

import nmtasks.beans.Task;

public interface TaskRepo extends CrudRepository<Task, Long> { }