package nmtasks.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TasksController {

	@RequestMapping("/")
	public String index(Model model) {
		model.addAttribute("message", "goodevening");
		return "login";
	}


}
