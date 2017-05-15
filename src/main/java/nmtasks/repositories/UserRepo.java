package nmtasks.repositories;


import org.springframework.data.repository.CrudRepository;

import nmtasks.beans.User;

public interface UserRepo extends CrudRepository<User, Long> { }