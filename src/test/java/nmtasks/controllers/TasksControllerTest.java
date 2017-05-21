package nmtasks.controllers;


import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import nmtasks.beans.User;
import nmtasks.repositories.UserRepo;

@SqlGroup({
  @Sql(executionPhase=ExecutionPhase.BEFORE_TEST_METHOD,scripts="classpath:/sql/scripts/test-before.sql"),
  @Sql(executionPhase=ExecutionPhase.AFTER_TEST_METHOD,scripts="classpath:/sql/scripts/test-after.sql")
})
public class TasksControllerTest {

  private MockMvc mockMvc;
  private UserRepo userRepo;
  
  private static final User mockuser = new User(1L);


  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.standaloneSetup(new TasksController(userRepo)).build();
  }

  @Test
  public void indexNoSession() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("login"))
        .andExpect(MockMvcResultMatchers.model().attribute("email","root@localhost"));

  }
  @Test
  public void indexWithSession() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/").sessionAttr("user", mockuser))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("tasks"));
  }
  
  @Test
  @SqlGroup({
    @Sql(executionPhase=ExecutionPhase.BEFORE_TEST_METHOD,scripts="classpath:/sql/scripts/test-before.sql"),
    @Sql(executionPhase=ExecutionPhase.AFTER_TEST_METHOD,scripts="classpath:/sql/scripts/test-after.sql")
  })
  public void login() throws Exception {

    mockMvc.perform(MockMvcRequestBuilders.post("/").param("email", "root@localhost").param("password", "root"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("login"));
        //.andExpect(MockMvcResultMatchers.redirectedUrl("/tasks"));
  }
  
}
