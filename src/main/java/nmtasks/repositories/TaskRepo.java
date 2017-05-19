package nmtasks.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import nmtasks.beans.Task;

public interface TaskRepo extends CrudRepository<Task, Long> { 

  List<Task> findAll();
  Page<Task> findByUserId(Long userId, Pageable pg);

  Page<Task> findByUserIdAndCompleteDateIsNull(Long userId, Pageable pg);
  Page<Task> findByUserIdAndCompleteDateIsNotNull(Long userId, Pageable pg);
  

  @Query(value="select t from Task t where (t.name like %:term% or t.description like %:term%) and t.userId=:userId and t.completeDate is not null")
  Page<Task> searchCompletedForUser(@Param("userId") Long userId, @Param("term") String searchTerm, Pageable pg);

  @Query(value="select t from Task t where (t.name like %:term% or t.description like %:term%) and t.userId=:userId and t.completeDate is null")
  Page<Task> searchIncompleteForUser(@Param("userId") Long userId, @Param("term") String searchTerm, Pageable pg);

  @Query(value="select t from Task t where (t.name like %:term% or t.description like %:term%) and t.userId=:userId")
  Page<Task> searchAllForUser(@Param("userId") Long userId, @Param("term") String searchTerm, Pageable pg);

  @Transactional
  @Modifying
  @Query("update Task t set t.completeDate = ?1 where t.id = ?2")
  int markComplete(Date completeDate, long id);

}