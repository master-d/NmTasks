package nmtasks;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

import nmtasks.beans.Task;
import nmtasks.beans.User;
import nmtasks.repositories.TaskRepo;
import nmtasks.repositories.UserRepo;
import nmtasks.util.NmTasksUtil;

@SpringBootApplication
public class Application {

	@Value("${spring.datasource.url}")
	private String jdbcString;
	@Value("classpath:sql/scripts/user-create.sql")
	private Resource userCreateSql;
	@Value("classpath:sql/scripts/task-create.sql")
	private Resource taskCreateSql;

	@Autowired
	private UserRepo userRepo;
	@Autowired
	private TaskRepo taskRepo;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            System.out.println("SPRING BOOT BEANS:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }

            // reset database
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
            User u = new User();
            u.setEmail("root@localhost");
            u.setName("Rob");
            u.setSalt("20ufjjJunk");
            u.setPassword(NmTasksUtil.getSHA512Hash("root", u.getSalt()));
            testUsers.add(u);
            User u2 = new User();
            u2.setEmail("boot@localhost");
            u2.setName("Bob");
            u2.setSalt("kefioeojwfj");
            u2.setPassword(NmTasksUtil.getSHA512Hash("boot", u.getSalt()));
            testUsers.add(u2);
            userRepo.save(testUsers);
            
            // insert test task records using the JPA repositories
            List<Task> testTasks = new LinkedList<>();
            Task t = new Task();
            t.setName("Walk the dog");
            t.setDescription("Walk the dog outside if it's not raining");
            t.setDueDate(new java.util.Date());
            t.setUserId(1);
            testTasks.add(t);
            taskRepo.save(testTasks);

            System.out.println("Sucessfully created tables and inserted test records");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("ERROR: reset-db: " + e.getMessage());
        }
            

        };
    }

}