package nmtasks.rest;

import nmtasks.beans.Message;
import nmtasks.beans.Task;
import nmtasks.beans.User;
import nmtasks.repositories.TaskRepo;
import nmtasks.repositories.UserRepo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = TasksSvc.class, secure = false)
public class TasksSvcTest {


  @MockBean
  private UserRepo userRepo;
  @MockBean
  private TaskRepo taskRepo;

  private TasksSvc tasksSvc;

  private User user = new User(1L);

  @Before
  public void setUp(){
    tasksSvc = new TasksSvc(userRepo, taskRepo);
    
  }
@Test
public void loadCompleteTasksForUser_givenTasksExist() {
  Task t = new Task(1L);
  t.setCompleteDate(new java.util.Date());
  t.setDescription("mock-description");
  t.setDueDate(new java.util.Date());
  t.setName("mock-task-name");
  t.setUserId(1L);

  Pageable pg = new PageRequest(0, 10);
  Map<String,String> params = new HashMap<>();
  params.put("complete","y");

  Mockito.when(taskRepo.findByUserIdAndCompleteDateIsNotNull(1L, pg)).thenReturn(toPagedResult(t));
  Page<Task> returnedTasks = tasksSvc.loadTasks(pg, user, params);
  Task returnedTask = returnedTasks.getContent().get(0);
  Assert.assertEquals(t.getCompleteDate(), returnedTask.getCompleteDate());
  Assert.assertEquals(t.getDescription(), returnedTask.getDescription());
  Assert.assertEquals(t.getDueDate(), returnedTask.getDueDate());
  Assert.assertEquals(t.getName(), returnedTask.getName());
  Assert.assertEquals(t.getUserId(), returnedTask.getUserId());
}

@Test
public void updateTaskOwnedByUser() {
  Task t = new Task(1L);
  t.setCompleteDate(new java.util.Date());
  t.setDescription("mock-description");
  t.setDueDate(new java.util.Date());
  t.setName("mock-task-name");
  t.setUserId(1L);

  Mockito.when(taskRepo.findOne(Matchers.anyLong())).thenReturn(t);
  Message returnMessage = tasksSvc.saveTask(t, user);
  Assert.assertTrue(returnMessage.isSuccess());

}

@Test
public void updateTaskOwnedByOtherUser() {
  Task t = new Task(1L);
  t.setCompleteDate(new java.util.Date());
  t.setDescription("mock-description");
  t.setDueDate(new java.util.Date());
  t.setName("mock-task-name");
  t.setUserId(2L);

  Mockito.when(taskRepo.findOne(Matchers.anyLong())).thenReturn(t);
  Message returnMessage = tasksSvc.saveTask(t, user);
  Assert.assertFalse(returnMessage.isSuccess());

}



@Test
public void loadInCompleteTasksForUser_givenTasksExist() {
  Task t = new Task(1L);
  t.setDescription("mock-description");
  t.setDueDate(new java.util.Date());
  t.setName("mock-task-name");
  t.setUserId(1L);

  Pageable pg = new PageRequest(0, 10);
  Map<String,String> params = new HashMap<>();
  params.put("complete","n");

  Mockito.when(taskRepo.findByUserIdAndCompleteDateIsNull(1L, pg)).thenReturn(toPagedResult(t));
  Page<Task> returnedTasks = tasksSvc.loadTasks(pg, user, params);
  Task returnedTask = returnedTasks.getContent().get(0);
  Assert.assertNull(t.getCompleteDate());
  Assert.assertEquals(t.getDescription(), returnedTask.getDescription());
  Assert.assertEquals(t.getDueDate(), returnedTask.getDueDate());
  Assert.assertEquals(t.getName(), returnedTask.getName());
  Assert.assertEquals(t.getUserId(), returnedTask.getUserId());
}

@Test
public void loadAllTasksForUserWithSearchQuery_givenTasksExist() {
  Task t = new Task(1L);
  t.setDescription("mock-description");
  t.setDueDate(new java.util.Date());
  t.setName("mock-task-name");
  t.setUserId(1L);

  Pageable pg = new PageRequest(0, 10);
  Map<String,String> params = new HashMap<>();
  params.put("complete","");
  params.put("search", "mock-search-string");

  Mockito.when(taskRepo.searchAllForUser(user.getId(), params.get("search"), pg)).thenReturn(toPagedResult(t));
  Page<Task> returnedTasks = tasksSvc.loadTasks(pg, user, params);
  Task returnedTask = returnedTasks.getContent().get(0);
  Assert.assertNull(t.getCompleteDate());
  Assert.assertEquals(t.getDescription(), returnedTask.getDescription());
  Assert.assertEquals(t.getDueDate(), returnedTask.getDueDate());
  Assert.assertEquals(t.getName(), returnedTask.getName());
  Assert.assertEquals(t.getUserId(), returnedTask.getUserId());
}


  @Test
  public void getAllUsers_givenUsersExist() {
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

  private <T> Page<T> toPagedResult(T... objs) {
    return new PageImpl<>(Arrays.asList(objs));
  }
}
