package de.hska.lkit.blogux.model;

import java.util.List;
import java.util.Set;
import java.io.Serializable;
/**
 * @author atimchenko
 *
 */
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;
  private String sessionToken;
  private String username;
  private String password;

  private String firstname;
  private String lastname;
  private String mail;
  private String bio;

  private Set<String> followers;
  private Set<String> follows;

  private List<Post> personalPosts;
  private List<Post> followingPosts;
  private List<Post> persAndFollowingPosts;

  public User() {

  }

  public User(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public String getSessionToken(){
    return sessionToken;
  }

  public void setSessionToken(String token){
    this.sessionToken = token;
  }

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

  public String getMail() {
    return mail;
  }

  public void setMail(String mail) {
    this.mail = mail;
  }

  public String getBio() {
    return bio;
  }

  public void setBio(String bio) {
    this.bio = bio;
  }


  public void setPassword(String password) {
    this.password = password;
  }

  public String getPassword() {
    return password;
  }

  public Set<String> getFollowers() {
    return followers;
  }

  public void setFollowers(Set<String> followers) {
    this.followers  = followers;
  }

  public Set<String> getFollows() {
    return follows;
  }

  public void setFollows(Set<String> follows) {
    this.follows = follows;
  }

  public void setPersonalPosts(List<Post> postsList){
    this.personalPosts = postsList;
  }

  public void setFollowingPosts(List<Post> postsList){
    this.followingPosts = postsList;
  }

  public void setPersAndFolPosts(List<Post> postsList){
    this.persAndFollowingPosts = postsList;
  }

  public List<Post> getPersonalPosts() {
    return personalPosts;
  }

  public List<Post> getFollowingPosts() {
    return followingPosts;
  }

  public List<Post> getPersAndFolPosts(){
    return persAndFollowingPosts;
  }

  public boolean amIFollow(String name){
    return this.follows.contains(name);
  }

  public boolean amIFollowedBy(String name){
    return this.followers.contains(name);
  }

  public void follow(User user) {
    this.follows.add(user.getUsername());
  }

  public void unfollow(User user) {
    this.follows.remove(user.getUsername());
  }

  public void getFollowed(User user) {
    this.followers.add(user.getUsername());
  }

  public void getUnfollowed(User user) {
    this.followers.remove(user.getUsername());
  }
}
