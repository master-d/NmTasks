package nmtasks.beans;
public class Message {

  private Boolean success;
  private String msg;
  private Long id;

  public Message() { }
  public Message(Boolean success, String msg) { this.success = success; this.msg = msg; }
  public Message(Boolean success, String msg, Long id) { this.success = success; this.msg = msg; this.id = id; }

  public Boolean isSuccess() {
    return success;
  }
  public void setSuccess(Boolean success) {
    this.success = success;
  }
  public String getMsg() {
    return msg;
  }
  public void setMsg(String msg) {
    this.msg = msg;
  }
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
}