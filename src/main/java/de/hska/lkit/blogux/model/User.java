package de.hska.lkit.blogux.model;

import java.util.Set;
import java.io.Serializable;

public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;
  private String username;
  private String picture;
  private String password;

  private String firstname;
  private String lastname;

  private Set<String> followers;
  private Set<String> follows;

  public User() {

  }

  /*
  public User(String username, String password, String id) {
    this.username = username;
    this.password = password;
    this.id = id;
  }
  */
  
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPicture() {
    return picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }

  public Set<String> getFollowers() {
    return followers;
  }

  public Set<String> getFollows() {
    return follows;
  }

  public void follow(User user) {
    this.follows.add(user.getId());
  }

  public void unfollow(User user) {
    this.follows.remove(user.getId());
  }

  public void getFollowed(User user) {
    this.followers.add(user.getId());
  }

  public void getUnfollowed(User user) {
    this.followers.remove(user.getId());
  }
}
