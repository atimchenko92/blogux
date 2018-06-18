package de.hska.lkit.blogux.places;

import javax.validation.constraints.Size;
import javax.validation.constraints.NotEmpty;
import de.hska.lkit.blogux.util.XSSSecurityConstraint;
import de.hska.lkit.blogux.util.UserExistsConstraint;

public class Login {

  @UserExistsConstraint
  @XSSSecurityConstraint
  @NotEmpty
  @Size(min=1, max=12, message="Username may only contain from 1 up to 12 characters")
  private String name;

  private String pwd;
  private String pwdConfirm;
  private String content;
  private boolean islogin;

  /**
   * @author atimchenko
   *
   */
  public Login(){
    setIslogin(true);
  }

  public boolean getIslogin() {
    return islogin;
  }

  public void setIslogin(boolean isLogin) {
    this.islogin = isLogin;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPwd(String pwd) {
    this.pwd = pwd;
  }

  public String getPwd() {
    return pwd;
  }

  public void setPwdConfirm(String pwdConfirm) {
    this.pwdConfirm = pwdConfirm;
  }

  public String getPwdConfirm() {
    return pwdConfirm;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
