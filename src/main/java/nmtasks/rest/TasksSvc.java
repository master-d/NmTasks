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
import nmtasks.util.NmTasksUtil;

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


	@RequestMapping(value="/validateLogin",method=RequestMethod.GET)
	public ResponseEntity<String> validateLogin(String email, String password) {
		try {
		List<User> users = userRepo.findByEmail(email);
		if (users.size() > 0) {
			// account exists. Check if password matches
			User user = users.get(0);
			if (user.getPassword().equals(NmTasksUtil.getSHA512Hash(password, user.getSalt()))) {
				return new ResponseEntity<>("login accepted", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Invalid password for account '" + email + "'", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			User user = new User();
			user.setEmail(email);
			user.setSalt(NmTasksUtil.generateSalt());
			user.setPassword(NmTasksUtil.getSHA512Hash(password, user.getSalt()));
			userRepo.save(user);
			return new ResponseEntity<>("Account created", HttpStatus.OK);
		}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value="servername", method=RequestMethod.GET)
	public ResponseEntity<String> convert(){
		String text = "DUCK";
		return new ResponseEntity<>(text, HttpStatus.OK);
	}
	
}