package de.hska.lkit.blogux.places;

import javax.validation.constraints.Size;
import de.hska.lkit.blogux.util.XSSSecurityConstraint;
import de.hska.lkit.blogux.model.User;

public class Settings{

  @Size(max=80, message="Max. 80 characters")
  @XSSSecurityConstraint
  private String bio;

  @Size(max=12, message="Max. 12 characters")
  @XSSSecurityConstraint
  private String firstName;

  @Size(max=20, message="Max. 20 characters")
  @XSSSecurityConstraint
  private String lastName;

  @Size(max=30, message="Max. 30 characters")
  @XSSSecurityConstraint
  private String mail;

  private boolean notifyMe;

  public Settings(){
  }

  public Settings(User currentUser){
    setBio(currentUser.getBio());
    setFirstName(currentUser.getFirstname());
    setLastName(currentUser.getLastname());
    setMail(currentUser.getMail());
    setNotifyMe(currentUser.getNotifyMe());
  }

  public boolean getNotifyMe(){
    return notifyMe;
  }

  public void setNotifyMe(boolean notifyMe){
    this.notifyMe = notifyMe;
  }

  public void setBio(String bio){
    this.bio = bio;
  }

  public String getBio(){
    return bio;
  }

  public void setFirstName(String firstName){
    this.firstName  = firstName;
  }

  public String getFirstName(){
    return firstName;
  }

  public void setLastName(String lastname){
    this.lastName = lastname;
  }

  public String getLastName(){
    return lastName;
  }

  public void setMail(String mail){
    this.mail = mail;
  }

  public String getMail(){
    return mail;
  }


}
