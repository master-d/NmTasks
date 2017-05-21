package nmtasks.rest;

import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import nmtasks.beans.Message;
import nmtasks.beans.Task;
import nmtasks.beans.User;
import nmtasks.repositories.TaskRepo;
import nmtasks.repositories.UserRepo;

@RestController
@RequestMapping("/api")
@SessionAttributes(value="user", types={ User.class })
public class TasksSvc{
	private UserRepo userRepo;
	private TaskRepo taskRepo;
	@Autowired
	public TasksSvc(UserRepo userRepo, TaskRepo taskRepo) {
		this.userRepo = userRepo;
		this.taskRepo = taskRepo;
	}

	@RequestMapping(value="/load/task",method=RequestMethod.GET)
	public Page<Task> loadTasks(Pageable pg,
		@ModelAttribute(name="user") User user,
		@RequestParam Map<String,String> params) {

		System.out.println("User with id " + user.getId() + " loading tasks");
		Page<Task> tasks;
		String completed = params.get("complete");
		String search = params.get("search");
		if (search == null || "".equals(search)) {
			if ("y".equals(completed))
				tasks = taskRepo.findByUserIdAndCompleteDateIsNotNull(user.getId(), pg);
			else if ("n".equals(completed))
				tasks = taskRepo.findByUserIdAndCompleteDateIsNull(user.getId(), pg);
			else
				tasks = taskRepo.findByUserId(user.getId(), pg);
		} else {
			if ("y".equals(completed))
				tasks = taskRepo.searchCompletedForUser(user.getId(), search, pg);
			else if ("n".equals(completed))
				tasks = taskRepo.searchIncompleteForUser(user.getId(), search, pg);
			else
				tasks = taskRepo.searchAllForUser(user.getId(), search, pg);
		}
		return tasks;
	}
	@RequestMapping(value="/save/task",method=RequestMethod.POST)
	public Message saveTask(@RequestBody Task task, @ModelAttribute(name="user") User user) {
		boolean isUpdate = task.getId() != null && task.getId() > 0;
		// make sure the user owns the record before updating it
		if (isUpdate && taskRepo.findOne(task.getId()).getUserId() != user.getId()) {
			return new Message(false, "You don't have rights to update this task", task.getId());
		} else {
			task.setUserId(user.getId());
			taskRepo.save(task);
			return new Message(true, (isUpdate ? "Updated" : "Inserted") + " task: '" + task.getName() + "'", task.getId());
		}
	}

	@RequestMapping(value="/delete/task/{taskid}",method=RequestMethod.DELETE)
	public Message deleteTask(@PathVariable("taskid") Long id, @ModelAttribute(name="user") User user) {
		// retrieve task from the database and make sure it is owned by the logged in user before deleting
		Task task = taskRepo.findOne(id);
		if (task.getUserId() == user.getId()) {
			taskRepo.delete(id);
			return new Message(true, "Successfully deleted task: '" + task.getName() + "'", id);
		} else {
			return new Message(false, "You don't have rights to delete this task", id);
		}
	}

	@RequestMapping(value="/mark-complete/task",method=RequestMethod.GET)
	public Message markComplete(@RequestParam("ids[]") Long[] ids, @ModelAttribute(name="user") User user) {
		StringBuilder msg = new StringBuilder();
		int updateCount = 0;
		// retrieve task from the database and make sure it is owned by the logged in user before deleting
		for (Long id: ids) {
			Task task = taskRepo.findOne(id);
			if (task.getUserId() == user.getId()) {
				updateCount += taskRepo.markComplete(new java.util.Date(), id);
				msg.append("Successfully marked task '"+ id + ":" + task.getName() + "' complete<br/>");
			} else {
				msg.append("You don't have rights to mark task id #" + id + " complete<br/>");
			}
		}
		return new Message(updateCount == ids.length, msg.toString());
	}

	@RequestMapping(value="/users",method=RequestMethod.GET)
	public List<User> getAllUsers() {
		return userRepo.findAll(); 
	}
	@RequestMapping(value="/users/{id}",method=RequestMethod.GET)
	public User getUserById(@PathVariable(name="id") Long id) {
		return userRepo.findOne(id); 
	}
	
}