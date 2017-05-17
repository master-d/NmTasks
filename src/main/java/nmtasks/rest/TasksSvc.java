package nmtasks.rest;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nmtasks.beans.Task;
import nmtasks.beans.User;
import nmtasks.repositories.TaskRepo;
import nmtasks.repositories.UserRepo;
import nmtasks.util.NmTasksUtil;

@RestController
@RequestMapping("/api")
public class TasksSvc{
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private TaskRepo taskRepo;

	TasksSvc() { }

	@RequestMapping(value="/load/task",method=RequestMethod.GET)
	public List<Task> loadTasks(Pageable pageable, @RequestParam(name="sort") String sort) {
		return taskRepo.findAll();
	}

	@RequestMapping(value="/users",method=RequestMethod.GET)
	public List<User> getAllUsers() {
		return userRepo.findAll(); 
	}
	@RequestMapping(value="/users/{id}",method=RequestMethod.GET)
	public User getUserById(@PathVariable(name="id") Long id) {
		return userRepo.findOne(id); 
	}


	@RequestMapping(value="servername", method=RequestMethod.GET)
	public ResponseEntity<String> convert(){
		String text = "DUCK";
		return new ResponseEntity<>(text, HttpStatus.OK);
	}
	
}