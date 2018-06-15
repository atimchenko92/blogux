package de.hska.lkit.blogux.socket.pojos;

public class NewPostNotification {

  private String sender;
  private String body;

  public NewPostNotification() {
  }

  public NewPostNotification(String sender, String body) {
    this.sender = sender;
    this.body = body;
  }

  public String getSender() {
    return sender;
  }

  public String getBody() {
    return body;
  }
}
