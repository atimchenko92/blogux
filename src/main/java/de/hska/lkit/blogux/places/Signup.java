package de.hska.lkit.blogux.places;

public class Signup {

  private String name;
  private String pwd;
  private String pwdConfirm;
  private String content;

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
