package de.hska.lkit.blogux.model;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import java.io.Serializable;

public class Post implements Serializable{

  private static final long serialVersionUID = 1L;
  private String id;
  private User author;
  private LocalDateTime datetime;
  private String text;

  public Post() {

  }

/*
  public Post(String id, User author, LocalDateTime datetime, String text) {
    this.id = id;
    this.author = author;
    this.datetime = datetime;
    this.text = text;
  }
  */

  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setAuthor(User author) {
    this.author = author;
  }

  public User getAuthor() {
    return author;
  }

  public void setDatetime(LocalDateTime datetime) {
    this.datetime = datetime;
  }

  public LocalDateTime getDatetime() {
    return datetime;
  }

  public void setText(String text){
    this.text = text;
  }

  public String getText() {
    return text;
  }

  public String getFormattedDatetime() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    return datetime.format(formatter);
  }

}
