package nmtasks.beans;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @Column
  private String name;
  @Column
  private String email;
  @Column
  private String password;
  @Column
  private String salt;
  @OneToMany
  private List<Task> tasks;
  public List<Task> getTasks() { return this.tasks; }

  public User() { }
  public User(long id) { this.id = id; }
  public Long getId() { return id; }
  public void setId(long id) { this.id = id; }
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }
  public String getPassword() { return password; }
  public void setPassword(String password) { this.password = password; }
  public String getSalt() { return salt; }
  public void setSalt(String salt) { this.salt = salt; }
  

}
