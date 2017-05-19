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

import com.fasterxml.jackson.databind.ObjectMapper;

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
    @Value("classpath:json/sample-users.json")
    private Resource sampleUsersJson;
    @Value("classpath:json/sample-tasks.json")
    private Resource sampleTasksJson;
    

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
            // insert test user records from the sample json files
            ObjectMapper mapper = new ObjectMapper();
            List<User> testUsers = mapper.readValue(NmTasksUtil.resourceToString(sampleUsersJson), 
            mapper.getTypeFactory().constructCollectionType(List.class, User.class));
            userRepo.save(testUsers);
            
            // insert test task records using the JPA repositories
            List<Task> testTasks = mapper.readValue(NmTasksUtil.resourceToString(sampleTasksJson), 
            mapper.getTypeFactory().constructCollectionType(List.class, Task.class));
            taskRepo.save(testTasks);

            System.out.println("Sucessfully created tables and inserted test records");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("ERROR: reset-db: " + e.getMessage());
        }
            

        };
    }

}