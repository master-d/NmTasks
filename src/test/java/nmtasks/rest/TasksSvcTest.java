package nmtasks.rest;

import nmtasks.beans.User;
import nmtasks.repositories.TaskRepo;
import nmtasks.repositories.UserRepo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Random;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = TasksSvc.class, secure = false)
public class TasksSvcTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserRepo userRepo;
  @MockBean
  private TaskRepo taskRepo;

  private TasksSvc tasksSvc;

  @Before
  public void setUp(){
    tasksSvc = new TasksSvc(userRepo, taskRepo);
    
  }

  @Test
  public void getAllUsers_givenUsersExist_thenReturnAllUsers() {
    User user = new User();
    user.setId(new Random().nextLong());
    user.setEmail("mock-email");
    user.setName("mock-name");
    user.setPassword("mock-password");
    user.setSalt("mock-salt");

    Mockito.when(userRepo.findAll()).thenReturn(Collections.singletonList(user));

    List<User> returnedUser = tasksSvc.getAllUsers();
    Assert.assertEquals(user.getId(), returnedUser.get(0).getId());
    Assert.assertEquals(user.getEmail(), returnedUser.get(0).getEmail());
    Assert.assertEquals(user.getName(), returnedUser.get(0).getName());
    Assert.assertEquals(user.getPassword(), returnedUser.get(0).getPassword());
    Assert.assertEquals(user.getSalt(), returnedUser.get(0).getSalt());

  }

}
