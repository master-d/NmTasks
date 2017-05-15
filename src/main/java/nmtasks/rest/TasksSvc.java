package nmtasks.rest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

	@Value("${spring.datasource.url}")
	private String jdbcString;
	@Value("classpath:sql/scripts/user-create.sql")
	private Resource userCreateSql;
	@Value("classpath:sql/scripts/task-create.sql")
	private Resource taskCreateSql;

	TasksSvc() { }
	
	
	@RequestMapping(value="reset-db", method=RequestMethod.GET)
	public ResponseEntity<String> resetDb(){
		// get connection to embedded H2 database
		//Class<?> driver = Class.forName("org.h2.Driver");
		try {
		Connection con = DriverManager.getConnection(jdbcString,"sa","");
		// drop tables
		PreparedStatement ps = con.prepareStatement("drop table user");
		ps.executeUpdate();
		ps = con.prepareStatement("drop table task");
		ps.executeUpdate();
		// recreate tables
		ps = con.prepareStatement(NmTasksUtil.resourceToString(userCreateSql));
		ps.executeUpdate();
		ps = con.prepareStatement(NmTasksUtil.resourceToString(taskCreateSql));
		ps.executeUpdate();
		// insert test user records using the JPA repositories
		List<User> testUsers = new LinkedList<>();
		User u = new User(1);
		u.setEmail("raricha@milwaukee.gov");
		u.setName("Rob");
		u.setSalt("20ufjjJunk");
		u.setPassword(NmTasksUtil.getSHA512Hash("123", u.getSalt()));
		testUsers.add(u);
		userRepo. save(testUsers);
		// insert test task records using the JPA repositories
		List<Task> testTasks = new LinkedList<>();
		Task t = new Task(1);
		t.setName("Walk the dog");
		t.setDescription("Walk the dog outside if it's not raining");
		t.setDueDate(new java.util.Date());
		t.setUserId(1);
		testTasks.add(t);
		taskRepo.save(testTasks);

		return new ResponseEntity<>("Sucessfully created tables and inserted test records", HttpStatus.OK);
	} catch (Exception e) {
		e.printStackTrace();
		return new ResponseEntity<>("ERROR: reset-db: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	}


	@RequestMapping(value="servername", method=RequestMethod.GET)
	public ResponseEntity<String> convert(){
		String text = "DUCK";
		return new ResponseEntity<>(text, HttpStatus.OK);
	}
	
}