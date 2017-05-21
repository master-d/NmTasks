package nmtasks.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import groovy.transform.Memoized;
import nmtasks.beans.User;
import nmtasks.repositories.UserRepo;
import nmtasks.util.NmTasksUtil;

@Controller
@SessionAttributes(value="user", types={ User.class })
public class TasksController {

	private UserRepo userRepo;

	@Autowired
	public TasksController(UserRepo userRepo) { this.userRepo = userRepo; }
	@RequestMapping("/")
	public String index(HttpSession session, Model model) {
		if (session.getAttribute("user") != null)
			return "tasks";
		else {
			model.addAttribute("email", "root@localhost");
			return "login";
		}
	}
	
	@RequestMapping("/tasks")
	public String tasks(HttpSession session, Model model) {
		if (session.getAttribute("user") == null) {
			model.addAttribute("message", "You must be logged in to view this page");
			return index(session, model);
		}
		else 
			return "tasks";
	}

	@RequestMapping("/logout")
	public String logout(SessionStatus status) {
		status.setComplete();
		return "redirect:/";
	}

	@RequestMapping(path= "/login", method=RequestMethod.POST)
	public String login(@RequestParam(name="email") String email,
		@RequestParam(name="password") String password,
		@RequestParam(name="q") String q,
		Model model) {
			model.addAttribute("email",email);
			if (!validEmail(email)) {
				model.addAttribute("message","Please enter a valid email address");
				return "login";
			} 
			if ("register".equalsIgnoreCase(q)) {
				return register(email, password, model);
			}
			try {
				List<User> users = userRepo.findByEmail(email);
				if (users.size() > 0) {
					// account exists. Check if password matches
					User user = users.get(0);
					if (user.getPassword().equals(NmTasksUtil.getSHA512Hash(password, user.getSalt()))) {
						model.addAttribute("user", user);
						return "redirect:/tasks";
					} else {
						model.addAttribute("message", "Invalid password for account '" + email + "'");
						return "login";
					}
				} else {
						model.addAttribute("message", "Account '" + email + "' does not exist. Please register");
						return "login";
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				model.addAttribute("message",e.getMessage());
				return "login";
			}
	}

	@RequestMapping(path= "/register", method=RequestMethod.POST)
	public String register(@RequestParam(name="email") String email,
		@RequestParam(name="password") String password,
		Model model) {
			model.addAttribute("email",email);
			if (!validEmail(email)) {
				model.addAttribute("message","Please enter a valid email address");
				return "login";
			} 
			try {
			// create a new
			User user = new User();
			user.setEmail(email);
			user.setSalt(NmTasksUtil.generateSalt());
			user.setPassword(NmTasksUtil.getSHA512Hash(password, user.getSalt()));
			userRepo.save(user);
			model.addAttribute("message","Successfully created account for '" +email + "'");
			model.addAttribute("user", user);
			return "redirect:/tasks";
			} catch (Exception e) {
				e.printStackTrace();
				model.addAttribute("message",e.getMessage());
				return "login";
			}
		}

		private boolean validEmail(String email) {
			return email.matches("^\\w+@((\\w+\\.\\w{2,3})|localhost)$");
		}

}
