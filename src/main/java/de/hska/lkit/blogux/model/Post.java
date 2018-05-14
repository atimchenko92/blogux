package de.hska.lkit.blogux.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * @author atimchenko
 *
 */
public class Post implements Serializable{

  private static final long serialVersionUID = 1L;
  private String id;
  private String author;
  private String datetime;
  private String text;

  public Post() {
    this.datetime = getFormattedDatetime();
  }

  public Post(String author, String text) {
    setAuthor(author);
    setText(text);
    this.datetime = getFormattedDatetime();
  }

/*
  public Post(String id, User author, LocalDateTime datetime, String text) {
    this.id = id;
    this.author = author;
    this.datetime = datetime;
    this.text = text;
  }
  */

  public String getDatetime() {
    return this.datetime;
  }

  public void setDatetime(String datetime) {
    this.datetime = datetime;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getAuthor() {
    return author;
  }

  public void setText(String text){
    this.text = text;
  }

  public String getText() {
    return text;
  }

  private String getFormattedDatetime() {
    return new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss").format(new java.util.Date());
  }

}
