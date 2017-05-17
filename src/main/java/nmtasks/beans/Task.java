package nmtasks.beans;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Task {

  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Long id;
  @Column
  private String name;
  @Column
  private String description;
  @Column
  @JsonFormat(pattern="M/d/yyyy")
  private Date dueDate;
  @Column
  @JsonFormat(pattern="M/d/yyyy")
  private Date completeDate;
  @Column(name="user_id")
  private long userId;
  @OneToOne
  @JoinColumn(name="id")
  private User user;
  public User getUser() { return this.user; }
  public Task() { }
  public Task(long id) { this.id = id; }

  public Long getId() { return id; }
  public void setId(long id) { this.id = id; }
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }
  public Date getDueDate() { return dueDate; }
  public void setDueDate(Date dueDate) { this.dueDate = dueDate; }
  public Date getCompleteDate() { return completeDate; }
  public void setCompleteDate(Date completeDate) { this.completeDate = completeDate; }
  public long getUserId() { return userId; }
  public void setUserId(long userId) { this.userId = userId; }
 
}
