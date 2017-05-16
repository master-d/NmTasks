package nmtasks.rest;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import nmtasks.beans.User;
import nmtasks.repositories.TaskRepo;
import nmtasks.repositories.UserRepo;

@RestController
@RequestMapping("/api")
public class TasksSvc{
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private TaskRepo taskRepo;

	TasksSvc() { }

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