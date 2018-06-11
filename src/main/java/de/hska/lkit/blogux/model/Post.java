package de.hska.lkit.blogux.model;

import javax.validation.constraints.Size;
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

  @Size(min=1, max=140, message="Post should contain from 1 to 140 characters")
  private String text;

  public Post() {
  }

  public Post(String author, String text) {
    setAuthor(author);
    setText(text);
    this.datetime = new SimpleDateFormat("d MMM yyyy HH:mm:ss").format(new java.util.Date());
  }

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


}
