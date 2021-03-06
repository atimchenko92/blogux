package de.hska.lkit.blogux.socket.pojos;

import javax.validation.constraints.Size;
import de.hska.lkit.blogux.util.XSSSecurityConstraint;

public class NewPostMsg {

  private String name;
  
  @Size(min=1, max=140, message="Post should contain from 1 to 140 characters")
  @XSSSecurityConstraint
  private String msg;

  public NewPostMsg() {
  }

  public NewPostMsg(String name, String msg) {
    this.name = name;
    this.msg = msg;
  }

  public String getName() {
    return name;
  }

  public String getMsg() {
    return msg;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }
}
