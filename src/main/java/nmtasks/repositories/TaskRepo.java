package nmtasks.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import nmtasks.beans.Task;

public interface TaskRepo extends CrudRepository<Task, Long> { 

  List<Task> findAll();
  Page<Task> findAll(Pageable pageable);

}