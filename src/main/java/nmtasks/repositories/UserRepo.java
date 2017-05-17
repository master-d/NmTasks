package nmtasks.repositories;


import java.util.List;

import org.springframework.data.repository.CrudRepository;

import nmtasks.beans.User;

public interface UserRepo extends CrudRepository<User, Long> { 

  List<User> findAll();
  List<User> findByEmail(String email);
}