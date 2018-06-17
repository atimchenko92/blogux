package de.hska.lkit.blogux.places;

import de.hska.lkit.blogux.util.XSSSecurityConstraint;
import de.hska.lkit.blogux.model.User;

public class Settings{

  @XSSSecurityConstraint
  private String bio;

  @XSSSecurityConstraint
  private String firstName;

  @XSSSecurityConstraint
  private String lastName;

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
